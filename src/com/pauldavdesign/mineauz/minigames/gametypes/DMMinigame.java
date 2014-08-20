package com.pauldavdesign.mineauz.minigames.gametypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.MinigameData;
import com.pauldavdesign.mineauz.minigames.MinigamePlayer;
import com.pauldavdesign.mineauz.minigames.MinigameSave;
import com.pauldavdesign.mineauz.minigames.Minigames;
import com.pauldavdesign.mineauz.minigames.PlayerData;
import com.pauldavdesign.mineauz.minigames.SQLCompletionSaver;
import com.pauldavdesign.mineauz.minigames.events.TimerExpireEvent;

public class DMMinigame extends MinigameType{
	private static Minigames plugin = Minigames.plugin;
	private PlayerData pdata = plugin.pdata;
	private MinigameData mdata = plugin.mdata;
	
	public DMMinigame() {
		setLabel("dm");
	}

	
	public boolean joinMinigame(MinigamePlayer player, Minigame mgm) {
		return callLMSJoin(player, mgm);
	}

	@SuppressWarnings("deprecation")
	
	public void quitMinigame(MinigamePlayer player, Minigame mgm, boolean forced) {
		if(mgm.getPlayers().size() == 0){
			if(mgm.getMpTimer() != null){
				mgm.getMpTimer().pauseTimer();
				mgm.getMpTimer().removeTimer();
				mgm.setMpTimer(null);
			}
			
			if(mgm.getMpBets() != null && (mgm.getMpTimer() == null || mgm.getMpTimer().getPlayerWaitTimeLeft() != 0)){
				if(mgm.getMpBets().getPlayersBet(player) != null){
					final ItemStack item = mgm.getMpBets().getPlayersBet(player).clone();
					final MinigamePlayer ply = player;
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						
						
						public void run() {
							ply.getPlayer().getInventory().addItem(item);
						}
					});
				}
				else if(mgm.getMpBets().getPlayersMoneyBet(player) != null){
					plugin.getEconomy().depositPlayer(player.getName(), mgm.getMpBets().getPlayersMoneyBet(player));
				}
			}
			mgm.setMpBets(null);
		}
		else if(mgm.getPlayers().size() == 1 && mgm.getMpTimer() != null && mgm.getMpTimer().getStartWaitTimeLeft() == 0 && !forced){
			pdata.endMinigame(mgm.getPlayers().get(0));
			
			if(mgm.getMpBets() != null){
				mgm.setMpBets(null);
			}
		}
		else if(mgm.getPlayers().size() < mgm.getMinPlayers() && mgm.getMpTimer() != null && mgm.getMpTimer().getStartWaitTimeLeft() != 0){
			mgm.getMpTimer().pauseTimer();
			mgm.getMpTimer().removeTimer();
			mgm.setMpTimer(null);
			for(MinigamePlayer pl : mgm.getPlayers()){
				pl.sendMessage(ChatColor.BLUE + "한 명의 플레이어를 더 기다립니다.");
			}
		}
		
		callGeneralQuit(player, mgm);

		if(mgm.getMpBets() != null && (mgm.getMpTimer() == null || mgm.getMpTimer().getPlayerWaitTimeLeft() != 0)){
			if(mgm.getMpBets().getPlayersBet(player) != null){
				final ItemStack item = mgm.getMpBets().getPlayersBet(player).clone();
				final MinigamePlayer ply = player;
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
					
					public void run() {
						ply.getPlayer().getInventory().addItem(item);
					}
				});
			}
			else if(mgm.getMpBets().getPlayersMoneyBet(player) != null){
				plugin.getEconomy().depositPlayer(player.getName(), mgm.getMpBets().getPlayersMoneyBet(player));
			}
			mgm.getMpBets().removePlayersBet(player);
		}
		player.getPlayer().updateInventory();
	}

	@SuppressWarnings("deprecation")
	
	public void endMinigame(MinigamePlayer player, Minigame mgm) {
		if(mgm.getMpBets() != null){
			if(mgm.getMpBets().hasBets()){
				player.getPlayer().getInventory().addItem(mgm.getMpBets().claimBets());
				mgm.setMpBets(null);
				player.getPlayer().updateInventory();
			}
			else{
				plugin.getEconomy().depositPlayer(player.getName(), mgm.getMpBets().claimMoneyBets());
				player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "미니게임에서 이겨 " + mgm.getMpBets().claimMoneyBets() + " 을(를) 버셨습니다!");
				mgm.setMpBets(null);
			}
		}
		//pdata.saveItems(player);
		pdata.saveInventoryConfig();
		
		boolean hascompleted = false;
		Configuration completion = null;
		
		player.sendMessage(ChatColor.GREEN + "" + mgm + " 미니게임에서 승리하셨습니다! 축하합니다!");
		if(plugin.getConfig().getBoolean("multiplayer.broadcastwin")){
			String score = "";
			if(player.getScore() != 0) {
				score = "스코어: " + player.getScore();
				plugin.getServer().broadcastMessage(ChatColor.GREEN + "[PMGO-L] " + ChatColor.WHITE + player.getName() + " 님이 " + mgm.getName() + " 미니게임에서 " + score + "점의 점수로 승리하였습니다!");
			} else {
				plugin.getServer().broadcastMessage(ChatColor.GREEN + "[PMGO-L] " + ChatColor.WHITE + player.getName() + " 님이 " + mgm.getName() + " 미니게임에서 승리하였습니다!");
			}
		}
		
		if(mgm.getEndPosition() != null){
			if(!player.getPlayer().isDead()){
//				player.teleport(mgm.getEndPosition());
				pdata.minigameTeleport(player, mgm.getEndPosition());
			}
			else{
//				pdata.addRespawnPosition(player.getPlayer(), mgm.getEndPosition());
				player.setRequiredQuit(true);
				player.setQuitPos(mgm.getEndPosition());
			}
		}
		
		if(mgm.getPlayers().isEmpty()){
			mgm.getMpTimer().setStartWaitTime(0);
			
			mgm.setMpTimer(null);
			for(MinigamePlayer pl : mgm.getPlayers()){
				mgm.getPlayers().remove(pl);
			}
		}
		else{
			mgm.getMpTimer().setStartWaitTime(0);
			List<MinigamePlayer> players = new ArrayList<MinigamePlayer>();
			players.addAll(mgm.getPlayers());
			for(int i = 0; i < players.size(); i++){
				if(players.get(i) instanceof Player){
					MinigamePlayer p = players.get(i);
					if(!p.getName().equals(player.getName())){
						p.sendMessage(ChatColor.RED + "미니게임에서 졌습니다. 안타깝네요..");
						pdata.quitMinigame(p, false);
					}
				}
				else{
					players.remove(i);
				}
			}
			mgm.setMpTimer(null);
			for(MinigamePlayer pl : players){
				mgm.getPlayers().remove(pl);
			}
		}
		
		if(plugin.getSQL() == null){
			completion = mdata.getConfigurationFile("completion");
			hascompleted = completion.getStringList(mgm.getName()).contains(player.getName());
			
			if(!completion.getStringList(mgm.getName()).contains(player.getName())){
				List<String> completionlist = completion.getStringList(mgm.getName());
				completionlist.add(player.getName());
				completion.set(mgm.getName(), completionlist);
				MinigameSave completionsave = new MinigameSave("completion");
				completionsave.getConfig().set(mgm.getName(), completionlist);
				completionsave.saveConfig();
			}
			
			issuePlayerRewards(player, mgm, hascompleted);
		}
		else{
			new SQLCompletionSaver(mgm.getName(), player, this);
		}
	}
	
	/*----------------*/
	/*-----EVENTS-----*/
	/*----------------*/
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerRespawn(PlayerRespawnEvent event){
		final MinigamePlayer ply = pdata.getMinigamePlayer(event.getPlayer());
		if(ply.isInMinigame() && ply.getMinigame().getType().equals("dm")){
			Minigame mg = ply.getMinigame();
			List<Location> starts = new ArrayList<Location>();
			
			starts.addAll(mg.getStartLocations());
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
			MinigamePlayer player = null;
			int score = 0;
			for(MinigamePlayer ply : event.getMinigame().getPlayers()){
				if(ply.getKills() > score){
					player = ply;
					score = ply.getKills();
				}
				else if(ply.getKills() == score){
					if(player != null && ply.getDeaths() < player.getDeaths()){
						player = ply;
					}
					else if(player == null){
						player = ply;
					}
				}
			}
			List<MinigamePlayer> players = new ArrayList<MinigamePlayer>();
			players.addAll(event.getMinigame().getPlayers());
			
			for(MinigamePlayer ply : players){
				if(ply != player){
					pdata.quitMinigame(ply, true);
				}
			}
			pdata.endMinigame(player);
		}
	}
}
