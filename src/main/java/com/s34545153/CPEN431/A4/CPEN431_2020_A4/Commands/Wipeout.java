package com.s34545153.CPEN431.A4.CPEN431_2020_A4.Commands;

import com.s34545153.CPEN431.A4.CPEN431_2020_A4.App;

public class Wipeout {
	
	public static void run(){
		wipeout();
	}
	
	private static void wipeout(){
		App.keyValue.clear();
	}
}
