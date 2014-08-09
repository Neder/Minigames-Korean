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
		return "�̴ϰ����� ����µ� ����մϴ�.";
	}

	public String[] getUsage() {
		return new String[] {"/minigame create <�̴ϰ���> [Ÿ��]"};
	}
	
	public String[] getParameters() {
		return null;
	}
	
	public String getPermissionMessage(){
		return "����� �̴ϰ����� ���� ������ �����ϴ�!";
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
						player.sendMessage(ChatColor.RED + "There is no Minigame type by the name \"" + args[1] + "\"!");
					}
				}
				Minigame mgm = new Minigame(mgmName, type, player.getLocation());
				
				player.sendMessage(ChatColor.GRAY + "The Minigame " + args[0] + " has been created.");
				
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
				sender.sendMessage(ChatColor.RED + "This Minigame already exists!");
			}
			return true;
		}
		return false;
	}
}
