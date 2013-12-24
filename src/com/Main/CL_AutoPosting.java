package com.Main;

import java.io.IOException;
import java.util.logging.Logger;

public class CL_AutoPosting {
	
	private final static Logger LOGGER = Logger.getLogger(CL_AutoPosting.class.getName());

	public static void main(String[] args) {
		try {
			//Set Properties file
			LOGGER.info("Reading Properties File");
			Utilities.setProperties();
			
			//Set up Loggers			
			LOGGER.info("Setting up Loggers");
			Utilities.MyLogger_setup();
			
			ReadInputData();
			PostAds.PostAllAds();
			
		} catch (IOException e) {
			LOGGER.severe(e.getMessage() + "---" + org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
		}
		catch (Exception e) {
			LOGGER.severe(e.getMessage() + "---" + org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
		}
		
		try {
			WriteOutputData();
		} catch (Exception e) {
			LOGGER.severe(e.getMessage() + "---" + org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
		}
	}
	
	static void ReadInputData() throws IOException {
		//Reading All Accounts from Input xls
		LOGGER.info("Reading All Input XLS");
		SuperInputContent.ReadInput();
	}
	
	static void WriteOutputData() throws IOException {
		LOGGER.info("Writing Output");
		SuperInputContent.WriteOutput();
	}

}