package com.PageEvents;

@SuppressWarnings("serial")
public class MyException extends Exception{
	
	public static final String LOGIN_UNSUCCESSFUL = "Login Unsuccessful";
	public static final String PROXY_NOT_AVAILABLE = "Specified Location not available in Proxies sheet";
	public static final String NO_ACCOUNT_LEFT_FOR_AD = "No account left that could be used to post this ad";
	
	public MyException() {
	}
	 
	public MyException(String msg) {
		super(msg);
	}
	
	public MyException(String msg, Exception e) {
		super(msg, e);
	}
}
