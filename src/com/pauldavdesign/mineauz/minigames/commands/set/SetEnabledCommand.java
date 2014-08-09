package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetEnabledCommand implements ICommand {

	
	public String getName() {
		return "enabled";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Sets whether the Minigame is enabled or not. (Default: disabled)";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> enabled <true/false>"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to change the Minigames enabled state!";
	}

	
	public String getPermission() {
		return "minigame.set.enabled";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			boolean enabled = Boolean.parseBoolean(args[0]);
			minigame.setEnabled(enabled);
			if(enabled){
				sender.sendMessage(ChatColor.GRAY + minigame.getName() + " is now enabled.");
			}
			else{
				sender.sendMessage(ChatColor.GRAY + minigame.getName() + " is now disabled.");
			}
			minigame.saveMinigame();
			return true;
		}
		return false;
	}

}
