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
		return "들어가기";
	}

	
	public String getCreatePermission() {
		return "minigame.sign.create.join";
	}

	
	public String getCreatePermissionMessage() {
		return "당신은 미니게임 들어가기 표지판을 만들 권한이 없습니다!";
	}

	
	public String getUsePermission() {
		return "minigame.sign.use.join";
	}

	
	public String getUsePermissionMessage() {
		return "당신은 미니게임 들어가기 표지판을 사용할 권한이 없습니다!";
	}

	
	public boolean signCreate(SignChangeEvent event) {
		if(plugin.mdata.hasMinigame(event.getLine(2))){
			event.setLine(1, ChatColor.GREEN + "들어가기");
			event.setLine(2, plugin.mdata.getMinigame(event.getLine(2)).getName());
			if(Minigames.plugin.hasEconomy()){
				if(!event.getLine(3).isEmpty() && !event.getLine(3).matches("\\$?[0-9]+(.[0-9]{2})?")){
					event.getPlayer().sendMessage(ChatColor.RED + "알수없는 돈 액수!");
					return false;
				}
				else if(event.getLine(3).matches("[0-9]+(.[0-9]{2})?")){
					event.setLine(3, event.getLine(3) + "원");
				}
			}
			else{
				event.setLine(3, "");
				event.getPlayer().sendMessage(ChatColor.RED + "돈을 사용하려면 Vault 가 필요합니다!");
			}
			return true;
		}
		event.getPlayer().sendMessage(ChatColor.RED + "\"" + event.getLine(2) + "\" 라는 이름을 가진 미니게임이 없습니다!");
		return false;
	}

	
	public boolean signUse(Sign sign, MinigamePlayer player) {
		if(player.getPlayer().getItemInHand().getType() == Material.AIR && !player.isInMinigame()){
			Minigame mgm = plugin.mdata.getMinigame(sign.getLine(2));
			if(mgm != null && (!mgm.getUsePermissions() || player.getPlayer().hasPermission("minigame.join." + mgm.getName().toLowerCase()))){
				if(mgm.isEnabled()){
					if(!sign.getLine(3).isEmpty() && Minigames.plugin.hasEconomy()){
						double amount = Double.parseDouble(sign.getLine(3).replace("원", ""));
						if(Minigames.plugin.getEconomy().getBalance(player.getName()) >= amount){
							Minigames.plugin.getEconomy().withdrawPlayer(player.getName(), amount);
						}
						else{
							player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "당신은 이 미니게임에 들어갈 만한 돈이 없습니다!");
							return false;
						}
					}
					plugin.pdata.joinMinigame(player, mgm);
					return true;
				}
				else if(!mgm.isEnabled()){
					player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "이 미니게임은 활성화 되어있지 않습니다.");
				}
			}
			else if(mgm == null){
				player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "이 미니게임은 존재하지 않습니다!");
			}
			else if(mgm.getUsePermissions()){
				player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "minigame.join." + mgm.getName().toLowerCase() + " 펄미션이 없습니다!");
			}
		}
		else if(!player.isInMinigame())
			player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "이 표지판을 사용하려면 손이 비어있어야 합니다!");
		return false;
	}

}
