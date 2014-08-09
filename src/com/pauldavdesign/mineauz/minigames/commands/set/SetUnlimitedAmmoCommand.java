package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetUnlimitedAmmoCommand implements ICommand {

	
	public String getName() {
		return "unlimitedammo";
	}

	
	public String[] getAliases() {
		return new String[] {"infammo"};
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Allows unlimited snowballs or eggs to be thrown.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> unlimitedammo <true/false>"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to enable unlimited ammo!";
	}

	
	public String getPermission() {
		return "minigame.set.unlimitedammo";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			Boolean bool = Boolean.parseBoolean(args[0]);
			minigame.setUnlimitedAmmo(bool);
			if(bool){
				sender.sendMessage(ChatColor.GRAY + "Unlimited ammo has been turned on for " + minigame);
			}
			else{
				sender.sendMessage(ChatColor.GRAY + "Unlimited ammo has been turned off for " + minigame);
			}
			return true;
		}
		return false;
	}

}
