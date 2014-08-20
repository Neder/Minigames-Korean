package com.pauldavdesign.mineauz.minigames.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;

public class StopCommand implements ICommand{

	
	public String getName() {
		return "stop";
	}
	
	
	public String[] getAliases(){
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "보물찾기 미니게임을 중지합니다.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame stop <미니게임>"};
	}

	
	public String getPermissionMessage() {
		return "보물찾기 미니게임을 중지할 권한이 없습니다!";
	}

	
	public String getPermission() {
		return "minigame.stop";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			Minigame mgm = plugin.mdata.getMinigame(args[0]);
			
			if(mgm != null && mgm.getThTimer() != null && mgm.getType().equals("th")){
				if(mgm.getThTimer().getChestInWorld()){
					plugin.getServer().broadcast(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + mgm.getName() + " 미니게임의 보물이 모든 월드에서 성공적으로 제거되었습니다!", "minigame.treasure.announce");
				}
				mgm.getThTimer().stopTimer();
				plugin.mdata.removeTreasure(mgm.getName());
				mgm.setThTimer(null);
			}
			else if(mgm == null || !mgm.getType().equals("th")){
				sender.sendMessage(ChatColor.RED + "\"" + args[0] + "\" 라는 이름을 가진 미니게임은 없습니다!");
			}
			else{
				sender.sendMessage(ChatColor.RED + mgm.getName() + " 미니게임은 실행되고 있지 않습니다!");
			}
			return true;
		}
		return false;
	}
}
