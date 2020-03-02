package com.s34545153.CPEN431.A4.CPEN431_2020_A4;

import com.google.protobuf.ByteString;

public class KVEntry {
	
	private ByteString value;
	private int version;
	
	public KVEntry(ByteString value, int version){
		this.value = value;
		this.version = version;
	}
	
	public ByteString getValue(){
		return this.value;
	}
	
	public int getVersion(){
		return this.version;
	}
	
	public int getApproximateSize(){
		return this.value.size() + 10;
	}
}
