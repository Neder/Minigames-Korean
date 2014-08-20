package com.pauldavdesign.mineauz.minigames.signs;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.pauldavdesign.mineauz.minigames.Minigames;

public class SignBase implements Listener{
	
	private static Map<String, MinigameSign> minigameSigns = new HashMap<String, MinigameSign>();
	
	static{
		registerMinigameSign(new FinishSign());
		registerMinigameSign(new JoinSign());
		registerMinigameSign(new BetSign());
		registerMinigameSign(new CheckpointSign());
		registerMinigameSign(new FlagSign());
		registerMinigameSign(new QuitSign());
		registerMinigameSign(new LoadoutSign());
		registerMinigameSign(new TeleportSign());
		registerMinigameSign(new SpectateSign());
		registerMinigameSign(new RewardSign());
		registerMinigameSign(new TeamSign());
	}
	
	public SignBase(){
		Minigames.plugin.getServer().getPluginManager().registerEvents(this, Minigames.plugin);
	}
	
	public static void registerMinigameSign(MinigameSign mgSign){
		minigameSigns.put(mgSign.getName().toLowerCase(), mgSign);
	}
	
	@EventHandler
	private void signPlace(SignChangeEvent event){
		String[] signinfo = event.getLines();
		if(signinfo[0].equalsIgnoreCase("[미니게임]") || signinfo[0].equalsIgnoreCase("[미겜]") || signinfo[0].equalsIgnoreCase("[미]")){
			if(minigameSigns.containsKey(signinfo[1].toLowerCase())){
				event.setLine(0, ChatColor.DARK_BLUE + "[미니게임]");
				MinigameSign mgSign = minigameSigns.get(signinfo[1].toLowerCase());
				
				if(mgSign.getCreatePermission() != null && !event.getPlayer().hasPermission(mgSign.getCreatePermission())){
					event.setCancelled(true);
					event.getBlock().breakNaturally();
					event.getPlayer().sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + mgSign.getCreatePermissionMessage());
					return;
				}
				
				if(!mgSign.signCreate(event)){
					event.setCancelled(true);
					event.getBlock().breakNaturally();
					event.getPlayer().sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "올바르지 않은 미니게임 표지판입니다!");
				}
			}
			else{
				event.setCancelled(true);
				event.getBlock().breakNaturally();
			}
		}
	}
	
	@EventHandler
	private void signUse(PlayerInteractEvent event){
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block cblock = event.getClickedBlock();
			if(cblock.getState() instanceof Sign){
				Sign sign = (Sign) cblock.getState();
				if(sign.getLine(0).equals(ChatColor.DARK_BLUE + "[미니게임]") && 
						minigameSigns.containsKey(ChatColor.stripColor(sign.getLine(1).toLowerCase()))){
					MinigameSign mgSign = minigameSigns.get(ChatColor.stripColor(sign.getLine(1).toLowerCase()));
					
					if(mgSign.getUsePermission() != null && !event.getPlayer().hasPermission(mgSign.getUsePermission())){
						event.setCancelled(true);
						event.getPlayer().sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + mgSign.getUsePermissionMessage());
						return;
					}
					
					event.setCancelled(true);
					
					mgSign.signUse(sign, Minigames.plugin.pdata.getMinigamePlayer(event.getPlayer()));
				}
			}
		}
	}

}
