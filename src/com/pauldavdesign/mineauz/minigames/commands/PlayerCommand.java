package com.pauldavdesign.mineauz.minigames.commands;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.MinigamePlayer;
import com.pauldavdesign.mineauz.minigames.MinigameUtils;
import com.pauldavdesign.mineauz.minigames.Minigames;

public class PlayerCommand implements ICommand {

	
	public String getName() {
		return "player";
	}

	
	public String[] getAliases() {
		return new String[] {"ply", "pl"};
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "플레이어의 정보에 대해 봅니다.";
	}

	
	public String[] getParameters() {
		return new String[] {"<플레이어 이름>", "list"};
	}

	
	public String[] getUsage() {
		return new String[]{
				"/minigame player <플레이어 이름>",
				"/minigame player list"
		};
	}

	
	public String getPermissionMessage() {
		return "당신은 미니게임 플레이어에 대해 볼 권한이 없습니다!";
	}

	
	public String getPermission() {
		return "minigame.player";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			if(args[0].equalsIgnoreCase("list")){
				List<MinigamePlayer> pls = new ArrayList<MinigamePlayer>();
				for(MinigamePlayer pl : Minigames.plugin.getPlayerData().getAllMinigamePlayers()){
					if(pl.isInMinigame()){
						pls.add(pl);
					}
				}
				
				sender.sendMessage(ChatColor.AQUA + "-----------미니게임을 플레이중인 사람-----------");
				if(!pls.isEmpty()){
					for(MinigamePlayer pl : pls){
						sender.sendMessage(ChatColor.GREEN + pl.getName() + ChatColor.GRAY + " (\"" + pl.getMinigame().getName() + "\" 플레이 중)");
					}
				}
				else{
					sender.sendMessage(ChatColor.RED + "없음");
				}
			}
			else{
				List<Player> plmatch = Minigames.plugin.getServer().matchPlayer(args[0]);
				if(!plmatch.isEmpty()){
					MinigamePlayer pl = Minigames.plugin.getPlayerData().getMinigamePlayer(plmatch.get(0));
					sender.sendMessage(ChatColor.AQUA + "--------" + pl.getName() + " 의 정보--------");
					if(pl.isInMinigame()){
						sender.sendMessage(ChatColor.GREEN + "미니게임: " + ChatColor.GRAY + pl.getMinigame().getName());
						sender.sendMessage(ChatColor.GREEN + "스코어: " + ChatColor.GRAY + pl.getScore());
						sender.sendMessage(ChatColor.GREEN + "킬: "  + ChatColor.GRAY + pl.getKills());
						sender.sendMessage(ChatColor.GREEN + "데스: " + ChatColor.GRAY + pl.getDeaths());
						sender.sendMessage(ChatColor.GREEN + "체크포인트로 돌아옴: " + ChatColor.GRAY + pl.getReverts());
						sender.sendMessage(ChatColor.GREEN + "플레이 시간: " + ChatColor.GRAY + 
								MinigameUtils.convertTime((int)((Calendar.getInstance().getTimeInMillis() - pl.getStartTime()) / 1000)));
					}
					else{
						sender.sendMessage(ChatColor.GREEN + "미니게임: " + ChatColor.RED + "미니게임 안에 없음");
					}
				}
				else{
					sender.sendMessage(ChatColor.RED + "Could not find a player by the name \"" + args[0] + "\"");
				}
			}
			return true;
		}
		return false;
	}

}