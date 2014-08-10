package com.pauldavdesign.mineauz.minigames.scoring;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.pauldavdesign.mineauz.minigames.CTFFlag;
import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.MinigamePlayer;
import com.pauldavdesign.mineauz.minigames.MinigameUtils;
import com.pauldavdesign.mineauz.minigames.events.EndMinigameEvent;
import com.pauldavdesign.mineauz.minigames.events.QuitMinigameEvent;
import com.pauldavdesign.mineauz.minigames.gametypes.TeamDMMinigame;

public class CTFType extends ScoreType{

	@Override
	public String getType() {
		return "ctf";
	}

	@Override
	public void balanceTeam(List<MinigamePlayer> players, Minigame minigame) {
		
		for(int i = 0; i < players.size(); i++){
			if(minigame.getType().equals("teamdm")){
				int team = -1;
				if(minigame.getBlueTeam().contains(players.get(i))){
					team = 1;
				}
				else if(minigame.getRedTeam().contains(players.get(i))){
					team = 0;
				}
				
				if(team == 1){
					if(minigame.getRedTeam().size() < minigame.getBlueTeam().size() - 1){
//						minigame.getBlueTeam().remove(players.get(i));
						minigame.removeBlueTeamPlayer(players.get(i));
						minigame.addRedTeamPlayer(players.get(i));
						team = 0;
						players.get(i).sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "����� " + ChatColor.RED + "���� ��" + ChatColor.WHITE + "�� �Ǿ����ϴ�.");
						mdata.sendMinigameMessage(minigame, players.get(i).getName() + " ���� " + ChatColor.RED + "���� ��" + ChatColor.WHITE + "�� �Ǽ̽��ϴ�.", null, players.get(i));
					}
				}
				else if(team == 0){
					if(minigame.getBlueTeam().size() < minigame.getRedTeam().size() - 1){
//						minigame.getRedTeam().remove(players.get(i));
						minigame.removeRedTeamPlayer(players.get(i));
						minigame.addBlueTeamPlayer(players.get(i));
						team = 1;
						players.get(i).sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "����� " + ChatColor.BLUE + "��� ��" + ChatColor.WHITE + "�� �Ǿ����ϴ�.");
						mdata.sendMinigameMessage(minigame, players.get(i).getName() + " ���� " + ChatColor.BLUE + "��� ��" + ChatColor.WHITE + "�� �Ǿ����ϴ�.", null, players.get(i));
					}
				}
				else{
					if(minigame.getRedTeam().size() <= minigame.getBlueTeam().size()){
						minigame.addRedTeamPlayer(players.get(i));
						team = 0;
						players.get(i).sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "����� " + ChatColor.RED + "���� ��" + ChatColor.WHITE + "�� �Ǿ����ϴ�.");
						mdata.sendMinigameMessage(minigame, players.get(i).getName() + " ���� " + ChatColor.RED + "���� ��" + ChatColor.WHITE + "�� �Ǿ����ϴ�.", null, players.get(i));
					}
					else if(minigame.getBlueTeam().size() <= minigame.getRedTeam().size()){
						minigame.addBlueTeamPlayer(players.get(i));
						team = 1;
						players.get(i).sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "����� " + ChatColor.BLUE + "��� ��" + ChatColor.WHITE + "�� �Ǿ����ϴ�.");
						mdata.sendMinigameMessage(minigame, players.get(i).getName() + " ���� " + ChatColor.BLUE + "��� ��" + ChatColor.WHITE + "�� �Ǿ����ϴ�.", null, players.get(i));
					}
				}
//				TeamDMMinigame.applyTeam(players.get(i), team);
			}
		}
	}
	
	@EventHandler
	private void takeFlag(PlayerInteractEvent event){
		MinigamePlayer ply = pdata.getMinigamePlayer(event.getPlayer());
		if(ply.isInMinigame() && !ply.getPlayer().isDead()){
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK && (event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN) && ply.getPlayer().getItemInHand().getType() == Material.AIR){
				Minigame mgm = ply.getMinigame();
				Sign sign = (Sign) event.getClickedBlock().getState();
				if(mgm.getScoreType().equals("ctf") && sign.getLine(1).equals(ChatColor.GREEN + "���")){
					if(!mgm.getBlueTeam().isEmpty() || !mgm.getRedTeam().isEmpty() || !mgm.getType().equals("teamdm")){
						int team = 0;
						if(mgm.getBlueTeam().contains(event.getPlayer())){
							team = 1;
						}
						
						if(!mgm.getType().equals("teamdm")){
							team = -1;
						}
						
						String sloc = MinigameUtils.createLocationID(event.getClickedBlock().getLocation());
						
						if((sign.getLine(2).equalsIgnoreCase(ChatColor.RED + "����") && team == 1) || 
								(sign.getLine(2).equalsIgnoreCase(ChatColor.BLUE + "���") && team == 0) ||
								sign.getLine(2).equalsIgnoreCase(ChatColor.GRAY + "�Ϲ�")){
							if(mgm.getFlagCarrier(ply) == null){
								if(!mgm.hasDroppedFlag(sloc)){
									int oTeam = 1;
									if(team == 1){
										oTeam = 0;
									}
									if(sign.getLine(2).equalsIgnoreCase(ChatColor.GRAY + "�Ϲ�")){
										oTeam = -1;
									}
									CTFFlag flag = new CTFFlag(event.getClickedBlock().getLocation(), oTeam, event.getPlayer(), mgm);
									mgm.addFlagCarrier(ply, flag);
									flag.removeFlag();
								}
								else{
									CTFFlag flag = mgm.getDroppedFlag(sloc);
									mgm.addFlagCarrier(ply, flag);
									if(!flag.isAtHome()){
										flag.stopTimer();
									}
									flag.removeFlag();
								}
								
								if(team == 0 && mgm.getFlagCarrier(ply).getTeam() == 1){
									String message = ply.getName() + " ���� " + ChatColor.BLUE + "��� ��" + ChatColor.WHITE + " �� ����� ���ƽ��ϴ�!";
									mdata.sendMinigameMessage(mgm, message, null, null);
									mgm.getFlagCarrier(ply).startCarrierParticleEffect(ply.getPlayer());
								}else if(team == 1 && mgm.getFlagCarrier(ply).getTeam() == 0){
									String message = ply.getName() + " ���� " + ChatColor.RED + "���� ��" + ChatColor.WHITE + " �� ����� ���ƽ��ϴ�!";
									mdata.sendMinigameMessage(mgm, message, null, null);
									mgm.getFlagCarrier(ply).startCarrierParticleEffect(ply.getPlayer());
								}
								else{
									String message = ply.getName() + " ���� " + ChatColor.GRAY + "�Ϲ�" + ChatColor.WHITE + " ����� ���ƽ��ϴ�!";
									mdata.sendMinigameMessage(mgm, message, null, null);
									mgm.getFlagCarrier(ply).startCarrierParticleEffect(ply.getPlayer());
								}
							}
							
						}
						else if((team == 0 && sign.getLine(2).equalsIgnoreCase(ChatColor.RED + "����") ||
								(team == 1 && sign.getLine(2).equalsIgnoreCase(ChatColor.BLUE + "���")) || 
								(team == 0 && sign.getLine(2).equalsIgnoreCase(ChatColor.GREEN + "ĸ��") && sign.getLine(3).equalsIgnoreCase(ChatColor.RED + "����")) ||
								(team == 1 && sign.getLine(2).equalsIgnoreCase(ChatColor.GREEN + "ĸ��") && sign.getLine(3).equalsIgnoreCase(ChatColor.BLUE + "���")) ||
								(sign.getLine(2).equalsIgnoreCase(ChatColor.GREEN + "ĸ��") && sign.getLine(3).equalsIgnoreCase(ChatColor.GRAY + "�⺻")))){
							
							String clickID = MinigameUtils.createLocationID(event.getClickedBlock().getLocation());
							
							if(mgm.getFlagCarrier(ply) != null && ((mgm.hasDroppedFlag(clickID) && mgm.getDroppedFlag(clickID).isAtHome()) || !mgm.hasDroppedFlag(clickID))){
								CTFFlag flag = mgm.getFlagCarrier(ply);
								flag.respawnFlag();
								String id = MinigameUtils.createLocationID(flag.getSpawnLocation());
								mgm.addDroppedFlag(id, flag);
								mgm.removeFlagCarrier(ply);
								
								boolean end = false;
								
								if(mgm.getType().equals("teamdm")){
									if(team == 1){
										mgm.incrementBlueTeamScore();
										
										if(mgm.getMaxScore() != 0 && mgm.getBlueTeamScore() >= mgm.getMaxScorePerPlayer(mgm.getPlayers().size())){
											end = true;
										}
									}
									else{
										mgm.incrementRedTeamScore();
										
										if(mgm.getMaxScore() != 0 && mgm.getRedTeamScore() >= mgm.getMaxScorePerPlayer(mgm.getPlayers().size())){
											end = true;
										}
									}
									
									if(team == 0){
										String message = ply.getName() + " ���� " + ChatColor.RED + "���� ��" + ChatColor.WHITE + " �� ����� ĸ�� �߽��ϴ�.";
										mdata.sendMinigameMessage(mgm, message, null, null);
									}else{
										String message = ply.getName() + " ���� " + ChatColor.BLUE + "��� ��" + ChatColor.WHITE + " �� ����� ĸ�� �߽��ϴ�.";;
										mdata.sendMinigameMessage(mgm, message, null, null);
									}
									flag.stopCarrierParticleEffect();
//									pdata.addPlayerScore(ply);
									ply.addScore();
									mgm.setScore(ply, ply.getScore());
									
									if(end){
										if(team == 0){
											mdata.sendMinigameMessage(mgm, ply.getName() + " ���� " + ChatColor.RED + "���� ��" + ChatColor.WHITE + " ��  ������ ����� ĸ�� �߽��ϴ�.", null, null);
										}
										else{
											mdata.sendMinigameMessage(mgm, ply.getName() + " ���� " + ChatColor.BLUE + "��� ��" + ChatColor.WHITE + " ��  ������ ����� ĸ�� �߽��ϴ�.", null, null);
										}
										if(team == 1){
											pdata.endTeamMinigame(1, mgm);
											mgm.resetFlags();
										}
										else{
											pdata.endTeamMinigame(0, mgm);
											mgm.resetFlags();
										}
									}
								}
								else{
//									pdata.addPlayerScore(ply);
									ply.addScore();
									mgm.setScore(ply, ply.getScore());
									if(mgm.getMaxScore() != 0 && ply.getScore() >= mgm.getMaxScorePerPlayer(mgm.getPlayers().size())){
										end = true;
									}
									
									mdata.sendMinigameMessage(mgm, ply.getName() + " ���� ����� ĸ�� �߽��ϴ�.", null, null);
									flag.stopCarrierParticleEffect();
									
									if(end){
										mdata.sendMinigameMessage(mgm, ChatColor.WHITE + ply.getName() + " ���� ������ ����� ĸ�� �߽��ϴ�.", null, null);
										
										pdata.endMinigame(ply);
										mgm.resetFlags();
									}
								}
							}
							else if(mgm.getFlagCarrier(ply) == null && mgm.hasDroppedFlag(clickID) && !mgm.getDroppedFlag(clickID).isAtHome()){
								CTFFlag flag = mgm.getDroppedFlag(sloc);
								if(mgm.hasDroppedFlag(sloc)){
									mgm.removeDroppedFlag(sloc);
									String newID = MinigameUtils.createLocationID(flag.getSpawnLocation());
									mgm.addDroppedFlag(newID, flag);
								}
								flag.respawnFlag();
								
								if(flag.getTeam() == 1){
									mdata.sendMinigameMessage(mgm, ply.getName() + " ���� " + ChatColor.BLUE + "��� ��" + ChatColor.WHITE + " �� ����� �ǵ��Ƚ��ϴ�!", null, null);
								}else if(flag.getTeam() == 0){
									mdata.sendMinigameMessage(mgm, ply.getName() + " ���� " + ChatColor.RED + "���� ��" + ChatColor.WHITE + " �� ����� �ǵ��Ƚ��ϴ�!", null, null);
								}
								else{
									mdata.sendMinigameMessage(mgm, ply.getName() + " ���� " + ChatColor.GRAY + "�⺻" + ChatColor.WHITE + " ����� �ǵ��Ƚ��ϴ�!", null, null);
									mgm.getFlagCarrier(ply).startCarrierParticleEffect(ply.getPlayer());
								}
							}
							else if(mgm.getFlagCarrier(ply) != null && mgm.hasDroppedFlag(clickID) && !mgm.getDroppedFlag(clickID).isAtHome()){
								ply.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "����� �ڽ��� ����� �ǵ��� �� �����ϴ�!");
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	private void dropFlag(PlayerDeathEvent event){
		MinigamePlayer ply = pdata.getMinigamePlayer(event.getEntity());
		if(ply.isInMinigame()){
			Minigame mgm = ply.getMinigame();
			if(mgm.isFlagCarrier(ply)){
				CTFFlag flag = mgm.getFlagCarrier(ply);
				Location loc = flag.spawnFlag(ply.getPlayer().getLocation());
				if(loc != null){
					String id = MinigameUtils.createLocationID(loc);
					mgm.addDroppedFlag(id, flag);
					mgm.removeFlagCarrier(ply);

					if(flag.getTeam() == 0){
						mdata.sendMinigameMessage(mgm, ply.getName() + " dropped " + ChatColor.RED + "Red Team's" + ChatColor.WHITE + " flag!", null, null);
					}else if(flag.getTeam() == 1){
						mdata.sendMinigameMessage(mgm, ply.getName() + " dropped " + ChatColor.BLUE + "Blue Team's" + ChatColor.WHITE + " flag!", null, null);
					}
					else{
						mdata.sendMinigameMessage(mgm, ply.getName() + " dropped the " + ChatColor.GRAY + "neutral" + ChatColor.WHITE + " flag!", null, null);
					}
					flag.stopCarrierParticleEffect();
					flag.startReturnTimer();
				}
				else{
					flag.respawnFlag();
					mgm.removeFlagCarrier(ply);
					flag.stopCarrierParticleEffect();
				}
			}
		}
	}
	
	@EventHandler
	private void playerQuitMinigame(QuitMinigameEvent event){
		if(event.getMinigame() != null && event.getMinigame().getScoreType().equals("ctf")) {
			if(event.getMinigame().isFlagCarrier(event.getMinigamePlayer())){
				event.getMinigame().getFlagCarrier(event.getMinigamePlayer()).stopCarrierParticleEffect();
				event.getMinigame().getFlagCarrier(event.getMinigamePlayer()).respawnFlag();
				event.getMinigame().removeFlagCarrier(event.getMinigamePlayer());
			}
			if(event.getMinigame().getPlayers().size() == 1){
				event.getMinigame().resetFlags();
			}
		}
		else if(event.getMinigame() == null) {
			Bukkit.getLogger().severe("----------------Problem Detected!----------------\n NULL Minigame! \n Player responsible: " + event.getPlayer() + 
					"\n Please report the info leading up to this message from the logs!"); //TODO: Debug message
		}
	}
	
	@EventHandler
	private void playerEndMinigame(EndMinigameEvent event){
		if(event.getMinigame().getScoreType().equals("ctf")){
			if(event.getMinigame().isFlagCarrier(event.getMinigamePlayer())){
				event.getMinigame().getFlagCarrier(event.getMinigamePlayer()).respawnFlag();
				event.getMinigame().getFlagCarrier(event.getMinigamePlayer()).stopCarrierParticleEffect();
				event.getMinigame().removeFlagCarrier(event.getMinigamePlayer());
			}
			if(event.getMinigame().getPlayers().size() == 1){
				event.getMinigame().resetFlags();
			}
		}
	}
	
	@EventHandler
	public void playerAutoBalance(PlayerDeathEvent event){
		MinigamePlayer ply = pdata.getMinigamePlayer(event.getEntity());
		if(ply.isInMinigame() && ply.getMinigame().getType().equals("teamdm")){
			int pteam = 0;
			if(ply.getMinigame().getBlueTeam().contains(ply.getPlayer())){
				pteam = 1;
			}
			final Minigame mgm = ply.getMinigame();
			
			if(mgm.getScoreType().equals("ctf")){
				if(pteam == 1){
					if(mgm.getRedTeam().size() < mgm.getBlueTeam().size() - 1){
						TeamDMMinigame.switchTeam(mgm, ply);
						ply.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "You have been switched to " + ChatColor.RED + "Red Team");
						for(MinigamePlayer pl : mgm.getPlayers()){
							if(pl != ply){
								pl.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + ply.getName() + " has been switched to " + ChatColor.RED + "Red Team");
							}
						}
					}
				}
				else{
					if(mgm.getBlueTeam().size() < mgm.getRedTeam().size()  - 1){
						TeamDMMinigame.switchTeam(mgm, ply);
						ply.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "You have been switched to " + ChatColor.BLUE + "Blue Team");
						for(MinigamePlayer pl : mgm.getPlayers()){
							if(pl != ply){
								pl.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + ply.getName() + " has been switched to " + ChatColor.BLUE + "Blue Team");
							}
						}
					}
				}
			}
		}
	}
}
