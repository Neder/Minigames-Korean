package com.pauldavdesign.mineauz.minigames.gametypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.Listener;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.MinigameData;
import com.pauldavdesign.mineauz.minigames.MinigamePlayer;
import com.pauldavdesign.mineauz.minigames.Minigames;
import com.pauldavdesign.mineauz.minigames.MultiplayerTimer;
import com.pauldavdesign.mineauz.minigames.PlayerData;

public abstract class MinigameType implements Listener{
	private static Minigames plugin;
	private PlayerData pdata;
	private MinigameData mdata;
	
	protected MinigameType(){
		plugin = Minigames.plugin;
		pdata = plugin.pdata;
		mdata = plugin.mdata;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public String typeLabel;
	
	public void setLabel(String label){
		typeLabel = label;
	}
	
	public String getLabel(){
		return typeLabel;
	}
	
	public abstract boolean joinMinigame(MinigamePlayer player, Minigame mgm);
	
	public abstract void quitMinigame(MinigamePlayer player, Minigame mgm, boolean forced);
	
	public abstract void endMinigame(MinigamePlayer player, Minigame mgm);
	
	public void callGeneralQuit(final MinigamePlayer player, final Minigame minigame){
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			
			public void run() {
				if(!player.getPlayer().isDead()){
					//player.teleport(minigame.getQuitPosition());
					pdata.minigameTeleport(player, minigame.getQuitPosition());
				}
				else{
					pdata.addRespawnPosition(player.getName(), minigame.getQuitPosition());
				}
			}
		});
	}
	
	public boolean callLMSJoin(MinigamePlayer player, Minigame mgm){
		if(mgm.getQuitPosition() != null && mgm.isEnabled() && mgm.getEndPosition() != null && mgm.getLobbyPosition() != null){
			
			String gametype = mgm.getType();
			if(gametype.equals("dm"))
				gametype = "deathmatch";
			if(mgm.getScoreType().equals("ctf"))
				gametype += " CTF";
			
			Location lobby = mgm.getLobbyPosition();
			if(/*!mgm.getPlayers().isEmpty() && */mdata.getMinigame(mgm.getName()).getPlayers().size() < mgm.getMaxPlayers()){
				if(mgm.canLateJoin() || mgm.getMpTimer() == null || mgm.getMpTimer().getPlayerWaitTimeLeft() != 0){
					//pdata.storePlayerData(player, mgm.getDefaultGamemode());
					player.storePlayerData();
					//pdata.addPlayerMinigame(player, mgm);
					player.setMinigame(mgm);
					
					mgm.addPlayer(player);
					if(mgm.getMpTimer() == null || mgm.getMpTimer().getStartWaitTimeLeft() != 0){
//						player.teleport(lobby);
						pdata.minigameTeleport(player, lobby);
						if(mgm.getMpTimer() == null && mgm.getPlayers().size() == mgm.getMaxPlayers()){
							mgm.setMpTimer(new MultiplayerTimer(mgm));
							mgm.getMpTimer().startTimer();
							mgm.getMpTimer().setPlayerWaitTime(0);
							mdata.sendMinigameMessage(mgm, "�̴ϰ����� �� á���ϴ�. ��ٸ��� �ʰ� �ٷ� �����մϴ�.", "info", null);
						}
					}
					else{
						player.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "5���Ŀ� �����մϴ�...");
						//player.teleport(lobby);
						pdata.minigameTeleport(player, lobby);
						final MinigamePlayer fply = player;
						final Minigame fmgm = mgm;
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							
							
							public void run() {
								if(fply.isInMinigame()){
									List<Location> locs = new ArrayList<Location>();
									locs.addAll(fmgm.getStartLocations());
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
					if(mgm.getType() == "dm") {
						player.sendMessage(ChatColor.GREEN + "������ġ �̴ϰ��ӿ� ���̽��ϴ�. /quit ���� ���� �� �ֽ��ϴ�.");
					} else if(mgm.getType() == "race") {
						player.sendMessage(ChatColor.GREEN + "���̽� �̴ϰ��ӿ� ���̽��ϴ�. /quit ���� ���� �� �ֽ��ϴ�.");
					}
				
					if(mgm.getMpTimer() == null && mgm.getPlayers().size() == mgm.getMinPlayers()){
						mgm.setMpTimer(new MultiplayerTimer(mgm));
						mgm.getMpTimer().startTimer();
						if(mgm.getPlayers().size() == mgm.getMaxPlayers()){
							mgm.setMpTimer(new MultiplayerTimer(mgm));
							mgm.getMpTimer().startTimer();
							mgm.getMpTimer().setPlayerWaitTime(0);
							mdata.sendMinigameMessage(mgm, "�̴ϰ����� �� á���ϴ�. ��ٸ��� �ʰ� �ٷ� �����մϴ�.", "info", null);
						}
					}
					else{
						int neededPlayers = mgm.getMinPlayers() - mgm.getPlayers().size();
						if(neededPlayers == 1){
							player.sendMessage(ChatColor.BLUE + "1 ���� �÷��̾ �� ��ٸ��ϴ�..");
						}
						else if(neededPlayers > 1){
							player.sendMessage(ChatColor.BLUE + "" + neededPlayers + " ���� �÷��̾ �� ��ٸ��ϴ�..");
						}
					}
					return true;
				}
				else if(mgm.getMpTimer().getPlayerWaitTimeLeft() == 0){
					player.sendMessage(ChatColor.RED + "�� �̴ϰ����� �̹� �����Ͽ����ϴ�. ���߿� �ٽ� �õ��ϼ���.");
					return false;
				}
			}
//			else if(mgm.getPlayers().isEmpty()){
//				player.teleport(lobby);
//				player.sendMessage(ChatColor.GREEN + "You have started a " + gametype + " minigame, type /minigame quit to exit.");
//				
//				int neededPlayers = mgm.getMinPlayers() - 1;
//				
//				if(neededPlayers > 0){
//					player.sendMessage(ChatColor.BLUE + "Waiting for " + neededPlayers + " more players.");
//				}
//				else
//				{
//					mgm.setMpTimer(new MultiplayerTimer(mgm.getName()));
//					mgm.getMpTimer().startTimer();
//				}
//				return true;
//			}
			else if(mgm.getPlayers().size() == mgm.getMaxPlayers()){
				player.sendMessage(ChatColor.RED + "�˼��մϴ�. �� �̴ϰ����� �̹� �� á���ϴ�.");
			}
		}
		else if(mgm.getQuitPosition() == null){
			player.sendMessage(ChatColor.RED + "�� �̴ϰ����� ���� �غ���� �ʾҽ��ϴ�!(�й��� �̵� ���� ����)");
		}
		else if(mgm.getEndPosition() == null){
			player.sendMessage(ChatColor.RED + "�� �̴ϰ����� ���� �غ���� �ʾҽ��ϴ�!(�¸��� �̵� ���� ����)");
		}
		else if(mgm.getLobbyPosition() == null){
			player.sendMessage(ChatColor.RED + "�� �̴ϰ����� ���� �غ���� �ʾҽ��ϴ�!(�κ� ����)");
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public void issuePlayerRewards(MinigamePlayer player, Minigame save, boolean hascompleted){
		if(save.getRewardItem() != null && !hascompleted){
			player.getPlayer().getInventory().addItem(save.getRewardItem());
		}
		else if(save.getSecondaryRewardItem() != null && hascompleted){
			player.getPlayer().getInventory().addItem(save.getSecondaryRewardItem());
		}
		player.getPlayer().updateInventory();
		
		if(Minigames.plugin.hasEconomy()){
			if(save.getRewardPrice() != 0 && !hascompleted){
				Minigames.plugin.getEconomy().depositPlayer(player.getName(), save.getRewardPrice());
				player.sendMessage(ChatColor.GREEN + "[PMGO-L] " + ChatColor.WHITE + String.format("����� ", save.getRewardPrice() + "���� �����̽��ϴ�."));
			}
			else if(save.getSecondaryRewardPrice() != 0 && hascompleted){
				Minigames.plugin.getEconomy().depositPlayer(player.getName(), save.getSecondaryRewardPrice());
				player.sendMessage(ChatColor.GREEN + "[PMGO-L] " + ChatColor.WHITE + String.format("����� ", save.getSecondaryRewardPrice() + "���� �����̽��ϴ�."));
			}
		}
	}
}
