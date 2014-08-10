package com.pauldavdesign.mineauz.minigames.signs;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.SignChangeEvent;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.MinigamePlayer;
import com.pauldavdesign.mineauz.minigames.Minigames;

public class FinishSign implements MinigameSign {
	
	private static Minigames plugin = Minigames.plugin;

	
	public String getName() {
		return "완료";
	}

	
	public String getCreatePermission() {
		return "minigame.sign.create.finish";
	}

	
	public String getCreatePermissionMessage() {
		return "미니게임 완료 표지판을 만들 권한이 없습니다!";
	}

	
	public String getUsePermission() {
		return null;
	}

	
	public String getUsePermissionMessage() {
		return null;
	}

	
	public boolean signCreate(SignChangeEvent event) {
		event.setLine(1, ChatColor.GREEN + "완료");
		if(!event.getLine(2).isEmpty() && plugin.mdata.hasMinigame(event.getLine(2))){
			event.setLine(2, plugin.mdata.getMinigame(event.getLine(2)).getName());
		}
		else if(!event.getLine(2).isEmpty()){
			event.getPlayer().sendMessage(ChatColor.RED + "그런 미니게임은 존재하지 않습니다!");
			return false;
		}
		return true;
	}

	
	public boolean signUse(Sign sign, MinigamePlayer player) {
		if(player.isInMinigame() && player.getPlayer().getItemInHand().getType() == Material.AIR){
			Minigame minigame = player.getMinigame();

			if(minigame.isSpectator(player)){
				return false;
			}
			
			if(!minigame.getFlags().isEmpty()){
				if(((LivingEntity)player.getPlayer()).isOnGround()){
					
					if(plugin.pdata.checkRequiredFlags(player, minigame.getName()).isEmpty()){
						if(sign.getLine(2).isEmpty() || sign.getLine(2).equals(player.getMinigame().getName())){
							plugin.pdata.endMinigame(player);
							plugin.pdata.partyMode(player);
						}
					}
					else{
						List<String> requiredFlags = plugin.pdata.checkRequiredFlags(player, minigame.getName());
						String flags = "";
						int num = requiredFlags.size();
						
						for(int i = 0; i < num; i++){
							flags += requiredFlags.get(i);
							if(i != num - 1){
								flags += ", ";
							}
						}
						player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "이 미니게임을 클리어할때 필요한 깃발:");
						player.sendMessage(ChatColor.GRAY + flags);
					}
				}
				return true;
			}
			else{
				if(((LivingEntity)player.getPlayer()).isOnGround()){
					plugin.pdata.endMinigame(player);
					plugin.pdata.partyMode(player);
					return true;
				}
			}
		}
		else if(player.getPlayer().getItemInHand().getType() != Material.AIR){
			player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "이 표지판을 사용하려면 손이 비어있어야 합니다!");
		}
		return false;
	}

}
