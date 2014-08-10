package com.pauldavdesign.mineauz.minigames;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

import com.pauldavdesign.mineauz.minigames.events.RevertCheckpointEvent;

public class Events implements Listener{
	private static Minigames plugin = Minigames.plugin;
	private PlayerData pdata = plugin.pdata;
	private MinigameData mdata = plugin.mdata;
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDeath(PlayerDeathEvent event){
		final MinigamePlayer ply = pdata.getMinigamePlayer(event.getEntity().getPlayer());
		if(ply == null) return;
		if(ply.isInMinigame()){
			Minigame mgm = ply.getMinigame();
			if(!mgm.hasDeathDrops()){
				event.getDrops().clear();
			}
			String msg = "";
			msg = event.getDeathMessage();
			event.setDeathMessage(null);
			event.setKeepLevel(true);
			event.setDroppedExp(0);
			
			ply.addDeath();
			
			pdata.partyMode(ply);
			
			if(ply.getPlayer().getKiller() != null){
				MinigamePlayer killer = pdata.getMinigamePlayer(ply.getPlayer().getKiller());
//				pdata.addPlayerKill(killer);
				killer.addKill();
			}
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					MinigameUtils.removePlayerArrows(ply);
				}
			});
			
			if(!msg.equals("")){
				mdata.sendMinigameMessage(mgm, msg, "error", null);
			}
			if(mgm.getLives() > 0 && mgm.getLives() <= ply.getDeaths()){
				ply.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "�̴ϰ��ӿ��� ���������ϴ�. ���� ���ڽó׿�.");
				ply.getPlayer().setHealth(2);
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
					public void run() {
						pdata.quitMinigame(ply, false);
					}
				});
			}
			else if(mgm.getLives() > 0){
				ply.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "���� ���: " + (mgm.getLives() - ply.getDeaths()));
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	private void playerRespawn(PlayerRespawnEvent event){
//		if(pdata.hasRespawnPosition(event.getPlayer())){
//			event.setRespawnLocation(pdata.getRespawnPosition(event.getPlayer()));
//			pdata.removeRespawnPosition(event.getPlayer());
//			pdata.getMinigamePlayer(event.getPlayer()).restorePlayerData();
//			//pdata.restorePlayerData(event.getPlayer());
//		}
	}
	
	@EventHandler
	public void playerDropItem(PlayerDropItemEvent event){
		MinigamePlayer ply = pdata.getMinigamePlayer(event.getPlayer());
		if(ply.isInMinigame()){
			Minigame mgm = pdata.getMinigamePlayer(event.getPlayer()).getMinigame();
			if(!mgm.hasItemDrops() || 
					mgm.isSpectator(pdata.getMinigamePlayer(event.getPlayer()))){
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void itemPickup(PlayerPickupItemEvent event){
		MinigamePlayer ply = pdata.getMinigamePlayer(event.getPlayer());
		if(ply.isInMinigame()){
			Minigame mgm = ply.getMinigame();
			if(!mgm.hasItemPickup() || 
					mgm.isSpectator(ply)){
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent event){
		MinigamePlayer ply = pdata.getMinigamePlayer(event.getPlayer());
		if(ply.isInMinigame()){
			//pdata.addDCPlayer(event.getPlayer(), mgm.getQuitPosition());
			pdata.addOfflineMinigamePlayer(pdata.getMinigamePlayer(event.getPlayer()));
			pdata.quitMinigame(pdata.getMinigamePlayer(event.getPlayer()), false);
		}
		
		pdata.removeMinigamePlayer(event.getPlayer());
		
		if(Bukkit.getServer().getOnlinePlayers().length == 1){
			for(String mgm : mdata.getAllMinigames().keySet()){
				if(mdata.getMinigame(mgm).getType().equals("th")){
					if(mdata.getMinigame(mgm).getThTimer() != null){
						mdata.getMinigame(mgm).getThTimer().pauseTimer(true);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerConnect(PlayerJoinEvent event){
		pdata.addMinigamePlayer(event.getPlayer());
		if(event.getPlayer().isOp() && plugin.getConfig().getBoolean("updateChecker")){
			long nextCheck = plugin.getLastUpdateCheck() + 86400000;
			if(nextCheck <= Calendar.getInstance().getTimeInMillis()){
				UpdateChecker check = new UpdateChecker(event.getPlayer());
				check.start();
				plugin.setLastUpdateCheck(Calendar.getInstance().getTimeInMillis());
			}
		}
		//if(pdata.hasDCPlayer(event.getPlayer())){
		if(pdata.hasOfflineMinigamePlayer(event.getPlayer().getName())){
			final Player ply = event.getPlayer();
			OfflineMinigamePlayer oply = pdata.getOfflineMinigamePlayer(event.getPlayer().getName());
			//Location loc = pdata.getDCPlayer(event.getPlayer());
			Location loc = oply.getLoginLocation();
			oply.restoreOfflineMinigamePlayer();
			pdata.removeOfflineMinigamePlayer(event.getPlayer().getName());
			
			//pdata.removeDCPlayer(event.getPlayer());
			/*plugin.getLogger().info("--------------------------DEBUG--------------------------");
			if(ply != null){
				plugin.getLogger().info("Player: " + ply.getName());
				if(loc == null){
					plugin.getLogger().info("Location: NO WHERE TO TELEPORT, ITS NULL! (Teleported them to spawn for safety!)");
					loc = plugin.getServer().getWorld("world").getSpawnLocation();
				}
				else
					plugin.getLogger().info("Location: X:" + loc.getBlockX() + ", Y:" + loc.getBlockY() + ", Z:" + loc.getBlockZ() + ", world:" + loc.getWorld().getName());
			}
			else
				plugin.getLogger().info("Player: OMG ITS NULL!!! D:");*/
			
			final Location floc = loc;
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
				public void run() {
					ply.teleport(floc);
					ply.setFireTicks(0);
				}
			}, 5L);
			
			final MinigamePlayer fply = pdata.getMinigamePlayer(event.getPlayer());
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
				public void run() {
					fply.restorePlayerData();
				}
			});
		}
		
//		final Player fply = event.getPlayer();
//		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//			
//			@Override
//			public void run() {
//				if(pdata.playerHasStoredItems(fply) && fply.isOnline()){
//					pdata.restorePlayerData(fply);
//				}
//			}
//		});
		
		if(Bukkit.getServer().getOnlinePlayers().length == 1){
			for(String mgm : mdata.getAllMinigames().keySet()){
				if(mdata.getMinigame(mgm).getType().equals("th")){
					if(mdata.getMinigame(mgm).getThTimer() != null){
						mdata.getMinigame(mgm).getThTimer().pauseTimer(false);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void playerInterract(PlayerInteractEvent event){
		MinigamePlayer ply = pdata.getMinigamePlayer(event.getPlayer());
		if(ply == null) return;
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block cblock = event.getClickedBlock();
			if(cblock.getState() instanceof Chest){
				if(mdata.hasTreasureHuntLocations()){
					for(String minigame : mdata.getAllTreasureHuntLocation()){
						if(mdata.getMinigame(minigame).getThTimer() != null){
							if(mdata.getMinigame(minigame).getThTimer().getTreasureFound() == false && mdata.getMinigame(minigame).getThTimer().getChestInWorld()){
								int x1 = mdata.getTreasureHuntLocation(minigame).getBlockX();
								int x2 = cblock.getLocation().getBlockX();
								int y1 = mdata.getTreasureHuntLocation(minigame).getBlockY();
								int y2 = cblock.getLocation().getBlockY();
								int z1 = mdata.getTreasureHuntLocation(minigame).getBlockZ();
								int z2 = cblock.getLocation().getBlockZ();
								if(x2 == x1 && y2 == y1 && z2 == z1){
									plugin.getServer().broadcast(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + minigame + "������" + event.getPlayer().getName() + " ���� ã���̽��ϴ�!", "minigame.treasure.announce");
									event.setCancelled(true);
									Chest chest = (Chest) cblock.getState();
									event.getPlayer().openInventory(chest.getInventory());
									mdata.getMinigame(minigame).getThTimer().setTreasureFound(true);
									mdata.getMinigame(minigame).getThTimer().setTimeLeft(5);
								}
							}
						}
					}
				}
			}
		}
		else if(event.getAction() == Action.LEFT_CLICK_BLOCK && event.getPlayer().hasPermission("minigame.sign.use.details")){
			Block cblock = event.getClickedBlock();
			if(cblock.getState() instanceof Sign){
				Sign sign = (Sign) cblock.getState();
				if(sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_BLUE + "[PMGO-L]")){
					if((sign.getLine(1).equalsIgnoreCase(ChatColor.GREEN + "����") || sign.getLine(1).equalsIgnoreCase(ChatColor.GREEN + "����")) && !ply.isInMinigame()){
						Minigame mgm = mdata.getMinigame(sign.getLine(2));
						if(mgm != null && (!mgm.getUsePermissions() || event.getPlayer().hasPermission("minigame.join." + mgm.getName().toLowerCase()))){
							if(!mgm.isEnabled()){
								event.getPlayer().sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "�� �̴ϰ����� ���� Ȱ��ȭ ���� �ʾҽ��ϴ�.");
							}
							else{
								event.getPlayer().sendMessage(ChatColor.GREEN + "------------------�̴ϰ��� ����------------------");
								String status = ChatColor.AQUA + "���� ����: ";
								if(!mgm.hasPlayers()){
									status += ChatColor.GREEN + "�������";
								}
								else if(mgm.getMpTimer() == null || mgm.getMpTimer().getPlayerWaitTimeLeft() > 0){
									status += ChatColor.GREEN + "�÷��̾ ��ٸ�����";
								}
								else{
									status += ChatColor.RED + "������";
								}
								
								if(!mgm.getType().equals("sp"))
									event.getPlayer().sendMessage(status);
								

								if(mgm.canLateJoin())
									event.getPlayer().sendMessage(ChatColor.AQUA + "������ �� ����: " + ChatColor.WHITE + "����");
								
								if(mgm.getMinigameTimer() != null){
									event.getPlayer().sendMessage(ChatColor.AQUA + "���� �ð�: " + MinigameUtils.convertTime(mgm.getMinigameTimer().getTimeLeft()));
								}
								
								if(mgm.getType().equals("teamdm")){
									event.getPlayer().sendMessage(ChatColor.AQUA + "���ھ�: " + ChatColor.RED + mgm.getRedTeamScore() + ChatColor.WHITE + " �� " + ChatColor.BLUE + mgm.getBlueTeamScore());
								}
								
								String playerCount = ChatColor.AQUA + "�÷��̾�  ��: " + ChatColor.GRAY;
								String players = ChatColor.AQUA + "�÷��̾��: ";
								
								if(mgm.hasPlayers()){
									playerCount += mgm.getPlayers().size() ;
									if(!mgm.getType().equals("sp")){
										playerCount += "/" + mgm.getMaxPlayers();
									}
									
									List<String> plyList = new ArrayList<String>();
									for(MinigamePlayer pl : mgm.getPlayers()){
										plyList.add(pl.getName());
									}
									players += MinigameUtils.listToString(plyList);
								}
								else{
									playerCount += "0";
									
									if(!mgm.getType().equals("sp")){
										playerCount += "/" + mgm.getMaxPlayers();
									}
									
									players += ChatColor.GRAY + "����";
								}
								
								event.getPlayer().sendMessage(playerCount);
								event.getPlayer().sendMessage(players);
							}
						}
						else if(mgm == null){
							event.getPlayer().sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "�� �̴ϰ����� �������� �ʽ��ϴ�!");
						}
						else if(mgm.getUsePermissions()){
							event.getPlayer().sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "minigame.join." + mgm.getName().toLowerCase() + " �޹̼��� �����ϴ�!");
						}
					}
				}	
			}
		}
		
		//Spectator disables:
		if(ply.isInMinigame() && pdata.getMinigamePlayer(event.getPlayer()).getMinigame().isSpectator(pdata.getMinigamePlayer(event.getPlayer()))){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onTeleportAway(PlayerTeleportEvent event){
		MinigamePlayer ply = pdata.getMinigamePlayer(event.getPlayer());
		if(ply == null) return;
		if(event.getCause() == TeleportCause.COMMAND || event.getCause() == TeleportCause.PLUGIN || event.getCause() == TeleportCause.ENDER_PEARL){
			if(ply.isInMinigame() && !ply.getAllowTeleport()){
				Location from = event.getFrom();
				Location to = event.getTo();
				if(from.getWorld() != to.getWorld() || from.distance(to) > 2){
					event.setCancelled(true);
					event.getPlayer().sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "�̴ϰ��� �ȿ����� �ڷ���Ʈ �� �� �����ϴ�!");
				}
			}
		}
	}
	
	@EventHandler
	public void onGMChange(PlayerGameModeChangeEvent event){
		MinigamePlayer ply = pdata.getMinigamePlayer(event.getPlayer());
		if(ply.isInMinigame() && !ply.getAllowGamemodeChange()){
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "�̴ϰ��� �ȿ����� ���Ӹ�带 ������ �� �����ϴ�!");
		}
	}
	
	@EventHandler
	public void onFlyToggle(PlayerToggleFlightEvent event){
		MinigamePlayer ply = pdata.getMinigamePlayer(event.getPlayer());
		if(ply.isInMinigame() && (!ply.getMinigame().isSpectator(ply) || !ply.getMinigame().canSpectateFly())){
			event.setCancelled(true);
			pdata.quitMinigame(ply, true);
			event.getPlayer().sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "�̴ϰ��� �߿��� �� �� �����ϴ�!");
		}
	}
	
	@EventHandler
	public void playerRevert(RevertCheckpointEvent event){
		if(event.getMinigamePlayer().isInMinigame() && (event.getMinigamePlayer().getMinigame().getType().equals("lms") || event.getMinigamePlayer().getMinigame().getType().equals("dm") || event.getMinigamePlayer().getMinigame().getType().equals("teamdm"))){
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + event.getMinigamePlayer().getMinigame().getType() + " �� �÷��� ���϶��� üũ����Ʈ�� ���ư� �� �����ϴ�!");
		}
	}
	
	@EventHandler
	private void commandExecute(PlayerCommandPreprocessEvent event){
		MinigamePlayer ply = pdata.getMinigamePlayer(event.getPlayer());
		if(ply.isInMinigame()){
			for(String comd : pdata.getDeniedCommands()){
				if(event.getMessage().contains(comd)){
					event.setCancelled(true);
					event.getPlayer().sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "�̴ϰ����� �÷��� ���϶��� �� ��ɾ ����� �� �����ϴ�!");
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void paintballHit(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Snowball){
			MinigamePlayer ply = pdata.getMinigamePlayer((Player) event.getEntity());
			if(ply == null) return;
			Snowball sb = (Snowball) event.getDamager();
			if(ply.isInMinigame() && ply.getMinigame().hasPaintBallMode()){
				if(sb.getShooter() instanceof Player){
					MinigamePlayer shooter = pdata.getMinigamePlayer((Player) sb.getShooter());
					Minigame mgm = ply.getMinigame();
					if(shooter == null) return;
					if(shooter.isInMinigame() && shooter.getMinigame().equals(ply.getMinigame())){
						int plyTeam = -1;
						int atcTeam = -2;
						if(mgm.getType().equals("teamdm")){
							plyTeam = 0;
							if(mgm.getBlueTeam().contains(ply.getPlayer())){
								plyTeam = 1;
							}
							atcTeam = 0;
							if(mgm.getBlueTeam().contains(shooter.getPlayer())){
								atcTeam = 1;
							}
						}
						if(plyTeam != atcTeam){
							int damage = mgm.getPaintBallDamage();
							event.setDamage(damage);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	private void playerShoot(ProjectileLaunchEvent event){
		if(event.getEntityType() == EntityType.SNOWBALL){
			Snowball snowball = (Snowball) event.getEntity();
			if(snowball.getShooter() != null && snowball.getShooter() instanceof Player){
				MinigamePlayer ply = pdata.getMinigamePlayer((Player) snowball.getShooter());
				if(ply == null) return;
				if(ply.isInMinigame() && ply.getMinigame().hasUnlimitedAmmo()){
					ply.getPlayer().getInventory().addItem(new ItemStack(Material.SNOW_BALL));
				}
			}
		}
		else if(event.getEntityType() == EntityType.EGG){
			Egg egg = (Egg) event.getEntity();
			if(egg.getShooter() != null && egg.getShooter() instanceof Player){
				MinigamePlayer ply = pdata.getMinigamePlayer((Player) egg.getShooter());
				if(shooter == null) return;
				if(ply.isInMinigame() && ply.getMinigame().hasUnlimitedAmmo()){
					ply.getPlayer().getInventory().addItem(new ItemStack(Material.EGG));
				}
			}
		}
	}
	
	@EventHandler
	private void playerHurt(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			MinigamePlayer ply = pdata.getMinigamePlayer((Player) event.getEntity());
			if(ply == null) return;
			if(ply.isInMinigame()){
				Minigame mgm = ply.getMinigame();
				if(mgm.isSpectator(ply)){
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	private void spectatorAttack(EntityDamageByEntityEvent event){
		if(event.getDamager() instanceof Player){
			MinigamePlayer ply = pdata.getMinigamePlayer((Player) event.getDamager());
			if(ply == null) return;
			if(ply.isInMinigame() && ply.getMinigame().isSpectator(ply)){
				event.setCancelled(true);
			}
		}
	}
}
