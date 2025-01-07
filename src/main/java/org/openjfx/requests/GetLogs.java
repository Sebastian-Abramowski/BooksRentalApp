package org.openjfx.requests;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.openjfx.database.Log;

public class GetLogs extends Request {
	public static ArrayList<Log> fromResult(ResultSet result) {
		ArrayList<Log> logs = new ArrayList<>();
		Log log;
		while((log = GetLog.fromResult(result)) != null) {
			logs.add(log);
		}
		return logs;
	}

	public static ArrayList<Log> request() {
		String query = "SELECT * FROM LOG ";
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}
}
