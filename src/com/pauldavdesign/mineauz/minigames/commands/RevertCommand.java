package com.pauldavdesign.mineauz.minigames.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.MinigamePlayer;
import com.pauldavdesign.mineauz.minigames.StoredPlayerCheckpoints;

public class RevertCommand implements ICommand{

	
	public String getName() {
		return "revert";
	}

	
	public String[] getAliases() {
		return new String[] {"r"};
	}

	
	public boolean canBeConsole() {
		return false;
	}

	
	public String getDescription() {
		return "�÷��̾��� ������ üũ����Ʈ�� ���ư��ϴ�.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame revert"};
	}

	
	public String getPermissionMessage() {
		return "����� üũ����Ʈ�� ���ư� ������ �����ϴ�!";
	}

	
	public String getPermission() {
		return "minigame.revert";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		MinigamePlayer player = plugin.pdata.getMinigamePlayer((Player)sender);
		
		if(player.isInMinigame()){
			plugin.pdata.revertToCheckpoint(player);
		}
		else if(plugin.pdata.hasStoredPlayerCheckpoint(player)){
			StoredPlayerCheckpoints spc = plugin.pdata.getPlayersStoredCheckpoints(player);
			if(spc.hasGlobalCheckpoint()){
				player.getPlayer().teleport(spc.getGlobalCheckpoint());
			}
		}
		else {
			player.sendMessage(ChatColor.RED + "����� �ƹ� üũ����Ʈ�� �����ϴ�!");
		}
		return true;
	}

}
