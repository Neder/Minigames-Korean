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
		return "�� �̴ϰ��ӿ� ����� ��� ���� �ʱ�ȭ ��ŵ�ϴ�. ex: ���ø������� �ʱ�ȭ ���� ���� ��";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame regen <�̴ϰ���>"};
	}

	
	public String getPermissionMessage() {
		return "����� �̴ϰ����� ������ ������ �����ϴ�!";
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
					sender.sendMessage(ChatColor.GRAY + mgm.getName() + "�̴ϰ����� ���ø��� ���� �����մϴ�.");
				}
				
				if(mgm.hasRestoreBlocks()){
					//mdata.restoreMinigameBlocks(mgm);
					sender.sendMessage(ChatColor.GRAY + mgm.getName() + "�̴ϰ����� ����� �����մϴ�.");
				}
			}
			else{
				sender.sendMessage(ChatColor.RED + "" + args[0] + " ��� �̸��� ���� �̴ϰ����� �����ϴ�!");
			}
			return true;
		}
		return false;
	}

}
