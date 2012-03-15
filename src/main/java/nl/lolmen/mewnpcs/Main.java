package nl.lolmen.mewnpcs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import nl.lolmewn.mewnpcs.quests.Quest;
import nl.lolmewn.mewnpcs.quests.QuestManager;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import com.topcat.npclib.NPCManager;
import com.topcat.npclib.entity.NPC;

public class Main extends JavaPlugin{
	
	private NPCManager npcManager;
	private Settings settings;
	private QuestManager questManager;
	private MySQL mysql;
	
	private HashMap<String, NPC> npcs = new HashMap<String, NPC>();
	
	public void onDisable(){
		
	}
	
	public Settings getSettings(){
		return this.settings;
	}
	
	public NPCManager getNPCManager(){
		return this.npcManager;
	}
	
	public QuestManager getQuestManager(){
		return this.questManager;
	}
	
	public MySQL getMySQL(){
		return this.mysql;
	}
	
	public void onEnable(){
		this.npcManager = new NPCManager(this);
		this.settings = new Settings();
		this.settings.loadSettings();
		this.questManager = new QuestManager(this);
		this.questManager.loadQuests();
		this.mysql = new MySQL(this.getSettings().getDbHost(), 
				this.getSettings().getDbPort(),
				this.getSettings().getDbUser(),
				this.getSettings().getDbPass(),
				this.getSettings().getDbName(),
				this.getSettings().getDbPrefix());
		this.getCommand("npc").setExecutor(new NPCExecutor(this));
	}
	
	public void saveData(){
		for(String name : this.npcs.keySet()){
			NPC npc = this.npcs.get(name);
			Location loc = npc.getBukkitEntity().getLocation();
			this.getMySQL().executeStatement("INSERT INTO " + this.getNPCTable() + " (name,x,y,z,yaw,pitch) VALUES " +
					"('" + name + "'," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch() + ") ON DUPLICATE KEY UPDATE " + 
					"name='" + name + "', x=" + loc.getX() + ", y=" + loc.getY() + ",z=" + loc.getZ() + ",yaw=" + loc.getYaw() + ",pitch=" + loc.getPitch());
		}
		for(Quest quest : this.questManager.getQuests()){
			for(String player : quest.getQuestingPlayers()){
				ResultSet set = this.getMySQL().executeQuery("SELECT * FROM " + this.getUserTable() + " WHERE player='" + player + "' AND quest='" + quest.getName() + "'");
				if(set == null){
					this.getLogger().info("Something is wrong with your database!");
					break;
				}
				try {
					boolean found = false;
					while(set.next()){
						//Already is a value in, needs updating
						this.getMySQL().executeStatement("UPDATE " + this.getUserTable() + " SET progress=" + quest.getPlayerProgress(player) + " WHERE player='" + player + "' AND quest='" + quest.getName() + "'");
						found = true;
						break;
					}
					if(!found){
						this.getMySQL().executeStatement("INSERT INTO " + this.getUserTable() + " (player, quest, progress) VALUES ('" + player + "', '" + quest.getName() + "', " + quest.getPlayerProgress(player) + ")");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public NPC spawnNPC(String name, Location loc){
		NPC spawn = this.getNPCManager().spawnHumanNPC(name, loc);
		this.npcs.put(name, spawn);
		return spawn;
	}
	
	public String getUserTable(){
		return this.getSettings().getDbPrefix() + "UserQuestData";
	}
	
	public String getNPCTable(){
		return this.getSettings().getDbPrefix() + "NPCS";
	}

}
