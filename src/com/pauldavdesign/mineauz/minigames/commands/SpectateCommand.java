package com.pauldavdesign.mineauz.minigames.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.MinigamePlayer;

public class SpectateCommand implements ICommand {

	
	public String getName() {
		return "spectate";
	}

	
	public String[] getAliases() {
		return new String[] {"spec"};
	}

	
	public boolean canBeConsole() {
		return false;
	}

	
	public String getDescription() {
		return "미니게임에서 강제로 구경하게 합니다.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame spectate <미니게임>"};
	}

	
	public String getPermissionMessage() {
		return "당신은 구경 커맨드를 사용할 권한이 없습니다!";
	}

	
	public String getPermission() {
		return "minigame.spectate";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			if(plugin.mdata.hasMinigame(args[0])){
				MinigamePlayer ply = plugin.pdata.getMinigamePlayer((Player) sender);
				Minigame mgm = plugin.mdata.getMinigame(args[0]);
				plugin.pdata.spectateMinigame(ply, mgm);
			}
			else{
				sender.sendMessage(ChatColor.RED + "" + args[0] + "라는 이름을 가진 미니게임은 없습니다!");
			}
			return true;
		}
		return false;
	}

}
