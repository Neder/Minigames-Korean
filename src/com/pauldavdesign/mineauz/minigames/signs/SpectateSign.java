package com.pauldavdesign.mineauz.minigames.signs;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.MinigamePlayer;
import com.pauldavdesign.mineauz.minigames.Minigames;

public class SpectateSign implements MinigameSign {
	
	private Minigames plugin = Minigames.plugin;

	
	public String getName() {
		return "구경";
	}

	
	public String getCreatePermission() {
		return "minigame.sign.create.spectate";
	}

	
	public String getCreatePermissionMessage() {
		return "미니게임 구경 표지판을 만들 권한이 없습니다!";
	}

	
	public String getUsePermission() {
		return "minigame.sign.use.spectate";
	}

	
	public String getUsePermissionMessage() {
		return "미니게임 구경 표지판을 사용할 권한이 없습니다!";
	}

	
	public boolean signCreate(SignChangeEvent event) {
		if(plugin.mdata.hasMinigame(event.getLine(2))){
			event.setLine(1, ChatColor.GREEN + "구경");
			event.setLine(2, plugin.mdata.getMinigame(event.getLine(2)).getName());
			return true;
		}
		event.getPlayer().sendMessage(ChatColor.RED + "\"" + event.getLine(2) + "\" 라는 이름을 가진 미니게임은 없습니다!");
		return false;
	}

	
	public boolean signUse(Sign sign, MinigamePlayer player) {
		if(player.getPlayer().getItemInHand().getType() == Material.AIR && !player.isInMinigame()){
			Minigame mgm = plugin.mdata.getMinigame(sign.getLine(2));
			if(mgm != null){
				if(mgm.isEnabled()){
					plugin.pdata.spectateMinigame(player, mgm);
					return true;
				}
				else if(!mgm.isEnabled()){
					player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "이 미니게임은 활성화 되지 않았습니다.");
				}
			}
			else if(mgm == null){
				player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "이 미니게임은 존재하지 않습니다!");
			}
		}
		else if(!player.isInMinigame())
			player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "이 표지판을 사용하려면 손이 비어있어야 합니다!");
		return false;
	}

}
