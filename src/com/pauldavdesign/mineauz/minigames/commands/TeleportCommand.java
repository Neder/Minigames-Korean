package com.pauldavdesign.mineauz.minigames.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.MinigamePlayer;
import com.pauldavdesign.mineauz.minigames.Minigames;

public class TeleportCommand implements ICommand{
	
	public String getName() {
		return "teleport";
	}
	
	public String[] getAliases(){
		return null;
	}

	public boolean canBeConsole() {
		return false;
	}

	public String getDescription() {
		return "�̴ϰ��� ������ �ڷ���Ʈ�� ������ ��ɾ��Դϴ�. ";
	}

	public String[] getUsage() {
		return new String[] {"/minigame teleport <x> <y> <z>"};
	}
	
	public String[] getParameters() {
		return null;
	}
	
	public String getPermissionMessage(){
		return "����� �̴ϰ��� ������ �ڷ���Ʈ�� ������ �����ϴ�!";
	}
	
	public String getPermission(){
		return "minigame.teleport";
	}

	public boolean onCommand(CommandSender sender, Minigame minigame, String label, String[] args) {
		if(args != null){
			Player player = (Player)sender;
			if(args.length >= 2 && args[0].matches("-?[0-9]+,-?[0-9]+") && args[1].matches("-?[0-9]+,-?[0-9]+") && args[2].matches("-?[0-9]+,-?[0-9]+")) {
				int x;
				int y;
				int z;
				x = Integer.parseInt(args[0]);
				y = Integer.parseInt(args[1]);
				z = Integer.parseInt(args[2]);
				Minigames.plugin.pdata.minigameTeleport((MinigamePlayer) player, new Location(player.getPlayer().getWorld(), x + 0.5, y, z + 0.5));
			} else {
				player.sendMessage(ChatColor.RED + "[Minigames] " + ChatColor.WHITE + "���� ���ڿ��� �մϴ�!");
			}
		}
		return false;
	}
}
