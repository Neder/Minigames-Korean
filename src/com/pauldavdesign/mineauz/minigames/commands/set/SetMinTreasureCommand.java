package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetMinTreasureCommand implements ICommand {

	
	public String getName() {
		return "mintreasure";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Sets the minimum number of items to spawn in a treasure hunt chest.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> mintreasure <Number>"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to set the minimum treasure!";
	}

	
	public String getPermission() {
		return "minigame.set.mintreasure";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			if(args[0].matches("[0-9]+")){
				int amount = Integer.parseInt(args[0]);
				minigame.setMinTreasure(amount);
				sender.sendMessage(ChatColor.GRAY + "Minimum items has been set to " + amount + " for " + minigame);
				return true;
			}
		}
		return false;
	}

}
