package com.pauldavdesign.mineauz.minigames.signs;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.SignChangeEvent;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.MinigamePlayer;

public class FlagSign implements MinigameSign {

	
	public String getName() {
		return "���";
	}

	
	public String getCreatePermission() {
		return "minigame.sign.create.flag";
	}

	
	public String getCreatePermissionMessage() {
		return "����� ��� ǥ������ ���� ������ �����ϴ�!";
	}

	
	public String getUsePermission() {
		return null;
	}

	
	public String getUsePermissionMessage() {
		return null;
	}

	
	public boolean signCreate(SignChangeEvent event) {
		event.setLine(1, ChatColor.GREEN + "���");
		if(event.getLine(2).equalsIgnoreCase("����")){
			event.setLine(2, ChatColor.RED + "����");
		}
		else if(event.getLine(2).equalsIgnoreCase("���")){
			event.setLine(2, ChatColor.BLUE + "���");
		}
		else if(event.getLine(2).equalsIgnoreCase("�⺻")){
			event.setLine(2, ChatColor.GRAY + "�⺻");
		}
		else if(event.getLine(2).equalsIgnoreCase("ĸ��") && !event.getLine(3).isEmpty()){
			event.setLine(2, ChatColor.GREEN + "ĸ��");
			if(event.getLine(3).equalsIgnoreCase("����")){
				event.setLine(3, ChatColor.RED + "����");
			}
			else if(event.getLine(3).equalsIgnoreCase("���")){
				event.setLine(3, ChatColor.BLUE + "���");
			}
			else if(event.getLine(3).equalsIgnoreCase("�⺻")){
				event.setLine(3, ChatColor.GRAY + "�⺻");
			}
			else{
				event.getBlock().breakNaturally();
				event.getPlayer().sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "ǥ���� ����� �˸��� �ʽ��ϴ�!" +
						" ������ ���� ����,���,�⺻�Դϴ�.");
				return false;
			}
		}
		return true;
	}

	
	public boolean signUse(Sign sign, MinigamePlayer player) {
		if(player.getPlayer().getItemInHand().getType() == Material.AIR && player.isInMinigame()){
			Minigame mgm = player.getMinigame();

			if(mgm.isSpectator(player)){
				return false;
			}
			if(!sign.getLine(2).isEmpty() && ((LivingEntity)player.getPlayer()).isOnGround() && 
					!mgm.getScoreType().equals("ctf") &&
					!player.hasFlag(sign.getLine(2).replaceAll(ChatColor.RED.toString(), "").replaceAll(ChatColor.BLUE.toString(), ""))){
				player.addFlag(sign.getLine(2).replaceAll(ChatColor.RED.toString(), "").replaceAll(ChatColor.BLUE.toString(), ""));
				player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + 
						ChatColor.WHITE + sign.getLine(2).replaceAll(ChatColor.RED.toString(), "").replaceAll(ChatColor.BLUE.toString(), "") + " ��� ������!");
				return true;
			}
		}
		else if(player.getPlayer().getItemInHand().getType() != Material.AIR)
			player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "�� ǥ������ ����Ϸ��� ���� ����־�� �մϴ�!");
		return false;
	}

}
