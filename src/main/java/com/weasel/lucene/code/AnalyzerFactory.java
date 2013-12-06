package com.weasel.lucene.code;

import org.apache.lucene.analysis.Analyzer;

import com.weasel.lucene.ik.lucene.IKAnalyzer;

/**
 * 
 * @author Dylan
 * @time 2013年7月2日
 */
public final class AnalyzerFactory {

	private AnalyzerFactory(){
		
	}
	
	public static Analyzer getIKAnalyzer(){
		return new IKAnalyzer(true);
	}
}
