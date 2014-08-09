package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetMaxPlayersCommand implements ICommand{

	
	public String getName() {
		return "maxplayers";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Sets the maximum players allowed to play a multiplayer Minigame. (Default: 4)";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> maxplayers <Number>"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to set the maximum players for a Minigame!";
	}

	
	public String getPermission() {
		return "minigame.set.maxplayers";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			if(args[0].matches("[0-9]+")){
				int max = Integer.parseInt(args[0]);
				minigame.setMaxPlayers(max);
				sender.sendMessage(ChatColor.GRAY + "Maximum players has been set to " + max + " for " + minigame);
				return true;
			}
		}
		return false;
	}

}
