package com.Main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

//import com.PageEvents.CLEvents;
import com.PageEvents.MyLogger;

public class Utilities {
	
	public static ArrayList<SuperInputContent> allAds;
	public static Properties props;
	//public static CLEvents clEvents;
	
	static void MyLogger_setup() throws IOException{
		try {
			MyLogger.setup();
		} catch(IOException e) {
			throw new IOException("Problem with creating log files");
		}
	}

	static void setProperties() throws IOException {
		props = new Properties();
		props.load(new FileInputStream("Properties\\config.properties"));

		System.setProperty("webdriver.firefox.bin", Utilities.props.getProperty("FirefoxPath"));
	}
	
	public static void waitForSecs(int secs) {
		try {
			Thread.sleep(secs*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
