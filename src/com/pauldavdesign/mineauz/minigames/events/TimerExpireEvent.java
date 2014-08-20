package com.pauldavdesign.mineauz.minigames.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.pauldavdesign.mineauz.minigames.Minigame;

public class TimerExpireEvent extends Event{
	private static final HandlerList handlers = new HandlerList();
	private Minigame minigame;
	
	public TimerExpireEvent(Minigame minigame){
		this.minigame = minigame;
	}
	
	public Minigame getMinigame(){
		return minigame;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
 
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
