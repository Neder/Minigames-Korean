package com.pauldavdesign.mineauz.minigames.signs;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

import com.pauldavdesign.mineauz.minigames.MinigamePlayer;
import com.pauldavdesign.mineauz.minigames.Minigames;

public class QuitSign implements MinigameSign {
	
	private static Minigames plugin = Minigames.plugin;

	
	public String getName() {
		return "������";
	}

	
	public String getCreatePermission() {
		return "minigame.sign.create.quit";
	}

	
	public String getCreatePermissionMessage() {
		return "�̴ϰ��� ������ ǥ������ ����� ������ �����ϴ�!";
	}

	
	public String getUsePermission() {
		return null;
	}

	
	public String getUsePermissionMessage() {
		return null;
	}

	
	public boolean signCreate(SignChangeEvent event) {
		event.setLine(1, ChatColor.GREEN + "������");
		return true;
	}

	
	public boolean signUse(Sign sign, MinigamePlayer player) {
		if(player.isInMinigame() && player.getPlayer().getItemInHand().getType() == Material.AIR){
			plugin.pdata.quitMinigame(player, false);
			return true;
		}
		else if(player.getPlayer().getItemInHand().getType() != Material.AIR)
			player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "�� ǥ������ ����Ϸ��� ���� ����־�� �մϴ�!");
		return false;
	}

}
