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
		return "플레이어의 마지막 체크포인트로 돌아갑니다.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame revert"};
	}

	
	public String getPermissionMessage() {
		return "당신은 체크포인트로 돌아갈 권한이 없습니다!";
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
			player.sendMessage(ChatColor.RED + "당신은 아무 체크포인트도 없습니다!");
		}
		return true;
	}

}
