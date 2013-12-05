package com.weasel.lucene.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.lucene.document.Field;

/**
 * 
 * @author Dylan
 * @time 2013-7-4
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LuceneIndex {

	/**
	 * 
	 * @return
	 */
	public Field.Store store();

	/**
	 * 
	 * @return
	 */
	public Field.Index index();
	
}
