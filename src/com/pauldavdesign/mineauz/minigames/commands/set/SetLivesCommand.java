package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetLivesCommand implements ICommand {

	
	public String getName() {
		return "lives";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Sets the amount of lives a player has in the Minigame. If they run out, they are booted from the game. 0 means unlimited.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> lives <number>"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to set the number of lives in a Minigame!";
	}

	
	public String getPermission() {
		return "minigame.set.lives";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			if(args[0].matches("[0-9]+")){
				int lives = Integer.parseInt(args[0]);
				minigame.setLives(lives);
				
				sender.sendMessage(ChatColor.GRAY + minigame.getName() + "'s lives has been set to " + lives);
				return true;
			}
		}
		return false;
	}

}
