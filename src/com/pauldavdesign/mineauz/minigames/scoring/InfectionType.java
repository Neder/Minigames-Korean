package com.pauldavdesign.mineauz.minigames.scoring;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.MinigamePlayer;
import com.pauldavdesign.mineauz.minigames.events.EndTeamMinigameEvent;
import com.pauldavdesign.mineauz.minigames.events.QuitMinigameEvent;
import com.pauldavdesign.mineauz.minigames.gametypes.TeamDMMinigame;

public class InfectionType extends ScoreType{
	
	private List<MinigamePlayer> infected = new ArrayList<MinigamePlayer>();

	@Override
	public String getType() {
		return "infection";
	}

	@Override
	public void balanceTeam(List<MinigamePlayer> players, Minigame minigame) {
		for(int i = 0; i < players.size(); i++){
			MinigamePlayer ply = players.get(i);
			if(!minigame.getType().equals("teamdm")){
				pdata.quitMinigame(ply, true);
				ply.sendMessage(ChatColor.RED + "[PMGO-L] " + ChatColor.WHITE + "감염 게임은 팀데스매치에서만 할 수 있습니다!");
			}
			else{
				int team = -1;
				if(minigame.getBlueTeam().contains(players.get(i))){
					team = 1;
				}
				else if(minigame.getRedTeam().contains(players.get(i))){
					team = 0;
				}
				
				if(team == 1){
					if(minigame.getRedTeam().size() < Math.ceil(players.size() * 0.18)){
						minigame.addRedTeamPlayer(players.get(i));
						team = 0;
						players.get(i).sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "당신은 " + ChatColor.RED + "감염되었습니다!");
						mdata.sendMinigameMessage(minigame, players.get(i).getName() + " 님은 " + ChatColor.RED + "감염되었습니다!", null, players.get(i));
					}
				}
				else if(team == -1){
					if(minigame.getRedTeam().size() < Math.ceil(players.size() * 0.18)){
						minigame.addRedTeamPlayer(players.get(i));
						team = 0;
						players.get(i).sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "당신은 " + ChatColor.RED + "감염되었습니다!");
						mdata.sendMinigameMessage(minigame, players.get(i).getName() + " 님은 " + ChatColor.RED + "감염되었습니다!", null, players.get(i));
					}
					else{
						minigame.addBlueTeamPlayer(players.get(i));
						team = 1;
						players.get(i).sendMessage(ChatColor.AQUA + "[PMGO-L] " + ChatColor.WHITE + "당신은 " + ChatColor.BLUE + "생존자입니다.");
						mdata.sendMinigameMessage(minigame, players.get(i).getName() + " 님은 " + ChatColor.BLUE + "생존자입니다.", null, players.get(i));
					}
				}
			}
		}
	}
	
	@EventHandler
	private void playerDeath(PlayerDeathEvent event){
		MinigamePlayer player = pdata.getMinigamePlayer(event.getEntity());
		if(player.isInMinigame()){
			Minigame mgm = player.getMinigame();
			if(mgm.getType().equals("teamdm") && mgm.getScoreType().equals("infection")){
				if(mgm.getBlueTeam().contains(event.getEntity())){
					TeamDMMinigame.switchTeam(mgm, player);
					infected.add(player);
					if(event.getEntity().getKiller() != null){
						MinigamePlayer killer = pdata.getMinigamePlayer(event.getEntity().getKiller());
//						pdata.addPlayerScore(killer);
						killer.addScore();
						mgm.setScore(killer, killer.getScore());
					}
//					pdata.setPlayerScore(event.getEntity(), 0);
					player.resetScore();
					mgm.setScore(player, player.getScore());
					
					if(mgm.getLives() != player.getDeaths()){
						mdata.sendMinigameMessage(mgm, event.getEntity().getName() + " 님은 " + ChatColor.RED + "감염되었습니다!", "error", null);
					}
					if(mgm.getBlueTeam().isEmpty()){
						event.getEntity().setHealth(2);
						pdata.endTeamMinigame(0, mgm);
					}
				}
				else{
					if(event.getEntity().getKiller() != null){
						MinigamePlayer killer = pdata.getMinigamePlayer(event.getEntity().getKiller());
//						pdata.addPlayerScore(killer);
						killer.addScore();
						mgm.setScore(killer, killer.getScore());
					}
				}
			}
		}
	}
	
	@EventHandler
	private void endTeamMinigame(EndTeamMinigameEvent event){
		if(event.getMinigame().getScoreType().equals("infection")){
			List<MinigamePlayer> infect = new ArrayList<MinigamePlayer>();
			infect.addAll(infected);
			for(MinigamePlayer inf : infect){
				if(event.getWinnningPlayers().contains(inf)){
					if(event.getWinningTeamInt() == 0){
						event.getWinnningPlayers().remove(inf);
						event.getLosingPlayers().add(inf);
					}
					infected.remove(inf);
				}
			}
		}
	}
	
	@EventHandler
	private void quitMinigame(QuitMinigameEvent event){
		if(infected.contains(event.getPlayer())){
			infected.remove(event.getPlayer());
		}
	}
}
