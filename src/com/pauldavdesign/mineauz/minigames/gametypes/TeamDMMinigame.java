package com.pauldavdesign.mineauz.minigames.gametypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.MinigameData;
import com.pauldavdesign.mineauz.minigames.MinigamePlayer;
import com.pauldavdesign.mineauz.minigames.MinigameSave;
import com.pauldavdesign.mineauz.minigames.Minigames;
import com.pauldavdesign.mineauz.minigames.MultiplayerTimer;
import com.pauldavdesign.mineauz.minigames.PlayerData;
import com.pauldavdesign.mineauz.minigames.events.TimerExpireEvent;

public class TeamDMMinigame extends MinigameType{
	private static Minigames plugin = Minigames.plugin;
	private PlayerData pdata = plugin.pdata;
	private MinigameData mdata = plugin.mdata;
	
	public TeamDMMinigame() {
		setLabel("teamdm");
	}
	
	
	public boolean joinMinigame(final MinigamePlayer player, Minigame mgm){
		if(mgm.getQuitPosition() != null && mgm.isEnabled() && mgm.getEndPosition() != null && mgm.getLobbyPosition() != null){
			
			int redSize = mgm.getRedTeam().size();
			int blueSize = mgm.getBlueTeam().size();
			
			Location lobby = mgm.getLobbyPosition();
			
			if(mgm.getPlayers().size() < mgm.getMaxPlayers()){
				if(mgm.canLateJoin() || mgm.getMpTimer() == null || mgm.getMpTimer().getPlayerWaitTimeLeft() != 0){
//					pdata.storePlayerData(player, mgm.getDefaultGamemode());
					player.storePlayerData();
//					pdata.addPlayerMinigame(player, mgm);
					player.setMinigame(mgm);
					mgm.addPlayer(player);
					
					if(mgm.getMpTimer() == null || mgm.getMpTimer().getStartWaitTimeLeft() != 0){
//						player.teleport(lobby);
						pdata.minigameTeleport(player, lobby);
					}
					else{
						int team;
						
						if(redSize <= blueSize){
							mgm.addRedTeamPlayer(player);
							player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "����� " + ChatColor.RED + "���� ��" + ChatColor.WHITE + " �� ���̽��ϴ�.");
							
							team = 0;
						}
						else{
							mgm.addBlueTeamPlayer(player);
							player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "����� " + ChatColor.BLUE + "��� ��" + ChatColor.WHITE + " �� ���̽��ϴ�.");
							
							team = 1;
						}
						
						player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "5�� �Ŀ� ���ϴ�...");
//						player.teleport(lobby);
						pdata.minigameTeleport(player, lobby);
						
						final MinigamePlayer fply = player;
						final Minigame fmgm = mgm;
						final int fteam = team;
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							
							
							public void run() {
								if(fply.isInMinigame()){
									List<Location> locs = new ArrayList<Location>();
									if(!fmgm.getStartLocationsRed().isEmpty()){
										if(fteam == 0){
											locs.addAll(fmgm.getStartLocationsRed());
										}
										else{
											locs.addAll(fmgm.getStartLocationsBlue());
										}
									}
									else{
										locs.addAll(fmgm.getStartLocations());
									}
									Collections.shuffle(locs);
									pdata.minigameTeleport(fply, locs.get(0));
									fmgm.getPlayersLoadout(fply).equiptLoadout(fply);
								}
							}
						}, 100);

						player.getPlayer().setScoreboard(mgm.getScoreboardManager());
						mgm.setScore(player, 1);
						mgm.setScore(player, 0);
					}
					player.sendMessage(ChatColor.GREEN + "��������ġ �̴ϰ����� �����ϼ̽��ϴ�. /quit ���� ������ �� �ֽ��ϴ�.");
				
					if(mgm.getMpTimer() == null && mgm.getPlayers().size() >= mgm.getMinPlayers()){
						mgm.setMpTimer(new MultiplayerTimer(mgm.getName()));
						mgm.getMpTimer().startTimer();
						if(mgm.getPlayers().size() == mgm.getMaxPlayers()){
							mgm.getMpTimer().setPlayerWaitTime(0);
							mdata.sendMinigameMessage(mgm, "�̴ϰ����� �� á���ϴ�. ��ٸ��� �ʰ� �ٷ� �����մϴ�.", "info", null);
						}
					}
					else if(mgm.getMpTimer() != null && mgm.getMpTimer().isPaused() && 
							(mgm.getBlueTeam().size() == mgm.getRedTeam().size() || 
							mgm.getBlueTeam().size() + 1 == mgm.getRedTeam().size() || 
							mgm.getBlueTeam().size() == mgm.getRedTeam().size() + 1)){
						mgm.getMpTimer().resumeTimer();
					}
					else{
						int neededPlayers = mgm.getMinPlayers() - mgm.getPlayers().size();
						if(neededPlayers == 1){
							player.sendMessage(ChatColor.BLUE + "1 ���� �÷��̾ �� ��ٸ��ϴ�.");
						}
						else if(neededPlayers > 1){
							player.sendMessage(ChatColor.BLUE + "" + neededPlayers + " ���� �÷��̾ �� ��ٸ��ϴ�.");
						}
					}
					
					return true;
				}
				else if(mgm.getMpTimer().getPlayerWaitTimeLeft() == 0){
					player.sendMessage(ChatColor.RED + "�� �̴ϰ����� �̹� �����߽��ϴ�. ���߿� �ٽ� �õ��ϼ���.");
					return false;
				}
			}
			else if(mgm.getPlayers().size() == mgm.getMaxPlayers()){
				player.sendMessage(ChatColor.RED + "�˼��մϴ�. �� �̴ϰ����� �� á���ϴ�.");
				return false;
			}
		}
		return false;
	}
	
	
	public void quitMinigame(MinigamePlayer player, Minigame mgm, boolean forced){
		if(mgm.getRedTeam().contains(player.getPlayer())){
			mgm.removeRedTeamPlayer(player);
		}
		else{
			mgm.removeBlueTeamPlayer(player);
		}
		
		if(mgm.getPlayers().size() == 0 && !forced){
			if(mgm.getMpTimer() != null){
				mgm.getMpTimer().pauseTimer();
				mgm.getMpTimer().removeTimer();
				mgm.setMpTimer(null);
			}
			
			if(mgm.getMpBets() != null && (mgm.getMpTimer() == null || mgm.getMpTimer().getPlayerWaitTimeLeft() != 0)){
				if(mgm.getMpBets().getPlayersMoneyBet(player) != null){
					plugin.getEconomy().depositPlayer(player.getName(), mgm.getMpBets().getPlayersMoneyBet(player));
				}
			}
			mgm.setMpBets(null);
		}
		else if(mgm.getPlayers().size() >= 1 && 
				(mgm.getRedTeam().size() == 0 ||
				mgm.getBlueTeam().size() == 0) &&
				mgm.getMpTimer() != null && 
				mgm.getMpTimer().getStartWaitTimeLeft() == 0
				&& !forced){
			
			if(mgm.getRedTeam().size() == 0){
				pdata.endTeamMinigame(1, mgm);
			}
			else{
				pdata.endTeamMinigame(0, mgm);
			}
			
			if(mgm.getMpBets() != null){
				mgm.setMpBets(null);
			}
		}
		else if(mgm.getPlayers().size() < mgm.getMinPlayers() && 
				mgm.getMpTimer() != null && 
				mgm.getMpTimer().getStartWaitTimeLeft() != 0
				&& !forced){
			mgm.getMpTimer().setPlayerWaitTime(10);
			mgm.getMpTimer().pauseTimer();
			mgm.getMpTimer().removeTimer();
			mgm.setMpTimer(null);
			for(MinigamePlayer pl : mgm.getPlayers()){
				pl.sendMessage(ChatColor.BLUE + "1 ���� �÷��̾ �� ��ٸ��ϴ�.");
			}
		}
		
		if(player.getPlayer().isDead()){
			player.getPlayer().setHealth(2);
		}
		callGeneralQuit(player, mgm);
		
		if(mgm.getMpBets() != null && (mgm.getMpTimer() == null || mgm.getMpTimer().getPlayerWaitTimeLeft() != 0) && !forced){
			if(mgm.getMpBets().getPlayersMoneyBet(player) != null){
				plugin.getEconomy().depositPlayer(player.getName(), mgm.getMpBets().getPlayersMoneyBet(player));
			}
			mgm.getMpBets().removePlayersBet(player);
		}
	}
	
	
	public void endMinigame(MinigamePlayer player, Minigame mgm){
		boolean hascompleted = false;
		Configuration completion = null;
		
		player.sendMessage(ChatColor.GREEN + "" + mgm + " �̴ϰ����� �����ϴ�! �����մϴ�!");
		
		if(mgm.getEndPosition() != null){
			if(!player.getPlayer().isDead()){
//				player.teleport(mgm.getEndPosition());
				pdata.minigameTeleport(player, mgm.getEndPosition());
			}
			else{
				pdata.addRespawnPosition(player.getPlayer(), mgm.getEndPosition());
			}
		}
		
		if(mgm.getRedTeam().contains(player.getPlayer())){
			mgm.removeRedTeamPlayer(player);
		}
		else{
			mgm.removeBlueTeamPlayer(player);
		}

		player.getPlayer().setFireTicks(0);
		
		if(plugin.getSQL() == null){
			completion = mdata.getConfigurationFile("completion");
			hascompleted = completion.getStringList(mgm.getName()).contains(player.getName());
			
			if(plugin.getSQL() == null){
				if(!completion.getStringList(mgm.getName()).contains(player.getName())){
					List<String> completionlist = completion.getStringList(mgm.getName());
					completionlist.add(player.getName());
					completion.set(mgm.getName(), completionlist);
					MinigameSave completionsave = new MinigameSave("completion");
					completionsave.getConfig().set(mgm.getName(), completionlist);
					completionsave.saveConfig();
				}
			}
			
			issuePlayerRewards(player, mgm, hascompleted);
		}
	}
	
	public static void switchTeam(Minigame mgm, MinigamePlayer player){
		if(mgm.getBlueTeam().contains(player.getPlayer())){
			mgm.removeBlueTeamPlayer(player);
			mgm.addRedTeamPlayer(player);
			mgm.removePlayersLoadout(player);
		}
		else{
			mgm.removeRedTeamPlayer(player);
			mgm.addBlueTeamPlayer(player);
			mgm.removePlayersLoadout(player);
		}
	}
	
	/*----------------*/
	/*-----EVENTS-----*/
	/*----------------*/
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerRespawn(PlayerRespawnEvent event){
		final MinigamePlayer ply = pdata.getMinigamePlayer(event.getPlayer());
		if(ply.isInMinigame() && ply.getMinigame().getType().equals("teamdm")){
			int team = 0;
			Minigame mg = ply.getMinigame();
			if(mg.getBlueTeam().contains(plugin.getServer().getOfflinePlayer(ply.getName()))){
				team = 1;
			}
			List<Location> starts = new ArrayList<Location>();
			if(!mg.getStartLocationsBlue().isEmpty() && !mg.getStartLocationsRed().isEmpty()){
				if(team == 1){
					starts.addAll(mg.getStartLocationsBlue());
				}
				else{
					starts.addAll(mg.getStartLocationsRed());
				}
				mg.getPlayersLoadout(ply).equiptLoadout(ply);
			}
			else{
				starts.addAll(mg.getStartLocations());
			}
			Collections.shuffle(starts);
			event.setRespawnLocation(starts.get(0));
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
				public void run() {
					ply.getPlayer().setNoDamageTicks(60);
				}
			});
			
			mg.getPlayersLoadout(ply).equiptLoadout(ply);
			
		}
	}
	
	@EventHandler
	public void timerExpire(TimerExpireEvent event){
		if(event.getMinigame().getType().equals(getLabel())){
			Minigame mgm = event.getMinigame();
			if(!event.getMinigame().getDefaultWinner().equals("none")){
				if(event.getMinigame().getDefaultWinner().equals("blue")){
					pdata.endTeamMinigame(1, event.getMinigame());
				}
				else{
					pdata.endTeamMinigame(0, event.getMinigame());
				}
			}
			else if(event.getMinigame().getBlueTeamScore() > event.getMinigame().getRedTeamScore()){
				pdata.endTeamMinigame(1, event.getMinigame());
			}
			else if(event.getMinigame().getBlueTeamScore() < event.getMinigame().getRedTeamScore()){
				pdata.endTeamMinigame(0, event.getMinigame());
			}
			else{
				List<MinigamePlayer> players = new ArrayList<MinigamePlayer>();
				players.addAll(event.getMinigame().getPlayers());
				
				mgm.setRedTeamScore(0);
				mgm.setBlueTeamScore(0);
				
				mgm.getMpTimer().setStartWaitTime(0);
				
				if(mgm.getMinigameTimer() != null){
					mgm.getMinigameTimer().stopTimer();
					mgm.setMinigameTimer(null);
				}
				
				if(mgm.getMpTimer() != null){
					mgm.getMpTimer().pauseTimer();
					mgm.getMpTimer().removeTimer();
					mgm.setMpTimer(null);
				}
				
				if(mgm.getFloorDegenerator() != null && mgm.getPlayers().size() == 0){
					mgm.getFloorDegenerator().stopDegenerator();
				}
				
				if(mgm.getMpBets() != null && mgm.getPlayers().size() == 0){
					mgm.setMpBets(null);
				}
				
				for(MinigamePlayer ply : players){
					pdata.quitMinigame(ply, true);
					if(!plugin.getConfig().getBoolean("multiplayer.broadcastwin")){
						ply.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.BLUE + "��� ��" + ChatColor.WHITE + " �� " + 
								ChatColor.RED + "���� ��" + ChatColor.WHITE + " �� " + event.getMinigame().getName() + " �̴ϰ��ӿ��� �����ϴ�, ");
						ply.sendMessage("����: " + ChatColor.BLUE + String.valueOf(event.getMinigame().getBlueTeamScore()) + ChatColor.WHITE + " �� " + 
								ChatColor.RED + event.getMinigame().getRedTeamScore());
					}
				}
				if(plugin.getConfig().getBoolean("multiplayer.broadcastwin")){
					plugin.getServer().broadcastMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.BLUE + "��� ��" + ChatColor.WHITE + " �� " + 
							ChatColor.RED + "���� ��" + ChatColor.WHITE + " �� " + event.getMinigame().getName() + " �̴ϰ��ӿ��� �����ϴ�, ");
					plugin.getServer().broadcastMessage("����: " + ChatColor.BLUE + String.valueOf(event.getMinigame().getBlueTeamScore()) + ChatColor.WHITE + " �� " + 
							ChatColor.RED + event.getMinigame().getRedTeamScore());
				}
			}
		}
	}
}
