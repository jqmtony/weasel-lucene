package com.weasel.lucene;

import java.util.Collection;

/**
 * @author Dylan
 * @time 2013-7-8
 */
public interface LuceneRepository <T>{

	/**
	 * 
	 * @param entity
	 */
	public void save(T entity);
	
	/**
	 * 
	 * @param docs
	 */
	void save(Collection<T> entities);
	/**
	 * 
	 */
	void deleteAll();
	
	/**
	 * 
	 * @param id
	 */
	void delete(String id);
	
	/**
	 * @param query
	 * @param clazz
	 * @return
	 */
	T findOneById(String id);
	/**
	 * 
	 * @param entity
	 */
	void update(T entity);
}
