package com.pauldavdesign.mineauz.minigames.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;

public class StartCommand implements ICommand{

	
	public String getName() {
		return "start";
	}
	
	
	public String[] getAliases(){
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "보물찾기 미니게임을 시작합니다.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame start <미니게임>"};
	}

	
	public String getPermissionMessage() {
		return "보물찾기 미니게임을 시작할 권한이 없습니다!";
	}

	
	public String getPermission() {
		return "minigame.start";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			Minigame mgm = plugin.mdata.getMinigame(args[0]);
			
			if(mgm != null && mgm.getThTimer() == null && mgm.getType().equals("th")){
				plugin.mdata.startGlobalMinigame(mgm.getName());
				mgm.setEnabled(true);
			}
			else if(mgm == null || !mgm.getType().equals("th")){
				sender.sendMessage(ChatColor.RED + "\"" + args[0] + "\" 라는 이름을 가진 보물찾기 미니게임은 없습니다!");
			}
			else if(mgm.getThTimer() != null){
				sender.sendMessage(ChatColor.RED + mgm.getName() + " 미니게임은 이미 시작하였습니다!");
			}
			return true;
		}
		return false;
	}

}
