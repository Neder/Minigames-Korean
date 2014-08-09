package com.pauldavdesign.mineauz.minigames.signs;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.MinigamePlayer;
import com.pauldavdesign.mineauz.minigames.Minigames;

public class LoadoutSign implements MinigameSign {
	
	private static Minigames plugin = Minigames.plugin;

	
	public String getName() {
		return "�ε�ƿ�";
	}

	
	public String getCreatePermission() {
		return "minigame.sign.create.loadout";
	}

	
	public String getCreatePermissionMessage() {
		return "�̴ϰ��� �ε�ƿ� ǥ������ ���� ������ �����ϴ�!";
	}

	
	public String getUsePermission() {
		return "minigame.sign.use.loadout";
	}

	
	public String getUsePermissionMessage() {
		return "�̴ϰ��� �ε�ƿ� ǥ������ ����� ������ �����ϴ�!";
	}

	
	public boolean signCreate(SignChangeEvent event) {
		event.setLine(1, ChatColor.GREEN + "�ε�ƿ�");
		return true;
	}

	
	public boolean signUse(Sign sign, MinigamePlayer player) {
		if(player.getPlayer().getItemInHand().getType() == Material.AIR && player.isInMinigame()){
			Minigame mgm = player.getMinigame();
			if(mgm == null || mgm.isSpectator(player)){
				return false;
			}
			
			if(mgm.hasLoadout(sign.getLine(2))){
				if(!mgm.getLoadout(sign.getLine(2)).getUsePermissions() || player.getPlayer().hasPermission("minigame.loadout." + sign.getLine(2).toLowerCase())){
					mgm.setPlayersLoadout(player, mgm.getLoadout(sign.getLine(2)));
					player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "" + sign.getLine(2) + " �ε�ƿ��� �����Ͽ����ϴ�.");
					
					if(mgm.getType().equals("sp") || (mgm.getMpTimer() != null && mgm.getMpTimer().getStartWaitTimeLeft() == 0)){
						if(sign.getLine(3).equalsIgnoreCase("������")){
							player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "�� �ε�ƿ��� ������ ������ �� ����˴ϴ�.");
						}
						else{
							mgm.getLoadout(sign.getLine(2)).equiptLoadout(player);
						}
					}
					return true;
				}
				else{
					player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "" + sign.getLine(2) + " �ε�ƿ��� ����� ������ �����ϴ�.");
				}
			}
			else if(plugin.mdata.hasLoadout(sign.getLine(2))){
				if(!plugin.mdata.getLoadout(sign.getLine(2)).getUsePermissions() || player.getPlayer().hasPermission("minigame.loadout." + sign.getLine(2).toLowerCase())){
					mgm.setPlayersLoadout(player, plugin.mdata.getLoadout(sign.getLine(2)));
					player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "" + sign.getLine(2) + " �ε�ƿ��� �����Ͽ����ϴ�.");

					if(mgm.getType().equals("sp") || (mgm.getMpTimer() != null && mgm.getMpTimer().getStartWaitTimeLeft() == 0)){
						if(sign.getLine(3).equalsIgnoreCase("������")){
							player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "�� �ε�ƿ��� ������ ������ �� ����˴ϴ�.");
						}
						else{
							plugin.mdata.getLoadout(sign.getLine(2)).equiptLoadout(player);
						}
					}
					return true;
				}
				else{
					player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "" + sign.getLine(2) + " �ε�ƿ��� ����� ������ �����ϴ�.");
				}
			}
			else{
				player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "�� �ε�ƿ��� �������� �ʽ��ϴ�!");
			}
		}
		else if(player.getPlayer().getItemInHand().getType() != Material.AIR)
			player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "�� ǥ������ ����Ϸ��� ���� ����־�� �մϴ�!");
		return false;
	}

}
