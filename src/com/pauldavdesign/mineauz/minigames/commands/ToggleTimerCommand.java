package com.pauldavdesign.mineauz.minigames.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;

public class ToggleTimerCommand implements ICommand{

	
	public String getName() {
		return "toggletimer";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "미니게임 타이머를 중지/재시작 합니다.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame toggletimer <미니게임>"};
	}

	
	public String getPermissionMessage() {
		return "미니게임의 타이머를 토글할 권한이 없습니다!";
	}

	
	public String getPermission() {
		return "minigame.toggletimer";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			Minigame mgm = plugin.mdata.getMinigame(args[0]);
			if(mgm != null){
				if(mgm.getMpTimer() != null){
					if(mgm.getMpTimer().isPaused()){
						mgm.getMpTimer().resumeTimer();
						sender.sendMessage(ChatColor.GRAY + "" + mgm.getName() + "미니게임의 타이머를 재시작합니다.");
					}
					else{
						mgm.getMpTimer().pauseTimer(sender.getName() + " 님이 타이머를 일시 정지시켰습니다.");
						sender.sendMessage(ChatColor.GRAY + "" + mgm.getName() + " 미니게임의 타이머를 일시 정지시켰습니다. (남은 시간" + mgm.getMpTimer().getPlayerWaitTimeLeft() + "초)");
					}
				}
				else{
					sender.sendMessage(ChatColor.RED + "그 미니게임에는 실행 중인 타이머가 없습니다!");
				}
			}
			else{
				sender.sendMessage(ChatColor.RED + "" + args[0] + "(이)라는 미니게임은 존재하지 않습니다!");
			}
			return true;
		}
		return false;
	}

}
