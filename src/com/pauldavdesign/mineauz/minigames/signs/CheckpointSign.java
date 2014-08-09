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
		return "üũ����";
	}

	
	public String getCreatePermission() {
		return "minigame.sign.create.checkpoint";
	}

	
	public String getCreatePermissionMessage() {
		return "����� �̴ϰ��� üũ����Ʈ ǥ������ ���� ������ �����ϴ�!";
	}

	
	public String getUsePermission() {
		return "minigame.sign.use.checkpoint";
	}

	
	public String getUsePermissionMessage() {
		return "����� �̴ϰ��� üũ����Ʈ ǥ������ ����� ������ �����ϴ�!";
	}

	
	public boolean signCreate(SignChangeEvent event) {
		event.setLine(1, ChatColor.GREEN + "üũ����Ʈ");
		if(event.getLine(2).equalsIgnoreCase("�۷ι�")){
			event.setLine(2, ChatColor.BLUE + "�۷ι�");
		}
		return true;
	}

	
	public boolean signUse(Sign sign, MinigamePlayer player) {
		if((player.isInMinigame() || (!player.isInMinigame() && sign.getLine(2).equals(ChatColor.BLUE + "�۷ι�"))) 
				&& player.getPlayer().getItemInHand().getType() == Material.AIR){
			if(player.isInMinigame() && player.getMinigame().isSpectator(player)){
				return false;
			}
			if(((LivingEntity)player.getPlayer()).isOnGround()){
				Location newloc = player.getPlayer().getLocation();
				if(!sign.getLine(2).equals(ChatColor.BLUE + "�۷ι�")){
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
				
				player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "üũ����Ʈ ������!");
				return true;
			}
			else{
				player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "����� ���⿡ üũ����Ʈ�� ������ �� �����ϴ�!");
			}
		}
		else
			player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "�� ǥ������ ����Ϸ��� ���� ����־�� �մϴ�!");
		return false;
	}

}
