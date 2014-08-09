package com.pauldavdesign.mineauz.minigames.signs;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.MinigamePlayer;
import com.pauldavdesign.mineauz.minigames.Minigames;

public class JoinSign implements MinigameSign {
	
	private static Minigames plugin = Minigames.plugin;

	
	public String getName() {
		return "����";
	}

	
	public String getCreatePermission() {
		return "minigame.sign.create.join";
	}

	
	public String getCreatePermissionMessage() {
		return "����� �̴ϰ��� ���� ǥ������ ���� ������ �����ϴ�!";
	}

	
	public String getUsePermission() {
		return "minigame.sign.use.join";
	}

	
	public String getUsePermissionMessage() {
		return "����� �̴ϰ��� ���� ǥ������ ����� ������ �����ϴ�!";
	}

	
	public boolean signCreate(SignChangeEvent event) {
		if(plugin.mdata.hasMinigame(event.getLine(2))){
			event.setLine(1, ChatColor.GREEN + "����");
			event.setLine(2, plugin.mdata.getMinigame(event.getLine(2)).getName());
			return true;
		}
		event.getPlayer().sendMessage(ChatColor.RED + "\"" + event.getLine(2) + "\" ��� �̸��� ���� �̴ϰ����� �����ϴ�!");
		return false;
	}

	
	public boolean signUse(Sign sign, MinigamePlayer player) {
		if(player.getPlayer().getItemInHand().getType() == Material.AIR && !player.isInMinigame()){
			Minigame mgm = plugin.mdata.getMinigame(sign.getLine(2));
			if(mgm != null && (!mgm.getUsePermissions() || player.getPlayer().hasPermission("minigame.join." + mgm.getName().toLowerCase()))){
				if(mgm.isEnabled()){
					plugin.pdata.joinMinigame(player, mgm);
					return true;
				}
				else if(!mgm.isEnabled()){
					player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "�� �̴ϰ����� Ȱ��ȭ �Ǿ����� �ʽ��ϴ�.");
				}
			}
			else if(mgm == null){
				player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "�� �̴ϰ����� �������� �ʽ��ϴ�!");
			}
			else if(mgm.getUsePermissions()){
				player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "minigame.join." + mgm.getName().toLowerCase() + " �޹̼��� �����ϴ�!");
			}
		}
		else if(!player.isInMinigame())
			player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "�� ǥ������ ����Ϸ��� ���� ����־�� �մϴ�!");
		return false;
	}

}
