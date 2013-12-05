package com.weasel.lucene.domain;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;

import com.weasel.lucene.annotation.LuceneIndex;

/**
 * @author Dylan
 * @time 2013-7-8
 */
public class Entity {

	@LuceneIndex(index=Index.NOT_ANALYZED_NO_NORMS,store = Store.YES)
	private String _id;

	public String get_id() {
		if (StringUtils.isEmpty(_id)) {
			_id = UUID.randomUUID().toString();
		}
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}
	
}
