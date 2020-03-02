package com.s34545153.CPEN431.A4.CPEN431_2020_A4.Commands;

import java.io.File;

public class GetPID {
	
	public static int run(){
		return getPID();
	}
	
    private static int getPID(){
    	try {
			return Integer.parseInt(new File("/proc/self").getCanonicalFile().getName());
		} catch (Exception e){
			return 0;
		}
    }
}
