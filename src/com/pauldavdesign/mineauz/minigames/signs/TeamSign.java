package com.pauldavdesign.mineauz.minigames.signs;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.MinigamePlayer;
import com.pauldavdesign.mineauz.minigames.Minigames;
import com.pauldavdesign.mineauz.minigames.gametypes.TeamDMMinigame;

public class TeamSign implements MinigameSign {
	
	private Minigames plugin = Minigames.plugin;
	
	
	public String getName() {
		return "��";
	}

	
	public String getCreatePermission() {
		return "minigame.sign.create.team";
	}

	
	public String getCreatePermissionMessage() {
		return "�̴ϰ��� �� ǥ������ ���� ������ �����ϴ�!";
	}

	
	public String getUsePermission() {
		return "minigame.sign.use.team";
	}

	
	public String getUsePermissionMessage() {
		return "�̴ϰ��� �� ǥ������ ����� ������ �����ϴ�!";
	}

	
	public boolean signCreate(SignChangeEvent event) {
		event.setLine(1, ChatColor.GREEN + "��");
		if(event.getLine(2).equalsIgnoreCase("����") || event.getLine(2).equalsIgnoreCase("��") ||
				event.getLine(2).equalsIgnoreCase("���") || event.getLine(2).equalsIgnoreCase("��") ||
				event.getLine(2).equalsIgnoreCase("�⺻")){
			if(event.getLine(2).equalsIgnoreCase("����") || event.getLine(2).equalsIgnoreCase("��")){
				event.setLine(2, ChatColor.RED + "����");
			}
			else if(event.getLine(2).equalsIgnoreCase("���") || event.getLine(2).equalsIgnoreCase("��")){
				event.setLine(2, ChatColor.BLUE + "���");
			}
			else if(event.getLine(2).equalsIgnoreCase("�ڿ�")){
				event.setLine(2, ChatColor.GRAY + "�ڿ�");
			}
			return true;
		}
		event.getPlayer().sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "3��° ������ \"����\", \"���\" �Ǵ� \"�ڿ�\"�̿��� �մϴ�!");
		return false;
	}

	
	public boolean signUse(Sign sign, MinigamePlayer player) {
		if(player.isInMinigame()){
			Minigame mgm = player.getMinigame();
			if(mgm.getType().equals("teamdm")){
				if(mgm.hasStarted() && !sign.getLine(2).equals(ChatColor.GRAY + "�ڿ�") &&
						((mgm.getRedTeam().contains(player.getPlayer()) && sign.getLine(2).equals(ChatColor.BLUE + "���") || 
								(mgm.getBlueTeam().contains(player.getPlayer()) && sign.getLine(2).equals(ChatColor.RED + "����"))))){
					player.getPlayer().damage(player.getHealth());
				}
				if(mgm.getBlueTeam().contains(player.getPlayer())){
					if(sign.getLine(2).equals(ChatColor.RED + "����")){
						if(mgm.getRedTeam().size() <= mgm.getBlueTeam().size()){
							TeamDMMinigame.switchTeam(mgm, player);
							plugin.mdata.sendMinigameMessage(mgm, player.getName() + " ���� " + ChatColor.RED + "���� ��" + ChatColor.WHITE + " �� �����̽��ϴ�.", null, player);
							player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "����� " + ChatColor.RED + "���� ��" + ChatColor.WHITE + " �� �����ϴ�.");
						}
						else{
							player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "�� �������� ��ĥ �� �����ϴ�!");
						}
					}
					else if(sign.getLine(2).equals(ChatColor.GRAY + "�ڿ�") && !mgm.hasStarted()){
						mgm.removeRedTeamPlayer(player);
						mgm.removeBlueTeamPlayer(player);
						plugin.mdata.sendMinigameMessage(mgm, player.getName() + " ���� �ڵ����� ���� �����˴ϴ�.", null, player);
						player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "�ڵ����� ���� �����˴ϴ�.");
					}
					return true;
				}
				else if(mgm.getRedTeam().contains(player.getPlayer())){
					if(sign.getLine(2).equals(ChatColor.BLUE + "���")){
						if(mgm.getRedTeam().size() >= mgm.getBlueTeam().size()){
							TeamDMMinigame.switchTeam(mgm, player);
							plugin.mdata.sendMinigameMessage(mgm, player.getName() + " ���� " + ChatColor.BLUE + "��� ��" + ChatColor.WHITE + " �� �����̽��ϴ�.", null, player);
							player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "����� " + ChatColor.BLUE + "��� ��" + ChatColor.WHITE + " �� �����ϴ�.");
						}
						else{
							player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "�� �������� ��ĥ �� �����ϴ�!");
						}
					}
					else if(sign.getLine(2).equals(ChatColor.GRAY + "�ڿ�") && !mgm.hasStarted()){
						mgm.removeRedTeamPlayer(player);
						mgm.removeBlueTeamPlayer(player);
						plugin.mdata.sendMinigameMessage(mgm, player.getName() + " ���� �ڵ����� ���� �����˴ϴ�.", null, player);
						player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "�ڵ����� ���� �����˴ϴ�.");
					}
					return true;
				}
				else{
					if(!mgm.hasStarted()){
						if(sign.getLine(2).equals(ChatColor.RED + "����")){
							if(mgm.getRedTeam().size() <= mgm.getBlueTeam().size()){
								mgm.addRedTeamPlayer(player);
								mgm.removeBlueTeamPlayer(player);
								plugin.mdata.sendMinigameMessage(mgm, player.getName() + " ���� " + ChatColor.RED + "���� ��" + ChatColor.WHITE + " �� �����̽��ϴ�.", null, player);
								player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "����� " + ChatColor.RED + "���� ��" + ChatColor.WHITE + " �� �����ϴ�.");
							}
							else{
								player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "�� �������� ��ĥ �� �����ϴ�!");
							}
						}
						else if(sign.getLine(2).equals(ChatColor.BLUE + "���")){
							if(mgm.getRedTeam().size() >= mgm.getBlueTeam().size()){
								mgm.addBlueTeamPlayer(player);
								mgm.removeRedTeamPlayer(player);
								plugin.mdata.sendMinigameMessage(mgm, player.getName() + " ���� " + ChatColor.BLUE + "��� ��" + ChatColor.WHITE + " �� �����̽��ϴ�.", null, player);
								player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "����� " + ChatColor.BLUE + "��� ��" + ChatColor.WHITE + " �� �����ϴ�.");
							}
							else{
								player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "�� �������� ��ĥ �� �����ϴ�!");
							}
						}
						return true;
					}
				}
			}
		}
		return false;
	}

}
