package com.s34545153.CPEN431.A4.CPEN431_2020_A4.Commands;

import com.google.protobuf.ByteString;
import com.s34545153.CPEN431.A4.CPEN431_2020_A4.App;
import com.s34545153.CPEN431.A4.CPEN431_2020_A4.KVEntry;

import ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest;

public class Remove {
	
	public static boolean run(KVRequest kvRequest){
		ByteString key = kvRequest.getKey();
		return remove(key);
		
	}
	
    private static boolean remove(ByteString key){
    	KVEntry previousValue = App.keyValue.remove(key);
    	if(previousValue == null){
    		return false;
    	}
    	App.keyValue.decrementSize(key.size() + previousValue.getApproximateSize());
    	return true;
    }
}
