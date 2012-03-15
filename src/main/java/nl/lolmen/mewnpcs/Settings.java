package nl.lolmen.mewnpcs;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class Settings {
	
	private File settings = new File("plugins" + File.separator + "MewnPCs" + File.separator + "settings.yml");
	
	private double version;
	private String dbHost;
	private String dbPass;
	private String dbUser;
	private String dbName;
	private String dbPrefix;
	private int dbPort;
	
	public void loadSettings(){
		if(!this.settings.exists()){
			this.makeSettings();
		}
		YamlConfiguration c = YamlConfiguration.loadConfiguration(settings);
		this.hasNode(c, "version", 0.1);
		this.setVersion(c.getDouble("version", 0.1));
		this.hasNode(c, "MySQL-User", "root");
		this.setDbUser(c.getString("MySQL-User"));
		this.hasNode(c, "MySQL-Pass", "p4ssw0rd");
		this.setDbPass(c.getString("MySQL-Pass"));
		this.hasNode(c, "MySQL-Host", "localhost");
		this.setDbHost(c.getString("MySQL-Host"));
		this.hasNode(c, "MySQL-Port", 3306);
		this.setDbPort(c.getInt("MySQL-Port"));
		this.hasNode(c, "MySQL-Database", "minecraft");
		this.setDbName(c.getString("MySQL-Database"));
		this.hasNode(c, "MySQL-Prefix", "MewnPC_");
		this.setDbPrefix(c.getString("MySQL-Prefix"));
	}
	
	private void hasNode(YamlConfiguration c, String path, Object def){
		if(!c.contains(path)){
			c.addDefault(path, def);
		}
		try {
			c.save(this.settings);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void makeSettings() {
		Bukkit.getLogger().info("Trying to create default settings...");
		try {
			this.settings.getParentFile().mkdirs();
			InputStream in = this.getClass().getClassLoader().getResourceAsStream("settings.yml");
			OutputStream out = new BufferedOutputStream(new FileOutputStream(this.settings));
			int c;
			while ((c = in.read()) != -1) {
				out.write(c);
			}
			out.flush();
			out.close();
			in.close();
			Bukkit.getLogger().info("Default settings created succesfully!");
		} catch (Exception e) {
			e.printStackTrace();
			Bukkit.getLogger().info("Error creating settings file! Using default settings!");
		}
	}

	public double getVersion() {
		return version;
	}

	private void setVersion(double version) {
		this.version = version;
	}

	protected String getDbHost() {
		return dbHost;
	}

	private void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	protected String getDbPass() {
		return dbPass;
	}

	private void setDbPass(String dbPass) {
		this.dbPass = dbPass;
	}

	protected String getDbUser() {
		return dbUser;
	}

	private void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	protected String getDbName() {
		return dbName;
	}

	private void setDbName(String dbName) {
		this.dbName = dbName;
	}

	protected int getDbPort() {
		return dbPort;
	}

	private void setDbPort(int dbPort) {
		this.dbPort = dbPort;
	}

	protected String getDbPrefix() {
		return dbPrefix;
	}

	private void setDbPrefix(String dbPrefix) {
		this.dbPrefix = dbPrefix;
	}

}
