package com.pauldavdesign.mineauz.minigames.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;

public class PartyModeCommand implements ICommand{

	
	public String getName() {
		return "partymode";
	}

	
	public String[] getAliases() {
		return new String[] {"pm", "party"};
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "��Ƽ ��带 ���� �մϴ�.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame partymode <true/false>"};
	}

	
	public String getPermissionMessage() {
		return "����� ��Ƽ ���� �ٲ� �� �����ϴ�!";
	}

	
	public String getPermission() {
		return "minigame.partymode";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			boolean bool = Boolean.parseBoolean(args[0]);
			plugin.pdata.setPartyMode(bool);
			if(bool){
				sender.sendMessage(ChatColor.GREEN + "��Ƽ ��尡 �������ϴ�! ��ȣ!");
			}
			else{
				sender.sendMessage(ChatColor.RED + "��Ƽ ��尡 �������ϴ�. �Ф�");
			}
			return true;
		}
		return false;
	}

}
