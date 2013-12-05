package com.weasel.lucene.code;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.DefaultSimilarity;

/**
 * @author Dylan
 * @time 2013-7-9
 */
public class CustomSimilarity extends DefaultSimilarity {

	@Override
	public float computeNorm(String field, FieldInvertState state) {
		// TODO Auto-generated method stub
		return super.computeNorm(field, state);
	}

	
}
