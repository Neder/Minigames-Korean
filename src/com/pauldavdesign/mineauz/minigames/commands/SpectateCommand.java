package com.pauldavdesign.mineauz.minigames.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.MinigamePlayer;

public class SpectateCommand implements ICommand {

	
	public String getName() {
		return "spectate";
	}

	
	public String[] getAliases() {
		return new String[] {"spec"};
	}

	
	public boolean canBeConsole() {
		return false;
	}

	
	public String getDescription() {
		return "�̴ϰ��ӿ��� ������ �����ϰ� �մϴ�.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame spectate <�̴ϰ���>"};
	}

	
	public String getPermissionMessage() {
		return "����� ���� Ŀ�ǵ带 ����� ������ �����ϴ�!";
	}

	
	public String getPermission() {
		return "minigame.spectate";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			if(plugin.mdata.hasMinigame(args[0])){
				MinigamePlayer ply = plugin.pdata.getMinigamePlayer((Player) sender);
				Minigame mgm = plugin.mdata.getMinigame(args[0]);
				plugin.pdata.spectateMinigame(ply, mgm);
			}
			else{
				sender.sendMessage(ChatColor.RED + "" + args[0] + "��� �̸��� ���� �̴ϰ����� �����ϴ�!");
			}
			return true;
		}
		return false;
	}

}
