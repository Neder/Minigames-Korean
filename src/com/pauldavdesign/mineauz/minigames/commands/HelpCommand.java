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
		return "���� Ŀ�ǵ��Դϴ�.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame help"};
	}

	
	public String getPermissionMessage() {
		return "����� ���� Ŀ�ǵ带 ����� ������ �����ϴ�!";
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
		sender.sendMessage(ChatColor.GREEN + "�̴ϰ��� Ŀ�ǵ� ����Ʈ");
		sender.sendMessage(ChatColor.BLUE + "/minigame");
		sender.sendMessage(ChatColor.GRAY + "�⺻ Ŀ�ǵ� (���� ��ɾ�: /mg)");
		if(player == null || player.hasPermission("minigame.join")){
			sender.sendMessage(ChatColor.BLUE + "/minigame join <�̴ϰ���>");
			sender.sendMessage(ChatColor.GRAY + "�̴ϰ��ӿ� ���ϴ�.");
		}
		if(player == null || player.hasPermission("minigame.quit")){
			sender.sendMessage(ChatColor.BLUE + "/minigame quit");
			sender.sendMessage("�̴ϰ��ӿ��� ���ɴϴ�.");
			if(player == null || player.hasPermission("minigame.quit.other")){
				sender.sendMessage("�ٸ� �÷��̾ ������ �� �� �����ϴ�.");
			}
		}
		if(player == null || player.hasPermission("minigame.end")){
			sender.sendMessage(ChatColor.BLUE + "/minigame end [�÷��̾�]");
			sender.sendMessage("�� Ȥ�� �ٸ� ����� �̴ϰ����� �����ϴ�.(����׿�)");
		}
		if(player == null || player.hasPermission("minigame.revert")){
			sender.sendMessage(ChatColor.BLUE + "/minigame revert");
			sender.sendMessage("üũ ����Ʈ�� ���ư��ϴ�. (���� ��ɾ�: /mg r)");
		}
		if(player == null || player.hasPermission("minigame.delete")){
			sender.sendMessage(ChatColor.BLUE + "/minigame delete <�̴ϰ���>");
			sender.sendMessage("�̴ϰ����� ����ϴ�.");
		}
		if(player == null || player.hasPermission("minigame.restoreinv")){
			sender.sendMessage(ChatColor.BLUE + "/minigame restoreinv <�÷��̾�>");
			sender.sendMessage("�÷��̾��� �κ��丮�� ������� �ǵ����ϴ�.");
		}
		if(player == null || player.hasPermission("minigame.hint")){
			sender.sendMessage(ChatColor.BLUE + "/minigame hint <�̴ϰ���>");
			sender.sendMessage("����ã�� �̴ϰ��ӿ��� ��Ʈ�� �޽��ϴ�.");
		}
		if(player == null || player.hasPermission("minigame.toggletimer")){
			sender.sendMessage(ChatColor.BLUE + "/minigame toggletimer <�̴ϰ���>");
			sender.sendMessage("��Ÿ�� Ÿ�̸Ӹ� ����/����� �մϴ�.");
		}
		if(player == null || player.hasPermission("minigame.list")){
			sender.sendMessage(ChatColor.BLUE + "/minigame list");
			sender.sendMessage("��� �̴ϰ����� ����� ���ϴ�.");
		}
		if(player == null || player.hasPermission("minigame.reload")){
			sender.sendMessage(ChatColor.BLUE + "/minigame reload");
			sender.sendMessage("�̴ϰ��� ������ ���ε� �մϴ�.");
		}
		if(player == null || player.hasPermission("minigame.teleport")){
			sender.sendMessage(ChatColor.BLUE + "/minigame teleport <x> <y> <z>");
			sender.sendMessage("�̴ϰ��� ������ �ڷ���Ʈ �մϴ�.");
		}
		
		sender.sendMessage(ChatColor.BLUE + "/minigame set <�̴ϰ���> <�Ķ����>...");
		sender.sendMessage("�̴ϰ����� �����մϴ�. /minigame set ���� �Ķ���͵��� �� �� �ֽ��ϴ�.");
		return true;
	}

}
