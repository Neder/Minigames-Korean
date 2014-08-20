package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetLobbyCommand implements ICommand{

	
	public String getName() {
		return "lobby";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return false;
	}

	
	public String getDescription() {
		return "Sets the lobby position of a Minigame to where you are standing.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> lobby"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to set the Minigames Lobby Position!";
	}

	
	public String getPermission() {
		return "minigame.set.lobby";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		minigame.setLobbyPosition(((Player)sender).getLocation());
		sender.sendMessage(ChatColor.GRAY + "Lobby position has been set for " + minigame);
		return true;
	}
}
