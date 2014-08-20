package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetEndCommand implements ICommand{

	
	public String getName() {
		return "end";
	}
	
	
	public String[] getAliases(){
		return null;
	}

	
	public boolean canBeConsole() {
		return false;
	}

	
	public String getDescription() {
		return "Sets the ending position for a player when they win a Minigame.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> end"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to set the end position!";
	}

	
	public String getPermission() {
		return "minigame.set.end";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		minigame.setEndPosition(((Player) sender).getLocation());
		sender.sendMessage(ChatColor.GRAY + "Ending position has been set for " + minigame);
		return true;
	}

}
