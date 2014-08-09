package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetItemPickupCommand implements ICommand {

	
	public String getName() {
		return "itempickup";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Changes whether a player can pickup items when in a Minigame. (Enabled by default)";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> itempickup <true/false>"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to change the item pickup state in a Minigame!";
	}

	
	public String getPermission() {
		return "minigame.set.itempickup";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			boolean bool = Boolean.parseBoolean(args[0]);
			minigame.setItemPickup(bool);
			if(bool){
				sender.sendMessage(ChatColor.GRAY + "Item pickup has been enabled for " + minigame);
			}
			else{
				sender.sendMessage(ChatColor.GRAY + "Item pickup has been disabled for " + minigame);
			}
			return true;
		}
		return false;
	}

}
