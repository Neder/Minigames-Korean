package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetMinPlayersCommand implements ICommand{

	
	public String getName() {
		return "minplayers";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Sets the minimum players for a multiplayer Minigame. (Default: 2)";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> minplayers <Number>"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to set the minimum players for a Minigame!";
	}

	
	public String getPermission() {
		return "minigame.set.minplayers";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			if(args[0].matches("[0-9]+")){
				int min = Integer.parseInt(args[0]);
				minigame.setMinPlayers(min);
				sender.sendMessage(ChatColor.GRAY + "Minimum players has been set to " + min + " for " + minigame);
				return true;
			}
		}
		return false;
	}

}
