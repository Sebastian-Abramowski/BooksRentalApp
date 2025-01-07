package org.openjfx.database;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;

import org.openjfx.helpers.Searchable;

public class Log implements Searchable {
	private int id;
	private Timestamp date;
	private String operation_type;
	private String table_name;

	public Log(int id, Timestamp date, String operation_type, String table_name) {
		this.id = id;
		this.date = date;
		this.operation_type = operation_type;
		this.table_name = table_name;
	}

	public int getId() { return id; }
	public Timestamp getDate() { return date; }
	public String getOperationType() { return operation_type; }
	public String getTableName() { return table_name; }

	public List<String> getSearchParams() {
		List<String> searchParams = new ArrayList<>();
		searchParams.add(date.toString());
		searchParams.add(operation_type);
		searchParams.add(table_name);
		return searchParams;
	}
}

