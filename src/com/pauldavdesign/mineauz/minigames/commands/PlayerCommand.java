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
		return "�÷��̾��� ������ ���� ���ϴ�.";
	}

	
	public String[] getParameters() {
		return new String[] {"<�÷��̾� �̸�>", "list"};
	}

	
	public String[] getUsage() {
		return new String[]{
				"/minigame player <�÷��̾� �̸�>",
				"/minigame player list"
		};
	}

	
	public String getPermissionMessage() {
		return "����� �̴ϰ��� �÷��̾ ���� �� ������ �����ϴ�!";
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
				
				sender.sendMessage(ChatColor.AQUA + "-----------�̴ϰ����� �÷������� ���-----------");
				if(!pls.isEmpty()){
					for(MinigamePlayer pl : pls){
						sender.sendMessage(ChatColor.GREEN + pl.getName() + ChatColor.GRAY + " (\"" + pl.getMinigame().getName() + "\" �÷��� ��)");
					}
				}
				else{
					sender.sendMessage(ChatColor.RED + "����");
				}
			}
			else{
				List<Player> plmatch = Minigames.plugin.getServer().matchPlayer(args[0]);
				if(!plmatch.isEmpty()){
					MinigamePlayer pl = Minigames.plugin.getPlayerData().getMinigamePlayer(plmatch.get(0));
					sender.sendMessage(ChatColor.AQUA + "--------" + pl.getName() + " �� ����--------");
					if(pl.isInMinigame()){
						sender.sendMessage(ChatColor.GREEN + "�̴ϰ���: " + ChatColor.GRAY + pl.getMinigame().getName());
						sender.sendMessage(ChatColor.GREEN + "���ھ�: " + ChatColor.GRAY + pl.getScore());
						sender.sendMessage(ChatColor.GREEN + "ų: "  + ChatColor.GRAY + pl.getKills());
						sender.sendMessage(ChatColor.GREEN + "����: " + ChatColor.GRAY + pl.getDeaths());
						sender.sendMessage(ChatColor.GREEN + "üũ����Ʈ�� ���ƿ�: " + ChatColor.GRAY + pl.getReverts());
						sender.sendMessage(ChatColor.GREEN + "�÷��� �ð�: " + ChatColor.GRAY + 
								MinigameUtils.convertTime((int)((Calendar.getInstance().getTimeInMillis() - pl.getStartTime()) / 1000)));
					}
					else{
						sender.sendMessage(ChatColor.GREEN + "�̴ϰ���: " + ChatColor.RED + "�̴ϰ��� �ȿ� ����");
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