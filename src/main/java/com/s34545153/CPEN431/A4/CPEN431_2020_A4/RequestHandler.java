package com.s34545153.CPEN431.A4.CPEN431_2020_A4;

import com.google.protobuf.ByteString;
import com.s34545153.CPEN431.A4.CPEN431_2020_A4.Errors;

public class RequestHandler {
	
	RequestHandler() {
	}
	   
   public ByteString addToCache(ByteString id, ByteString message){
 	   //System.out.println("IN ADDTOCACHE");
 	   if(isCacheOverFlow() == true){
 		   ByteString overloadMessage = App.msgHandler.generatePayload(Errors.OVERLOAD, null, -1, -1, App.overloadWaitTime, -1);
 		   return overloadMessage;
 	   }
 	   else{
 		   App.serverCache.put(id, message);
 		   return message;
 	   }
    }
    
    public boolean isCacheOverFlow(){
 	   //System.out.println("IN ISCACHEOVERFLOW");
 	   return App.serverCache.size() > App.maxCacheSize * App.cacheScaleFactor;
    }
}
