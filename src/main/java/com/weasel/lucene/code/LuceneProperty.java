package com.weasel.lucene.code;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.util.Version;

/**
 * 
 * @author Dylan
 * @time 2013-7-8
 */
public final class LuceneProperty {

	public final static Version LUCENE_VERSION = Version.LUCENE_36;
	public final static Store DEFAULT_STORE = Field.Store.YES;
	public final static Index DEFAULT_INDEX = Field.Index.ANALYZED;
	
	private LuceneProperty() {
	}
	
	
}
