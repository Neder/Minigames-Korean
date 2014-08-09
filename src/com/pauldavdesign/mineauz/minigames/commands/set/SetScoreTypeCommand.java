package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetScoreTypeCommand implements ICommand {

	
	public String getName() {
		return "scoretype";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Sets the scoring type for a multiplayer Minigame.";
	}

	
	public String[] getParameters() {
		String[] types = new String[plugin.getScoreTypes().getScoreTypes().keySet().size() + 1];
		int inc = 0;
		for(String type : plugin.getScoreTypes().getScoreTypes().keySet()){
			types[inc] = type;
			inc++;
		}
		types[inc] = "none";
		return types;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> scoretype <Parameter>"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to set the score type!";
	}

	
	public String getPermission() {
		return "minigame.set.scoretype";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			boolean bool = false;
			for(String par : getParameters()){
				if(par.equalsIgnoreCase(args[0])){
					bool = true;
					break;
				}
			}
			
			if(bool){
				minigame.setScoreType(args[0].toLowerCase());
				sender.sendMessage(ChatColor.GRAY + minigame.getName() + " score type has been set to " + args[0]);
				return true;
			}
		}
		return false;
	}

}
