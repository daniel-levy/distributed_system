package com.s34545153.CPEN431.A4.CPEN431_2020_A4;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.protobuf.ByteString;

import ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest;
import ca.NetSysLab.ProtocolBuffers.Message.Msg;

public class App 
{
	//Private class fields
	public static KVStore<ByteString, KVEntry> keyValue;
	public static Cache<ByteString, ByteString> serverCache;
	public static DatagramSocket socket;
	public static final int overloadWaitTime = 5; 
	public static final long maxCacheSize = 80000; //90000
	public static final double cacheScaleFactor = 0.98;
	
	public static MessageHandler msgHandler = new MessageHandler();
	public static RequestHandler reqHandler = new RequestHandler();
	public static CommandHandler cmdHandler = new CommandHandler();
	
	//App constructor
	public App() throws Exception{
		App.keyValue = new KVStore<ByteString, KVEntry>();
		App.serverCache = CacheBuilder.newBuilder()
				.expireAfterWrite(5000, TimeUnit.MILLISECONDS)
				.maximumSize(maxCacheSize)
				.build();
	}
	
    public static void main( String[] args ) throws Exception{
    	//System.out.println("IN MAIN");
    	//The port the server will be running on should be provided as an argument
    	if(args.length < 1){
    		////System.out.println("One or more arguments are not provided. Please try again");
    		return;
    	}
    	
    	//Get the port to listen on and start the server
    	int port = Integer.parseInt(args[0]);
    	App server = new App();	
    	server.serve(port);
    }
    
    private void serve(int serverPort) throws SocketException{
    	
    	//System.out.println("IN SERVE");
    	//Create fields needed for server operation
    	App.socket = new DatagramSocket(serverPort);
    	byte[] receiveBuffer = new byte[16536];
    	DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
    	Msg receivedMessage = null;
    	ByteString responsePayload = null;
 	    ByteString cachedValue = null;
        InetAddress ip = null;
        int port = 0;
		
    	while(true){
    		try{
    			//Receive information from socket and convert it to a message
    			App.socket.receive(receivePacket);
	            receivedMessage = msgHandler.receiveMessage(receivePacket);
	            
	            //Received the source IP and port information
	            ip = receivePacket.getAddress();
	            port = receivePacket.getPort();
	            
	            //Pass the message and the return information to the request handler and start a new thread
//	            RequestHandler cur = new RequestHandler(receivedMessage, ip, port);
//	            cur.start();
	            
	     	    ByteString id = receivedMessage.getMessageID();
	            ByteString payload = receivedMessage.getPayload();
	            
     	    	//Ensure that corruption did not occur by comparing to generated checksum
                long newCheckSum = msgHandler.generateCheckSum(id.toByteArray(), payload.toByteArray());	            
                if(receivedMessage.getCheckSum() != newCheckSum){
             	   continue;
                }
                //Check to see if message is cached and resend if it is
                ByteString cachedMessage = (ByteString) App.serverCache.getIfPresent(id);
                if(cachedMessage != null){
                	msgHandler.sendMessage(id, cachedMessage, ip, port);
     	           continue;
                }
               
                //Parse out the application payload
                KVRequest kvRequest = KVRequest.parseFrom(payload);
               
                //Ensure that fields are correctly formatted
                int formatCheck = msgHandler.checkFormatting(kvRequest.getCommand(), kvRequest);
                if(formatCheck != Errors.SUCCESS){
             	   responsePayload = msgHandler.generatePayload(formatCheck, null, -1, -1, -1, -1);
             	   cachedValue = reqHandler.addToCache(id, responsePayload);
             	  msgHandler.sendMessage(id, cachedValue, ip, port);
             	   continue;
                }
               
                //Handle the command or send an overload message
                if(reqHandler.isCacheOverFlow() == true){
             	   //System.out.println("HERE " + App.serverCache.size());
             	   responsePayload = msgHandler.generatePayload(Errors.OVERLOAD, null, -1, -1, App.overloadWaitTime, -1);
             	  msgHandler.sendMessage(id, cachedValue, ip, port);
                }
                else {
                	cmdHandler.handle(kvRequest, id, ip, port);
                }
    		}
    		catch(OutOfMemoryError ome){
    			//System.out.println("System out of memory");
    			responsePayload = msgHandler.generatePayload(Errors.OVERLOAD, null, -1, -1, App.overloadWaitTime, -1);
    			cachedValue = reqHandler.addToCache(receivedMessage.getMessageID(), responsePayload);
    			msgHandler.sendMessage(receivedMessage.getMessageID(), cachedValue, ip, port);		
    		}
    		catch(Exception e){
    			responsePayload = msgHandler.generatePayload(Errors.KV_FAILURE, null, -1, -1, -1, -1);
    			cachedValue = reqHandler.addToCache(receivedMessage.getMessageID(), responsePayload);
    			msgHandler.sendMessage(receivedMessage.getMessageID(), cachedValue, ip, port);
    		}
    	}
    }
}