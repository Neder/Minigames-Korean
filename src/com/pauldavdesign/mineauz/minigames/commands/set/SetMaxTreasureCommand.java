package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetMaxTreasureCommand implements ICommand {

	
	public String getName() {
		return "maxtreasure";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Sets the maximum number of items to spawn in a treasure hunt chest.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> maxtreasure <Number>"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to set the max treasure for a Minigame!";
	}

	
	public String getPermission() {
		return "minigame.set.maxtreasure";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			if(args[0].matches("[0-9]+")){
				int amount = Integer.parseInt(args[0]);
				minigame.setMaxTreasure(amount);
				sender.sendMessage(ChatColor.GRAY + "Maximum items has been set to " + amount + " for " + minigame);
				return true;
			}
		}
		return false;
	}

}
