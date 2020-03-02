package com.s34545153.CPEN431.A4.CPEN431_2020_A4.Commands;

import com.google.protobuf.ByteString;
import com.s34545153.CPEN431.A4.CPEN431_2020_A4.App;
import com.s34545153.CPEN431.A4.CPEN431_2020_A4.KVEntry;

import ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest;

public class Get {

	public static KVEntry run(KVRequest kvRequest){
		//System.out.println("IN GET_RUN");
		return get(kvRequest.getKey());
	}
	
    private static KVEntry get(ByteString key){
    	//System.out.println("IN GET");
    	KVEntry value = App.keyValue.get(key);
    	return value;
    }
}
