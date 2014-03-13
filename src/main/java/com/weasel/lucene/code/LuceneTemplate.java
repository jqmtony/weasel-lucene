package com.weasel.lucene.code;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import com.weasel.lang.helper.BeanMapConverter;
import com.weasel.lang.helper.GodHands;
import com.weasel.lucene.annotation.Highlight;

public class LuceneTemplate implements LuceneOperations {

	public static final IndexWriterConfig DEFAULT_CONFIG = new IndexWriterConfig(Version.LUCENE_36, AnalyzerFactory.getIKAnalyzer());
	protected Directory directory = null;
	protected IndexWriterConfig config = null;
	protected IndexWriter indexCreater = null;
	protected IndexReader reader = null;
	protected IndexSearcher searcher = null;
	private Class<?> entityClass;

	public LuceneTemplate(Class<?> entityClass) {
		this.entityClass = entityClass;
		config = DEFAULT_CONFIG;
		initContext();
	}

	public LuceneTemplate(IndexWriterConfig config, Class<?> entityClass) {
		this.entityClass = entityClass;
		this.config = config;
		initContext();
	}

	@Override
	public void saveDocument(Document doc) {
		try {
			indexCreater.addDocument(doc);
			indexCreater.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	@Override
	public void saveDocument(Collection<Document> docs) {
		try {
			for (Document doc : docs) {
				indexCreater.addDocument(doc);
			}
			indexCreater.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	@Override
	public void delete(Query query) {
		try {
			indexCreater.deleteDocuments(query);
			indexCreater.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}

	@Override
	public void deleteAll() {
		try {
			indexCreater.deleteAll();
			indexCreater.commit();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	@Override
	public void update(Term term, Document doc) {
		try {
			indexCreater.updateDocument(term, doc);
			indexCreater.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Page<T> queryPage(Page<T> page, Query query) {
		TopDocsCollector<ScoreDoc> results = TopScoreDocCollector.create(page.getPageSize(), true);
		List<T> pageResult = new ArrayList<T>();
		try {
			getSearcher().search(query, results);
			page.setTotal(results.getTotalHits());
			TopDocs hits = results.topDocs(page.getOffset(), page.getPageSize());
			for (ScoreDoc doc : hits.scoreDocs) {
				Document document = getSearcher().doc(doc.doc);
				LuceneDocument ldoc = new LuceneDocument(document);
				T entity = (T) BeanMapConverter.mapToBean(ldoc, entityClass);
				Highlighter highlighter = getHighlighter(query);
				List<String> highlightFieldsName = getHighlightFieldsName();
				for (String fieldName : highlightFieldsName) {
					try {
						String highlightText = highlighter.getBestFragment(AnalyzerFactory.getIKAnalyzer(), fieldName, document.get(fieldName));
						GodHands.setFieldValue(entity, fieldName, highlightText);
					} catch (InvalidTokenOffsetsException e) {
						e.printStackTrace();
					}
				}
				pageResult.add(entity);
			}
			page.setResult(pageResult);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return page;
	}

	/**
	 * 创建一个highlighter
	 * 
	 * @param query
	 * @return
	 */
	private Highlighter getHighlighter(Query query) {
		SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
		String[] queryStr = query.toString().split(" ");
		String searchKey = queryStr[0].split(":")[0];
		List<String> highlightFieldsName = getHighlightFieldsName();
		Highlighter highlighter = new Highlighter(formatter, new QueryScorer(structureHighlightQuery(searchKey, highlightFieldsName.toArray(new String[highlightFieldsName.size()]))));
		highlighter.setTextFragmenter(new SimpleFragmenter(20));
		return highlighter;
	}

	/**
	 * 通过反射，拿到有Highlight注解的字段名称
	 * 
	 * @return
	 */
	private List<String> getHighlightFieldsName() {
		List<String> highlightFieldsName = new ArrayList<String>();
		Field[] fields = entityClass.getDeclaredFields();
		for (Field field : fields) {
			if (null != field.getAnnotation(Highlight.class)) {
				highlightFieldsName.add(field.getName());
			}
		}
		return highlightFieldsName;
	}

	/**
	 * 构造highlight的查询
	 * 
	 * @param searchKey
	 * @param keyFields
	 * @return
	 */
	private Query structureHighlightQuery(String searchKey, String... keyFields) {
		String[] searchKeys = new String[keyFields.length];
		for (int i = 0; i < keyFields.length; i++) {
			searchKeys[i] = searchKey;
		}
		try {
			return MultiFieldQueryParser.parse(Version.LUCENE_36, searchKeys, keyFields, AnalyzerFactory.getIKAnalyzer());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected String indexDir() {
		throw new UnsupportedOperationException("is not support in this class,please override it...");
	}

	protected void initContext() {
		try {
			directory = new SimpleFSDirectory(new File(indexDir()));
			indexCreater = new IndexWriter(directory, config);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private IndexReader getReader() {
		try {
			if (null == reader)
				reader = IndexReader.open(directory);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return reader;
	}

	private IndexSearcher getSearcher() {
		if (null == searcher) {
			searcher = new IndexSearcher(getReader(), Executors.newSingleThreadExecutor());
		}
		return searcher;
	}

	protected void close() {
		/*try {
			indexCreater.close();
			directory.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T findOneById(Query query) {
		try {
			TopDocs hits = getSearcher().search(query, 1);
			if (hits.totalHits > 1) {
				throw new RuntimeException("find more one result...");
			}
			for (ScoreDoc doc : hits.scoreDocs) {
				Document document = getSearcher().doc(doc.doc);
				LuceneDocument ldoc = new LuceneDocument(document);
				T entity = (T) BeanMapConverter.mapToBean(ldoc, entityClass);
				Highlighter highlighter = getHighlighter(query);
				List<String> highlightFieldsName = getHighlightFieldsName();
				for (String fieldName : highlightFieldsName) {
					try {
						String highlightText = highlighter.getBestFragment(AnalyzerFactory.getIKAnalyzer(), fieldName, document.get(fieldName));
						GodHands.setFieldValue(entity, fieldName, highlightText);
					} catch (InvalidTokenOffsetsException e) {
						e.printStackTrace();
					}
				}
				return entity;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
