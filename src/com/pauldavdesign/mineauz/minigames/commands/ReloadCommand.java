package com.pauldavdesign.mineauz.minigames.commands;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.Minigames;

public class ReloadCommand implements ICommand{

	
	public String getName() {
		return "reload";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "미니게임 설정 파일을 리로드합니다.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame reload"};
	}

	
	public String getPermissionMessage() {
		return "플러그인을 리로드할 권한이 없습니다!";
	}

	
	public String getPermission() {
		return "minigame.reload";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		Player[] players = plugin.getServer().getOnlinePlayers();
		for(Player p : players){
			if(plugin.pdata.getMinigamePlayer(p).isInMinigame()){
				plugin.pdata.quitMinigame(plugin.pdata.getMinigamePlayer(p), true);
			}
		}
		
		Minigames.plugin.mdata.getAllMinigames().clear();
		
		try{
			plugin.getConfig().load(plugin.getDataFolder() + "/config.yml");
		}
		catch(FileNotFoundException ex){
			plugin.getLogger().info("설정을 로드하는데 실패하였습니다. 새로 만듭니다.");
			try{
				plugin.getConfig().save(plugin.getDataFolder() + "/config.yml");
			} 
			catch(IOException e){
				plugin.getLogger().log(Level.SEVERE, "config.yml 를 저장할 수 없습니다!");
				e.printStackTrace();
			}
		}
		catch(Exception e){
			plugin.getLogger().log(Level.SEVERE, "설정을 로드하는데 실패하였습니다!");
			e.printStackTrace();
		}
		
		List<String> mgs = new ArrayList<String>();
		if(Minigames.plugin.getConfig().contains("minigames")){
			mgs = Minigames.plugin.getConfig().getStringList("minigames");
		}
		final List<String> allMGS = new ArrayList<String>();
		allMGS.addAll(mgs);
		
		if(!mgs.isEmpty()){
			for(String mgm : allMGS){
				Minigame game = new Minigame(mgm);
				game.loadMinigame();
				Minigames.plugin.mdata.addMinigame(game);
			}
		}
		
		sender.sendMessage(ChatColor.GREEN + "미니게임 설정이 리로드 되었습니다.");
		return true;
	}

}
