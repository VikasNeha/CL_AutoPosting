package com.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

import com.PageEvents.ExcelInteractor;

public class SuperInputContent {
	public String Email = "";
	public String Gmail_Password = "";
	public String CL_Password = "";
	
	public String Location = "";
	public String Title = "";
	public String Price = "";
	public String Specific_Location = "";
	public String Description = "";
	
	public String Year = "";
	public String MakeModel = "";
	public String Odometer = ""; 
	
	public String Proxy = "";
	public String Proxy_Port = "";
	public String Proxy_Username = "";
	public String Proxy_Password = "";
	
	public boolean result = false;
	public String resultComment = "Not Processed";
	
	public String imageFolder = "";
	public String imagePath = "";
	public String outputImagePath = "";
	
	public String phone = "";
	
	public int row;
	
	static void ReadInput() throws IOException {
		//Initialize global accounts variable
		Utilities.allAds = new ArrayList<SuperInputContent>();
		
		//Read input xls and Accounts sheet
		HSSFSheet sheet = ExcelInteractor.ReadXLSReturnSheet(0);
		
		//Iterate through Rows
		Iterator<Row> rowIterator = sheet.iterator();
		while(rowIterator.hasNext()) {
			SuperInputContent currentAd = new SuperInputContent();
			Row row = rowIterator.next();
			if (row.getRowNum() == 0)
				continue;
			
			//Iterate through each cell of a row
			Iterator<Cell> cellIterator = row.iterator();
			boolean continueWithLoopFlag = false;
			while(cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				DataFormatter formatter = new DataFormatter();
	            switch(cell.getColumnIndex()) {
				case 0: 
					currentAd.Proxy = cell.getStringCellValue().trim();
					break;
				case 1:
					currentAd.Proxy_Port = formatter.formatCellValue(cell).trim();
					break;
				case 2:
					currentAd.Proxy_Username = cell.getStringCellValue().trim();
					break;
				case 3:
					currentAd.Proxy_Password = cell.getStringCellValue().trim();
					break;
				case 4:
					currentAd.Email = cell.getStringCellValue().trim();
					if (currentAd.Email.trim().isEmpty())
						continueWithLoopFlag = true;
					break;
				case 5:
					currentAd.Gmail_Password = cell.getStringCellValue().trim();
					break;
				case 6:
					currentAd.CL_Password = cell.getStringCellValue().trim();
					break;
				case 7:
					currentAd.Location = cell.getStringCellValue().trim();
					break;
				case 8:
					currentAd.Title = cell.getStringCellValue().trim();
					break;
				case 9:
					currentAd.Price = formatter.formatCellValue(cell).trim();
					break;
				case 10:
					currentAd.Specific_Location = formatter.formatCellValue(cell).trim();
					break;
				case 11:
					currentAd.Description = cell.getStringCellValue().trim();
					break;
				case 12:
					currentAd.Year = formatter.formatCellValue(cell).trim();
					break;
				case 13:
					currentAd.MakeModel = formatter.formatCellValue(cell).trim();
					break;
				case 14:
					currentAd.Odometer = formatter.formatCellValue(cell).trim();
					break;
				case 15:
					currentAd.imageFolder = formatter.formatCellValue(cell).trim();;
					if (!(currentAd.imageFolder.isEmpty())) {
						String imagePathDir = Utilities.props.getProperty("ImagePath");
						currentAd.imagePath = imagePathDir + "\\" + currentAd.imageFolder;
					}
					break;
				case 16:
					currentAd.phone = formatter.formatCellValue(cell).trim();
					break;
				}
			}
			if (continueWithLoopFlag == true)
				continue;
			currentAd.row = row.getRowNum();
			Utilities.allAds.add(currentAd);
		}
	}

	static void WriteOutput() throws IOException {
		try {
			FileInputStream file = new FileInputStream(new File(Utilities.props.getProperty("InputXLSPath")));
			HSSFWorkbook workbook = new HSSFWorkbook(file);
			HSSFSheet sheet = workbook.getSheetAt(0);
			Cell cell = null;
			
			HSSFCellStyle failedCellStyle = workbook.createCellStyle();
			failedCellStyle.setFillBackgroundColor(new HSSFColor.RED().getIndex());
			
			for (SuperInputContent currentAd : Utilities.allAds) {
				String result = "FAIL";
				String resultComment = "";
				String outputImagePath = "";
				try {
					if (currentAd.result == true)
						result = "PASS";
					else
						result = "FAIL";
					resultComment = currentAd.resultComment;
					outputImagePath = System.getProperty("user.dir") + "\\" + currentAd.outputImagePath;
				} catch (NullPointerException e) {
					result = "FAIL";
				}
				cell = sheet.getRow(currentAd.row).getCell(17, Row.CREATE_NULL_AS_BLANK);
				cell.setCellValue(result);
				if (result == "FAIL") 
					cell.setCellStyle(failedCellStyle);
				
				cell = sheet.getRow(currentAd.row).getCell(18, Row.CREATE_NULL_AS_BLANK);
				cell.setCellValue(resultComment);
				
				CellStyle hlink_style = workbook.createCellStyle();
		        Font hlink_font = workbook.createFont();
		        hlink_font.setUnderline(Font.U_SINGLE);
		        hlink_font.setColor(IndexedColors.BLUE.getIndex());
		        hlink_style.setFont(hlink_font);
				CreationHelper createHelper = workbook.getCreationHelper();
				Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_FILE);
				
				cell = sheet.getRow(currentAd.row).getCell(19, Row.CREATE_NULL_AS_BLANK);
				cell.setCellValue(outputImagePath);
				link.setAddress(outputImagePath);
				cell.setHyperlink(link);
				cell.setCellStyle(hlink_style);	
			}
			
			file.close();
			
			FileOutputStream outFile = new FileOutputStream(new File(Utilities.props.getProperty("InputXLSPath")));
		    workbook.write(outFile);
		    outFile.close();
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("Input xls file not found");
		} catch (IOException e) {
			throw new IOException("Problem in interacting with Input xls", e);
		}
	}

}
