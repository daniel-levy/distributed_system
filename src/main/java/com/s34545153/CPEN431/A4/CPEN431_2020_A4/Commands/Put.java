package com.s34545153.CPEN431.A4.CPEN431_2020_A4.Commands;

import com.google.protobuf.ByteString;
import com.s34545153.CPEN431.A4.CPEN431_2020_A4.App;
import com.s34545153.CPEN431.A4.CPEN431_2020_A4.KVEntry;

import ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest;

public class Put {
	
	public static boolean run(KVRequest kvRequest){
		//System.out.println("IN PUT_RUN");
		int version = kvRequest.getVersion();		
		return put(kvRequest.getKey(), kvRequest.getValue(), version);
	}
	
    private static boolean put(ByteString key, ByteString value, int version){
    	//System.out.println("IN PUT");
    	KVEntry input = new KVEntry(value, version);
    	int size = key.size() + input.getApproximateSize();
    	KVEntry result = App.keyValue.put(key, input, size);
    	return result != null ? true : false;
    }
}
