package com.pauldavdesign.mineauz.minigames.signs;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.SignChangeEvent;

import com.pauldavdesign.mineauz.minigames.MinigamePlayer;
import com.pauldavdesign.mineauz.minigames.Minigames;
import com.pauldavdesign.mineauz.minigames.StoredPlayerCheckpoints;

public class CheckpointSign implements MinigameSign {
	
	private static Minigames plugin = Minigames.plugin;

	
	public String getName() {
		return "체크포인";
	}

	
	public String getCreatePermission() {
		return "minigame.sign.create.checkpoint";
	}

	
	public String getCreatePermissionMessage() {
		return "당신은 미니게임 체크포인트 표지판을 만들 권한이 없습니다!";
	}

	
	public String getUsePermission() {
		return "minigame.sign.use.checkpoint";
	}

	
	public String getUsePermissionMessage() {
		return "당신은 미니게임 체크포인트 표지판을 사용할 권한이 없습니다!";
	}

	
	public boolean signCreate(SignChangeEvent event) {
		event.setLine(1, ChatColor.GREEN + "체크포인트");
		if(event.getLine(2).equalsIgnoreCase("글로벌")){
			event.setLine(2, ChatColor.BLUE + "글로벌");
		}
		return true;
	}

	
	public boolean signUse(Sign sign, MinigamePlayer player) {
		if((player.isInMinigame() || (!player.isInMinigame() && sign.getLine(2).equals(ChatColor.BLUE + "글로벌"))) 
				&& player.getPlayer().getItemInHand().getType() == Material.AIR){
			if(player.isInMinigame() && player.getMinigame().isSpectator(player)){
				return false;
			}
			if(((LivingEntity)player.getPlayer()).isOnGround()){
				Location newloc = player.getPlayer().getLocation();
				if(!sign.getLine(2).equals(ChatColor.BLUE + "글로벌")){
					player.setCheckpoint(newloc);
				}
				else{
					if(!plugin.pdata.hasStoredPlayerCheckpoint(player)){
						StoredPlayerCheckpoints spc = new StoredPlayerCheckpoints(player.getName(), newloc);
						plugin.pdata.addStoredPlayerCheckpoints(player.getName(), spc);
					}
					else{
						StoredPlayerCheckpoints spc = plugin.pdata.getPlayersStoredCheckpoints(player);
						spc.setGlobalCheckpoint(newloc);
					}
				}
				
				player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "체크포인트 설정됨!");
				return true;
			}
			else{
				player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "당신은 여기에 체크포인트를 설정할 수 없습니다!");
			}
		}
		else
			player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "이 표지판을 사용하려면 손이 비어있어야 합니다!");
		return false;
	}

}
