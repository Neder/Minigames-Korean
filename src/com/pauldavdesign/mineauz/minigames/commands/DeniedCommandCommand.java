package com.pauldavdesign.mineauz.minigames.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;

public class DeniedCommandCommand implements ICommand {

	
	public String getName() {
		return "deniedcommand";
	}

	
	public String[] getAliases() {
		return new String[] {"deniedcomd", "deniedcom"};
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Sets commands to be disabled when playing a Minigame. (eg: home or spawn)";
	}

	
	public String[] getParameters() {
		return new String[] {"add", "remove", "list"};
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame deniedcommand add <Command>", "/minigame deniedcommand remove <Command>", "/minigame deniedcommand list"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to set denied commands!";
	}

	
	public String getPermission() {
		return "minigame.deniedcommands";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			if(args[0].equalsIgnoreCase("add") && args.length >= 2){
				plugin.pdata.addDeniedCommand(args[1]);
				sender.sendMessage(ChatColor.GRAY + "Added \"" + args[1] + "\" to the denied command list.");
				return true;
			}
			else if(args[0].equalsIgnoreCase("remove") && args.length >= 2){
				plugin.pdata.removeDeniedCommand(args[1]);
				sender.sendMessage(ChatColor.GRAY + "Removed \"" + args[1] + "\" from the denied command list.");
				return true;
			}
			else if(args[0].equalsIgnoreCase("list")){
				String coms = "";
				boolean switchColour = false;
				for(String par : plugin.pdata.getDeniedCommands()){
					if(switchColour){
						coms += ChatColor.WHITE + par;
						if(!par.equalsIgnoreCase(plugin.pdata.getDeniedCommands().get(plugin.pdata.getDeniedCommands().size() - 1))){
							coms += ChatColor.WHITE + ", ";
						}
						switchColour = false;
					}
					else{
						coms += ChatColor.GRAY + par;
						if(!par.equalsIgnoreCase(plugin.pdata.getDeniedCommands().get(plugin.pdata.getDeniedCommands().size() - 1))){
							coms += ChatColor.WHITE + ", ";
						}
						switchColour = true;
					}
				}
				sender.sendMessage(ChatColor.GRAY + "Disabled Commands: " + coms);
				return true;
			}
		}
		return false;
	}

}
