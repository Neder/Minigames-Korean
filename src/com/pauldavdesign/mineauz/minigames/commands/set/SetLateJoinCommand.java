package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetLateJoinCommand implements ICommand {

	
	public String getName() {
		return "latejoin";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Enables a player to join after the game has already started. This can only be used in Multiplayer Minigames.\n" +
				"Warning: Do not use this in an LMS Minigame, for obvious reasons.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> latejoin <true/false>"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to enable late joining to a Minigame!";
	}

	
	public String getPermission() {
		return "minigame.set.latejoin";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			Boolean bool = Boolean.parseBoolean(args[0]);
			minigame.setLateJoin(bool);
			if(bool){
				sender.sendMessage(ChatColor.GRAY + "Late join has been enabled for " + minigame);
			}
			else{
				sender.sendMessage(ChatColor.GRAY + "Late join has been disabled for " + minigame);
			}
			return true;
		}
		return false;
	}

}
