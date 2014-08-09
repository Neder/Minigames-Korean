package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetLocationCommand implements ICommand{

	
	public String getName() {
		return "location";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Sets the location name for a treasure hunt Minigame.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> location <Location Name Here>"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to set a Minigames location!";
	}

	
	public String getPermission() {
		return "minigame.set.location";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			String location = "";
			for(int i = 0; i < args.length; i++){
				location += args[i];
				if(i != args.length - 1){
					location += " ";
				}
			}
			minigame.setLocation(location);
			sender.sendMessage(ChatColor.GRAY + "The location name for " + minigame + " has been set to " + location);
			return true;
		}
		return false;
	}

}
