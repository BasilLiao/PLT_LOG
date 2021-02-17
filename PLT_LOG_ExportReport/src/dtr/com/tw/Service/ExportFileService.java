package dtr.com.tw.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExportFileService {
	public static String FILE_NAME = "";

	// 產生表檔
	public static boolean createExcelFile(XSSFWorkbook workbook, String fileName) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date)); // 2016/11/16 12:08:43
		boolean check = false;
		try {
			// 取得位置
			String current = new java.io.File(".").getCanonicalPath();
			System.out.println(current);
			FILE_NAME = current + "\\" + fileName + " D_" + dateFormat.format(date) + ".xlsx";
			// 製造檔案
			FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();
			check = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return check;
	}
}