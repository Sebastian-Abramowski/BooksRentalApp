package org.openjfx.requests;
import org.openjfx.database.ErrorHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PrintBorrowStats extends Request {
	public static void fromResult(ResultSet result) {
		try {
			while (result.next()) {
				System.out.printf( "Year = %d, Month = %d, Total = %d%n"
						, result.getInt( 1 ), result.getInt( 2 ), result.getInt( 3 ));
			}
		}
		catch(SQLException e) {
			new ErrorHandler(e);
		}
	}

	public static void print() {
		String query = "SELECT * FROM BORROW_STAT ";
		ResultSet result = executeRequest(query);
		fromResult(result);
	}
}
