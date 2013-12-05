package com.weasel.lucene.test.domain;

import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;

import com.weasel.lucene.annotation.Highlight;
import com.weasel.lucene.annotation.LuceneIndex;
import com.weasel.lucene.domain.Entity;

/**
 * 
 * @author Dylan
 * @time 2013-7-8
 */
public class User extends Entity{

	@Highlight
	@LuceneIndex(index = Index.NOT_ANALYZED, store = Store.YES)
	private String username;

	private String password;

	@LuceneIndex(index = Index.ANALYZED, store = Store.YES)
	private String content;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
