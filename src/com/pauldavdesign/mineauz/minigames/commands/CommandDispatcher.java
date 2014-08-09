package com.pauldavdesign.mineauz.minigames.commands;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pauldavdesign.mineauz.minigames.Minigames;
import com.pauldavdesign.mineauz.minigames.commands.set.SetCommand;

public class CommandDispatcher implements CommandExecutor{
	private static Map<String, ICommand> commands = new HashMap<String, ICommand>();
	private static Minigames plugin = Minigames.plugin;
	private static BufferedWriter cmdFile;
	
	static{
		if(plugin.getConfig().getBoolean("outputCMDToFile")){
			try {
				cmdFile = new BufferedWriter(new FileWriter(plugin.getDataFolder() + "/cmds.txt"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		registerCommand(new CreateCommand());
		registerCommand(new SetCommand());
		registerCommand(new JoinCommand());
		registerCommand(new StartCommand());
		registerCommand(new StopCommand());
		registerCommand(new QuitCommand());
		registerCommand(new RevertCommand());
		registerCommand(new HintCommand());
		registerCommand(new InfoCommand());
		registerCommand(new EndCommand());
		//registerCommand(new RegenCommand());
//		registerCommand(new RestoreInvCommand());
		registerCommand(new HelpCommand());
		registerCommand(new ReloadCommand());
		registerCommand(new ListCommand());
		registerCommand(new ToggleTimerCommand());
		registerCommand(new DeleteCommand());
		registerCommand(new PartyModeCommand());
		registerCommand(new DeniedCommandCommand());
		registerCommand(new GlobalLoadoutCommand());
		registerCommand(new SpectateCommand());
		registerCommand(new TeleportCommand());
		
		if(plugin.getConfig().getBoolean("outputCMDToFile")){
			try {
				cmdFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void registerCommand(ICommand command){
		commands.put(command.getName(), command);
		
		if(plugin.getConfig().getBoolean("outputCMDToFile")){
			try {
				cmdFile.write("커맨드: " + command.getName());
				cmdFile.newLine();
				if(command.getDescription() != null){
					cmdFile.write("설명:");
					cmdFile.newLine();
					cmdFile.write(command.getDescription());
					cmdFile.newLine();
				}
				if(command.getParameters() != null){
					cmdFile.write("값:");
					cmdFile.newLine();
					for(String par : command.getParameters()){
						cmdFile.write(par + ", ");
					}
					cmdFile.newLine();
				}
				if(command.getUsage() != null){
					cmdFile.write("사용법:");
					cmdFile.newLine();
					for(String use : command.getUsage()){
						cmdFile.write(use);
						cmdFile.newLine();
					}
				}
				if(command.getAliases() != null){
					cmdFile.write("줄임 명령어:");
					cmdFile.newLine();
					for(String alias : command.getAliases()){
						cmdFile.write(alias + ", ");
					}
					cmdFile.newLine();
				}
				if(command.getPermission() != null){
					cmdFile.write("권한:");
					cmdFile.newLine();
					cmdFile.write(command.getPermission());
					cmdFile.newLine();
				}
				cmdFile.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player ply = null;
		if(sender instanceof Player){
			ply = (Player)sender;
		}
		
		if(args != null && args.length > 0){
			ICommand comd = null;
			String[] shortArgs = null;
			
			if(commands.containsKey(args[0].toLowerCase())){
				comd = commands.get(args[0].toLowerCase());
			}
			else{
AliasCheck:		for(ICommand com : commands.values()){
					if(com.getAliases() != null){
						for(String alias : com.getAliases()){
							if(args[0].equalsIgnoreCase(alias)){
								comd = com;
								break AliasCheck;
							}
						}
					}
				}
			}
			
			if(args != null && args.length > 1){
				shortArgs = new String[args.length - 1];
				for(int i = 1; i < args.length; i++){
					shortArgs[i - 1] = args[i];
				}
			}
			
			if(comd != null){
				if((ply == null && comd.canBeConsole()) || ply != null){
					if(ply == null || (comd.getPermission() == null || ply.hasPermission(comd.getPermission()))){
						boolean returnValue = comd.onCommand(sender, null, label, shortArgs);
						if(!returnValue){
							sender.sendMessage(ChatColor.GREEN + "------------------커맨드 정보------------------");
							sender.sendMessage(ChatColor.BLUE + "설명: " + ChatColor.WHITE + comd.getDescription());
							if(comd.getParameters() != null){
								String parameters = "";
								boolean switchColour = false;
								for(String par : comd.getParameters()){
									if(switchColour){
										parameters += ChatColor.WHITE + par;
										if(!par.equalsIgnoreCase(comd.getParameters()[comd.getParameters().length - 1])){
											parameters += ChatColor.WHITE + ", ";
										}
										switchColour = false;
									}
									else{
										parameters += ChatColor.GRAY + par;
										if(!par.equalsIgnoreCase(comd.getParameters()[comd.getParameters().length - 1])){
											parameters += ChatColor.WHITE + ", ";
										}
										switchColour = true;
									}
								}
								sender.sendMessage(ChatColor.BLUE + "값: " + parameters);
							}
							sender.sendMessage(ChatColor.BLUE + "사용법: ");
							sender.sendMessage(comd.getUsage());
							if(comd.getAliases() != null){
								String aliases = "";
								boolean switchColour = false;
								for(String alias : comd.getAliases()){
									if(switchColour){
										aliases += ChatColor.WHITE + alias;
										if(!alias.equalsIgnoreCase(comd.getAliases()[comd.getAliases().length - 1])){
											aliases += ChatColor.WHITE + ", ";
										}
										switchColour = false;
									}
									else{
										aliases += ChatColor.GRAY + alias;
										if(!alias.equalsIgnoreCase(comd.getAliases()[comd.getAliases().length - 1])){
											aliases += ChatColor.WHITE + ", ";
										}
										switchColour = true;
									}
								}
								sender.sendMessage(ChatColor.BLUE + "줄임 명령어: " + aliases);
							}
						}
					}
					else{
						sender.sendMessage(ChatColor.RED + comd.getPermissionMessage());
						sender.sendMessage(ChatColor.RED + comd.getPermission());
					}
					return true;
				}
				else{
					sender.sendMessage(ChatColor.RED + "이 명령어를 사용하려면 플레이어여야 합니다!");
					return true;
				}
			}
		}
		else{
			sender.sendMessage(ChatColor.GREEN + "Minigames");
			sender.sendMessage(ChatColor.GRAY + "제작자: " + plugin.getDescription().getAuthors().get(0));
			sender.sendMessage(ChatColor.GRAY + "버전: " +  plugin.getDescription().getVersion());
			sender.sendMessage(ChatColor.GRAY + "/미니게임 도움말 으로 도움말을 보세요. 수정 및 한글화: 잇스테이크");
			return true;
		}
		return false;
	}
}
