package org.openjfx.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DatabaseTest {
	private Database database;

	@Before
	public void setup() {
		database = Database.getInstance();

		int status = database.executeUpdate("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255))");
		assertEquals(0, status);
	}

	@After
	public void teardown() {
		int status = database.executeUpdate("DROP TABLE test_table");
		assertEquals(0, status);

		database.close();
	}

	@Test
	public void testExecuteQuery() {
		database.executeUpdate("INSERT INTO test_table VALUES (1, 'Test')");

		ResultSet resultSet = database.executeQuery("SELECT * FROM test_table");
		assertNotNull(resultSet);

		try {
			assertTrue(resultSet.next());
			assertEquals(1, resultSet.getInt("id"));
			assertEquals("Test", resultSet.getString("name"));
		} catch(SQLException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testExecuteUpdate() {
		int inserted = database.executeUpdate("INSERT INTO test_table VALUES (1, 'Test')");
		assertEquals(1, inserted);

		inserted = database.executeUpdate("INSERT INTO test_table VALUES (2, 'Test2')");
		assertEquals(1, inserted);

		int deleted = database.executeUpdate("DELETE FROM test_table WHERE name LIKE 'Test%'");
		assertEquals(2, deleted);
	}

	/*
	 * If this fails, you've seriously fucked up. Run DatabaseInitializer.
	 */
	@Test
	public void testTables() {
		Boolean status = database.tableExists("USER");
		assertEquals(true, status);

		status = database.tableExists("BOOK");
		assertEquals(true, status);

		status = database.tableExists("WISH");
		assertEquals(true, status);
	}
}
