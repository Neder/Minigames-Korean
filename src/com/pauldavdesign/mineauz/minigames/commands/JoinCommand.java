package com.pauldavdesign.mineauz.minigames.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pauldavdesign.mineauz.minigames.Minigame;

public class JoinCommand implements ICommand{

	
	public String getName() {
		return "join";
	}
	
	
	public String[] getAliases(){
		return null;
	}

	
	public boolean canBeConsole() {
		return false;
	}

	
	public String getDescription() {
		return "�̴ϰ��ӿ� ������ ����  �մϴ�.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame join <�̴ϰ���>"};
	}

	
	public String getPermissionMessage() {
		return "�̴ϰ��ӿ� �� ������ �����ϴ�!";
	}

	
	public String getPermission() {
		return "minigame.join";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		Player player = (Player)sender;
		if(args != null){
			Minigame mgm = plugin.mdata.getMinigame(args[0]);
			if(mgm != null && (!mgm.getUsePermissions() || player.hasPermission("minigame.join." + mgm.getName().toLowerCase()))){
				if(!plugin.pdata.getMinigamePlayer(player).isInMinigame()){
					sender.sendMessage(ChatColor.GREEN + "" + mgm + "�̴ϰ����� �����մϴ�!");
					plugin.pdata.joinMinigame(plugin.pdata.getMinigamePlayer(player), mgm);
				}
				else {
					player.sendMessage(ChatColor.RED + "�̹� �̴ϰ����� �÷����ϰ� �ֽ��ϴ�! �����ٰ� �ٽ� �����ʽÿ�!");
				}
			}
			else if(mgm != null && mgm.getUsePermissions()){
				player.sendMessage(ChatColor.RED + "minigame.join." + mgm.getName().toLowerCase() + " �޹̼��� �����ϴ�!");
			}
			else{
				player.sendMessage(ChatColor.RED + "�� �̴ϰ����� �������� �ʽ��ϴ�!");
			}
			return true;
		}
		return false;
	}

}
