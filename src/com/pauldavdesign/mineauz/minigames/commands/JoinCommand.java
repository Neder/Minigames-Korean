package com.pauldavdesign.mineauz.minigames.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pauldavdesign.mineauz.minigames.Minigame;

public class JoinCommand implements ICommand{

	
	public String getName() {
		return "join";
	}
	
	
	public String[] getAliases(){
		return null;
	}

	
	public boolean canBeConsole() {
		return false;
	}

	
	public String getDescription() {
		return "미니게임에 강제로 들어가게 합니다.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame join <미니게임>"};
	}

	
	public String getPermissionMessage() {
		return "미니게임에 들어갈 권한이 없습니다!";
	}

	
	public String getPermission() {
		return "minigame.join";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		Player player = (Player)sender;
		if(args != null){
			Minigame mgm = plugin.mdata.getMinigame(args[0]);
			if(mgm != null && (!mgm.getUsePermissions() || player.hasPermission("minigame.join." + mgm.getName().toLowerCase()))){
				if(!plugin.pdata.getMinigamePlayer(player).isInMinigame()){
					sender.sendMessage(ChatColor.GREEN + "" + mgm + "미니게임을 시작합니다!");
					plugin.pdata.joinMinigame(plugin.pdata.getMinigamePlayer(player), mgm);
				}
				else {
					player.sendMessage(ChatColor.RED + "이미 미니게임을 플레이하고 있습니다! 나갔다가 다시 들어오세요!");
				}
			}
			else if(mgm != null && mgm.getUsePermissions()){
				player.sendMessage(ChatColor.RED + "minigame.join." + mgm.getName().toLowerCase() + " 펄미션이 없습니다!");
			}
			else{
				player.sendMessage(ChatColor.RED + "그 미니게임은 존재하지 않습니다!");
			}
			return true;
		}
		return false;
	}

}
