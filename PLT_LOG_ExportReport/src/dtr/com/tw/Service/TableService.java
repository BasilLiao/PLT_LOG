package dtr.com.tw.Service;

import org.json.JSONArray;
import org.json.JSONObject;

import dtr.com.tw.Main;

public class TableService {
	// 針對Table轉型
	private static int i = 0;

	public static Object[][] createTable(JSONArray list) {
		if(list==null || list.length()<1) {
			Main.FLAG_Thead = true;
			return null;
		}
		i = 0;
		Object[][] data = new Object[list.length()][12];
		list.forEach(s -> {
			JSONObject one = new JSONObject();
			one = (JSONObject)s;
			data[i][0] = one.has("WorkOrder")?one.getString("WorkOrder"):"";
			data[i][1] = one.has("WorkModel")?one.getString("WorkModel"):"";
			data[i][2] = one.has("SN")?one.getString("SN"):"";
			data[i][3] = one.has("FileSize")?one.getInt("FileSize"):"";
			data[i][4] = one.has("BIOS")?one.getString("BIOS"):"";
			data[i][5] = one.has("IMEI")?one.getString("IMEI"):"";
			data[i][6] = one.has("EC")?one.getString("EC"):"";
			data[i][7] = one.has("ECN")?one.getString("ECN"):"";
			data[i][8] = one.has("MB SN")?one.getString("MB SN"):"";
			data[i][9] = one.has("LAN1 MAC")?one.getString("LAN1 MAC"):"";
			data[i][10] = one.has("LAN2 MAC")?one.getString("LAN2 MAC"):"";
			data[i][11] = one.has("WIFI MAC")?one.getString("WIFI MAC"):"";
			i += 1;
		});

		return data;
	}

}
