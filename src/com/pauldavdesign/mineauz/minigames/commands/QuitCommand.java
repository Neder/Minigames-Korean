package com.pauldavdesign.mineauz.minigames.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.MinigamePlayer;

public class QuitCommand implements ICommand{

	
	public String getName() {
		return "quit";
	}
	
	
	public String[] getAliases(){
		return new String[] {"q"};
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "자신 또는 다른 사람을 미니게임에서 나가게 합니다.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame quit [플레이어]"};
	}

	
	public String getPermissionMessage() {
		return "당신은 미니게임에서 나갈 권한이 없습니다!";
	}

	
	public String getPermission() {
		return "minigame.quit";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args == null && sender instanceof Player){
			MinigamePlayer player = plugin.pdata.getMinigamePlayer((Player)sender);
			if(player.isInMinigame()){
				plugin.pdata.quitMinigame(player, false);
			}
			else {
				sender.sendMessage(ChatColor.RED + "미니게임 안에 없습니다!");
			}
			return true;
		}
		else if(args != null){
			Player player = null;
			if(sender instanceof Player){
				player = (Player)sender;
			}
			if(player == null || player.hasPermission("minigame.quit.other")){
				List<Player> players = plugin.getServer().matchPlayer(args[0]);
				MinigamePlayer ply = null;
				if(args[0].equals("ALL")){
					for(MinigamePlayer pl : plugin.getPlayerData().getAllMinigamePlayers()){
						if(pl.isInMinigame()){
							plugin.pdata.quitMinigame(pl, true);
						}
					}
					sender.sendMessage(ChatColor.GRAY + "모든 플레이어가 미니게임에서 나가졌습니다.");
					return true;
				}
				else if(players.isEmpty()){
					sender.sendMessage(ChatColor.RED + args[0] + "라는 이름을 가진 미니게임은 없습니다.");
					return true;
				}
				else{
					ply = plugin.pdata.getMinigamePlayer(players.get(0));
				}
				
				if(ply != null && ply.isInMinigame()){
					plugin.pdata.quitMinigame(ply, false);
					sender.sendMessage(ChatColor.GRAY + ply.getName() + " 를 강제로 미니게임에서 나가게 했습니다.");
				}
				else{
					sender.sendMessage(ChatColor.RED + "그런 이름을 가진 플레이어는 없습니다!");
				}
			}
			else if(player != null){
				sender.sendMessage(ChatColor.RED + "다른 사람을 나가게 할 권한이 없습니다!");
				sender.sendMessage(ChatColor.RED + "minigame.quit.other");
			}
			return true;
		}
		return false;
	}

}
