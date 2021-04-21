package dtr.com.tw.Service;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

public class ExcelService {

	public static XSSFWorkbook createExcelAll(JSONArray list) {

		// Step1.創建Excel
		XSSFWorkbook workbook = new XSSFWorkbook();
		// Step2.分頁建立
		XSSFSheet sheet = workbook.createSheet("製令與產品狀況表");
		// Step3.格式文字
		XSSFFont font = workbook.createFont();// Word Style setting
		font.setBold(true);

		XSSFCellStyle style;
		style = workbook.createCellStyle();// Set in the setting
		style.setFont(font);
		style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setBorderBottom(BorderStyle.THIN);

		// ========表標題========
		// 凍結欄位
		sheet.createFreezePane(1, 1, 1, 1);
		Row row = sheet.createRow(0);// 新添加(行數)
		row = sheet.createRow(0);// 新添加(行數)
		int colNum = 0;
		colNum = 0;// 欄數

		// Step5.更新表頭
		row.createCell(colNum++).setCellValue("製令工單號");
		row.createCell(colNum++).setCellValue("產品型號");
		row.createCell(colNum++).setCellValue("SN(出貨/產品)序號");
		row.createCell(colNum++).setCellValue("BIOS");
		row.createCell(colNum++).setCellValue("IMEI");
		row.createCell(colNum++).setCellValue("EC");
		row.createCell(colNum++).setCellValue("ECN");
		row.createCell(colNum++).setCellValue("M/B序號");
		row.createCell(colNum++).setCellValue("LAN1 MAC");
		row.createCell(colNum++).setCellValue("LAN2 MAC");
		row.createCell(colNum++).setCellValue("WIFI MAC");
		row.createCell(colNum++).setCellValue("SIZE(Byte)");

		// 樣式添加+設置寬度
		for (int i = 0; i < colNum; i++) {
			// 寬度
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 20 / 10);
			// 樣式
			row.getCell(i).setCellStyle(style);
		}
		// ========產生表身========
		int lineNumber = 1;
		int rowNum = 0;
		colNum = 0;
		for (Object cell : list) {
			JSONObject one = new JSONObject();
			one = (JSONObject) cell;
			colNum = 0;// 欄數
			row = sheet.createRow(lineNumber + (rowNum++));
			// 進行資料投入
			row.createCell(colNum++).setCellValue(one.getString("WorkOrder"));
			row.createCell(colNum++).setCellValue(one.getString("WorkModel"));
			row.createCell(colNum++).setCellValue(one.getString("SN"));
			row.createCell(colNum++).setCellValue(one.getString("BIOS"));
			row.createCell(colNum++).setCellValue(one.getString("IMEI"));
			row.createCell(colNum++).setCellValue(one.getString("EC"));
			row.createCell(colNum++).setCellValue(one.getString("ECN"));
			row.createCell(colNum++).setCellValue(one.getString("MB SN"));
			row.createCell(colNum++).setCellValue(one.getString("LAN1 MAC"));
			row.createCell(colNum++).setCellValue(one.getString("LAN2 MAC"));
			row.createCell(colNum++).setCellValue(one.getString("WIFI MAC"));
			row.createCell(colNum++).setCellValue(one.getLong("FileSize"));
		}
		return workbook;
	}
}