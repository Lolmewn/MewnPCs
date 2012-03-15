package nl.lolmewn.mewnpcs.quests;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.configuration.file.YamlConfiguration;

import nl.lolmen.mewnpcs.Main;
import nl.lolmewn.mewnpcs.quests.Quest.nextUp;

public class QuestManager {
	
	private Main plugin;
	private File questFile = new File("plugins" + File.separator + "MewnPCs" + File.separator + "quests.yml");
	
	private HashMap<String, Quest> quests = new HashMap<String, Quest>();
	
	public QuestManager(Main main){
		this.plugin = main;
	}
	
	private Main getPlugin(){
		return this.plugin;
	}
	
	public void loadQuests(){
		if(!this.questFile.exists()){
			this.createQuestFile();
		}
		YamlConfiguration c = YamlConfiguration.loadConfiguration(this.questFile);
		for(String keys : c.getConfigurationSection("").getKeys(false)){
			if(!c.contains(keys + ".enabled")){
				this.getPlugin().getLogger().warning("Couldn't find " + keys + ".enabled in quest file, not loading " + keys);
				continue;
			}
			if(!c.contains(keys + ".list")){
				this.getPlugin().getLogger().warning("Couldn't find " + keys + ".list in quest file, not loading " + keys);
				continue;
			}
			String name = c.getString(keys + ".name", keys);
			boolean enabled = c.getBoolean(keys + ".enabled");
			Quest q = new Quest(name, enabled);
			for(String item : c.getConfigurationSection(keys + ".list").getKeys(false)){
				nextUp type = q.getNextUpFromString(item);
				if(type == null){
					this.getPlugin().getLogger().warning("Couldn't load " + keys + ".list." + item + " in quest file, unknown todo type");
					continue;
				}
				if(type == nextUp.BRING || type == nextUp.GIVEPLAYER){
					for(String subItem : c.getConfigurationSection(keys + ".list." + item).getKeys(false)){
						if(subItem.equalsIgnoreCase("items")){
							q.addTodo(type, "items:" + c.getString(keys + ".list." + item + ".items"));
							continue;
						}
						if(subItem.equalsIgnoreCase("npc")){
							q.addTodo(type, "npc:" + c.getString(keys + ".list." + item + ".npc"));
							continue;
						}
						if(subItem.equalsIgnoreCase("money")){
							q.addTodo(type, "money:" + c.getString(keys + ".list." + item + ".money"));
							continue;
						}
						this.getPlugin().getLogger().warning("Couldn't load " + keys + ".list." + item + "." + subItem + " in quest file, unknown type");
						continue;
					}
					continue;
				}
				q.addTodo(type, c.getString(keys + ".list." + item));
			}
			this.quests.put(keys, q);
			this.getPlugin().getLogger().info("Quest " + keys + " loaded!");
		}
	}

	private void createQuestFile() {
		this.getPlugin().getLogger().info("Trying to create default quests...");
		try {
			new File("plugins/MewnPCs/").mkdir();
			File efile = this.questFile;
			InputStream in = this.getClass().getClassLoader().getResourceAsStream("quests.yml");
			OutputStream out = new BufferedOutputStream(new FileOutputStream(efile));
			int c;
			while ((c = in.read()) != -1) {
				out.write(c);
			}
			out.flush();
			out.close();
			in.close();
			this.getPlugin().getLogger().info("Default quest file created succesfully!");
		} catch (Exception e) {
			e.printStackTrace();
			this.getPlugin().getLogger().info("Error creating quest file! Not using quests!");
		}
	}
	
	public HashSet<Quest> getQuests(){
		HashSet<Quest> qs = new HashSet<Quest>();
		for(String qName : this.quests.keySet()){
			qs.add(this.quests.get(qName));
		}
		return qs;
	}

}
