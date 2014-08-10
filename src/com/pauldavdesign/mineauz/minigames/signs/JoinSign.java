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
			if(Minigames.plugin.hasEconomy()){
				if(!event.getLine(3).isEmpty() && !event.getLine(3).matches("\\$?[0-9]+(.[0-9]{2})?")){
					event.getPlayer().sendMessage(ChatColor.RED + "�˼����� �� �׼�!");
					return false;
				}
				else if(event.getLine(3).matches("[0-9]+(.[0-9]{2})?")){
					event.setLine(3, event.getLine(3) + "��");
				}
			}
			else{
				event.setLine(3, "");
				event.getPlayer().sendMessage(ChatColor.RED + "���� ����Ϸ��� Vayle �� �ʿ��մϴ�!");
			}
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
					if(!sign.getLine(3).isEmpty() && Minigames.plugin.hasEconomy()){
						double amount = Double.parseDouble(sign.getLine(3).replace("��", ""));
						if(Minigames.plugin.getEconomy().getBalance(player.getName()) >= amount){
							Minigames.plugin.getEconomy().withdrawPlayer(player.getName(), amount);
						}
						else{
							player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "����� �� �̴ϰ��ӿ� �� ���� ���� �����ϴ�!");
							return false;
						}
					}
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
