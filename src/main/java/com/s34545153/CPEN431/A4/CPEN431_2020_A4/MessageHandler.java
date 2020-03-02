package com.s34545153.CPEN431.A4.CPEN431_2020_A4;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.zip.CRC32;

import com.google.common.primitives.Bytes;
import com.google.protobuf.ByteString;

import ca.NetSysLab.ProtocolBuffers.Message;
import ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest;
import ca.NetSysLab.ProtocolBuffers.KeyValueResponse.KVResponse;
import ca.NetSysLab.ProtocolBuffers.Message.Msg;
import com.s34545153.CPEN431.A4.CPEN431_2020_A4.Errors;

public class MessageHandler {
	
	MessageHandler(){}
	
	public Msg receiveMessage(DatagramPacket packet){
		//System.out.println("IN RECEIVEMESSAGE");
    	try{
			int numBytesReceived = packet.getLength();
	        
	        //Copy buffer contents without padding
	        byte[] receivedData = new byte[numBytesReceived];
	        for(int i = 0; i < numBytesReceived; i++){
	        	receivedData[i] = packet.getData()[i];
	        }
	        
	        return Msg.parseFrom(receivedData);
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }
	
	public byte[] generateResponse(ByteString id, ByteString payload){
		//System.out.println("IN GENERATERESPONSE");
		Message.Msg.Builder msg = Message.Msg.newBuilder();
		msg.setMessageID(id);
		msg.setPayload(payload);
		msg.setCheckSum(generateCheckSum(id.toByteArray(), payload.toByteArray()));
		return msg.build().toByteArray();
   }
	
   public ByteString generatePayload(int errorCode, ByteString value, int pid, int version, int overloadWaitTime, int membershipCount){
	   //System.out.println("IN GENERATEPAYLOAD");
	   KVResponse.Builder kvResponse = KVResponse.newBuilder();
	   kvResponse.setErrCode(errorCode);
	   if(value != null){
		   kvResponse.setValue(value);
	   }
	   if(pid != -1){
		   kvResponse.setPid(pid);
	   }
	   if(version != -1){
		   kvResponse.setVersion(version);
	   }
	   if(overloadWaitTime != -1){
		   kvResponse.setOverloadWaitTime(overloadWaitTime);
	   }
	   if(membershipCount != -1){
		   kvResponse.setMembershipCount(membershipCount);
	   }
	   return kvResponse.build().toByteString();
   }
   
   public void sendMessage(ByteString id, ByteString payload, InetAddress ip, int port) {
	   //System.out.println("IN SENDMESSAGE");
	   byte[] messageBytes = generateResponse(id, payload);
	   DatagramPacket sendPacket = new DatagramPacket(messageBytes, messageBytes.length, ip, port);
	   try {
		   App.socket.send(sendPacket);
	   }
	   catch(Exception e) {
		   e.printStackTrace();
	   }
   }
   
   public int checkFormatting(int code, KVRequest kv){
	   //System.out.println("IN CHECKFORMATTING");
	   byte[] key = kv.getKey().toByteArray();
	   byte[] value = kv.getValue().toByteArray();
	   switch(code){
	       case CommandCodes.PUT:
	    	   if(key.length > 0 && value.length > 0){
	    		   if(key.length > 32){
	    			   key = null;
	    			   value = null;
	    			   return Errors.INVALID_KEY;
	    		   }
	    		   else if(value.length > 10000){
	    			   key = null;
	    			   value = null;
	    			   return Errors.INVALID_VALUE;
	    		   }
	    		   else{
	    			   key = null;
	    			   value = null;
	    			   return Errors.SUCCESS;
	    		   }
	    	   }
	    	   else if(key.length > 0) {
	    		   key = null;
	    		   value = null;
	    		   return Errors.INVALID_KEY;
	    	   }
	    	   else {
	    		   key = null;
	    		   value = null;
	    		   return Errors.INVALID_VALUE;
	    	   }
			
	       case CommandCodes.GET:
	       case CommandCodes.REMOVE:
	    	   if(key.length > 0){
	    		   if(key.length > 32){
	    			   key = null;
	    			   value = null;
	    			   return Errors.INVALID_KEY;
	    		   }
	    		   else{
	    			   key = null;
	    			   value = null;
	    			   return Errors.SUCCESS;
	    		   }
	    	   }
	    	   else {
	    		   key = null;
	    		   value = null;
	    		   return Errors.INVALID_KEY;
	    	   }
				
	       default:
	    	   key = null;
	    	   value = null;
	    	   return Errors.SUCCESS;		
	   }
   }
   
   public long generateCheckSum(byte[] id, byte[] payload){
	   //System.out.println("IN GENERATECHECKSUM");
       CRC32 crc = new CRC32();
       byte[] byteArr = Bytes.concat(id, payload);
       crc.update(byteArr);
       byteArr = null;
       return crc.getValue();
   }
}
