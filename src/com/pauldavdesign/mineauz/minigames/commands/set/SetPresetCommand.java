package com.pauldavdesign.mineauz.minigames.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.MinigameData;
import com.pauldavdesign.mineauz.minigames.Minigames;
import com.pauldavdesign.mineauz.minigames.commands.ICommand;

public class SetPresetCommand implements ICommand {

	
	public String getName() {
		return "preset";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "Automatically sets up a Minigame using a preset provided. " +
				"Note: This will not set up any positions for you, you still have to do them yourself.";
	}

	
	public String[] getParameters() {
		String[] arr = new String[Minigames.plugin.mdata.getAllPresets().size()];
		int inc = 0;
		for(String preset : Minigames.plugin.mdata.getAllPresets()){
			arr[inc] = preset;
			inc++;
		}
		return arr;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame set <Minigame> preset <Preset>", 
							"/minigame set <Minigame> preset <Preset> info"
		};
	}

	
	public String getPermissionMessage() {
		return "You do not have permission to use a preset on a Minigame!";
	}

	
	public String getPermission() {
		return "minigame.set.preset";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			MinigameData mdata = Minigames.plugin.mdata;
			if(args.length == 1 && mdata.hasPreset(args[0].toLowerCase())){
				mdata.getPreset(args[0].toLowerCase()).execute(minigame);
				sender.sendMessage(ChatColor.GRAY + "The Minigame has been set up using the " + args[0] + " preset.");
				return true;
			}
			else if(args.length >= 2 && mdata.hasPreset(args[0].toLowerCase()) && args[1].equalsIgnoreCase("info")){
				sender.sendMessage(ChatColor.AQUA + "------------------Preset Info------------------");
				sender.sendMessage(mdata.getPreset(args[0].toLowerCase()).getInfo());
				return true;
			}
			else{
				sender.sendMessage(ChatColor.GRAY + "There is no preset by the name " + args[0]);
			}
		}
		return false;
	}

}
