package com.s34545153.CPEN431.A4.CPEN431_2020_A4;

import java.net.InetAddress;

import com.google.protobuf.ByteString;
import com.s34545153.CPEN431.A4.CPEN431_2020_A4.Errors;
import com.s34545153.CPEN431.A4.CPEN431_2020_A4.Commands.*;

import ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest;

public class CommandHandler {
	

	
	CommandHandler(){}

	public void handle(KVRequest kvRequest, ByteString id, InetAddress ip, int port){
		//System.out.println("IN HANDLE");
		
		boolean status = false;
		ByteString responsePayload;
		ByteString cachedValue;
		
		switch(kvRequest.getCommand()){
	    	case CommandCodes.PUT:
	    		status = Put.run(kvRequest);
	    		if(status == true){
        			responsePayload = App.msgHandler.generatePayload(Errors.SUCCESS, null, -1, -1, -1, -1);
        			cachedValue = App.reqHandler.addToCache(id, responsePayload);
        			App.msgHandler.sendMessage(id, cachedValue, ip, port);
        		}
        		else{
        			responsePayload = App.msgHandler.generatePayload(Errors.OUT_OF_SPACE, null, -1, -1, -1, -1);
        			cachedValue = App.reqHandler.addToCache(id, responsePayload);
        			App.msgHandler.sendMessage(id, cachedValue, ip, port);
        		}
	    		break;
	
	    	case CommandCodes.GET:
	    		KVEntry getValue = Get.run(kvRequest);
	    		if(getValue == null){
	    			responsePayload = App.msgHandler.generatePayload(Errors.NONEXISTENT_KEY, null,-1, -1, -1, -1);
	    			cachedValue = App.reqHandler.addToCache(id, responsePayload);
	    			App.msgHandler.sendMessage(id, cachedValue, ip, port);
	    		}
	    		else{
	    			responsePayload = App.msgHandler.generatePayload(Errors.SUCCESS, getValue.getValue(),
	    					-1, getValue.getVersion(), -1, -1);
	    			cachedValue = App.reqHandler.addToCache(id, responsePayload);
	    			App.msgHandler.sendMessage(id, cachedValue, ip, port);
	    		}
	    		break;
	    		
	    	case CommandCodes.REMOVE:
	    		status = Remove.run(kvRequest);
	    		if(status){
	    			responsePayload = App.msgHandler.generatePayload(Errors.SUCCESS, null,-1, -1, -1, -1);
	    			cachedValue = App.reqHandler.addToCache(id, responsePayload);
	    			App.msgHandler.sendMessage(id, cachedValue, ip, port);
	    		}
	    		else{
	    			responsePayload = App.msgHandler.generatePayload(Errors.NONEXISTENT_KEY, null,-1, -1, -1, -1);
	    			cachedValue = App.reqHandler.addToCache(id, responsePayload);
	    			App.msgHandler.sendMessage(id, cachedValue, ip, port);
	    		}
	    		break;
	    		
	    	case CommandCodes.SHUTDOWN:
	    		Shutdown.run();
	    		break;
	    		
	    	case CommandCodes.WIPEOUT:
	    		Wipeout.run();
    			responsePayload = App.msgHandler.generatePayload(Errors.SUCCESS, null,-1, -1, -1, -1);
    			cachedValue = App.reqHandler.addToCache(id, responsePayload);
    			App.msgHandler.sendMessage(id, cachedValue, ip, port);
				break;
				
	    	case CommandCodes.IS_ALIVE:
	    		status = IsAlive.run();
	    		if(status){
	    			responsePayload = App.msgHandler.generatePayload(Errors.SUCCESS, null,-1, -1, -1, -1);
	    			cachedValue = App.reqHandler.addToCache(id, responsePayload);
	    			App.msgHandler.sendMessage(id, cachedValue, ip, port);
	    		}
	    		break;
	    		
	    	case CommandCodes.GET_PID:
	    		int pid = GetPID.run();
	    		responsePayload = App.msgHandler.generatePayload(Errors.SUCCESS, null, pid, -1, -1, -1);
	    		cachedValue = App.reqHandler.addToCache(id, responsePayload);
	    		App.msgHandler.sendMessage(id, cachedValue, ip, port);
				break;
				
	    	case CommandCodes.GET_MEMBERSHIP_COUNT:
	    		int memCount = GetMembershipCount.run();
    			responsePayload = App.msgHandler.generatePayload(Errors.SUCCESS, null,-1, -1, -1, memCount);
    			cachedValue = App.reqHandler.addToCache(id, responsePayload);
    			App.msgHandler.sendMessage(id, cachedValue, ip, port);
				break;
				
	    	default:
    			responsePayload = App.msgHandler.generatePayload(Errors.UNRECOGNIZED_CMD, null,-1, -1, -1, -1);
    			cachedValue = App.reqHandler.addToCache(id, responsePayload);
    			App.msgHandler.sendMessage(id, cachedValue, ip, port);
				break;
	    }
	}
}
