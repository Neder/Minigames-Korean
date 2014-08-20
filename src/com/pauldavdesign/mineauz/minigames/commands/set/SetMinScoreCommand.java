package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetMinScoreCommand implements ICommand {

	
	public String getName() {
		return "minscore";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Sets the minimum score amount for deathmatch and team deathmatch minigames. (Default: 5)";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> minscore <Number>"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to set the minimum score for a Minigame!";
	}

	
	public String getPermission() {
		return "minigame.set.minscore";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			if(args[0].matches("[0-9]+")){
				int minscore = Integer.parseInt(args[0]);
				minigame.setMinScore(minscore);
				sender.sendMessage(ChatColor.GRAY + "Minimum score has been set to " + minscore + " for " + minigame.getName());
				return true;
			}
		}
		return false;
	}

}
