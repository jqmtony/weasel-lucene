package com.weasel.lucene.code;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;

public class LuceneDocument implements Map<String, Object> {

	private Map<String,Object> doc = new HashMap<String,Object>();
	
	public LuceneDocument(Document document){
		for(Fieldable field : document.getFields()){
			doc.put(field.name(), field.stringValue());
		}
	}
	
	@Override
	public int size() {
		return doc.size();
	}

	@Override
	public boolean isEmpty() {
		return doc.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return doc.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return doc.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		return doc.get(key);
	}

	@Override
	public Object put(String key, Object value) {
		return doc.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return doc.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		doc.putAll(m);
	}

	@Override
	public void clear() {
		doc.clear();
	}

	@Override
	public Set<String> keySet() {
		return doc.keySet();
	}

	@Override
	public Collection<Object> values() {
		return doc.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return doc.entrySet();
	}

}
