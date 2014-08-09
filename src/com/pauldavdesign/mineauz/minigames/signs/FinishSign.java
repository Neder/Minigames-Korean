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
		return "�Ϸ�";
	}

	
	public String getCreatePermission() {
		return "minigame.sign.create.finish";
	}

	
	public String getCreatePermissionMessage() {
		return "�̴ϰ��� �Ϸ� ǥ������ ���� ������ �����ϴ�!";
	}

	
	public String getUsePermission() {
		return null;
	}

	
	public String getUsePermissionMessage() {
		return null;
	}

	
	public boolean signCreate(SignChangeEvent event) {
		event.setLine(1, ChatColor.GREEN + "�Ϸ�");
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
						plugin.pdata.endMinigame(player);
						plugin.pdata.partyMode(player);
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
						player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "�� �̴ϰ����� Ŭ�����Ҷ� �ʿ��� ���:");
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
			player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "�� ǥ������ ����Ϸ��� ���� ����־�� �մϴ�!");
		}
		return false;
	}

}
