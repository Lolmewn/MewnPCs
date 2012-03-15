package nl.lolmen.mewnpcs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NPCExecutor implements CommandExecutor {
	
	private Main plugin;
	
	public NPCExecutor(Main m){
		this.plugin = m;
	}
	
	private Main getPlugin(){
		return this.plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		//only called when /npc is done
		if(args.length == 0){
			sender.sendMessage("[MewnPCs] Version " + this.getPlugin().getSettings().getVersion() + " build " + this.getPlugin().getDescription().getVersion());
			return true;
		}
		if(args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("spawn")){
			if(!sender.hasPermission("mewnpcs.npc.create")){
				sender.sendMessage("You do not have permissions to do this!");
				return true;
			}
			if(!(sender instanceof Player)){
				sender.sendMessage("You have to be a player to use this command!");
				return true;
			}
			if(args.length == 1){
				sender.sendMessage("Incorrect usage: /npc " + args[0] + " name");
				return true;
			}
			if(this.getPlugin().getNPCManager().getHumanNPCByName(args[1]).contains(args[1])){
				sender.sendMessage("This name is already in use!");
				return true;
			}
			this.getPlugin().spawnNPC(args[1], ((Player)sender).getLocation());
			sender.sendMessage("NPC " + args[1] + " spawned on your position!");
			return true;
		}
		sender.sendMessage("Unknown MewnPCs command. Help: /npc help");
		return true;
	}

}
