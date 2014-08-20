package com.pauldavdesign.mineauz.minigames.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pauldavdesign.mineauz.minigames.Minigame;

public class CreateCommand implements ICommand{
	
	public String getName() {
		return "create";
	}
	
	public String[] getAliases(){
		return null;
	}

	public boolean canBeConsole() {
		return false;
	}

	public String getDescription() {
		return "미니게임을 만드는데 사용합니다.";
	}

	public String[] getUsage() {
		return new String[] {"/minigame create <미니게임> [타입]"};
	}
	
	public String[] getParameters() {
		return null;
	}
	
	public String getPermissionMessage(){
		return "미니게임을 만들 권한이 없습니다!";
	}
	
	public String getPermission(){
		return "minigame.create";
	}

	public boolean onCommand(CommandSender sender, Minigame minigame, String label, String[] args) {
		if(args != null){
			Player player = (Player)sender;
			if(!plugin.mdata.hasMinigame(args[0])){
				String mgmName = args[0];
				String type = "sp";
				if(args.length >= 2){
					if(plugin.mdata.getMinigameTypes().contains(args[1].toLowerCase())){
						type = args[1];
					}
					else{
						player.sendMessage(ChatColor.RED + "\"" + args[1] + "\" 라는 미니게임 타입이 존재하지 않습니다!");
					}
				}
				Minigame mgm = new Minigame(mgmName, type, player.getLocation());
				
				player.sendMessage(ChatColor.GRAY + "미니게임 " + args[0] + " 를 만들었습니다.");
				
				List<String> mgs = null;
				if(plugin.getConfig().contains("minigames")){
					mgs = plugin.getConfig().getStringList("minigames");
				}
				else{
					mgs = new ArrayList<String>();
				}
				mgs.add(mgmName);
				plugin.getConfig().set("minigames", mgs);
				plugin.saveConfig();
				
				mgm.saveMinigame();
				plugin.mdata.addMinigame(mgm);
			}else{
				sender.sendMessage(ChatColor.RED + "그 이름을 가진 미니게임이 이미 존재합니다!");
			}
			return true;
		}
		return false;
	}
}
