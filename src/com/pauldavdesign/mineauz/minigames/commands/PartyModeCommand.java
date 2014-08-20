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
		return "파티 모드를 끄고 켭니다.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame partymode <true/false>"};
	}

	
	public String getPermissionMessage() {
		return "파티 모드로 바꿀 권한이 없습니다!";
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
				sender.sendMessage(ChatColor.GREEN + "파티 모드가 켜졌습니다! 야호!");
			}
			else{
				sender.sendMessage(ChatColor.RED + "파티 모드가 꺼졌습니다. ㅠㅠ");
			}
			return true;
		}
		return false;
	}

}
