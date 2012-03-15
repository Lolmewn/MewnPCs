package nl.lolmewn.mewnpcs.quests;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Quest {
	
	private String name;
	private boolean enabled;
	
	private HashMap<Integer, ToDo> list = new HashMap<Integer, ToDo>();
	private HashMap<String, Integer> playerProgress = new HashMap<String, Integer>();
	
	public enum nextUp{
		TALK, NPCTALK, BRING, TELLPLAYER, KILL, KILLPLAYER, KILLPLAYERNODEATH, KILLNODEATH, NOTAVAILABLE, GIVEPLAYER, MINEBLOCK
	}
	
	public Quest(String name, boolean enabled){
		this.setName(name);
		this.setEnabled(enabled);
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	private void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public void addTodo(nextUp type, String value){
		this.list.put(list.size(), new ToDo(type, value));
	}
	
	public nextUp getNextUp(int value){
		Iterator<Integer> list = this.list.keySet().iterator();
		int count = 0;
		if(list == null || this.list.isEmpty()){
			return nextUp.NOTAVAILABLE;
		}
		while(list.hasNext()){
			if(count == value){
				return this.list.get(list.next()).getNext();
			}
			count++;
		}
		return nextUp.NOTAVAILABLE;
	}
	
	public nextUp getNextUpFromString(String value){
		return nextUp.valueOf(value);
	}
	
	public void playerStart(String name){
		this.playerProgress.put(name, 0);
	}
	
	public int getPlayerProgress(String name){
		return this.playerProgress.get(name) == null ? -1 : this.playerProgress.get(name);
	}
	
	public boolean isQuesting(String name){
		return this.playerProgress.containsKey(name);
	}
	
	public Set<String> getQuestingPlayers(){
		return this.playerProgress.keySet();
	}

}
