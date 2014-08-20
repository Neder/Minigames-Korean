package com.pauldavdesign.mineauz.minigames.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;

public class RegenCommand implements ICommand{

	
	public String getName() {
		return "regen";
	}

	
	public String[] getAliases() {
		return new String[] {"sregen", "regenerate"};
	}

	
	public boolean canBeConsole() {
		return false;
	}

	
	public String getDescription() {
		return "그 미니게임에 저장된 모든 블럭을 초기화 시킵니다. ex: 스플리프에서 초기화 되지 않은 눈";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame regen <미니게임>"};
	}

	
	public String getPermissionMessage() {
		return "당신은 미니게임을 리젠할 권한이 없습니다!";
	}

	
	public String getPermission() {
		return "minigame.regen";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			Minigame mgm = plugin.mdata.getMinigame(args[0]);
			
			if(mgm != null){
				if(mgm.getType().equals("spleef")){
					//SpleefFloorGen floor = new SpleefFloorGen(mgm.getSpleefFloor1(), mgm.getSpleefFloor2());
					//floor.regenFloor(mgm.getSpleefFloorMaterial(), true);
					sender.sendMessage(ChatColor.GRAY + mgm.getName() + "미니게임의 스플리프 눈을 리젠합니다.");
				}
				
				if(mgm.hasRestoreBlocks()){
					//mdata.restoreMinigameBlocks(mgm);
					sender.sendMessage(ChatColor.GRAY + mgm.getName() + "미니게임의 블록을 리젠합니다.");
				}
			}
			else{
				sender.sendMessage(ChatColor.RED + "" + args[0] + " 라는 이름을 가진 미니게임은 없습니다!");
			}
			return true;
		}
		return false;
	}

}
