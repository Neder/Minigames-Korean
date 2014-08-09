package com.pauldavdesign.mineauz.minigames.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pauldavdesign.mineauz.minigames.Minigame;

public class HelpCommand implements ICommand{

	
	public String getName() {
		return "help";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "도움말 커맨드입니다.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame help"};
	}

	
	public String getPermissionMessage() {
		return "당신은 도움말 커맨드를 사용할 권한이 없습니다!";
	}

	
	public String getPermission() {
		return "minigame.help";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		Player player = null;
		if(sender instanceof Player){
			player = (Player)sender;
		}
		sender.sendMessage(ChatColor.GREEN + "미니게임 커맨드 리스트");
		sender.sendMessage(ChatColor.BLUE + "/minigame");
		sender.sendMessage(ChatColor.GRAY + "기본 커맨드 (줄임 명령어: /mg)");
		if(player == null || player.hasPermission("minigame.join")){
			sender.sendMessage(ChatColor.BLUE + "/minigame join <미니게임>");
			sender.sendMessage(ChatColor.GRAY + "미니게임에 들어갑니다.");
		}
		if(player == null || player.hasPermission("minigame.quit")){
			sender.sendMessage(ChatColor.BLUE + "/minigame quit");
			sender.sendMessage("미니게임에서 나옵니다.");
			if(player == null || player.hasPermission("minigame.quit.other")){
				sender.sendMessage("다른 플레이어도 나가게 할 수 없습니다.");
			}
		}
		if(player == null || player.hasPermission("minigame.end")){
			sender.sendMessage(ChatColor.BLUE + "/minigame end [플레이어]");
			sender.sendMessage("나 혹은 다른 사람의 미니게임을 끝냅니다.(디버그용)");
		}
		if(player == null || player.hasPermission("minigame.revert")){
			sender.sendMessage(ChatColor.BLUE + "/minigame revert");
			sender.sendMessage("체크 포인트로 돌아갑니다. (줄임 명령어: /mg r)");
		}
		if(player == null || player.hasPermission("minigame.delete")){
			sender.sendMessage(ChatColor.BLUE + "/minigame delete <미니게임>");
			sender.sendMessage("미니게임을 지웁니다.");
		}
		if(player == null || player.hasPermission("minigame.restoreinv")){
			sender.sendMessage(ChatColor.BLUE + "/minigame restoreinv <플레이어>");
			sender.sendMessage("플레이어의 인벤토리를 원래대로 되돌립니다.");
		}
		if(player == null || player.hasPermission("minigame.hint")){
			sender.sendMessage(ChatColor.BLUE + "/minigame hint <미니게임>");
			sender.sendMessage("보물찾기 미니게임에서 힌트를 받습니다.");
		}
		if(player == null || player.hasPermission("minigame.toggletimer")){
			sender.sendMessage(ChatColor.BLUE + "/minigame toggletimer <미니게임>");
			sender.sendMessage("쿨타임 타이머를 중지/재시작 합니다.");
		}
		if(player == null || player.hasPermission("minigame.list")){
			sender.sendMessage(ChatColor.BLUE + "/minigame list");
			sender.sendMessage("모든 미니게임의 목록을 봅니다.");
		}
		if(player == null || player.hasPermission("minigame.reload")){
			sender.sendMessage(ChatColor.BLUE + "/minigame reload");
			sender.sendMessage("미니게임 파일을 리로드 합니다.");
		}
		if(player == null || player.hasPermission("minigame.teleport")){
			sender.sendMessage(ChatColor.BLUE + "/minigame teleport <x> <y> <z>");
			sender.sendMessage("미니게임 내에서 텔레포트 합니다.");
		}
		
		sender.sendMessage(ChatColor.BLUE + "/minigame set <미니게임> <파라메터>...");
		sender.sendMessage("미니게임을 설정합니다. /minigame set 으로 파라메터들을 볼 수 있습니다.");
		return true;
	}

}
