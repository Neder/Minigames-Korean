package com.pauldavdesign.mineauz.minigames;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;

import com.pauldavdesign.mineauz.minigames.events.EndMinigameEvent;
import com.pauldavdesign.mineauz.minigames.events.EndTeamMinigameEvent;
import com.pauldavdesign.mineauz.minigames.events.JoinMinigameEvent;
import com.pauldavdesign.mineauz.minigames.events.QuitMinigameEvent;
import com.pauldavdesign.mineauz.minigames.events.RevertCheckpointEvent;
import com.pauldavdesign.mineauz.minigames.events.SpectateMinigameEvent;
import com.pauldavdesign.mineauz.minigames.scoring.ScoreTypes;

public class PlayerData {
	private Map<String, MinigamePlayer> minigamePlayers = new HashMap<String, MinigamePlayer>();
	private Map<String, OfflineMinigamePlayer> offlineMinigamePlayers = new HashMap<String, OfflineMinigamePlayer>();
	

	private Map<String, StoredPlayerCheckpoints> storedPlayerCheckpoints = new HashMap<String, StoredPlayerCheckpoints>();

	
	private boolean partyMode = false;
	
	private List<String> deniedCommands = new ArrayList<String>();
	

	
	private static Minigames plugin = Minigames.plugin;
	private MinigameData mdata = plugin.mdata;
	MinigameSave invsave = new MinigameSave("playerinv");
	
	public PlayerData(){}
	
	public void joinMinigame(MinigamePlayer player, Minigame minigame) {
		String gametype = minigame.getType();
		
		JoinMinigameEvent event = new JoinMinigameEvent(player, minigame);
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		if(!event.isCancelled()){
			if(mdata.getMinigameTypes().contains(gametype)){
				player.setAllowTeleport(true);
				player.getPlayer().setFallDistance(0);
				if(mdata.minigameType(gametype).joinMinigame(player, minigame)){
					plugin.getLogger().info(player.getName() + " 님이 " + minigame.getName() + " 를 시작하셨습니다.");
					mdata.sendMinigameMessage(minigame, player.getName() + " 님이 " + minigame.getName() + " 에 들어오셨습니다.", null, player);
					
					player.getPlayer().setGameMode(minigame.getDefaultGamemode());
					player.setAllowGamemodeChange(false);
					player.getPlayer().setAllowFlight(false);
					player.setAllowTeleport(false);
					player.setStartTime(Calendar.getInstance().getTimeInMillis());

					
					if(hasStoredPlayerCheckpoint(player)){
						if(getPlayersStoredCheckpoints(player).hasCheckpoint(minigame.getName())){
							player.setCheckpoint(getPlayersStoredCheckpoints(player).getCheckpoint(minigame.getName()));
							StoredPlayerCheckpoints spc = getPlayersStoredCheckpoints(player);
							if(spc.hasFlags(minigame.getName())){
								player.setFlags(spc.getFlags(minigame.getName()));
							}
							if(spc.hasTime(minigame.getName())){
								player.setStoredTime(spc.getTime(minigame.getName()));
							}
							if(spc.hasDeaths(minigame.getName())) {
								player.setDeaths(spc.getDeaths(minigame.getName()));
							}
							if(spc.hasReverts(minigame.getName())){
								player.setReverts(spc.getReverts(minigame.getName()));
							}
							getPlayersStoredCheckpoints(player).removeCheckpoint(minigame.getName());
							getPlayersStoredCheckpoints(player).removeFlags(minigame.getName());
							spc.removeDeaths(minigame.getName());
							spc.removeTime(minigame.getName());
							spc.removeReverts(minigame.getName());
							if(getPlayersStoredCheckpoints(player).hasNoCheckpoints() && !getPlayersStoredCheckpoints(player).hasGlobalCheckpoint()){
								storedPlayerCheckpoints.remove(player.getName());
							}
							revertToCheckpoint(player);
						}
					}
					
					for(MinigamePlayer pl : minigame.getSpectators()){
						player.getPlayer().hidePlayer(pl.getPlayer());
					}

					for(PotionEffect potion : player.getPlayer().getActivePotionEffects()){
						player.getPlayer().removePotionEffect(potion.getType());
					}
				}
			}
			else{
				player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "그런 게임타입은 존재하지 않습니다!");
			}
		}
	}
	
	public void spectateMinigame(MinigamePlayer player, Minigame minigame) {
		SpectateMinigameEvent event = new SpectateMinigameEvent(player, minigame);
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		if(!event.isCancelled()){
			player.storePlayerData();
			player.setMinigame(minigame);
			player.getPlayer().setGameMode(GameMode.ADVENTURE);
			
			minigame.addSpectator(player);
			minigameTeleport(player, minigame.getStartLocations().get(0));
			
			if(minigame.canSpectateFly()){
				player.getPlayer().setAllowFlight(true);
			}
			for(MinigamePlayer pl : minigame.getPlayers()){
				pl.getPlayer().hidePlayer(player.getPlayer());
			}
			
			player.getPlayer().setScoreboard(minigame.getScoreboardManager());
			
			for(PotionEffect potion : player.getPlayer().getActivePotionEffects()){
				player.getPlayer().removePotionEffect(potion.getType());
			}
			player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "" + minigame + " 미니게임을 구경하고 계십니다.\n" +
					"/quit 으로 구경 모드에서 나가실 수 있습니다.");
			mdata.sendMinigameMessage(minigame, player.getName() + " 님이 " + minigame + " 미니게임을 구경하고 계십니다.", null, player);
		}
	}
	
	public void joinWithBet(MinigamePlayer player, Minigame minigame, Double money){
		
		JoinMinigameEvent event = new JoinMinigameEvent(player, minigame, true);
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		if(!event.isCancelled()){
			if(minigame != null && minigame.isEnabled() && (minigame.getMpTimer() == null || minigame.getMpTimer().getPlayerWaitTimeLeft() != 0)){
				if(minigame.getMpBets() == null && (player.getPlayer().getItemInHand().getType() != Material.AIR || money != 0)){
					minigame.setMpBets(new MultiplayerBets());
				}
				MultiplayerBets pbet = minigame.getMpBets(); 
				ItemStack item = player.getPlayer().getItemInHand().clone();
				if(pbet != null && 
						((money != 0 && pbet.canBet(player, money) && plugin.getEconomy().getBalance(player.getName()) >= money) || 
								(pbet.canBet(player, item) && item.getType() != Material.AIR && pbet.betValue(item.getType()) > 0))){
					if(minigame.getPlayers().isEmpty() || minigame.getPlayers().size() != minigame.getMaxPlayers()){
						player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "배팅을 하셨군요! 행운을 빕니다!");
						if(money == 0){
							pbet.addBet(player, item);
						}
						else{
							pbet.addBet(player, money);
							plugin.getEconomy().withdrawPlayer(player.getName(), money);
						}
						player.getPlayer().getInventory().removeItem(new ItemStack(item.getType(), 1));
						joinMinigame(player, minigame);
					}
					else{
						player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "죄송합니다. 이 미니게임은 꽉 찼습니다.");
					}
				}
				else if(item.getType() == Material.AIR && money == 0){
					player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "아무것도 배팅할 수 없습니다!");
				}
				else if(money != 0 && !pbet.canBet(player, money)){
					player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "이번 라운드에서 알맞은 돈을 배팅하지 않았습니다!");
					player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "" + minigame.getMpBets().getHighestMoneyBet() + " 원을 배팅하셔야 합니다.");
				}
				else if(money != 0 && plugin.getEconomy().getBalance(player.getName()) < money){
					player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "돈이 없습니다!");
					player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "" + minigame.getMpBets().getHighestMoneyBet() + " 원을 배팅하셔야 합니다.");
				}
				else{
					player.sendMessage(ChatColor.RED + "이번 라운드에 알맞은 아이템을 베팅하지 않았습니다!");
					player.sendMessage(ChatColor.RED + "" + minigame.getMpBets().highestBetName() + " 를 배팅하셔야 합니다.");
				}
			}
			else if(minigame != null && minigame.getMpTimer() != null && minigame.getMpTimer().getPlayerWaitTimeLeft() == 0){
				player.sendMessage(ChatColor.RED + "이 게임은 이미 시작했습니다. 나중에 다시 시도해 주세요.");
			}
		}
	}
	
	public void startMPMinigame(Minigame minigame){
		List<MinigamePlayer> players = new ArrayList<MinigamePlayer>();
		players.addAll(minigame.getPlayers());
		
		Collections.shuffle(players);
		
		if(minigame.getType().equals("teamdm") && ScoreTypes.getScoreType(minigame.getScoreType()) != null){
			ScoreTypes.getScoreType(minigame.getScoreType()).balanceTeam(players, minigame);
		}
		
		Location start = null;
		int pos = 0;
		int bluepos = 0;
		int redpos = 0;
		
		for(MinigamePlayer ply : players){
			if(!minigame.getType().equals("teamdm")){
				if(pos < minigame.getStartLocations().size()){
					start = minigame.getStartLocations().get(pos);
					ply.setStartTime(Calendar.getInstance().getTimeInMillis());
					minigameTeleport(ply, start);
					ply.setCheckpoint(start);
					if(minigame.getMaxScore() != 0 && minigame.getType().equals("dm") && !minigame.getScoreType().equals("none")){
						ply.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "승리까지 남은 스코어: " + minigame.getMaxScorePerPlayer(minigame.getPlayers().size()));
					}
				} 
				else{
					pos = 0;
					if(!minigame.getStartLocations().isEmpty()){
						start = minigame.getStartLocations().get(0);
						minigameTeleport(ply, start);
						ply.setCheckpoint(start);
						if(minigame.getMaxScore() != 0 && minigame.getType().equals("dm") && !minigame.getScoreType().equals("none")){
							ply.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "승리까지 남은 스코어: " + minigame.getMaxScorePerPlayer(minigame.getPlayers().size()));
						}
					}
					else {
						ply.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "시작 지점이 제대로 설정되지 않았습니다!");
						quitMinigame(ply, false);
					}
				}
			}
			else{
				int team = -1;
				if(minigame.getBlueTeam().contains(ply.getPlayer())){
					team = 1;
				}
				else if(minigame.getRedTeam().contains(ply.getPlayer())){
					team = 0;
				}
				if(!minigame.getStartLocationsRed().isEmpty() && !minigame.getStartLocationsBlue().isEmpty()){
					if(team == 0 && redpos < minigame.getStartLocationsRed().size()){
						start = minigame.getStartLocationsRed().get(redpos);
						redpos++;
					}
					else if(team == 1 && bluepos < minigame.getStartLocationsBlue().size()){
						start = minigame.getStartLocationsBlue().get(bluepos);
						bluepos++;
					}
					else if(team == 0 && !minigame.getStartLocationsRed().isEmpty()){
						redpos = 0;
						start = minigame.getStartLocationsRed().get(redpos);
						redpos++;
					}
					else if(team == 1 && !minigame.getStartLocationsBlue().isEmpty()){
						bluepos = 0;
						start = minigame.getStartLocationsBlue().get(bluepos);
						bluepos++;
					}
					else if(minigame.getStartLocationsBlue().isEmpty() || minigame.getStartLocationsRed().isEmpty()){
						ply.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "시작 지점이 제대로 설정되지 않았습니다!");
						quitMinigame(ply, false);
					}
				}
				else{
					if(pos <= minigame.getStartLocations().size()){
						start = minigame.getStartLocations().get(pos);
						minigameTeleport(ply, start);
						ply.setCheckpoint(start);
					} 
					else{
						pos = 1;
						if(!minigame.getStartLocations().isEmpty()){
							start = minigame.getStartLocations().get(0);
							minigameTeleport(ply, start);
							ply.setCheckpoint(start);
						}
						else {
							ply.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "시작 지점이 제대로 설정되지 않았습니다!");
							quitMinigame(ply, false);
						}
					}
				}
				
				if(start != null){
					minigameTeleport(ply, start);
					ply.setCheckpoint(start);
					if(minigame.getMaxScore() != 0 && !minigame.getScoreType().equals("none")){
						ply.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "승리까지 남은 스코어: " + minigame.getMaxScorePerPlayer(minigame.getPlayers().size()));
					}
				}
				
				if(minigame.getLives() > 0){
					ply.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "남은 목숨: " + minigame.getLives());
				}
			}
			pos++;
			if(!minigame.getPlayersLoadout(ply).getItems().isEmpty()){
				minigame.getPlayersLoadout(ply).equiptLoadout(ply);
			}
			ply.getPlayer().setScoreboard(minigame.getScoreboardManager());
			minigame.setScore(ply, 1);
			minigame.setScore(ply, 0);
		}
		
		if(minigame.hasPlayers()){
			if(minigame.getSpleefFloor1() != null && minigame.getSpleefFloor2() != null){
				minigame.addFloorDegenerator();
				minigame.getFloorDegenerator().startDegeneration();
			}
	
			if(minigame.hasRestoreBlocks()){
				for(RestoreBlock block : minigame.getRestoreBlocks().values()){
					minigame.getBlockRecorder().addBlock(block.getLocation().getBlock(), null);
				}
			}
			
			if(minigame.getTimer() > 0){
				minigame.setMinigameTimer(new MinigameTimer(minigame, minigame.getTimer()));
				mdata.sendMinigameMessage(minigame, MinigameUtils.convertTime(minigame.getTimer()) + " 초 남음.", null, null);
			}
		}
	}
	
	public void revertToCheckpoint(MinigamePlayer player) {
		
		RevertCheckpointEvent event = new RevertCheckpointEvent(player);
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		if(!event.isCancelled()){
			minigameTeleport(player, player.getCheckpoint());
			player.addRevert();
			player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "체크포인트로 되돌아왔습니다.");
		}
	}
	
	public void quitMinigame(MinigamePlayer player, boolean forced){
		Minigame minigame = player.getMinigame();

		QuitMinigameEvent event = new QuitMinigameEvent(player, minigame, forced);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if(!event.isCancelled()){
			if(!minigame.isSpectator(player)){
				player.setAllowTeleport(true);
				
				if(player.getPlayer().getVehicle() != null){
					Vehicle vehicle = (Vehicle) player.getPlayer().getVehicle();
					vehicle.eject();
				}
				
				player.getPlayer().closeInventory();
				
				MinigameUtils.removePlayerArrows(player);
				
				if(!forced){
					mdata.sendMinigameMessage(minigame, player.getName() + " 님이 " + minigame + " 에서 나가셨습니다.", "error", player);
				}
				else{
					mdata.sendMinigameMessage(minigame, player.getName() + " 님이 " + minigame + " 에서 나가지셨습니다.", "error", player);
				}
	
				minigame.removePlayersLoadout(player);
				
				final MinigamePlayer ply = player;
				if(!player.getPlayer().isDead()) {
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						
						public void run() {
							if(ply.getPlayer().isOnline() && !ply.getPlayer().isDead()){
								ply.restorePlayerData();
							}
						}
					});
				}

				player.removeMinigame();
				minigame.removePlayer(player);
				mdata.minigameType(minigame.getType()).quitMinigame(player, minigame, forced);
				
				for(PotionEffect potion : player.getPlayer().getActivePotionEffects()){
					player.getPlayer().removePotionEffect(potion.getType());
				}
				player.getPlayer().setFallDistance(0);
				
				final MinigamePlayer fplayer = player;
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
					public void run() {
						fplayer.getPlayer().setFireTicks(0);
						fplayer.getPlayer().setNoDamageTicks(60);
					}
				});
				
				player.clearFlags();
				player.resetDeaths();
				player.resetKills();
				player.resetScore();
				player.resetTime();
				player.resetReverts();
				player.removeCheckpoint();
				
				plugin.getLogger().info(player.getName() + " 님이 " + minigame + " 에서 나가셨습니다.");
				if(minigame.getPlayers().size() == 0){
					if(minigame.getMinigameTimer() != null){
						minigame.getMinigameTimer().stopTimer();
						minigame.setMinigameTimer(null);
					}
					
					if(minigame.getFloorDegenerator() != null){
						minigame.getFloorDegenerator().stopDegenerator();
					}
					
					if(minigame.getBlockRecorder().hasData()){
						minigame.getBlockRecorder().restoreBlocks();
						minigame.getBlockRecorder().restoreEntities();
					}
					
					if(minigame.getMpBets() != null){
						minigame.setMpBets(null);
					}
				}
				minigame.getScoreboardManager().resetScores(player.getPlayer());
				
				for(MinigamePlayer pl : minigame.getSpectators()){
					player.getPlayer().showPlayer(pl.getPlayer());
				}
				
				ply.setAllowTeleport(true);
				ply.setAllowGamemodeChange(true);
			}
			else{
				if(player.getPlayer().getVehicle() != null){
					Vehicle vehicle = (Vehicle) player.getPlayer().getVehicle();
					vehicle.eject();
				}
				player.getPlayer().setFallDistance(0);
				
				player.getPlayer().closeInventory();
				final Player fplayer = player.getPlayer();
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
					public void run() {
						fplayer.setFireTicks(0);
						fplayer.setNoDamageTicks(60);
					}
				});
				
				final MinigamePlayer ply = player;
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
					public void run() {
						if(ply.getPlayer().isOnline()){
							ply.restorePlayerData();
						}
					}
				});
				
				minigameTeleport(player, minigame.getQuitPosition());
				player.removeMinigame();
				minigame.removeSpectator(player);
				
				for(PotionEffect potion : player.getPlayer().getActivePotionEffects()){
					player.getPlayer().removePotionEffect(potion.getType());
				}
				
				for(MinigamePlayer pl : minigame.getPlayers()){
					pl.getPlayer().showPlayer(player.getPlayer());
				}
				
				ply.getPlayer().setFlying(false);
				ply.setAllowTeleport(true);
				ply.setAllowGamemodeChange(true);
				
				player.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "" + minigame + " 미니게임을 더이상 구경하지 않습니다.");
				mdata.sendMinigameMessage(minigame, player.getName() + " 님은 이제 " + minigame + " 미니게임을 더이상 구경하지 않습니다.", "error", player);
			}
		}
	}
	
	public void endMinigame(final MinigamePlayer player){
		Minigame minigame = player.getMinigame();
		
		EndMinigameEvent event = new EndMinigameEvent(player, minigame);
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		player.setEndTime(Calendar.getInstance().getTimeInMillis());
		if(!event.isCancelled()){
			if(player.getPlayer().getVehicle() != null){
				Vehicle vehicle = (Vehicle) player.getPlayer().getVehicle();
				vehicle.eject();
			}
			
			player.getPlayer().closeInventory();
			if(player.getPlayer().isOnline() && !player.getPlayer().isDead()){
				player.restorePlayerData();
			}
			
			MinigameUtils.removePlayerArrows(player);
			
			player.removeMinigame();
			minigame.removePlayer(player);
			minigame.removePlayersLoadout(player);
			player.getPlayer().setFallDistance(0);
			mdata.minigameType(minigame.getType()).endMinigame(player, minigame);
			
			for(PotionEffect potion : player.getPlayer().getActivePotionEffects()){
				player.getPlayer().removePotionEffect(potion.getType());
			}
			player.getPlayer().setFireTicks(0);
			player.getPlayer().setNoDamageTicks(60);
			
			player.clearFlags();
			
			if(plugin.getSQL() == null || plugin.getSQL().getSql() == null){
				player.resetDeaths();
				player.resetKills();
				player.resetScore();
				player.resetTime();
				player.resetReverts();
			}
			
			if(minigame.getMinigameTimer() != null){
				minigame.getMinigameTimer().stopTimer();
				minigame.setMinigameTimer(null);
			}
			
			if(minigame.getFloorDegenerator() != null && minigame.getPlayers().size() == 0){
				minigame.getFloorDegenerator().stopDegenerator();
			}
			
			if(minigame.getMpBets() != null && minigame.getPlayers().size() == 0){
				minigame.setMpBets(null);
			}
			
			if(minigame.getBlockRecorder().hasData()){
				if(!minigame.getType().equalsIgnoreCase("sp") || minigame.getPlayers().isEmpty()){
					minigame.getBlockRecorder().restoreBlocks();
					minigame.getBlockRecorder().restoreEntities();
				}
				else if(minigame.getPlayers().isEmpty()){
					minigame.getBlockRecorder().restoreBlocks(player);
					minigame.getBlockRecorder().restoreEntities(player);
				}
			}
			
			for(MinigamePlayer pl : minigame.getSpectators()){
				player.getPlayer().showPlayer(pl.getPlayer());
			}
			
			player.setAllowTeleport(true);
			player.setAllowGamemodeChange(true);

			plugin.getLogger().info(player.getName() + " 님이 " + minigame + " 를 완료하셨습니다.");
			minigame.getScoreboardManager().resetScores(player.getPlayer());
		}
	}
	
	public void endTeamMinigame(int teamnum, Minigame minigame){
		
		List<MinigamePlayer> losers = new ArrayList<MinigamePlayer>();
		List<MinigamePlayer> winners = new ArrayList<MinigamePlayer>();
		
		if(teamnum == 1){
			//Blue team
			for(OfflinePlayer ply : minigame.getRedTeam()){
				losers.add(getMinigamePlayer(ply.getName()));
			}
			for(OfflinePlayer ply : minigame.getBlueTeam()){
				winners.add(getMinigamePlayer(ply.getName()));
			}
		}
		else{
			//Red team
			for(OfflinePlayer ply : minigame.getRedTeam()){
				winners.add(getMinigamePlayer(ply.getName()));
			}
			for(OfflinePlayer ply : minigame.getBlueTeam()){
				losers.add(getMinigamePlayer(ply.getName()));
			}
		}
		

		EndTeamMinigameEvent event = new EndTeamMinigameEvent(losers, winners, minigame, teamnum);
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		if(!event.isCancelled()){
			if(event.getWinningTeamInt() == 1){
				if(plugin.getConfig().getBoolean("multiplayer.broadcastwin")){
					String score = "";
					if(minigame.getRedTeamScore() != 0 && minigame.getBlueTeamScore() != 0){
						score = ", " + ChatColor.BLUE + minigame.getBlueTeamScore() + ChatColor.WHITE + " 대 " + ChatColor.RED + minigame.getRedTeamScore();
					}
					plugin.getServer().broadcastMessage(ChatColor.GREEN + "[PMGO-L] " + ChatColor.BLUE + "블루 팀" + ChatColor.WHITE + " 이 " + minigame.getName() + "에서" + score + "으로 승리하셨습니다.");
				}
			}
			else{
				if(plugin.getConfig().getBoolean("multiplayer.broadcastwin")){
					String score = "";
					if(minigame.getRedTeamScore() != 0 && minigame.getBlueTeamScore() != 0){
						score = ", " + ChatColor.RED + minigame.getRedTeamScore() + ChatColor.WHITE + " 대 " + ChatColor.BLUE + minigame.getBlueTeamScore();
					}
					plugin.getServer().broadcastMessage(ChatColor.GREEN + "[PMGO-L] " + ChatColor.RED + "레드 팀" + ChatColor.WHITE + " 이 " + minigame.getName() + "에서" + score + "으로 승리하셨습니다.");
				}
			}
			
			minigame.setRedTeamScore(0);
			minigame.setBlueTeamScore(0);
			
			minigame.getMpTimer().setStartWaitTime(0);
			
			List<MinigamePlayer> winplayers = new ArrayList<MinigamePlayer>();
			winplayers.addAll(event.getWinnningPlayers());
	
			if(plugin.getSQL() != null){
				new SQLCompletionSaver(minigame.getName(), winplayers, mdata.minigameType(minigame.getType()));
			}
			
			if(minigame.getMpBets() != null){
				if(minigame.getMpBets().hasMoneyBets()){
					List<MinigamePlayer> plys = new ArrayList<MinigamePlayer>();
					plys.addAll(event.getWinnningPlayers());
					
					if(!plys.isEmpty()){
						double bets = minigame.getMpBets().claimMoneyBets() / (double) plys.size();
						BigDecimal roundBets = new BigDecimal(bets);
						roundBets = roundBets.setScale(2, BigDecimal.ROUND_HALF_UP);
						bets = roundBets.doubleValue();
						for(MinigamePlayer ply : plys){
							plugin.getEconomy().depositPlayer(ply.getName(), bets);
							ply.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "이겨서 " + bets + "원을 받으셨습니다.");
						}
					}
				}
				minigame.setMpBets(null);
			}
			
			if(!event.getLosingPlayers().isEmpty()){
				List<MinigamePlayer> loseplayers = new ArrayList<MinigamePlayer>();
				loseplayers.addAll(event.getLosingPlayers());
				for(int i = 0; i < loseplayers.size(); i++){
					if(loseplayers.get(i) instanceof MinigamePlayer){
						final MinigamePlayer p = loseplayers.get(i);
						
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
	
							public void run() {
								p.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "졌습니다! 운이 나쁘시네요.");
								quitMinigame(p, true);
							}
						});
					}
					else{
						loseplayers.remove(i);
					}
				}
				minigame.setMpTimer(null);
				for(MinigamePlayer pl : loseplayers){
					minigame.getPlayers().remove(pl);
				}
			}
			
			for(int i = 0; i < winplayers.size(); i++){
				if(winplayers.get(i) instanceof MinigamePlayer){
					final MinigamePlayer p = winplayers.get(i);
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						
						public void run() {
							endMinigame(p);
						}
					});
				}
				else{
					winplayers.remove(i);
				}
			}
			
			minigame.setMpTimer(null);
		}
	}
	
	@Deprecated
	public boolean playerInMinigame(Player player){
		return minigamePlayers.get(player.getName()).isInMinigame();
	}
	
	@Deprecated
	public List<Player> playersInMinigame(){
		List<Player> players = new ArrayList<Player>();
		for(Player player : plugin.getServer().getOnlinePlayers()){
			if(playerInMinigame(player)){
				players.add(player);
			}
		}
		return players;
	}
	
	public void addMinigamePlayer(Player player){
		minigamePlayers.put(player.getName(), new MinigamePlayer(player));
	}
	
	public void removeMinigamePlayer(Player player){
		if(minigamePlayers.containsKey(player.getName())){
			minigamePlayers.remove(player.getName());
		}
	}
	
	public MinigamePlayer getMinigamePlayer(Player player){
		return minigamePlayers.get(player.getName());
	}
	
	public MinigamePlayer getMinigamePlayer(String player){
		return minigamePlayers.get(player);
	}
	
	public Collection<MinigamePlayer> getAllMinigamePlayers(){
		return minigamePlayers.values();
	}
	
	public boolean hasMinigamePlayer(String name){
		return minigamePlayers.containsKey(name);
	}
	
	public void addOfflineMinigamePlayer(MinigamePlayer player){
		Location loc = null;
		if(player.getQuitPos() != null) {
			loc = player.getQuitPos();
		}
		else {
			loc = player.getMinigame().getQuitPosition();
		}
		offlineMinigamePlayers.put(player.getName(), new OfflineMinigamePlayer(player.getName(), player.getStoredItems(), player.getStoredArmour(), player.getFood(), player.getHealth(), player.getSaturation(), player.getLastGamemode(), loc));
	}
	
	public void addOfflineMinigamePlayer(OfflineMinigamePlayer player){
		offlineMinigamePlayers.put(player.getPlayer(), player);
	}
	
	public OfflineMinigamePlayer getOfflineMinigamePlayer(String name){
		return offlineMinigamePlayers.get(name);
	}
	
	public boolean hasOfflineMinigamePlayer(String name){
		return offlineMinigamePlayers.containsKey(name);
	}
	
	public void removeOfflineMinigamePlayer(String name){
		offlineMinigamePlayers.remove(name);
	}
	

//	public void storePlayerInventory(String player, ItemStack[] items, ItemStack[] armour, Integer health, Integer food, Float saturation){
//		OfflineMinigamePlayer oply = new OfflineMinigamePlayer(player, items, armour, food, health, saturation, GameMode.SURVIVAL, resPos.get(player));
//		offlineMinigamePlayers.put(player, oply);
//	}
	

	public List<String> checkRequiredFlags(MinigamePlayer player, String minigame){
		List<String> checkpoints = new ArrayList<String>();
		checkpoints.addAll(mdata.getMinigame(minigame).getFlags());
		List<String> pchecks = player.getFlags();
		
		if(!pchecks.isEmpty()){
			checkpoints.removeAll(pchecks);
		}
		
		return checkpoints;
	}
	
	
	public Configuration getInventorySaveConfig(){
		return invsave.getConfig();
	}
	
	public void saveInventoryConfig(){
		invsave.saveConfig();
	}
	
	public boolean onPartyMode(){
		return partyMode;
	}
	
	public void setPartyMode(boolean mode){
		partyMode = mode;
	}
	
	public void partyMode(MinigamePlayer player){
		if(onPartyMode()){
			Location loc = player.getPlayer().getLocation();
			Firework firework = (Firework) player.getPlayer().getWorld().spawnEntity(loc, EntityType.FIREWORK);
			FireworkMeta fwm = firework.getFireworkMeta();
			
			Random chance = new Random();
			Type type = Type.BALL_LARGE;
			if(chance.nextInt(100) < 50){
				type = Type.BALL;
			}
			
			Color col = Color.fromRGB(chance.nextInt(255), chance.nextInt(255), chance.nextInt(255));
			
			FireworkEffect effect = FireworkEffect.builder().with(type).withColor(col).flicker(chance.nextBoolean()).trail(chance.nextBoolean()).build();
			fwm.addEffect(effect);
			fwm.setPower(0);
			firework.setFireworkMeta(fwm);
		}
	}

	public List<String> getDeniedCommands() {
		return deniedCommands;
	}

	public void setDeniedCommands(List<String> deniedCommands) {
		this.deniedCommands = deniedCommands;
	}
	
	public void addDeniedCommand(String command){
		deniedCommands.add(command);
	}
	
	public void removeDeniedCommand(String command){
		deniedCommands.remove(command);
	}
	
	public void saveDeniedCommands(){
		plugin.getConfig().set("disabledCommands", deniedCommands);
		plugin.saveConfig();
	}
	
	public void loadDeniedCommands(){
		setDeniedCommands(plugin.getConfig().getStringList("disabledCommands"));
	}
	
	public boolean hasStoredPlayerCheckpoint(MinigamePlayer player){
		if(storedPlayerCheckpoints.containsKey(player.getName())){
			return true;
		}
		return false;
	}
	
	public StoredPlayerCheckpoints getPlayersStoredCheckpoints(MinigamePlayer player){
		return storedPlayerCheckpoints.get(player.getName());
	}
	
	public void addStoredPlayerCheckpoint(MinigamePlayer player, String minigame){
		StoredPlayerCheckpoints spc = new StoredPlayerCheckpoints(player.getName(),
				minigame,
				player.getCheckpoint(),
				Calendar.getInstance().getTimeInMillis() - player.getStartTime(),
				player.getDeaths(),
				player.getReverts());
		storedPlayerCheckpoints.put(player.getName(), spc);
	}
	
	public void addStoredPlayerCheckpoints(String name, StoredPlayerCheckpoints spc){
		storedPlayerCheckpoints.put(name, spc);
	}
	
	public void minigameTeleport(MinigamePlayer player, Location location){
		if(player.isInMinigame()){
			player.setAllowTeleport(true);
			player.getPlayer().teleport(location);
			player.setAllowTeleport(false);
		}
		else{
			player.getPlayer().teleport(location);
		}
	}
}
