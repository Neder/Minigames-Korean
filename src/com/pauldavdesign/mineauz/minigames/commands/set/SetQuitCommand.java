package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetQuitCommand implements ICommand{

	
	public String getName() {
		return "quit";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return false;
	}

	
	public String getDescription() {
		return "Sets the quitting position of a Minigame to where you are standing.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> quit"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to set a Minigames quit position!";
	}

	
	public String getPermission() {
		return "minigame.set.quit";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		Player player = (Player)sender;
		minigame.setQuitPosition(player.getLocation());
		sender.sendMessage(ChatColor.GRAY + "Quit position has been set for " + minigame);
		return true;
	}

}
