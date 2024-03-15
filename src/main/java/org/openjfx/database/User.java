package org.openjfx.database;

public class User {
	private int id;
	private String login;
	private Boolean admin;

	public User(int id, String login, Boolean admin)
	{
		this.id = id;
		this.login = login;
		this.admin = admin;
	}

	public int getID() { return id; }
	public String getLogin() { return login; }
	public Boolean isAdmin() { return admin; }
}
