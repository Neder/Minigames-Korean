package com.pauldavdesign.mineauz.minigames.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;

public class ListCommand implements ICommand{

	
	public String getName() {
		return "list";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "미니게임 리스트를 봅니다.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame list"};
	}

	
	public String getPermissionMessage() {
		return "당신은 모든 미니게임 리스트를 볼 권한이 없습니다!";
	}

	
	public String getPermission() {
		return "minigame.list";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		List<String> mglist = plugin.getConfig().getStringList("minigames");
		String minigames = "";
		
		for(int i = 0; i < mglist.size(); i++){
			minigames += mglist.get(i);
			if(i != mglist.size() - 1){
				minigames += ", ";
			}
		}
		
		sender.sendMessage(ChatColor.GRAY + minigames);
		return true;
	}

}
