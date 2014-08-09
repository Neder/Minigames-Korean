package com.pauldavdesign.mineauz.minigames;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class MultiplayerTimer{
	private int playerWaitTime;
	private int startWaitTime;
	private String minigame;
	private static Minigames plugin = Minigames.plugin;
	private PlayerData pdata = plugin.pdata;
	private MinigameData mdata = plugin.mdata;
	private boolean paused = false;
	private int taskID = -1;
	private List<Integer> timeMsg = new ArrayList<Integer>();
	
	public MultiplayerTimer(String mg){
		playerWaitTime = plugin.getConfig().getInt("multiplayer.waitforplayers");
		startWaitTime = plugin.getConfig().getInt("multiplayer.startcountdown");
		timeMsg.addAll(plugin.getConfig().getIntegerList("multiplayer.timerMessageInterval"));
		minigame = mg;
	}
	
	public void startTimer(){
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			public void run() {
				if(playerWaitTime != 0 && !paused){
					if(playerWaitTime == plugin.getConfig().getInt("multiplayer.waitforplayers")){
						sendPlayersMessage(minigame, ChatColor.GRAY + "�÷��̾ ��ٸ��ϴ�..");
						sendPlayersMessage(minigame, ChatColor.GRAY + "" + playerWaitTime + "�� ����.");
					}
					else if(timeMsg.contains(playerWaitTime)){
						sendPlayersMessage(minigame, ChatColor.GRAY + "" + playerWaitTime + "�� ����.");
					}
					playerWaitTime -= 1;
				}
				else if(playerWaitTime == 0 && startWaitTime !=0 && !paused){
					if(startWaitTime == plugin.getConfig().getInt("multiplayer.startcountdown")){
						sendPlayersMessage(minigame, ChatColor.GRAY + "�̴ϰ����� �����մϴ�..");
						sendPlayersMessage(minigame, ChatColor.GRAY + "" + startWaitTime + "�� ����.");
					}
					else if(timeMsg.contains(startWaitTime)){
						sendPlayersMessage(minigame, ChatColor.GRAY + "" + startWaitTime + "�� ����.");
					}
					startWaitTime -= 1;
				}
				else if(playerWaitTime == 0 && startWaitTime == 0){
					if(startWaitTime == 0 && playerWaitTime == 0){
						sendPlayersMessage(minigame, ChatColor.GREEN + "����!");
						reclearInventories(minigame);
						pdata.startMPMinigame(minigame);
					}
					Bukkit.getScheduler().cancelTask(taskID);
				}
			}
		}, 0, 20);
	}
	
	public void sendPlayersMessage(String minigame, String message){
		for(MinigamePlayer ply : mdata.getMinigame(minigame).getPlayers()){
			ply.sendMessage(message);
		}
	}
	
	public void reclearInventories(String minigame){
		for(MinigamePlayer ply : mdata.getMinigame(minigame).getPlayers()){
			ply.getPlayer().getInventory().clear();
		}
	}
	
	public int getPlayerWaitTimeLeft(){
		return playerWaitTime;
	}
	
	public int getStartWaitTimeLeft(){
		return startWaitTime;
	}
	
	public void setPlayerWaitTime(int time){
		playerWaitTime = time;
	}
	
	public void setStartWaitTime(int time){
		startWaitTime = time;
	}
	
	public void pauseTimer(){
		paused = true;
		for(MinigamePlayer ply : mdata.getMinigame(minigame).getPlayers()){
			ply.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "���� Ÿ�̸Ӱ� �����Ǿ����ϴ�.");
		}
	}
	
	public void pauseTimer(String reason){
		paused = true;
		for(MinigamePlayer ply : mdata.getMinigame(minigame).getPlayers()){
			ply.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "���� Ÿ�̸Ӱ� �����Ǿ����ϴ�. ����: " + reason);
		}
	}
	
	public void removeTimer(){
		if(taskID != -1){
			Bukkit.getScheduler().cancelTask(taskID);
		}
	}
	
	public void resumeTimer(){
		paused = false;
		for(MinigamePlayer ply : mdata.getMinigame(minigame).getPlayers()){
			ply.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "���� Ÿ�̸Ӱ� �ٽ� �����Ͽ����ϴ�.");
		}
	}
	
	public boolean isPaused(){
		return paused;
	}
}
