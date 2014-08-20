package com.pauldavdesign.mineauz.minigames;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class UpdateChecker extends Thread{
	
	private Player ply;
	
	public UpdateChecker(Player player){
		ply = player;
	}
	
	@Override
	public void run(){
		List<String> update = MinigameUtils.checkForUpdate("http://mineauz.pauldavdesign.com/mgmversion.txt", Minigames.plugin.getDescription().getVersion());
		if(update != null){
			ply.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "업데이트 확인됨! 새 버전: " + update.get(0));
			if(update.size() > 1){
				ply.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "바뀐 부분:");
				for(int i = 1; i < update.size(); i++){
					ply.sendMessage("- " + update.get(i));
				}
			}
		}
	}
}
