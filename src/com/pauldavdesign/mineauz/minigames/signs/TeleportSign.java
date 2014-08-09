package com.pauldavdesign.mineauz.minigames.signs;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

import com.pauldavdesign.mineauz.minigames.MinigamePlayer;
import com.pauldavdesign.mineauz.minigames.Minigames;

public class TeleportSign implements MinigameSign {

	
	public String getName() {
		return "텔레포트";
	}

	
	public String getCreatePermission() {
		return "minigame.sign.create.teleport";
	}

	
	public String getCreatePermissionMessage() {
		return "텔레포트 표지판을 만들 권한이 없습니다!";
	}

	
	public String getUsePermission() {
		return "minigame.sign.use.teleport";
	}

	
	public String getUsePermissionMessage() {
		return "텔레포트 표지판을 사용할 권한이 없습니다!";
	}

	
	public boolean signCreate(SignChangeEvent event) {
		event.setLine(1, ChatColor.GREEN + "텔레포트");
		if(event.getLine(2).isEmpty()){
			return false;
		}
		else{
			if(!event.getLine(2).matches("-?[0-9]+,[0-9]+,-?[0-9]+")){
				return false;
			}
		}
		return true;
	}

	
	public boolean signUse(Sign sign, MinigamePlayer player) {
		if(!sign.getLine(2).isEmpty() && sign.getLine(2).matches("-?[0-9]+,[0-9]+,-?[0-9]+")){
			int x;
			int y;
			int z;
			String[] split = sign.getLine(2).split(",");
			x = Integer.parseInt(split[0]);
			y = Integer.parseInt(split[1]);
			z = Integer.parseInt(split[2]);
			
			if(!sign.getLine(3).isEmpty() && sign.getLine(3).matches("-?[0-9]+,-?[0-9]+")){
				float yaw;
				float pitch;
				String[] split2 = sign.getLine(3).split(",");
				yaw = Float.parseFloat(split2[0]);
				pitch = Float.parseFloat(split2[1]);
				Minigames.plugin.pdata.minigameTeleport(player, new Location(player.getPlayer().getWorld(), x + 0.5, y, z + 0.5, yaw, pitch));
				return true;
			}
			Minigames.plugin.pdata.minigameTeleport(player, new Location(player.getPlayer().getWorld(), x + 0.5, y, z + 0.5));
			return true;
		}
		player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "알맞지 않은 텔레포트 표지판입니다!");
		return false;
	}

}
