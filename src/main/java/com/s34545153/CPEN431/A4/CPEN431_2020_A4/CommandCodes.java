package com.s34545153.CPEN431.A4.CPEN431_2020_A4;

public class CommandCodes {
	//Standard types
	public static final int PUT = 0x01;
	public static final int GET = 0x02;
	public static final int REMOVE = 0x03;
	public static final int SHUTDOWN = 0x04;
	public static final int WIPEOUT = 0x05;
	public static final int IS_ALIVE = 0x06;
	public static final int GET_PID = 0x07;
	public static final int GET_MEMBERSHIP_COUNT = 0x08;
	
	//Defined types
	public static final int OVERFLOW_TEST = 0x21;
	public static final int OOS_TEST = 0x22;
	public static final int KV_FAILURE_TEST = 0x23;
}
