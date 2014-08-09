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
		return "팀";
	}

	
	public String getCreatePermission() {
		return "minigame.sign.create.team";
	}

	
	public String getCreatePermissionMessage() {
		return "미니게임 팀 표지판을 만들 권한이 없습니다!";
	}

	
	public String getUsePermission() {
		return "minigame.sign.use.team";
	}

	
	public String getUsePermissionMessage() {
		return "미니게임 팀 표지판을 사용할 권한이 없습니다!";
	}

	
	public boolean signCreate(SignChangeEvent event) {
		event.setLine(1, ChatColor.GREEN + "팀");
		if(event.getLine(2).equalsIgnoreCase("레드") || event.getLine(2).equalsIgnoreCase("ㄹ") ||
				event.getLine(2).equalsIgnoreCase("블루") || event.getLine(2).equalsIgnoreCase("ㅂ") ||
				event.getLine(2).equalsIgnoreCase("기본")){
			if(event.getLine(2).equalsIgnoreCase("레드") || event.getLine(2).equalsIgnoreCase("ㄹ")){
				event.setLine(2, ChatColor.RED + "레드");
			}
			else if(event.getLine(2).equalsIgnoreCase("블루") || event.getLine(2).equalsIgnoreCase("ㅂ")){
				event.setLine(2, ChatColor.BLUE + "블루");
			}
			else if(event.getLine(2).equalsIgnoreCase("자연")){
				event.setLine(2, ChatColor.GRAY + "자연");
			}
			return true;
		}
		event.getPlayer().sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "3번째 라인은 \"레드\", \"블루\" 또는 \"자연\"이여야 합니다!");
		return false;
	}

	
	public boolean signUse(Sign sign, MinigamePlayer player) {
		if(player.isInMinigame()){
			Minigame mgm = player.getMinigame();
			if(mgm.getType().equals("teamdm")){
				if(mgm.hasStarted() && !sign.getLine(2).equals(ChatColor.GRAY + "자연") &&
						((mgm.getRedTeam().contains(player.getPlayer()) && sign.getLine(2).equals(ChatColor.BLUE + "블루") || 
								(mgm.getBlueTeam().contains(player.getPlayer()) && sign.getLine(2).equals(ChatColor.RED + "레드"))))){
					player.getPlayer().damage(player.getHealth());
				}
				if(mgm.getBlueTeam().contains(player.getPlayer())){
					if(sign.getLine(2).equals(ChatColor.RED + "레드")){
						if(mgm.getRedTeam().size() <= mgm.getBlueTeam().size()){
							TeamDMMinigame.switchTeam(mgm, player);
							plugin.mdata.sendMinigameMessage(mgm, player.getName() + " 님이 " + ChatColor.RED + "레드 팀" + ChatColor.WHITE + " 에 들어오셨습니다.", null, player);
							player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "당신은 " + ChatColor.RED + "레드 팀" + ChatColor.WHITE + " 에 들어갔습니다.");
						}
						else{
							player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "팀 벨런스를 망칠 수 없습니다!");
						}
					}
					else if(sign.getLine(2).equals(ChatColor.GRAY + "자연") && !mgm.hasStarted()){
						mgm.removeRedTeamPlayer(player);
						mgm.removeBlueTeamPlayer(player);
						plugin.mdata.sendMinigameMessage(mgm, player.getName() + " 님은 자동으로 팀이 설정됩니다.", null, player);
						player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "자동으로 팀이 설정됩니다.");
					}
					return true;
				}
				else if(mgm.getRedTeam().contains(player.getPlayer())){
					if(sign.getLine(2).equals(ChatColor.BLUE + "블루")){
						if(mgm.getRedTeam().size() >= mgm.getBlueTeam().size()){
							TeamDMMinigame.switchTeam(mgm, player);
							plugin.mdata.sendMinigameMessage(mgm, player.getName() + " 님이 " + ChatColor.BLUE + "블루 팀" + ChatColor.WHITE + " 에 들어오셨습니다.", null, player);
							player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "당신은 " + ChatColor.BLUE + "블루 팀" + ChatColor.WHITE + " 에 들어갔습니다.");
						}
						else{
							player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "팀 벨런스를 망칠 수 없습니다!");
						}
					}
					else if(sign.getLine(2).equals(ChatColor.GRAY + "자연") && !mgm.hasStarted()){
						mgm.removeRedTeamPlayer(player);
						mgm.removeBlueTeamPlayer(player);
						plugin.mdata.sendMinigameMessage(mgm, player.getName() + " 님은 자동으로 팀이 설정됩니다.", null, player);
						player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "자동으로 팀이 설정됩니다.");
					}
					return true;
				}
				else{
					if(!mgm.hasStarted()){
						if(sign.getLine(2).equals(ChatColor.RED + "레드")){
							if(mgm.getRedTeam().size() <= mgm.getBlueTeam().size()){
								mgm.addRedTeamPlayer(player);
								mgm.removeBlueTeamPlayer(player);
								plugin.mdata.sendMinigameMessage(mgm, player.getName() + " 님이 " + ChatColor.RED + "레드 팀" + ChatColor.WHITE + " 에 들어오셨습니다.", null, player);
								player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "당신은 " + ChatColor.RED + "레드 팀" + ChatColor.WHITE + " 에 들어갔습니다.");
							}
							else{
								player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "팀 벨런스를 망칠 수 없습니다!");
							}
						}
						else if(sign.getLine(2).equals(ChatColor.BLUE + "블루")){
							if(mgm.getRedTeam().size() >= mgm.getBlueTeam().size()){
								mgm.addBlueTeamPlayer(player);
								mgm.removeRedTeamPlayer(player);
								plugin.mdata.sendMinigameMessage(mgm, player.getName() + " 님이 " + ChatColor.BLUE + "블루 팀" + ChatColor.WHITE + " 에 들어오셨습니다.", null, player);
								player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "당신은 " + ChatColor.BLUE + "블루 팀" + ChatColor.WHITE + " 에 들어갔습니다.");
							}
							else{
								player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "팀 벨런스를 망칠 수 없습니다!");
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
