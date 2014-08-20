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
		return "로드아웃";
	}

	
	public String getCreatePermission() {
		return "minigame.sign.create.loadout";
	}

	
	public String getCreatePermissionMessage() {
		return "미니게임 로드아웃 표지판을 만들 권한이 없습니다!";
	}

	
	public String getUsePermission() {
		return "minigame.sign.use.loadout";
	}

	
	public String getUsePermissionMessage() {
		return "미니게임 로드아웃 표지판을 사용할 권한이 없습니다!";
	}

	
	public boolean signCreate(SignChangeEvent event) {
		event.setLine(1, ChatColor.GREEN + "로드아웃");
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
					player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "" + sign.getLine(2) + " 로드아웃을 장착하였습니다.");
					
					if(mgm.getType().equals("sp") || (mgm.getMpTimer() != null && mgm.getMpTimer().getStartWaitTimeLeft() == 0)){
						if(sign.getLine(3).equalsIgnoreCase("리스폰")){
							player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "이 로드아웃은 다음번 리스폰 때 적용됩니다.");
						}
						else{
							mgm.getLoadout(sign.getLine(2)).equiptLoadout(player);
						}
					}
					return true;
				}
				else{
					player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "" + sign.getLine(2) + " 로드아웃을 사용할 권한이 없습니다.");
				}
			}
			else if(plugin.mdata.hasLoadout(sign.getLine(2))){
				if(!plugin.mdata.getLoadout(sign.getLine(2)).getUsePermissions() || player.getPlayer().hasPermission("minigame.loadout." + sign.getLine(2).toLowerCase())){
					mgm.setPlayersLoadout(player, plugin.mdata.getLoadout(sign.getLine(2)));
					player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "" + sign.getLine(2) + " 로드아웃을 장착하였습니다.");

					if(mgm.getType().equals("sp") || (mgm.getMpTimer() != null && mgm.getMpTimer().getStartWaitTimeLeft() == 0)){
						if(sign.getLine(3).equalsIgnoreCase("리스폰")){
							player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "이 로드아웃은 다음번 리스폰 때 적용됩니다.");
						}
						else{
							plugin.mdata.getLoadout(sign.getLine(2)).equiptLoadout(player);
						}
					}
					return true;
				}
				else{
					player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "" + sign.getLine(2) + " 로드아웃을 사용할 권한이 없습니다.");
				}
			}
			else{
				player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "이 로드아웃은 존재하지 않습니다!");
			}
		}
		else if(player.getPlayer().getItemInHand().getType() != Material.AIR)
			player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "이 표지판을 사용하려면 손이 비어있어야 합니다!");
		return false;
	}

}
