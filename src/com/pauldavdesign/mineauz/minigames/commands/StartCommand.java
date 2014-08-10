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
		return "����ã�� �̴ϰ����� �����մϴ�.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame start <�̴ϰ���>"};
	}

	
	public String getPermissionMessage() {
		return "����ã�� �̴ϰ����� ������ ������ �����ϴ�!";
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
				sender.sendMessage(ChatColor.RED + "\"" + args[0] + "\" ��� �̸��� ���� ����ã�� �̴ϰ����� �����ϴ�!");
			}
			else if(mgm.getThTimer() != null){
				sender.sendMessage(ChatColor.RED + mgm.getName() + " �̴ϰ����� �̹� �����Ͽ����ϴ�!");
			}
			return true;
		}
		return false;
	}

}
