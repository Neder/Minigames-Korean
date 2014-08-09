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
		return "베팅";
	}

	
	public String getCreatePermission() {
		return "minigame.sign.create.bet";
	}

	
	public String getCreatePermissionMessage() {
		return "당신은 배팅 표지판을 만들 권한이 없습니다!";
	}

	
	public String getUsePermission() {
		return "minigame.sign.use.bet";
	}

	
	public String getUsePermissionMessage() {
		return "당신은 베팅 표지판을 사용할 권한이 없습니다!";
	}

	
	public boolean signCreate(SignChangeEvent event) {
		if(plugin.mdata.hasMinigame(event.getLine(2))){
			event.setLine(1, ChatColor.GREEN + "베팅");
			event.setLine(2, plugin.mdata.getMinigame(event.getLine(2)).getName());
			if(event.getLine(3).matches("[0-9]+")){
				event.setLine(3, event.getLine(3) + "원");
			}
			return true;
		}
		event.getPlayer().sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "\"" + event.getLine(2) + "\" 라는 이름을 가진 미니게임은 없습니다!");
		return false;
	}

	
	public boolean signUse(Sign sign, MinigamePlayer player) {
		Minigame mgm = plugin.mdata.getMinigame(sign.getLine(2));
		if(mgm != null && (player.getPlayer().getItemInHand().getType() != Material.AIR || (sign.getLine(3).endsWith("원") && player.getPlayer().getItemInHand().getType() == Material.AIR))){
			if(mgm.isEnabled() && (!mgm.getUsePermissions() || player.getPlayer().hasPermission("minigame.join." + mgm.getName().toLowerCase()))){
				if(mgm.isSpectator(player)){
					return false;
				}
				
				if(!sign.getLine(3).endsWith("원")){
					plugin.pdata.joinWithBet(player, plugin.mdata.getMinigame(sign.getLine(2)), 0d);
				}
				else{
					if(plugin.hasEconomy()){
						Double bet = Double.parseDouble(sign.getLine(3).replace("원", ""));
						plugin.pdata.joinWithBet(player, plugin.mdata.getMinigame(sign.getLine(2)), bet);
						return true;
					}
					else{
						player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "이 서버는 Vault 를 사용하지 않습니다! 돈 베팅이 비활성화 되었습니다.");
					}
				}
			}
			else if(!mgm.isEnabled()){
				player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "이 미니게임은 활성화되지 않았습니다.");
			}
			else if(mgm.getUsePermissions()){
				player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "\"minigame.join." + mgm.getName().toLowerCase() + "\" 펄미션이 없습니다!");
			}
		}
		else if(mgm != null && player.getPlayer().getItemInHand().getType() == Material.AIR && !sign.getLine(3).endsWith("원")){
			player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "손에 베팅할 것을 들고 클릭해 주십시오!");
		}
		else if(mgm != null && player.getPlayer().getItemInHand().getType() != Material.AIR && sign.getLine(3).endsWith("원")){
			player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "이 표지판을 사용하려면 손에 아무것도 없어야 합니다.");
		}
		else{
			player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "그 미니게임은 존재하지 않습니다!");
		}
		return false;
	}

}
