package com.pauldavdesign.mineauz.minigames.signs;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.MinigamePlayer;
import com.pauldavdesign.mineauz.minigames.Minigames;

public class BetSign implements MinigameSign{
	
	private static Minigames plugin = Minigames.plugin;

	
	public String getName() {
		return "����";
	}

	
	public String getCreatePermission() {
		return "minigame.sign.create.bet";
	}

	
	public String getCreatePermissionMessage() {
		return "����� ���� ǥ������ ���� ������ �����ϴ�!";
	}

	
	public String getUsePermission() {
		return "minigame.sign.use.bet";
	}

	
	public String getUsePermissionMessage() {
		return "����� ���� ǥ������ ����� ������ �����ϴ�!";
	}

	
	public boolean signCreate(SignChangeEvent event) {
		if(plugin.mdata.hasMinigame(event.getLine(2))){
			event.setLine(1, ChatColor.GREEN + "����");
			event.setLine(2, plugin.mdata.getMinigame(event.getLine(2)).getName());
			if(event.getLine(3).matches("[0-9]+")){
				event.setLine(3, event.getLine(3) + "��");
			}
			return true;
		}
		event.getPlayer().sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "\"" + event.getLine(2) + "\" ��� �̸��� ���� �̴ϰ����� �����ϴ�!");
		return false;
	}

	
	public boolean signUse(Sign sign, MinigamePlayer player) {
		Minigame mgm = plugin.mdata.getMinigame(sign.getLine(2));
		if(mgm != null && (player.getPlayer().getItemInHand().getType() != Material.AIR || (sign.getLine(3).endsWith("��") && player.getPlayer().getItemInHand().getType() == Material.AIR))){
			if(mgm.isEnabled() && (!mgm.getUsePermissions() || player.getPlayer().hasPermission("minigame.join." + mgm.getName().toLowerCase()))){
				if(mgm.isSpectator(player)){
					return false;
				}
				
				if(!sign.getLine(3).endsWith("��")){
					plugin.pdata.joinWithBet(player, plugin.mdata.getMinigame(sign.getLine(2)), 0d);
				}
				else{
					if(plugin.hasEconomy()){
						Double bet = Double.parseDouble(sign.getLine(3).replace("��", ""));
						plugin.pdata.joinWithBet(player, plugin.mdata.getMinigame(sign.getLine(2)), bet);
						return true;
					}
					else{
						player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "�� ������ Vault �� ������� �ʽ��ϴ�! �� ������ ��Ȱ��ȭ �Ǿ����ϴ�.");
					}
				}
			}
			else if(!mgm.isEnabled()){
				player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "�� �̴ϰ����� Ȱ��ȭ���� �ʾҽ��ϴ�.");
			}
			else if(mgm.getUsePermissions()){
				player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "\"minigame.join." + mgm.getName().toLowerCase() + "\" �޹̼��� �����ϴ�!");
			}
		}
		else if(mgm != null && player.getPlayer().getItemInHand().getType() == Material.AIR && !sign.getLine(3).endsWith("��")){
			player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "�տ� ������ ���� ��� Ŭ���� �ֽʽÿ�!");
		}
		else if(mgm != null && player.getPlayer().getItemInHand().getType() != Material.AIR && sign.getLine(3).endsWith("��")){
			player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "�� ǥ������ ����Ϸ��� �տ� �ƹ��͵� ����� �մϴ�.");
		}
		else{
			player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "�� �̴ϰ����� �������� �ʽ��ϴ�!");
		}
		return false;
	}

}
