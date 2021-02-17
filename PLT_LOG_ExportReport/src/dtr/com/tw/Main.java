package dtr.com.tw;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Year;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;

import dtr.com.tw.Service.ExcelService;
import dtr.com.tw.Service.ExportFileService;
import dtr.com.tw.Service.FtpService;
import dtr.com.tw.Service.TableService;

public class Main {

	private JFrame frmDtrPltLog;
	private JTextField textField_0;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JLabel lblNewLabel_5;
	private JTable table;
	private JScrollPane scrollPane_0;
	private JLabel lblNewLabel;
	private JLabel lblPassword;
	private JTextField textField;
	private JPasswordField passwordField;
	private JTextPane txtpnna_0;

	private static String ftpHost = "";
	private static String ftpUserName = "";
	private static String ftpPassword = "";
	private static Integer ftpPort = 21;
	private static String ftpPath = "";
	private Integer year = 2021;
	private static String localPath = "";
	private static boolean FLAG_Thead = true;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmDtrPltLog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		String[] columns = { "工單號", "型號", "SN(產品)", "BIOS", "IMEI", "EC", "ECN", "M/B序號", "LAN1 MAC", "LAN2 MAC",
				"WIFI MAC" };
		Object[][] data = new Object[1][11];
		// step0.抓取properties 內容
		String propFileName = "config.properties";
		Properties prop = new Properties();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		try {
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			txtpnna_0.setText(e1.toString());
		}

		// Step1. 取得參數(config.properties. FTP ...等等)
		ftpHost = prop.getProperty("ftp.ftpHost");
		ftpUserName = prop.getProperty("ftp.ftpUserName");
		ftpPassword = prop.getProperty("ftp.ftpPassword");
		ftpPort = Integer.parseInt(prop.getProperty("ftp.ftpPort"));
		ftpPath = prop.getProperty("ftp.ftpPath");
		localPath = prop.getProperty("ftp.localPath");
		System.out.println("Step1.config.properties 抓取_參數");

		frmDtrPltLog = new JFrame();
		frmDtrPltLog.setTitle("DTR PLT Log to Excel Beta v0.01.0 by Digital Signage");
		frmDtrPltLog.setFont(new Font("Dialog", Font.BOLD, 16));
		frmDtrPltLog.setBounds(100, 100, 1300, 700);
		frmDtrPltLog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDtrPltLog.getContentPane().setLayout(null);

		textField_0 = new JTextField();
		textField_0.setFont(new Font("新細明體", Font.PLAIN, 16));
		textField_0.setBounds(198, 72, 206, 35);
		frmDtrPltLog.getContentPane().add(textField_0);
		textField_0.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setFont(new Font("新細明體", Font.PLAIN, 16));
		textField_1.setBounds(198, 117, 206, 35);
		frmDtrPltLog.getContentPane().add(textField_1);
		textField_1.setColumns(10);

		JLabel lblNewLabel_0 = new JLabel("製令單號");
		lblNewLabel_0.setFont(new Font("新細明體", Font.PLAIN, 16));
		lblNewLabel_0.setBounds(10, 72, 178, 35);
		frmDtrPltLog.getContentPane().add(lblNewLabel_0);

		JLabel lblNewLabel_1 = new JLabel("機種型號");
		lblNewLabel_1.setFont(new Font("新細明體", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(10, 117, 178, 35);
		frmDtrPltLog.getContentPane().add(lblNewLabel_1);

		txtpnna_0 = new JTextPane();
		txtpnna_0.setFont(new Font("新宋体", Font.PLAIN, 16));
		txtpnna_0.setText("說明 : Log to Excel = 輸出Excel/Log to Table = 顯示在Table上 \n有任何問題 管理員 請打分機:321 ");
		txtpnna_0.setBounds(10, 207, 1266, 108);
		frmDtrPltLog.getContentPane().add(txtpnna_0);

		JButton btnNewButton_0 = new JButton("Search_Log_to_Excel");
		btnNewButton_0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (FLAG_Thead) {
					FLAG_Thead = false;
					new Thread() {
						public void run() {

							// Step2. 查詢項目
							String mk_id = textField_0.getText().trim();// 製令單 ID
							String mk_model = textField_1.getText().trim();// 型號 名稱
							String mk_sn = textField_2.getText().trim();// SN產品 序號
							String[] search = { mk_id, mk_model, mk_sn };
							String txt_show = "說明 : Log to Excel = 輸出Excel/Log to Table = 顯示在Table上 ";
							txt_show += "\n有任何問題 管理員 請打分機:321 ";
							txt_show += " \n製令單_ : " + mk_id + " _機種型號_ : " + mk_model + " _SN產品序號_ : " + mk_sn;
							txtpnna_0.setText(txt_show);
							System.out.println(
									"Step2.製令單_ : " + mk_id + " _機種型號_ : " + mk_model + " _SN產品序號_ : " + mk_sn);

							// Step3. 執行項目 LOG to JSONArray
							year = Year.now().getValue();
							JSONArray list = FtpService.downFile(ftpHost, ftpPort, ftpUserName, ftpPassword,
									ftpPath + year + '/', search, localPath);
							// Step4. 執行項目 JSONArray to Excel
							XSSFWorkbook workbook = ExcelService.createExcelAll(list);
							// Step4. 執行項目 Excel to File
							ExportFileService.createExcelFile(workbook, "PLT_Excel");

							txt_show += " \ndone...";
							txtpnna_0.setText(txt_show);
							FLAG_Thead = true;
						}
					}.start();
				}
			}
		});
		btnNewButton_0.setBounds(1080, 117, 184, 35);
		frmDtrPltLog.getContentPane().add(btnNewButton_0);

		JLabel lblNewLabel_2 = new JLabel("SN(產品/出貨)序號");
		lblNewLabel_2.setFont(new Font("新細明體", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(10, 162, 178, 35);
		frmDtrPltLog.getContentPane().add(lblNewLabel_2);

		textField_2 = new JTextField();
		textField_2.setFont(new Font("新細明體", Font.PLAIN, 16));
		textField_2.setColumns(10);
		textField_2.setBounds(198, 162, 206, 35);
		frmDtrPltLog.getContentPane().add(textField_2);

		textField_3 = new JTextField();
		textField_3.setFont(new Font("新細明體", Font.PLAIN, 16));
		textField_3.setColumns(10);
		textField_3.setBounds(118, 13, 174, 35);
		frmDtrPltLog.getContentPane().add(textField_3);

		textField_4 = new JTextField();
		textField_4.setFont(new Font("新細明體", Font.PLAIN, 16));
		textField_4.setColumns(10);
		textField_4.setBounds(350, 13, 52, 35);
		frmDtrPltLog.getContentPane().add(textField_4);

		textField_5 = new JTextField();
		textField_5.setFont(new Font("新細明體", Font.PLAIN, 16));
		textField_5.setColumns(10);
		textField_5.setBounds(474, 13, 184, 35);
		frmDtrPltLog.getContentPane().add(textField_5);

		JLabel lblNewLabel_3 = new JLabel("FTP設定_IP:");
		lblNewLabel_3.setFont(new Font("新細明體", Font.PLAIN, 16));
		lblNewLabel_3.setBounds(10, 13, 98, 35);
		frmDtrPltLog.getContentPane().add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("Port:");
		lblNewLabel_4.setFont(new Font("新細明體", Font.PLAIN, 16));
		lblNewLabel_4.setBounds(302, 13, 38, 35);
		frmDtrPltLog.getContentPane().add(lblNewLabel_4);

		lblNewLabel_5 = new JLabel(" Path:");
		lblNewLabel_5.setFont(new Font("新細明體", Font.PLAIN, 16));
		lblNewLabel_5.setBounds(412, 13, 52, 35);
		frmDtrPltLog.getContentPane().add(lblNewLabel_5);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 58, 1286, 4);
		frmDtrPltLog.getContentPane().add(separator);

		JButton btnNewButton_1 = new JButton("Search_Log_to_Table");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (FLAG_Thead) {
					FLAG_Thead = false;
					new Thread() {
						public void run() {
							// Step2. 查詢項目
							String mk_id = textField_0.getText().trim();// 製令單 ID
							String mk_model = textField_1.getText().trim();// 型號 名稱
							String mk_sn = textField_2.getText().trim();// SN產品 序號
							String[] search = { mk_id, mk_model, mk_sn };
							String txt_show = "說明 : Log to Excel = 輸出Excel/Log to Table = 顯示在Table上 ";
							txt_show += "\n有任何問題 管理員 請打分機:321 ";
							txt_show += "\n製令單_ : " + mk_id + " _機種型號_ : " + mk_model + " _SN產品序號_ : " + mk_sn;
							txtpnna_0.setText(txt_show);

							System.out.println(
									"Step2.製令單_ : " + mk_id + " _機種型號_ : " + mk_model + " _SN產品序號_ : " + mk_sn);
							// Step3. 執行項目 LOG to JSONArray
							year = Year.now().getValue();
							JSONArray list = FtpService.downFile(ftpHost, ftpPort, ftpUserName, ftpPassword,
									ftpPath + year + '/', search, localPath);
							System.out.println("Step3.查詢清單_ : " + list.toString());
							// Step4. 執行項目 JSONArray to Object[][]
							Object[][] data = TableService.createTable(list);
							// Step5. 取代顯示Table
							table = new JTable(data, columns);
							table.setRowHeight(30);
							table.setFont(new Font("新宋体", Font.BOLD, 16));
							table.getTableHeader().setFont(new Font("新宋体", Font.BOLD, 16));
							table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
							table.getColumnModel().getColumn(0).setMinWidth(140);
							table.getColumnModel().getColumn(1).setMinWidth(80);
							table.getColumnModel().getColumn(2).setMinWidth(140);
							table.getColumnModel().getColumn(3).setMinWidth(140);
							table.getColumnModel().getColumn(4).setMinWidth(150);
							table.getColumnModel().getColumn(5).setMinWidth(110);
							table.getColumnModel().getColumn(6).setMinWidth(300);
							table.getColumnModel().getColumn(7).setMinWidth(130);
							table.getColumnModel().getColumn(8).setMinWidth(170);
							table.getColumnModel().getColumn(9).setMinWidth(170);
							table.getColumnModel().getColumn(10).setMinWidth(170);

							scrollPane_0.setViewportView(table);
							
							
							txt_show += " \ndone...";
							txtpnna_0.setText(txt_show);
							FLAG_Thead = true;
						}
					}.start();
				}
			}
		});
		btnNewButton_1.setBounds(1080, 72, 184, 35);
		frmDtrPltLog.getContentPane().add(btnNewButton_1);

		scrollPane_0 = new JScrollPane();
		scrollPane_0.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_0.setBounds(10, 339, 1266, 314);
		frmDtrPltLog.getContentPane().add(scrollPane_0);

		table = new JTable(data, columns);
		table.setEnabled(false);
		table.setBounds(0, 0, 1600, 500);
		table.setFont(new Font("新宋体", Font.BOLD, 16));
		table.getTableHeader().setFont(new Font("新宋体", Font.BOLD, 16));
		table.setBackground(Color.WHITE);
		table.setRowHeight(30);
		scrollPane_0.setViewportView(table);
		

		lblNewLabel = new JLabel("Account:");
		lblNewLabel.setFont(new Font("新細明體", Font.PLAIN, 16));
		lblNewLabel.setBounds(668, 13, 69, 35);
		frmDtrPltLog.getContentPane().add(lblNewLabel);

		lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("新細明體", Font.PLAIN, 16));
		lblPassword.setBounds(863, 13, 69, 35);
		frmDtrPltLog.getContentPane().add(lblPassword);

		textField = new JTextField();
		textField.setFont(new Font("新細明體", Font.PLAIN, 16));
		textField.setColumns(10);
		textField.setBounds(747, 13, 106, 35);
		frmDtrPltLog.getContentPane().add(textField);

		passwordField = new JPasswordField();
		passwordField.setBounds(942, 13, 105, 35);
		frmDtrPltLog.getContentPane().add(passwordField);

		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(new Color(160, 160, 160));
		separator_1.setBounds(0, 325, 1286, 4);
		frmDtrPltLog.getContentPane().add(separator_1);

	}
}