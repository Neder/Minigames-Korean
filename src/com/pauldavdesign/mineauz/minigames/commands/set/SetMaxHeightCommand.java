package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetMaxHeightCommand implements ICommand {

	
	public String getName() {
		return "maxheight";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Sets the maximum Y height from the start location for a treasure chest to pick its random, however, " +
				"if there are blocks in the way, it can still move above this height. (Default: 20)";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> maxheight <Number>"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to modify the treasures spawn height!";
	}

	
	public String getPermission() {
		return "minigame.set.maxheight";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			if(args[0].matches("[0-9]+")){
				int num = Integer.parseInt(args[0]);
				minigame.setMaxHeight(num);
				sender.sendMessage(ChatColor.GRAY + "Maximum height variance for " + minigame + " has been set to " + num);
				return true;
			}
		}
		return false;
	}

}
