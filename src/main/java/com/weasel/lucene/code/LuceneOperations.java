package com.weasel.lucene.code;

import java.util.Collection;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;

/**
 * @author Dylan
 * @time 2013-7-4
 */
public interface LuceneOperations {

	/**
	 * @param doc
	 */
	void saveDocument(Document doc);
	
	/**
	 * 
	 * @param docs
	 */
	void saveDocument(Collection<Document> docs);
	
	/**
	 * @param query
	 */
	void delete(Query query);
	
	/**
	 * 
	 */
	void deleteAll();
	
	/**
	 * 
	 * @param term
	 * @param doc
	 * @param analyzer
	 */
	void update(Term term,Document doc);
	
	/**
	 * 
	 * @return
	 */
	<T> Page<T> queryPage(Page<T> page,Query query);
	
	/**
	 * @param query
	 * @param clazz
	 * @return
	 */
	<T> T findOneById(Query query);
	
}
