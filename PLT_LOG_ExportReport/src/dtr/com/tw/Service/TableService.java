package dtr.com.tw.Service;

import org.json.JSONArray;
import org.json.JSONObject;

public class TableService {
	// 針對Table轉型
	private static int i = 0;

	public static Object[][] createTable(JSONArray list) {
		i = 0;
		Object[][] data = new Object[list.length()][12];
		list.forEach(s -> {
			JSONObject one = new JSONObject();
			one = (JSONObject)s;
			data[i][0] = one.getString("WorkOrder");
			data[i][1] = one.getString("WorkModel");
			data[i][2] = one.getString("SN");
			data[i][3] = one.getString("BIOS");
			data[i][4] = one.getString("IMEI");
			data[i][5] = one.getString("EC");
			data[i][6] = one.getString("ECN");
			data[i][7] = one.getString("MB SN");
			data[i][8] = one.getString("LAN1 MAC");
			data[i][9] = one.getString("LAN2 MAC");
			data[i][10] = one.getString("WIFI MAC");
			i += 1;
		});

		return data;
	}

}
