package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetAllowEnderpearlsCommand implements ICommand {

	public String getName() {
		return "allowenderpearls";
	}

	public String[] getAliases() {
		return null;
	}

	public boolean canBeConsole() {
		return true;
	}

	public String getDescription() {
		return "Sets whether players can use enderpearls in a Minigame.";
	}

	public String[] getParameters() {
		return null;
	}

	public String[] getUsage() {
		return new String[] { "/minigame set <Minigame> allowenderpearls <true / false>"};
	}

	public String getPermissionMessage() {
		return "You don't have permission to change allow enderpearl usage!";
	}

	public String getPermission() {
		return "minigame.set.allowenderpearls";
	}

	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			boolean bool = Boolean.parseBoolean(args[0]);
			if(bool){
				minigame.setAllowEnderpearls(bool);
				sender.sendMessage(ChatColor.GRAY + "Allowed Enderpearl usage in " + minigame);
			}
			else{
				minigame.setAllowEnderpearls(bool);
				sender.sendMessage(ChatColor.GRAY + "Disallowed Enderpearl usage in " + minigame);
			}
			return true;
		}
		return false;
	}

}
