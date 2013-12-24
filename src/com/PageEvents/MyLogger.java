package com.PageEvents;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {
  static private FileHandler fileTxt;
  static private SimpleFormatter formatterTxt;

  static private FileHandler fileHTML;
  static private Formatter formatterHTML;

  static public void setup() throws IOException {

    // Get the global logger to configure it
    Logger logger = Logger.getLogger("");

    logger.setLevel(Level.INFO);
    Calendar cal = Calendar.getInstance();
    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String dateStr = sdf.format(cal.getTime());
    fileTxt = new FileHandler("log_" + dateStr + ".txt");
    fileHTML = new FileHandler("log_" + dateStr + ".html");

    // Create txt Formatter
    formatterTxt = new SimpleFormatter();
    fileTxt.setFormatter(formatterTxt);
    logger.addHandler(fileTxt);

    // Create HTML Formatter
    formatterHTML = new LogHTMLFormatter();
    fileHTML.setFormatter(formatterHTML);
    logger.addHandler(fileHTML);
  }
} 