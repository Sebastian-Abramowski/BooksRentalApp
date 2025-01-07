module org.openjfx {
	requires transitive javafx.graphics;
	requires transitive javafx.controls;
	requires javafx.fxml;
	requires transitive java.sql;
	requires com.h2database;
	opens org.openjfx;
	exports org.openjfx;
	exports org.openjfx.controller;
	opens org.openjfx.controller;
	opens org.openjfx.database to javafx.base;
	exports org.openjfx.database;
}
