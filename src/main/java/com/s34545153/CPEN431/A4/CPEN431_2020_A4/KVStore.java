package com.s34545153.CPEN431.A4.CPEN431_2020_A4;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.s34545153.CPEN431.A4.CPEN431_2020_A4.KVEntry;


import com.google.protobuf.ByteString;

public class KVStore<ByteString,KVEntry>{
	private Map<ByteString,KVEntry> kvStore;
	private int maxCapacity = 47500000;
	private int runningSize;
	private float resize = (float) 0.95;

	
	public KVStore(){
		this.kvStore = new HashMap<ByteString,KVEntry>();
	}
	
	public int getMaxSize(){
		return this.maxCapacity;
	}

	public int size() {
		return this.kvStore.size();
	}

	public boolean isEmpty() {
		return this.kvStore.isEmpty();
	}

	public boolean containsKey(ByteString key) {
		return this.kvStore.containsKey(key);
	}

	public boolean containsValue(KVEntry value) {
		return this.kvStore.containsValue(value);
	}

	public KVEntry get(ByteString key) {
		return this.kvStore.get(key);
	}

	public KVEntry put(ByteString key, KVEntry value, int size){
		if(this.runningSize + size < this.maxCapacity){
			this.runningSize += size;
			this.kvStore.put(key, value);
			return value;
		}
		else {
			return null;
		}
	}

	public KVEntry remove(ByteString key) {
		return this.kvStore.remove(key);
	}

	public void putAll(Map<ByteString, KVEntry> m) {
		this.kvStore.putAll(m);
	}

	public void clear() {
		this.runningSize = 0;
		this.kvStore = null;
		this.kvStore = new HashMap<ByteString,KVEntry>();
	}

	public Set<ByteString> keySet() {
		return this.kvStore.keySet();
	}

	public Collection<KVEntry> values() {
		return this.kvStore.values();
	}

	public Set<java.util.Map.Entry<ByteString, KVEntry>> entrySet() {
		return this.kvStore.entrySet();
	}
	
	public void decrementSize(int size){
		this.runningSize -= size;
	}

}
