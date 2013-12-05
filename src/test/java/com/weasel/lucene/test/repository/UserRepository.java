package com.weasel.lucene.test.repository;

import com.weasel.lucene.LuceneRepositorySupport;
import com.weasel.lucene.test.domain.User;

public class UserRepository extends LuceneRepositorySupport<User> {

	public UserRepository() {
		super(User.class);
	}

	@Override
	protected String indexDir() {
		return "D:\\lucene-index";
	}

}
