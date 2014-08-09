package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetPaintballCommand implements ICommand {

	
	public String getName() {
		return "paintball";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Sets a Minigame to be in paintball mode. This lets snowballs damage players. " +
				"(Default: false, default damage: 2)";
	}

	
	public String[] getParameters() {
		return new String[] {"damage"};
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> paintball <true/false>", 
				"/minigame set <Minigame> paintball damage <Number>"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to set paintball mode!";
	}

	
	public String getPermission() {
		return "minigame.set.paintball";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			if(args.length == 1){
				boolean bool = Boolean.parseBoolean(args[0]);
				minigame.setPaintBallMode(bool);
				if(bool){
					sender.sendMessage(ChatColor.GRAY + "Paintball mode has been enabled for " + minigame);
				}
				else{
					sender.sendMessage(ChatColor.GRAY + "Paintball mode has been disabled for " + minigame);
				}
				return true;
			}
			else if(args.length >= 2){
				if(args[0].equalsIgnoreCase("damage") && args[1].matches("[0-9]+")){
					minigame.setPaintBallDamage(Integer.parseInt(args[1]));
					sender.sendMessage(ChatColor.GRAY + "Paintball damage has been set to " + args[1] + " for " + minigame);
					return true;
				}
			}
		}
		return false;
	}

}
