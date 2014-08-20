package com.pauldavdesign.mineauz.minigames.commands;

//import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.MinigamePlayer;

@Deprecated
public class RestoreInvCommand implements ICommand{

	
	public String getName() {
		return "restoreinv";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Restores a players inventory if it wasn't automatically restored. (Eg: After a crash)";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame restoreinv <Player Name>"};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to restore a players inventory!";
	}

	
	public String getPermission() {
		return "minigame.restoreinv";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			Set<String> set = plugin.pdata.getInventorySaveConfig().getConfigurationSection("inventories").getKeys(false);
			
			List<Player> players = plugin.getServer().matchPlayer(args[0]);
			MinigamePlayer reqpl = null;
			if(!players.isEmpty()){
				reqpl = plugin.pdata.getMinigamePlayer(players.get(0));
			}
			else{
				sender.sendMessage(ChatColor.RED + "No player found by the name " + args[0]);
				//return truel
			}
			
			if(!reqpl.isInMinigame() && set.contains(reqpl.getName())){
				//plugin.pdata.restorePlayerData(reqpl);
				
				sender.sendMessage(ChatColor.GRAY + "The inventory for " + reqpl.getName() + " has been restored.");
				reqpl.sendMessage(ChatColor.GRAY + "Your inventory has been restored.");
				//plugin.pdata.saveItems(reqpl);
			}
			else if(!set.contains(reqpl.getName())){
				sender.sendMessage(ChatColor.RED + "This players inventory is not stored!");
			}
//			else if(plugin.pdata.playerInMinigame(reqpl)){
//				sender.sendMessage(ChatColor.RED + "This player is currently in a minigame, old inventory cannot be restored!");
//			}
			return true;
		}
		return false;
	}

}
