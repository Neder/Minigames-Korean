package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetStartTimeCommand implements ICommand {

	
	public String getName() {
		return "starttime";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Overrides the default game start timer in the lobby after waiting for players time has expired or maximum players are reached. " +
				"If time is 0 then the default time is used. (Default: 0)";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> starttime <Time>"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to modify the start time of a Minigame!";
	}

	
	public String getPermission() {
		return "minigame.set.starttime";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			if(args[0].matches("[0-9]+")){
				int time = Integer.parseInt(args[0]);
				minigame.setStartWaitTime(time);
				if(time != 0){
					sender.sendMessage(ChatColor.GRAY + "Start time has been set to " + time + " seconds for " + minigame);
				}
				else{
					sender.sendMessage(ChatColor.GRAY + "Start time for " + minigame + " has been reset.");
				}
			}
			else{
				sender.sendMessage(ChatColor.RED + args[0] + " is not a valid number!");
			}
			return true;
		}
		return false;
	}

}