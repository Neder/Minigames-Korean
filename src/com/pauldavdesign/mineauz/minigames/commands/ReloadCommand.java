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
		return "�̴ϰ��� ���� ������ ���ε��մϴ�.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame reload"};
	}

	
	public String getPermissionMessage() {
		return "�÷������� ���ε��� ������ �����ϴ�!";
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
			plugin.getLogger().info("������ �ε��ϴµ� �����Ͽ����ϴ�. ���� ����ϴ�.");
			try{
				plugin.getConfig().save(plugin.getDataFolder() + "/config.yml");
			} 
			catch(IOException e){
				plugin.getLogger().log(Level.SEVERE, "config.yml �� ������ �� �����ϴ�!");
				e.printStackTrace();
			}
		}
		catch(Exception e){
			plugin.getLogger().log(Level.SEVERE, "������ �ε��ϴµ� �����Ͽ����ϴ�!");
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
		
		sender.sendMessage(ChatColor.GREEN + "�̴ϰ��� ������ ���ε� �Ǿ����ϴ�.");
		return true;
	}

}
