package org.openjfx.requests;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetAmount extends Request{

	public static int request(){
		String query = "SELECT sum(amount) FROM BOOK";
		ResultSet result = executeRequest(query);
		int amount = -1;
		try {
			result.next();
			amount = result.getInt( 1 );
		} catch (SQLException e) {
			throw new RuntimeException( e );
		}
		return amount;
	}
}

