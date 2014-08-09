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
		return "����ã�� �̴ϰ����� �����մϴ�.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame stop <�̴ϰ���>"};
	}

	
	public String getPermissionMessage() {
		return "����ã�� �̴ϰ����� ������ ������ �����ϴ�!";
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
					plugin.getServer().broadcast(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + mgm.getName() + " �̴ϰ����� ������ ��� ���忡�� ���������� ���ŵǾ����ϴ�!", "minigame.treasure.announce");
				}
				mgm.getThTimer().stopTimer();
				plugin.mdata.removeTreasure(mgm.getName());
				mgm.setThTimer(null);
			}
			else if(mgm == null || !mgm.getType().equals("th")){
				sender.sendMessage(ChatColor.RED + "\"" + args[0] + "\" ��� �̸��� ���� �̴ϰ����� �����ϴ�!");
			}
			else{
				sender.sendMessage(ChatColor.RED + mgm.getName() + " �̴ϰ����� ����ǰ� ���� �ʽ��ϴ�!");
			}
			return true;
		}
		return false;
	}
}
