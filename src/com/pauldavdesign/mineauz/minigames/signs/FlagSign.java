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
		return "깃발";
	}

	
	public String getCreatePermission() {
		return "minigame.sign.create.flag";
	}

	
	public String getCreatePermissionMessage() {
		return "당신은 깃발 표지판을 만들 권한이 없습니다!";
	}

	
	public String getUsePermission() {
		return null;
	}

	
	public String getUsePermissionMessage() {
		return null;
	}

	
	public boolean signCreate(SignChangeEvent event) {
		event.setLine(1, ChatColor.GREEN + "깃발");
		if(event.getLine(2).equalsIgnoreCase("레드")){
			event.setLine(2, ChatColor.RED + "레드");
		}
		else if(event.getLine(2).equalsIgnoreCase("블루")){
			event.setLine(2, ChatColor.BLUE + "블루");
		}
		else if(event.getLine(2).equalsIgnoreCase("기본")){
			event.setLine(2, ChatColor.GRAY + "기본");
		}
		else if(event.getLine(2).equalsIgnoreCase("캡처") && !event.getLine(3).isEmpty()){
			event.setLine(2, ChatColor.GREEN + "캡처");
			if(event.getLine(3).equalsIgnoreCase("레드")){
				event.setLine(3, ChatColor.RED + "레드");
			}
			else if(event.getLine(3).equalsIgnoreCase("블루")){
				event.setLine(3, ChatColor.BLUE + "블루");
			}
			else if(event.getLine(3).equalsIgnoreCase("기본")){
				event.setLine(3, ChatColor.GRAY + "기본");
			}
			else{
				event.getBlock().breakNaturally();
				event.getPlayer().sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "표지판 양식이 맞지 않습니다!" +
						" 가능한 값은 레드,블루,기본입니다.");
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
						ChatColor.WHITE + sign.getLine(2).replaceAll(ChatColor.RED.toString(), "").replaceAll(ChatColor.BLUE.toString(), "") + " 깃발 가져감!");
				return true;
			}
		}
		else if(player.getPlayer().getItemInHand().getType() != Material.AIR)
			player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "이 표지판을 사용하려면 손이 비어있어야 합니다!");
		return false;
	}

}
