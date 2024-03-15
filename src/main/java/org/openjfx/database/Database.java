package org.openjfx.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Uses the Singleton pattern. The database is initialized the first time
 * getInstance() is called. Then it's available until close() is called.
 */
public class Database {
	private static Database instance;
	private static Boolean exists = false;

	private Connection connection;

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
		if(!exists) {
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

		executeUpdate("DROP TABLE IF EXISTS BORROW");
		executeUpdate("DROP TABLE IF EXISTS WISH");
		executeUpdate("DROP TABLE IF EXISTS BOOK");
		executeUpdate("DROP TABLE IF EXISTS \"USER\"");


		executeUpdate("CREATE TABLE \"USER\" (" +
					  "user_id INT AUTO_INCREMENT PRIMARY KEY," +
					  "login VARCHAR(255) NOT NULL," +
					  "password VARCHAR(255) NOT NULL," +
					  "admin BOOLEAN DEFAULT false)");

		executeUpdate("CREATE TABLE BOOK (" +
					  "book_id INT AUTO_INCREMENT PRIMARY KEY," +
					  "title VARCHAR(255) NOT NULL," +
					  "author VARCHAR(255) NOT NULL," +
					  "category VARCHAR(255) NOT NULL," +
					  "rating INT CHECK (rating BETWEEN 1 AND 5)," +
					  "amount INT CHECK (amount >= 0))");

		executeUpdate("CREATE TABLE WISH (" +
					  "wish_id INT AUTO_INCREMENT PRIMARY KEY," +
					  "user_id INT NOT NULL," +
					  "book_id INT NOT NULL," +
					  "days INT CHECK (days > 0)," +
					  "wish_date DATE NOT NULL DEFAULT CURRENT_DATE," +
					  "FOREIGN KEY (user_id) REFERENCES \"USER\"(user_id) ON DELETE CASCADE," +
					  "FOREIGN KEY (book_id) REFERENCES BOOK(book_id) ON DELETE CASCADE)");

		executeUpdate("CREATE TABLE BORROW (" +
					  "borrow_id INT AUTO_INCREMENT PRIMARY KEY," +
					  "user_id INT NOT NULL," +
					  "book_id INT NOT NULL," +
					  "days INT CHECK (days > 0)," +
					  "borrow_date DATE NOT NULL DEFAULT CURRENT_DATE," +
					  "acknowledged BOOLEAN DEFAULT false," +
					  "FOREIGN KEY (user_id) REFERENCES \"USER\"(user_id) ON DELETE CASCADE," +
					  "FOREIGN KEY (book_id) REFERENCES BOOK(book_id) ON DELETE CASCADE)");


		executeUpdate("INSERT INTO \"USER\" (login, password, admin) VALUES ('admin', 'admin', true)");
		executeUpdate("INSERT INTO \"USER\" (login, password) VALUES ('Franek', '123')");
		executeUpdate("INSERT INTO \"USER\" (login, password) VALUES ('Seba', '123')");
		executeUpdate("INSERT INTO \"USER\" (login, password) VALUES ('Olek', '123')");
		executeUpdate("INSERT INTO \"USER\" (login, password) VALUES ('Boguś', '123')");


		executeUpdate("INSERT INTO BOOK (title, author, category, rating, amount) " +
					  "VALUES ('To Kill a Mockingbird', 'Harper Lee', 'Fiction', 4, 5)");
		executeUpdate("INSERT INTO BOOK (title, author, category, rating, amount) " +
					  "VALUES ('1984', 'George Orwell', 'Dystopian', 5, 8)");
		executeUpdate("INSERT INTO BOOK (title, author, category, rating, amount) " +
					  "VALUES ('The Great Gatsby', 'F. Scott Fitzgerald', 'Classic', 4, 6)");
		executeUpdate("INSERT INTO BOOK (title, author, category, rating, amount) " +
					  "VALUES ('The Hobbit', 'J.R.R. Tolkien', 'Fantasy', 4, 7)");
		executeUpdate("INSERT INTO BOOK (title, author, category, rating, amount) " +
					  "VALUES ('The Catcher in the Rye', 'J.D. Salinger', 'Coming of Age', 4, 3)");
		executeUpdate("INSERT INTO BOOK (title, author, category, rating, amount) " +
					  "VALUES ('The Da Vinci Code', 'Dan Brown', 'Mystery', 3, 4)");
		executeUpdate("INSERT INTO BOOK (title, author, category, rating, amount) " +
					  "VALUES ('The Alchemist', 'Paulo Coelho', 'Philosophical Fiction', 4, 2)");
		executeUpdate("INSERT INTO BOOK (title, author, category, rating, amount) " +
					  "VALUES ('Pride and Prejudice', 'Jane Austen', 'Romance', 5, 1)");
		executeUpdate("INSERT INTO BOOK (title, author, category, rating, amount) " +
					  "VALUES ('The Lord of the Rings', 'J.R.R. Tolkien', 'Fantasy', 4, 9)");
		executeUpdate("INSERT INTO BOOK (title, author, category, rating, amount) " +
					  "VALUES ('Brave New World', 'Aldous Huxley', 'Dystopian', 4, 0)");
		executeUpdate("INSERT INTO BOOK (title, author, category, rating, amount) " +
					  "VALUES ('Sense and Sensibility', 'Jane Austen', 'Romance', 4, 2)");
		executeUpdate("INSERT INTO BOOK (title, author, category, rating, amount) " +
					  "VALUES ('Fahrenheit 451', 'Ray Bradbury', 'Dystopian', 5, 6)");
		executeUpdate("INSERT INTO BOOK (title, author, category, rating, amount) " +
					  "VALUES ('The Fellowship of the Ring', 'J.R.R. Tolkien', 'Fantasy', 4, 8)");
		executeUpdate("INSERT INTO BOOK (title, author, category, rating, amount) " +
					  "VALUES ('The Grapes of Wrath', 'John Steinbeck', 'Classic', 4, 4)");
		executeUpdate("INSERT INTO BOOK (title, author, category, rating, amount) " +
					  "VALUES ('The Fountainhead', 'Ayn Rand', 'Philosophical Fiction', 3, 7)");


		executeUpdate("INSERT INTO WISH (user_id, book_id, days, wish_date) " +
					  "VALUES ((SELECT user_id FROM \"USER\" WHERE login = 'Franek'), 6, 7, '2020-12-12')");
		executeUpdate("INSERT INTO WISH (user_id, book_id, days) " +
					  "VALUES ((SELECT user_id FROM \"USER\" WHERE login = 'Franek'), 6, 5)");

		executeUpdate("INSERT INTO WISH (user_id, book_id, days) " +
					  "VALUES ((SELECT user_id FROM \"USER\" WHERE login = 'Seba'), 8, 5)");
		executeUpdate("INSERT INTO WISH (user_id, book_id, days) " +
					  "VALUES ((SELECT user_id FROM \"USER\" WHERE login = 'Seba'), 7, 4)");

		executeUpdate("INSERT INTO WISH (user_id, book_id, days) " +
					  "VALUES ((SELECT user_id FROM \"USER\" WHERE login = 'Olek'), 10, 6)");
		executeUpdate("INSERT INTO WISH (user_id, book_id, days) " +
					  "VALUES ((SELECT user_id FROM \"USER\" WHERE login = 'Olek'), 2, 3)");

		executeUpdate("INSERT INTO WISH (user_id, book_id, days) " +
					  "VALUES ((SELECT user_id FROM \"USER\" WHERE login = 'Boguś'), 1, 10)");
		executeUpdate("INSERT INTO WISH (user_id, book_id, days) " +
					  "VALUES ((SELECT user_id FROM \"USER\" WHERE login = 'Boguś'), 5, 8)");


		executeUpdate("INSERT INTO BORROW (user_id, book_id, days, acknowledged) " +
					  "VALUES ((SELECT user_id FROM \"USER\" WHERE login = 'Franek'), 3, 14, true)");
		executeUpdate("INSERT INTO BORROW (user_id, book_id, days, acknowledged, borrow_date) " +
					  "VALUES ((SELECT user_id FROM \"USER\" WHERE login = 'Franek'), 1, 7, false, '2020-12-12')");

		executeUpdate("INSERT INTO BORROW (user_id, book_id, days, acknowledged) " +
					  "VALUES ((SELECT user_id FROM \"USER\" WHERE login = 'Seba'), 5, 10, false)");
		executeUpdate("INSERT INTO BORROW (user_id, book_id, days, acknowledged) " +
					  "VALUES ((SELECT user_id FROM \"USER\" WHERE login = 'Seba'), 4, 12, true)");

		executeUpdate("INSERT INTO BORROW (user_id, book_id, days, acknowledged) " +
					  "VALUES ((SELECT user_id FROM \"USER\" WHERE login = 'Olek'), 9, 21, false)");
		executeUpdate("INSERT INTO BORROW (user_id, book_id, days, acknowledged) " +
					  "VALUES ((SELECT user_id FROM \"USER\" WHERE login = 'Olek'), 6, 8, true)");

		executeUpdate("INSERT INTO BORROW (user_id, book_id, days, acknowledged) " +
					  "VALUES ((SELECT user_id FROM \"USER\" WHERE login = 'Boguś'), 8, 15, true)");
		executeUpdate("INSERT INTO BORROW (user_id, book_id, days, acknowledged) " +
					  "VALUES ((SELECT user_id FROM \"USER\" WHERE login = 'Boguś'), 2, 5, false)");
	}

	/*
	 * This is for SQL that DOES NOT CHANGE the database. Probably SELECT only.
	 *
	 * If you want to SELECT anything, here's the available tables:
	 *
	 * USER:
	 *  - int user_id
	 *  - str login
	 *  - str password
	 *  - bool admin
	 *
	 * BOOK:
	 *  - int book_id
	 *  - str title
	 *  - str author
	 *  - str category
	 *  - int rating
	 *  - int amount
	 *
	 * WISH:
	 *  - int wish_id
	 *  - int user_id
	 *  - int book_id
	 *  - int days
	 *  - date wish_date
	 *
	 * BORROW:
	 *  - int borrow_id
	 *  - int user_id
	 *  - int book_id
	 *  - int days
	 *  - date borrow_date
	 *  - bool acknowledged
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
	 *  - 0 for CREATE or DROP, but you shouldn't use this outside of tests
	 *  - <num of rows> for INSERT or DELETE
	 */
	public int executeUpdate(String sql) {
		try {
			int result = connection.prepareStatement(sql).executeUpdate();
			connection.commit();
			return result;
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
			}
		} catch(SQLException e) {
			new ErrorHandler(e);
		}
	}
}
