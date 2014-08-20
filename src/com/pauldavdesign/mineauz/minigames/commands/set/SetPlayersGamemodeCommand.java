package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetPlayersGamemodeCommand implements ICommand {

	
	public String getName() {
		return "gamemode";
	}

	
	public String[] getAliases() {
		return new String[] {"gm"};
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Sets the players gamemode when they join a Minigame. (Default: adventure)";
	}

	
	public String[] getParameters() {
		return new String[] {"survival", "adventure", "creative", "0", "1", "2"};
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> gamemode <Parameter>"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to set the players gamemode for a Minigame!";
	}

	
	public String getPermission() {
		return "minigame.set.gamemode";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			if(args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0")){
				minigame.setDefaultGamemode(0);
				sender.sendMessage(minigame.getName() + "'s gamemode has been set to Survival.");
				return true;
			}
			else if(args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2")){
				minigame.setDefaultGamemode(2);
				sender.sendMessage(minigame.getName() + "'s gamemode has been set to Adventure.");
				return true;
			}
			else if(args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1")){
				minigame.setDefaultGamemode(1);
				sender.sendMessage(minigame.getName() + "'s gamemode has been set to Creative.");
				return true;
			}
		}
		return false;
	}

}
