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
		return "�̴ϰ��� Ÿ�̸Ӹ� ����/����� �մϴ�.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame toggletimer <�̴ϰ���>"};
	}

	
	public String getPermissionMessage() {
		return "�̴ϰ����� Ÿ�̸Ӹ� ����� ������ �����ϴ�!";
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
						sender.sendMessage(ChatColor.GRAY + "" + mgm.getName() + "�̴ϰ����� Ÿ�̸Ӹ� ������մϴ�.");
					}
					else{
						mgm.getMpTimer().pauseTimer(sender.getName() + " ���� Ÿ�̸Ӹ� �������׽��ϴ�.");
						sender.sendMessage(ChatColor.GRAY + "" + mgm.getName() + " �̴ϰ����� Ÿ�̸Ӹ� �������׽��ϴ�. (���� �ð�" + mgm.getMpTimer().getPlayerWaitTimeLeft() + "��)");
					}
				}
				else{
					sender.sendMessage(ChatColor.RED + "�� �̴ϰ����� ����� Ÿ�̸Ӱ� �����ϴ�!");
				}
			}
			else{
				sender.sendMessage(ChatColor.RED + "" + args[0] + " �̴ϰ����� �������� �ʽ��ϴ�!");
			}
			return true;
		}
		return false;
	}

}
