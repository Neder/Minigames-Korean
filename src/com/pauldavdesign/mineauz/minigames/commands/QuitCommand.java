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
		return "�ڽ� �Ǵ� �ٸ� ����� �̴ϰ��ӿ��� ������ �մϴ�.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame quit [�÷��̾�]"};
	}

	
	public String getPermissionMessage() {
		return "����� �̴ϰ��ӿ��� ���� ������ �����ϴ�!";
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
				sender.sendMessage(ChatColor.RED + "�̴ϰ��� �ȿ� �����ϴ�!");
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
					sender.sendMessage(ChatColor.GRAY + "��� �÷��̾ �̴ϰ��ӿ��� ���������ϴ�.");
					return true;
				}
				else if(players.isEmpty()){
					sender.sendMessage(ChatColor.RED + args[0] + "��� �̸��� ���� �̴ϰ����� �����ϴ�.");
					return true;
				}
				else{
					ply = plugin.pdata.getMinigamePlayer(players.get(0));
				}
				
				if(ply != null && ply.isInMinigame()){
					plugin.pdata.quitMinigame(ply, false);
					sender.sendMessage(ChatColor.GRAY + ply.getName() + " �� ������ �̴ϰ��ӿ��� ������ �߽��ϴ�.");
				}
				else{
					sender.sendMessage(ChatColor.RED + "�׷� �̸��� ���� �÷��̾�� �����ϴ�!");
				}
			}
			else if(player != null){
				sender.sendMessage(ChatColor.RED + "�ٸ� ����� ������ �� ������ �����ϴ�!");
				sender.sendMessage(ChatColor.RED + "minigame.quit.other");
			}
			return true;
		}
		return false;
	}

}
