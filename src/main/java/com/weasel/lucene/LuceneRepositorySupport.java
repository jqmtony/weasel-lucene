package com.weasel.lucene;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import com.weasel.lucene.annotation.LuceneIndex;
import com.weasel.lucene.code.LuceneProperty;
import com.weasel.lucene.code.LuceneTemplate;
import com.weasel.lucene.domain.Entity;
import com.weasel.core.helper.GodHands;

/**
 * 
 * @author Dylan
 * @time 2013-7-8
 */
public class LuceneRepositorySupport<T extends Entity> extends LuceneTemplate implements LuceneRepository<T> {

	public LuceneRepositorySupport(Class<T> entityClass) {
		super(entityClass);
	}

	@Override
	public void save(T entity) {
		Validate.notNull(entity, "entity must not be null");
		super.saveDocument(buildDocument(entity));
	}
	
	@Override
	public void save(Collection<T> entities) {
		Validate.notNull(entities, "entities must not be null");
		List<Document> docs = new ArrayList<Document>();
		for(T entity : entities){
			Document doc = buildDocument(entity);
			docs.add(doc);
		}
		super.saveDocument(docs);
	}
	
	@Override
	public void delete(String _id) {
		Validate.notEmpty(_id,"_id must not be empty");
		Query query = new TermQuery(new Term("_id", _id));
		super.delete(query);
	}
	
	/**
	 * @param entity
	 * @return
	 */
	protected Document buildDocument(T entity) {
		Validate.notNull(entity, "entity must not be null");
		Document doc = new Document();
		Field[] fields = GodHands.getAccessibleFields(entity);
		for (Field field : fields) {
			LuceneIndex luceneIndex = field.getAnnotation(LuceneIndex.class);
			String fieldName = field.getName();
			Store store = (null != luceneIndex && null != luceneIndex.store()) ? luceneIndex.store() : LuceneProperty.DEFAULT_STORE;
			Index index = (null != luceneIndex && null != luceneIndex.store()) ? luceneIndex.index() : LuceneProperty.DEFAULT_INDEX;
			Object fieldValue = GodHands.getFieldValue(entity, fieldName);
			Fieldable fieldable = new org.apache.lucene.document.Field(fieldName, null != fieldValue ? fieldValue.toString() : "null", store, index);
			doc.add(fieldable);
		}
		return doc;
	}

	@Override
	public T findOneById(String _id) {
		Validate.notEmpty(_id,"_id must not be empty");
		Query query = new TermQuery(new Term("_id", _id));
		return super.findOneById(query);
	}

	@Override
	public void update(T entity) {
		Validate.notNull(entity, "entity must not be null");
		super.update(new Term("_id", entity.get_id()), buildDocument(entity));
	}


}
