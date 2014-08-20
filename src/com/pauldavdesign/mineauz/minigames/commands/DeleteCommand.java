package com.pauldavdesign.mineauz.minigames.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.MinigameSave;

public class DeleteCommand implements ICommand{

	
	public String getName() {
		return "delete";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Deletes a Minigame from existance. It will be gone forever! (A very long time)";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame delete <Minigame>"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to delete Minigames!";
	}

	
	public String getPermission() {
		return "minigame.delete";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			Minigame mgm = plugin.mdata.getMinigame(args[0]);
			
			if(mgm != null){
				MinigameSave save = new MinigameSave(mgm.getName(), "config");
				
				if(save.getConfig().get(mgm.getName()) != null){
					save.deleteFile();
					List<String> ls = plugin.getConfig().getStringList("minigames");
					ls.remove(mgm.getName());
					plugin.getConfig().set("minigames", ls);
					plugin.mdata.removeMinigame(mgm.getName());
					plugin.saveConfig();
					sender.sendMessage(ChatColor.RED + "The minigame " + mgm.getName() + " has been removed");
				}
				else {
					sender.sendMessage(ChatColor.RED + "That minigame does not exist!");
				}
			}
			return true;
		}
		return false;
	}

}
