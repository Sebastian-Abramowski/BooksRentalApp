package org.openjfx.database;

import java.sql.*;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import org.h2.api.Trigger;
import org.openjfx.requests.*;


/*
 * Uses the Singleton pattern. The database is initialized the first time
 * getInstance() is called. Then it's available until close() is called.
 */
public class Database {
	private static Database instance = null;
	private Connection connection;
	List<String> tableNames = Arrays.asList(
			"CATEGORY",
			"BOOK",
			"BOOK_AUTHOR",
			"USER",
			"CLIENT",
			"ADMIN",
			"BOOK_INSTANCE",
			"WISH",
			"BORROW",
			"ACTIVE_BORROW",
			"ARCHIVED_BORROW",
			"PERSON"
	);
	private Database() {

		try {
			Class.forName("org.h2.Driver");
			connection = DriverManager.getConnection("jdbc:h2:./database/main;DB_CLOSE_DELAY=-1", "sa", "");
		} catch(ClassNotFoundException | SQLException e) {
			new ErrorHandler(e);
		}
	}

	/*
	 * Call this to access the database. Kind of like this:
	 *
	 * Database database = Database.getInstance();
	 */
	public static synchronized Database getInstance() {
		if(instance == null) {
			instance = new Database();
		}

		return instance;
	}

	/*
	 * DO NOT CALL THIS! I've already called it. It purges the entire database.
	 */
	public void createDatabase(Boolean areYouSure) {
		if(!areYouSure) {
			return; /* Silly jokes.  */
		}

		executeUpdate("DROP ALL OBJECTS");

		executeUpdate("CREATE TABLE CATEGORY (" +
				"id INT PRIMARY KEY AUTO_INCREMENT NOT NULL," +
				"name VARCHAR(25) NOT NULL" +
				")");

		executeUpdate("CREATE TABLE BOOK (" +
				"id INT PRIMARY KEY AUTO_INCREMENT NOT NULL," +
				"isbn VARCHAR(13) UNIQUE NOT NULL," +
				"title VARCHAR(75) NOT NULL," +
				"rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5)," +
				"category_id INT NOT NULL," +
				"FOREIGN KEY (category_id) REFERENCES CATEGORY(id) ON DELETE CASCADE" +
				")");

		executeUpdate("CREATE TABLE PERSON (" +
				"id INT PRIMARY KEY AUTO_INCREMENT NOT NULL," +
				"name VARCHAR(20) NOT NULL," +
				"surname VARCHAR(20) NOT NULL," +
				"UNIQUE (name, surname)" +
				")");

		executeUpdate("CREATE TABLE BOOK_AUTHOR (" +
				"book_id INT NOT NULL," +
				"person_id INT NOT NULL," +
				"PRIMARY KEY (book_id, person_id)," +
				"FOREIGN KEY (book_id) REFERENCES BOOK(id) ON DELETE CASCADE," +
				"FOREIGN KEY (person_id) REFERENCES PERSON(id) ON DELETE CASCADE" +
				")");

		executeUpdate("CREATE TABLE \"USER\" (" +
				"id INT PRIMARY KEY AUTO_INCREMENT NOT NULL," +
				"login VARCHAR(20) UNIQUE NOT NULL," +
				"password VARCHAR(64) NOT NULL," +
				"person_id INT NOT NULL," +
				"FOREIGN KEY (person_id) REFERENCES PERSON(id) ON DELETE CASCADE" +
				")");

		executeUpdate("CREATE TABLE CLIENT (" +
				"id INT PRIMARY KEY NOT NULL," +
				"phone_number VARCHAR(9) UNIQUE NOT NULL," +
				"FOREIGN KEY (id) REFERENCES \"USER\"(id) ON DELETE CASCADE" +
				")");

		executeUpdate("CREATE TABLE ADMIN (" +
				"id INT PRIMARY KEY NOT NULL," +
				"FOREIGN KEY (id) REFERENCES \"USER\"(id) ON DELETE CASCADE" +
				")");

		executeUpdate("CREATE TABLE BOOK_INSTANCE (" +
				"id INT PRIMARY KEY AUTO_INCREMENT NOT NULL," +
				"book_id INT NOT NULL," +
				"FOREIGN KEY (book_id) REFERENCES BOOK(id) ON DELETE CASCADE" +
				")");

		executeUpdate("CREATE TABLE WISH (" +
				"id INT PRIMARY KEY AUTO_INCREMENT NOT NULL," +
				"book_id INT NOT NULL," +
				"days INT NOT NULL," +
				"date DATE NOT NULL DEFAULT CURRENT_DATE," +
				"client_id INT NOT NULL," +
				"UNIQUE (book_id, client_id)," +
				"FOREIGN KEY (book_id) REFERENCES BOOK(id) ON DELETE CASCADE," +
				"FOREIGN KEY (client_id) REFERENCES CLIENT(id) ON DELETE CASCADE" +
				")");

		executeUpdate("CREATE TABLE BORROW (" +
				"id INT PRIMARY KEY AUTO_INCREMENT NOT NULL," +
				"book_instance_id INT NOT NULL," +
				"days INT NOT NULL," +
				"date DATE NOT NULL DEFAULT CURRENT_DATE," +
				"acknowledged BOOLEAN NOT NULL DEFAULT false," +
				"approver_id INT NOT NULL," +
				"client_id INT NOT NULL," +
				"FOREIGN KEY (book_instance_id) REFERENCES BOOK_INSTANCE(id) ON DELETE CASCADE," +
				"FOREIGN KEY (approver_id) REFERENCES ADMIN(id) ON DELETE CASCADE," +
				"FOREIGN KEY (client_id) REFERENCES CLIENT(id) ON DELETE CASCADE" +
				")");

		executeUpdate("CREATE TABLE ACTIVE_BORROW (" +
				"id INT PRIMARY KEY NOT NULL," +
				"FOREIGN KEY (id) REFERENCES BORROW(id) ON DELETE CASCADE" +
				")");

		executeUpdate("CREATE TABLE ARCHIVED_BORROW (" +
				"id INT PRIMARY KEY NOT NULL," +
				"date DATE NOT NULL DEFAULT CURRENT_DATE," +
				"approver_id INT NOT NULL," +
				"FOREIGN KEY (id) REFERENCES BORROW(id) ON DELETE CASCADE," +
				"FOREIGN KEY (approver_id) REFERENCES ADMIN(id) ON DELETE CASCADE" +
				")");

		executeUpdate("CREATE TABLE LOG (" +
				"id INT PRIMARY KEY AUTO_INCREMENT NOT NULL," +
				"date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
				"operation_type VARCHAR(10) NOT NULL," +
				"target_table VARCHAR(15) NOT NULL" +
				")");

		executeUpdate("CREATE TABLE BORROW_STAT (" +
				"\"year\" INT NOT NULL," +
				"\"month\" INT NOT NULL," +
				"PRIMARY KEY(\"year\", \"month\")," +
				"total INT NOT NULL" +

				")");

		createLogTriggers();
		createTrigger( getBorrowTriggerTemplate() );
		createTrigger( getBorrowStatTrigger() );


		executeUpdate("INSERT INTO CATEGORY (name) VALUES " +
		/*  1 */"('Fiction')," +
		/*  2 */"('Dystopian')," +
		/*  3 */"('Classic')," +
		/*  4 */"('Fantasy')," +
		/*  5 */"('Coming of Age')," +
		/*  6 */"('Mystery')," +
		/*  7 */"('Philosophical Fiction')," +
		/*  8 */"('Romance')," +
		/*  9 */"('Children')," +
		/* 10 */"('Science Fiction')," +
		/* 11 */"('Horror')," +
		/* 12 */"('Biography')," +
		/* 13 */"('Other')"
				);

		executeUpdate("INSERT INTO PERSON (name, surname) VALUES " +
		/*  1 */"('Franciszek', 'Balcerak')," +
		/*  2 */"('Bogumił', 'Stoma')," +
		/*  3 */"('Aleksander', 'Stanoch')," +
		/*  4 */"('Sebastian', 'Abramowski')," +
		/*  5 */"('Patryk', 'Zdziech')," +
		/*  6 */"('Harper', 'Lee')," +
		/*  7 */"('George', 'Orwell')," +
		/*  8 */"('F. Scott', 'Fitzgerald')," +
		/*  9 */"('J.R.R.', 'Tolkien')," +
		/* 10 */"('J.D.', 'Salinger')," +
		/* 11 */"('Dan', 'Brown')," +
		/* 12 */"('Paulo', 'Coelho')," +
		/* 13 */"('Jane', 'Austen')," +
		/* 14 */"('Aldous', 'Huxley')," +
		/* 15 */"('Ray', 'Bradbury')," +
		/* 16 */"('John', 'Steinbeck')," +
		/* 17 */"('Ayn', 'Rand')," +
		/* 18 */"('J.K.', 'Rowling')," +
		/* 19 */"('Neil', 'Gaiman')," +
		/* 20 */"('Terry', 'Pratchett')," +
		/* 21 */"('George R.R.', 'Martin')," +
		/* 22 */"('Homer', '')," +
		/* 23 */"('Alex', 'Michaelides')"
				);

		executeUpdate("INSERT INTO \"USER\" (login, password, person_id) VALUES " +
		/*  1 */"('franek', '123', 1)," +
		/*  2 */"('boguś', '123', 2)," +
		/*  3 */"('olek', '123', 3)," +
		/*  4 */"('seba', '123', 4)," +
		/*  5 */"('patryk', '123', 5)"
				);

		executeUpdate("INSERT INTO ADMIN (id) VALUES " +
		/*  1 */"(1)," +
		/*  2 */"(2)"
				);

		executeUpdate("INSERT INTO CLIENT (id, phone_number) VALUES " +
		/*  1 */"(3, '123456789')," +
		/*  2 */"(4, '987654321')," +
		/*  3 */"(5, '123123123')"
				);

		executeUpdate("INSERT INTO BOOK (isbn, title, rating, category_id) VALUES " +
		/*  1 */"('9780061120084', 'To Kill a Mockingbird', 2, 1)," +
		/*  2 */"('9780451524935', '1984', 4, 2)," +
		/*  3 */"('9780743273565', 'The Great Gatsby', 5, 3)," +
		/*  4 */"('9780547928227', 'The Hobbit', 5, 4)," +
		/*  5 */"('9780316769488', 'The Catcher in the Rye', 3, 5)," +
		/*  6 */"('9780385514244', 'The Da Vinci Code', 4, 6)," +
		/*  7 */"('9780062502179', 'The Alchemist', 4, 7)," +
		/*  8 */"('9780141439471', 'Pride and Prejudice', 5, 8)," +
		/*  9 */"('9780060935467', 'Brave New World', 4, 9)," +
		/* 10 */"('9780007117116', 'Fahrenheit 451', 4, 10)," +
		/* 11 */"('9780544003415', 'The Lord of the Rings', 5, 4)," +
		/* 12 */"('9780141040370', 'Sense and Sensibility', 2, 8)," +
		/* 13 */"('9780143039433', 'The Grapes of Wrath', 3, 3)," +
		/* 14 */"('9780451191151', 'Atlas Shrugged', 4, 7)," +
		/* 15 */"('9780439708180', 'Harry Potter and the Philosophers Stone', 5, 4)," +
		/* 16 */"('9780060853983', 'Good Omens', 5, 4)," +
		/* 17 */"('9780553103540', 'A Song of Ice and Fire', 5, 4)," +
		/* 18 */"('9780140449116', 'The Iliad', 5, 4)," +
		/* 19 */"('9781250301697', 'The Silent Patient', 5, 6)," +
		/* 20 */"('9780060853976', 'Good Omens: The Nice and Accurate Prophecies of Agnes Nutter, Witch', 5, 4)"
				);

		executeUpdate("INSERT INTO BOOK_AUTHOR (book_id, person_id) VALUES " +
		/*  1 */"(1, 6)," +
		/*  2 */"(2, 7)," +
		/*  3 */"(3, 8)," +
		/*  4 */"(4, 9)," +
		/*  5 */"(5, 10)," +
		/*  6 */"(6, 11)," +
		/*  7 */"(7, 12)," +
		/*  8 */"(8, 13)," +
		/*  9 */"(9, 14)," +
		/* 10 */"(10, 15)," +
		/* 11 */"(11, 9)," +
		/* 12 */"(12, 13)," +
		/* 13 */"(13, 16)," +
		/* 14 */"(14, 17)," +
		/* 15 */"(15, 18)," +
		/* 16 */"(16, 19)," +
		/* 17 */"(16, 20)," +
		/* 18 */"(17, 21)," +
		/* 19 */"(18, 22)," +
		/* 20 */"(19, 23)," +
		/* 21 */"(20, 19)," +
		/* 22 */"(20, 20)"
				);

		executeUpdate("INSERT INTO BOOK_INSTANCE (book_id) VALUES " +
		/*  1 */"(3)," +
		/*  2 */"(17)," +
		/*  3 */"(18)," +
		/*  4 */"(2)," +
		/*  5 */"(4)," +
		/*  6 */"(15)," +
		/*  7 */"(17)," +
		/*  8 */"(15)," +
		/*  9 */"(9)," +
		/* 10 */"(9)," +
		/* 11 */"(16)," +
		/* 12 */"(10)," +
		/* 13 */"(18)," +
		/* 14 */"(16)," +
		/* 15 */"(13)," +
		/* 16 */"(6)," +
		/* 17 */"(8)," +
		/* 18 */"(16)," +
		/* 19 */"(6)," +
		/* 20 */"(1)," +
		/* 21 */"(4)," +
		/* 22 */"(12)," +
		/* 23 */"(1)," +
		/* 24 */"(15)," +
		/* 25 */"(16)," +
		/* 26 */"(6)," +
		/* 27 */"(15)," +
		/* 28 */"(4)," +
		/* 29 */"(16)," +
		/* 30 */"(5)," +
		/* 31 */"(11)," +
		/* 32 */"(9)," +
		/* 33 */"(2)," +
		/* 34 */"(8)," +
		/* 35 */"(12)," +
		/* 36 */"(19)," +
		/* 37 */"(5)," +
		/* 38 */"(11)," +
		/* 39 */"(16)," +
		/* 40 */"(11)"
				);

		executeUpdate("INSERT INTO BORROW (book_instance_id, days, date, acknowledged, approver_id, client_id) VALUES " + "(19, 1, '2023-01-15', true, 1, 3)," +
		"(1, 1, '2023-04-10', true, 1, 3)," +
		"(2, 1, '2023-04-05', true, 1, 3)," +
		"(3, 1, '2023-04-20', true, 1, 3)," +
		"(4, 1, '2023-04-15', true, 1, 3)," +
		"(5, 1, '2023-04-10', true, 1, 3)," +
		"(6, 1, '2023-04-25', true, 1, 3)," +
		"(7, 1, '2023-04-10', true, 1, 3)," +
		"(8, 1, '2023-04-15', true, 1, 3)," +
		"(9, 1, '2023-03-05', true, 1, 3)," +
		"(10, 1, '2023-03-20', true, 1, 3)," +
		"(11, 1, '2023-03-10', true, 1, 3)," +
		"(12, 1, '2023-03-25', true, 1, 3)," +
		"(13, 1, '2023-01-15', true, 1, 3)," +
		"(14, 1, '2023-01-05', true, 1, 3)," +
		"(15, 1, '2023-01-20', true, 1, 3)," +
		"(16, 1, '2023-01-15', true, 1, 3)," +
		"(18, 1, '2023-01-10', true, 1, 3)," +
		"(19, 1, '2023-01-25', true, 1, 3)," +
		"(20, 1, '2023-01-10', true, 1, 3)");

		var borrows = GetBorrows.fromActiveBorrows( GetActiveBorrows.request() );
		for (var borrow: borrows){
			DelActiveBorrow.request( borrow, GetAdmin.request( 1 ) );
		}
		executeUpdate("INSERT INTO BORROW (book_instance_id, days, date, acknowledged, approver_id, client_id) VALUES " + "(36, 1, '2023-01-15', true, 1, 3)," +
						"(5, 1, '2022-04-10', false, 1, 3)");


		/* insert any new lines above this comment */
		/* if you change anything in this function, you will probably want to run Initializer.java */
	}

	public void printLogs() {
		ResultSet result = executeQuery( "SELECT * FROM LOG" );
		try{
		while(result.next()) {
			Timestamp date = result.getTimestamp(2);
			String operation = result.getString(3);
			String target = result.getString(4);
			System.out.println("Date: " + date + ", Operation: " + operation + ", Target: " + target);
			}
		}
		catch(SQLException e) {
			new ErrorHandler(e);
		}
	}

	private void createLogTriggers() {
		for (String table: tableNames){
			String query = getLogTriggerTemplate(table);
			createTrigger( query );
		}
	}

	private void createTrigger(String query) {
		try {
			connection.createStatement().execute(query);
		} catch (SQLException e) {
			throw new RuntimeException( e );
		}
	}

	private static String getLogTriggerTemplate(String table) {
		String queryTemplate = "CREATE TRIGGER trg_log_after_insert_@table " +
				"AFTER INSERT ON \"@table\" " +
				"FOR EACH ROW " +
				"CALL \"org.openjfx.database.Database$LogTrigger\"; " +
				"CREATE TRIGGER trg_log_after_update_@table " +
				"AFTER UPDATE ON \"@table\" " +
				"FOR EACH ROW " +
				"CALL \"org.openjfx.database.Database$LogTrigger\"; " +
				"CREATE TRIGGER trg_log_after_delete_@table " +
				"AFTER DELETE ON \"@table\" " +
				"FOR EACH ROW " +
				"CALL \"org.openjfx.database.Database$LogTrigger\"";
		queryTemplate = queryTemplate.replace("@table", table );
		return queryTemplate;
	}


	private static String getBorrowTriggerTemplate() {
		String queryTemplate;
		queryTemplate = "CREATE TRIGGER trg_after_insert_BORROW " +
				"AFTER INSERT ON \"BORROW\" " +
				"FOR EACH ROW " +
				"CALL \"org.openjfx.database.Database$BorrowTrigger\"";
		return queryTemplate;
	}

	private static String getBorrowStatTrigger() {
		String queryTemplate;
		queryTemplate = "CREATE TRIGGER trg_after_delete_ACTIVE_BORROW " +
				"BEFORE DELETE ON \"ACTIVE_BORROW\" " +
				"FOR EACH ROW " +
				"CALL \"org.openjfx.database.Database$ArchivedBorrowStatTrigger\"";
		return queryTemplate;
	}
	/*
	 * This is for SQL that DOES NOT CHANGE the database. Probably SELECT only.
	 *
	 * Look above at `createDatabase()` to see what tables there are.
	 */
	public ResultSet executeQuery(String sql) {
		try {
			return connection.prepareStatement(sql).executeQuery();
		} catch(SQLException e) {
			new ErrorHandler(e);
			return null;
		}
	}

	/*
	 * This is for SQL that CHANGES the database. Probably everything except SELECT.
	 *
	 * The return code is:
	 *  - <num of rows> for INSERT, UPDATE or DELETE
	 *  - 0 for CREATE or DROP, but you shouldn't use this outside of tests
	 *  - -1 for any error
	 */
	public int executeUpdate(String sql) {
		try {
			return connection.prepareStatement(sql).executeUpdate();
		} catch(SQLException e) {
			new ErrorHandler(e);
			return -1;
		}
	}

	/*
	 * Just for tests.
	 */
	public Boolean tableExists(String tableName) {
		try {
			ResultSet tables = connection.getMetaData().getTables(null, null, tableName, null);
			return !!tables.next();
		} catch(SQLException e) {
			new ErrorHandler(e);
			return false;
		}
	}

	/*
	 * Closes the database connection. Use this as "cleanup" / destructor.
	 */
	public void close() {
		try {
			if(connection != null && !connection.isClosed()) {
				connection.close();
				instance = null;
			}
		} catch(SQLException e) {
			new ErrorHandler(e);
		}
	}


	public static class LogTrigger implements Trigger {
		int operation;
		String table;

		@Override
		public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) {
			operation = type;
			table = tableName;
		}

		@Override
		public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
			String query =
				"INSERT INTO LOG (operation_type, target_table)" +
				"VALUES ('%s', '%s');";
			String new_operation;
			switch (operation) {
				case 1:
					new_operation = "INSERT";
					break;
				case 2:
					new_operation = "UPDATE";
					break;
				case 4:
					new_operation = "DELETE";
					break;
				default:
					new_operation = "";
			}
			query = String.format( query, new_operation, table );
			conn.prepareStatement( query ).executeUpdate();
		}
	}

	public static class BorrowTrigger implements Trigger {
		int operation;
		String table;

		@Override
		public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) {
			operation = type;
			table = tableName;
		}

		@Override
		public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
			String query =
					"INSERT INTO ACTIVE_BORROW (id)" +
							"VALUES (%d);";

			query = String.format( query, newRow[0]);
			conn.prepareStatement( query ).executeUpdate();
		}
	}

	public static class ArchivedBorrowStatTrigger implements Trigger {
		int operation;
		String table;

		@Override
		public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) {
			operation = type;
			table = tableName;
		}

		@Override
		public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
			int id = (int) oldRow[0];
			Borrow borrow = GetBorrow.request(id);
			int year = borrow.getDate().getYear();
			int month = borrow.getDate().getMonthValue();

			String query = String.format("SELECT * FROM BORROW_STAT WHERE \"year\" = %d and \"month\" = %d", year, month);
			ResultSet res = conn.prepareStatement(query).executeQuery();

			if (res.next()) {
				query = String.format("UPDATE BORROW_STAT SET total = %d WHERE \"year\" = %d AND \"month\" = %d"
						, res.getInt("total") + 1, year, month);
			} else {
				query = String.format("INSERT INTO BORROW_STAT (\"year\", \"month\", total) VALUES (%d, %d, 1)", year, month);

			}
			conn.prepareStatement(query).executeUpdate();
		}
	}
}
