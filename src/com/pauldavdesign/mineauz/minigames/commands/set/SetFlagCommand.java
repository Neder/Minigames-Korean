package com.pauldavdesign.mineauz.minigames.commands.set;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetFlagCommand implements ICommand{

	
	public String getName() {
		return "flag";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Sets capture flags for SP and Race Minigames. These can be captured with [Flag] Signs.";
	}

	
	public String[] getParameters() {
		return new String[] {"add", "remove", "clear", "list"};
	}

	
	public String[] getUsage() {
		return new String[] {
				"/minigame set <Minigame> flag add <Name>",
				"/minigame set <Minigame> flag remove <Name>",
				"/minigame set <Minigame> flag clear",
				"/minigame set <Minigame> flag list"
		};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to modify the flags in a Minigame!";
	}

	
	public String getPermission() {
		return "minigame.set.flag";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			if(args[0].equalsIgnoreCase("add") && args.length >= 2){
				minigame.addFlag(args[1]);
				sender.sendMessage(ChatColor.GRAY + args[1] + " flag added to " + minigame);
				return true;
			}
			else if(args[0].equalsIgnoreCase("remove") && args.length >= 2){
				if(minigame.removeFlag(args[1])){
					sender.sendMessage(ChatColor.GRAY + "Removed the " + args[1] + " flag.");
				}
				else{
					sender.sendMessage(ChatColor.RED + "There is no flag by the name " + args[1] + ".");
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("list")){
				if(minigame.hasFlags()){
					List<String> flag = minigame.getFlags();
					String flags = "";
					for(int i = 0; i < flag.size(); i++){
						flags += flag.get(i);
						if(i != flag.size() - 1){
							flags += ", ";
						}
					}
					sender.sendMessage(ChatColor.BLUE + "All " + minigame.getName() + " flags:");
					sender.sendMessage(ChatColor.GRAY + flags);
				}
				else{
					sender.sendMessage(ChatColor.RED + "There are no flags in " + minigame.getName() + "!");
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("clear")){
				if(minigame.hasFlags()){
					minigame.getFlags().clear();
					sender.sendMessage(ChatColor.GRAY + "Cleared all flags from " + minigame.getName());
				}
				else{
					sender.sendMessage(ChatColor.RED + "There are no flags in " + minigame.getName() + "!");
				}
				return true;
			}
		}
		return false;
	}

}
