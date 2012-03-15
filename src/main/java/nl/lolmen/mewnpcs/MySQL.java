package nl.lolmen.mewnpcs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {
	
	private String host, username, password, database, prefix;
	private int port;
	
	private boolean fault;
	
	private Statement st;
	private Connection con;
	
	public MySQL(String host, int port, String username, String password, String database, String prefix){
		this.host = host;
		this.username = username;
		this.password = password;
		this.database = database;
		this.prefix = prefix;
		this.port = port;
		this.connect();
		this.setupDatabase();
	}

	private void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database;
			System.out.println("[MewnPCs] Connecting to database on " + url);
			this.con = DriverManager.getConnection(url, this.username, this.password);
			this.st = this.con.createStatement();
			System.out.println("[MewnPCs] MySQL initiated succesfully!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			this.setFault(true);
		} catch (SQLException e) {
			e.printStackTrace();
			this.setFault(true);
		} finally {
			if(this.fault){
				System.out.println("[MewnPCs] MySQL initialisation failed!");
			}
		}
	}

	private void setupDatabase() {
		this.executeStatement("CREATE TABLE IF NOT EXISTS " + this.prefix + "NPCS" + 
				"(name varchar(255), " +
				"x double, " +
				"y double, " +
				"z double, " +
				"yaw float, " +
				"pitch float)");
		this.executeStatement("CREATE TABLE IF NOT EXISTS " + this.prefix + "UserQuestData" + 
				"(player varchar(255), " +
				"quest varchar(255), " +
				"progress int)");
	}

	public boolean isFault() {
		return fault;
	}

	private void setFault(boolean fault) {
		this.fault = fault;
	}
	
	public int executeStatement(String statement){
		if(isFault()){
			System.out.println("[MewnPCs] Can't execute statement, something wrong with connection");
			return 0;
		}
		try {
			this.st = this.con.createStatement();
			int re = this.st.executeUpdate(statement);
			this.st.close();
			return re;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public ResultSet executeQuery(String statement){
		if(isFault()){
			System.out.println("[MewnPCs] Can't execute query, something wrong with connection");
			return null;
		}
		if(statement.toLowerCase().startsWith("update") || statement.toLowerCase().startsWith("insert") || statement.toLowerCase().startsWith("delete")){
			this.executeStatement(statement);
			return null;
		}
		try {
			this.st = this.con.createStatement();
			ResultSet set = this.st.executeQuery(statement);
			//this.st.close();
			return set;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void close(){
		if(isFault()){
			System.out.println("[MewnPCs] Can't close connection, something wrong with it");
			return;
		}
		try {
			this.con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}