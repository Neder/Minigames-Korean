package com.pauldavdesign.mineauz.minigames;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import com.pauldavdesign.mineauz.minigames.events.TimerExpireEvent;

public class MinigameTimer{
	private int time = 0;
	private Minigame minigame;
	private List<Integer> timeMsg = new ArrayList<Integer>();
	private static Minigames plugin = Minigames.plugin;
	private int taskID = -1;
	
	public MinigameTimer(Minigame minigame, int time){
		this.time = time;
		this.minigame = minigame;
		timeMsg.addAll(plugin.getConfig().getIntegerList("multiplayer.timerMessageInterval"));
		startTimer();
	}
	
	public void startTimer(){
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			public void run() {
				time -= 1;
				if(timeMsg.contains(time)){
					plugin.mdata.sendMinigameMessage(minigame, MinigameUtils.convertTime(time) + " ���ҽ��ϴ�.", null, null);
				}
				else if(time == 0){
					Bukkit.getServer().getPluginManager().callEvent(new TimerExpireEvent(minigame));
					stopTimer();
				}
			}
		}, 0, 20);
	}
	
	public void stopTimer(){
		if(taskID != -1){
			Bukkit.getScheduler().cancelTask(taskID);
		}
	}
	
	public int getTimeLeft(){
		return time;
	}
}
