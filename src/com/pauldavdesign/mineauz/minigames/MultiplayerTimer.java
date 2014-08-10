package com.pauldavdesign.mineauz.minigames;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class MultiplayerTimer{
	private int playerWaitTime;
	private int startWaitTime;
	private Minigame minigame;
	private static Minigames plugin = Minigames.plugin;
	private PlayerData pdata = plugin.pdata;
	private boolean paused = false;
	private int taskID = -1;
	private List<Integer> timeMsg = new ArrayList<Integer>();
	
	public MultiplayerTimer(Minigame mg){
		playerWaitTime = plugin.getConfig().getInt("multiplayer.waitforplayers");
		if(minigame.getStartWaitTime() == 0) {
			startWaitTime = plugin.getConfig().getInt("multiplayer.startcountdown");
		}
		else {
			startWaitTime = minigame.getStartWaitTime();
		}
		timeMsg.addAll(plugin.getConfig().getIntegerList("multiplayer.timerMessageInterval"));
	}
	
	public void startTimer(){
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			public void run() {
				if(playerWaitTime != 0 && !paused){
					if(playerWaitTime == plugin.getConfig().getInt("multiplayer.waitforplayers")){
						sendPlayersMessage(ChatColor.GRAY + "�÷��̾ ��ٸ��ϴ�..");
						sendPlayersMessage(ChatColor.GRAY + "" + playerWaitTime + "�� ����.");
					}
					else if(timeMsg.contains(playerWaitTime)){
						sendPlayersMessage(ChatColor.GRAY + "" + playerWaitTime + "�� ����.");
					}
					playerWaitTime -= 1;
				}
				else if(playerWaitTime == 0 && startWaitTime !=0 && !paused){
					if(startWaitTime == plugin.getConfig().getInt("multiplayer.startcountdown")){
						sendPlayersMessage(ChatColor.GRAY + "�̴ϰ����� �����մϴ�..");
						sendPlayersMessage(ChatColor.GRAY + "" + startWaitTime + "�� ����.");
					}
					else if(timeMsg.contains(startWaitTime)){
						sendPlayersMessage(ChatColor.GRAY + "" + startWaitTime + "�� ����.");
					}
					startWaitTime -= 1;
				}
				else if(playerWaitTime == 0 && startWaitTime == 0){
					if(startWaitTime == 0 && playerWaitTime == 0){
						sendPlayersMessage(ChatColor.GREEN + "����!");
						reclearInventories(minigame);
						pdata.startMPMinigame(minigame);
					}
					Bukkit.getScheduler().cancelTask(taskID);
				}
			}
		}, 0, 20);
	}
	
	public void sendPlayersMessage(String message){
		for(MinigamePlayer ply : minigame.getPlayers()){
			ply.sendMessage(message);
		}
	}
	
	public void reclearInventories(Minigame minigame){
		for(MinigamePlayer ply : minigame.getPlayers()){
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
		for(MinigamePlayer ply : minigame.getPlayers()){
			ply.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "���� Ÿ�̸Ӱ� �����Ǿ����ϴ�.");
		}
	}
	
	public void pauseTimer(String reason){
		paused = true;
		for(MinigamePlayer ply : minigame.getPlayers()){
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
		for(MinigamePlayer ply : minigame.getPlayers()){
			ply.sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "���� Ÿ�̸Ӱ� �ٽ� �����Ͽ����ϴ�.");
		}
	}
	
	public boolean isPaused(){
		return paused;
	}
}
