package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetDefaultWinnerCommand implements ICommand {

	
	public String getName() {
		return "defaultwinner";
	}

	
	public String[] getAliases() {
		return new String[] {"defwin"};
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Sets which team will win when the timer expires and neither team has won. (Useful for attack/defend modes of CTF) (Default: none).";
	}

	
	public String[] getParameters() {
		return new String[] {"red", "r", "blue", "b", "none"};
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> defaultwinner <Parameter>"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to set the default winner of a Minigame!";
	}

	
	public String getPermission() {
		return "minigame.set.defaultwinner";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			if(args[0].equalsIgnoreCase("r") || args[0].equalsIgnoreCase("red")){
				minigame.setDefaultWinner("red");
				sender.sendMessage(ChatColor.GRAY + "The default winner of " + minigame + " has been set to red.");
				return true;
			}
			else if(args[0].equalsIgnoreCase("b") || args[0].equalsIgnoreCase("blue")){
				minigame.setDefaultWinner("blue");
				sender.sendMessage(ChatColor.GRAY + "The default winner of " + minigame + " has been set to blue.");
				return true;
			}
			else if(args[0].equalsIgnoreCase("none")){
				minigame.setDefaultWinner("none");
				sender.sendMessage(ChatColor.GRAY + "The default winner of " + minigame + " has been set to none.");
				return true;
			}
		}
		return false;
	}

}
