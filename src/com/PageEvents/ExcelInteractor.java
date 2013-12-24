package com.PageEvents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.Main.Utilities;

public class ExcelInteractor {
	
	public static HSSFSheet ReadXLSReturnSheet(int sheetIndex) throws IOException {
		try {
			FileInputStream file = new FileInputStream(new File(Utilities.props.getProperty("InputXLSPath")));
			HSSFWorkbook workbook = new HSSFWorkbook(file);
			HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
			return sheet;
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("Input xls file not found");
		} catch (IOException e) {
			throw new IOException("Problem in interacting with Input xls", e);
		}
	}
}