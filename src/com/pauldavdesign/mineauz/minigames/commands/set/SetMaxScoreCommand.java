package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetMaxScoreCommand implements ICommand {

	
	public String getName() {
		return "maxscore";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Sets the maximum score for a deathmatch or team deathmatch Minigame. (Default: 10)";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> maxscore <Number>"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to set the maximum score of a Minigame!";
	}

	
	public String getPermission() {
		return "minigame.set.maxscore";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			if(args[0].matches("[0-9]+")){
				int maxscore = Integer.parseInt(args[0]);
				minigame.setMaxScore(maxscore);
				sender.sendMessage(ChatColor.GRAY + "Maximum score has been set to " + maxscore + " for " + minigame.getName());
				return true;
			}
		}
		return false;
	}

}
