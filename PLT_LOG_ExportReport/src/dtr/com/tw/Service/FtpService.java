package dtr.com.tw.Service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONArray;
import org.json.JSONObject;

import dtr.com.tw.Main;

public class FtpService {
	private static FTPClient ftpClient = new FTPClient();
	public static JSONArray list_failed = new JSONArray();

	/**
	 * Description: 從FTP伺服器下載檔案
	 * 
	 * @Version1.0
	 * 
	 * @param url        FTP伺服器hostname
	 * @param port       FTP伺服器埠
	 * @param username   FTP登入賬號
	 * @param password   FTP登入密碼
	 * @param remotePath FTP伺服器上的相對路徑
	 * @param fileName   要下載的檔名
	 * @param localPath  下載後儲存到本地的路徑
	 * @return
	 */
	public static JSONArray downFile(String url, int port, String username, String password, String remotePath, String[] searchName,
			String localPath) {
		System.out.println(new Date());
		JSONArray list = new JSONArray();
		list_failed = new JSONArray();
		try {
			// 登入 如果採用預設埠，可以使用ftp.connect(url)的方式直接連線FTP伺服器
			ftpClient.connect(url, port);
			ftpClient.login(username, password);
			// 設定檔案傳輸型別為二進位制+UTF-8 傳輸
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.setControlEncoding("UTF-8");
			// 獲取ftp登入應答程式碼
			int reply = ftpClient.getReplyCode();
			// 驗證是否登陸成功
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				System.err.println("FTP server refused connection.");
				return null;
			}

			// 轉移到FTP伺服器目錄至指定的目錄下
			ftpClient.changeWorkingDirectory(new String(remotePath.getBytes("UTF-8"), "iso-8859-1"));

			// 獲取檔案列表(查詢)
			JSONObject one = new JSONObject();

			FTPFile[] fs = ftpClient.listFiles();
			ByteArrayOutputStream is = new ByteArrayOutputStream();
			String line = "";

			for (FTPFile ff : fs) {
				String[] f_n = ff.getName().split("_");
				// 比對_查詢條件
				// 符合條件
				if ((searchName[0].equals("") || f_n[0].indexOf(searchName[0]) != -1)
						&& (searchName[1].equals("") || f_n[1].indexOf(searchName[1]) != -1)
						&& (searchName[2].equals("") || f_n[2].indexOf(searchName[2]) != -1)) {
					/** 不使用下載 **/
					// File localFile = new File(localPath + "/" + ff.getName());
					// OutputStream is = new FileOutputStream(localFile);
					is.reset();
					ftpClient.retrieveFile(ff.getName(), is);
					BufferedReader bufferedReader = new BufferedReader(new StringReader(is.toString("UTF-8")));

					// 第一行抓取+檢驗 (檔頭BOM)去除 是否為正確
					line = bufferedReader.readLine();
					if (line != null && line.charAt(0) != '{') {
						line = line.substring(1);
					}

					// 字串 轉 JSON
					// System.out.println(line);
					// System.out.println(new Date() + " | " + ff.getName() + " " + nb++);
					// 如果異常為空
					try {
						new JSONObject(line);
					} catch (Exception ex) {
						one = new JSONObject();
						long file_size = ff.getSize();
						one.put("FileSize", file_size);
						one.put("WorkOrder", f_n[0]);
						one.put("WorkModel", f_n[1]);
						one.put("SN", f_n[2]);
						list.put(one);
						list_failed.put(one);
						continue;
					}
					one = new JSONObject(line);
					// 補型號/補主機板號/轉16進制
					one.put("WorkModel", f_n[1]);
					String mbSn[] = (one.getString("UUID").split("-"));
					one.put("MB SN", mbSn[mbSn.length - 1]);
					long file_size = ff.getSize();
					one.put("FileSize", file_size);
					list.put(one);

					// 關閉串流
					bufferedReader.close();
				}
			}
			is.close();
			ftpClient.logout();
		} catch (Exception e) {
			e.printStackTrace();
			Main.FLAG_Thead = true;
			return list;
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		System.out.println(new Date());
		return list;
	}
}