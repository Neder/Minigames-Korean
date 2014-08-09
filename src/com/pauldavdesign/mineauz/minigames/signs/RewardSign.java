package com.pauldavdesign.mineauz.minigames.signs;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Sign;
import org.bukkit.configuration.Configuration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.SignChangeEvent;

import com.pauldavdesign.mineauz.minigames.MinigamePlayer;
import com.pauldavdesign.mineauz.minigames.MinigameSave;
import com.pauldavdesign.mineauz.minigames.MinigameUtils;
import com.pauldavdesign.mineauz.minigames.Minigames;

public class RewardSign implements MinigameSign {
	
	private static Minigames plugin = Minigames.plugin;

	
	public String getName() {
		return "보상";
	}

	
	public String getCreatePermission() {
		return "minigame.sign.create.reward";
	}

	
	public String getCreatePermissionMessage() {
		return "미니게임 보상 표지판을 만들 권한이 없습니다!";
	}

	
	public String getUsePermission() {
		return "minigame.sign.use.reward";
	}

	
	public String getUsePermissionMessage() {
		return "미니게임 보상 표지판을 사용할 권한이 없습니다!";
	}

	
	public boolean signCreate(SignChangeEvent event) {
		event.setLine(1, ChatColor.GREEN + "보상");
		if(event.getLine(2).isEmpty()){
			event.getPlayer().sendMessage(ChatColor.RED + "보상 이름을 3번째 줄에 작성해야 합니다!");
			return false;
		}
		String[] split = event.getLine(3).split(" ");
		if(!event.getLine(3).isEmpty()){
			if(!split[0].matches("[0-9]+(:[0-9]+)?(:[0-9]+(,[0-9]+)?)+?")){
				if(Material.getMaterial(split[0].toUpperCase()) != null){
					return true;
				}
				event.getPlayer().sendMessage(ChatColor.RED + "\"" + split[0] + "\", 라는 이름을 가진 아이템은 없습니다. ID 를 적어 보세요.");
			}
			else{
				if(Material.getMaterial(Integer.parseInt(split[0].split(":")[0])) != null)
					return true;
				else
					event.getPlayer().sendMessage(ChatColor.RED + "그런 ID를 사용하는 아이템은 없습니다!");
			}
		}
		else{
			event.getPlayer().sendMessage(ChatColor.BLUE + "[PMGO-L] " + ChatColor.WHITE + "보상으로 주고싶은 아이템을 대고" +
					" 표지판을 클릭하세요. (10초 남음)");
			final Location loc = event.getBlock().getLocation();
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
				
				public void run() {
					if(loc.getBlock().getState() instanceof Sign){
						Sign sign = (Sign) loc.getBlock().getState();
						if(sign.getLine(3).isEmpty()){
							loc.getBlock().breakNaturally();
						}
					}
				}
			}, 200);
			return true;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	
	public boolean signUse(Sign sign, MinigamePlayer player) {
		if(!sign.getLine(3).isEmpty()){
			ItemStack item = null;
			
			Configuration completion = plugin.mdata.getConfigurationFile("completion");
			boolean hascompleted = completion.getStringList(sign.getLine(2)).contains(player.getName());
			
			if(!hascompleted){
				List<String> completionlist = completion.getStringList(sign.getLine(2));
				completionlist.add(player.getName());
				completion.set(sign.getLine(2), completionlist);
				MinigameSave completionsave = new MinigameSave("completion");
				completionsave.getConfig().set(sign.getLine(2), completionlist);
				completionsave.saveConfig();
				
				String[] split = sign.getLine(3).split(" ");
				if(!split[0].matches("[0-9]+(:[0-9]+)?(:[0-9]+(,[0-9]+)?)+?")){
					if(Material.getMaterial(split[0].toUpperCase()) != null){
						int amount = 1;
						if(split.length == 2 && split[1].matches("[0-9]+")){
							amount = Integer.parseInt(split[1]);
						}
						item = new ItemStack(Material.getMaterial(split[0].toUpperCase()), amount);
					}
				}
				else{
					if(Material.getMaterial(Integer.parseInt(split[0].split(":")[0])) != null){
						String[] split2 = split[0].split(":");
						short damage = 0;
						if(split2.length >= 2 && split2[1].matches("[0-9]+")){
							damage = Short.parseShort(split2[1]);
						}
	
						int amount = 1;
						if(split.length == 2 && split[1].matches("[0-9]+")){
							amount = Integer.parseInt(split[1]);
						}
						
						item = new ItemStack(Integer.parseInt(split2[0]), amount);
						
						item.setDurability(damage);

						if(split2.length > 2){
							for(int i = 2; i < split2.length; i++){
								String[] enchants = split2[i].split(",");
								if(enchants[0].matches("[0-9]+")){
									int level = 1;
									if(enchants.length == 2 && enchants[1].matches("[0-9]+")){
										level = Integer.parseInt(enchants[1]);
									}
									item.addEnchantment(Enchantment.getById(Integer.parseInt(enchants[0])), level);
								}
							}
						}
					}
				}
				
				if(item != null){
					player.getPlayer().getInventory().addItem(item);
					player.sendMessage("" + item.getAmount() + " 개의 " + MinigameUtils.getItemStackName(item) + "을 성공적으로 받으셨습니다!");
					player.getPlayer().updateInventory();
					return true;
				}
			}
		}
		else{
			if(player.getPlayer().getItemInHand().getType() != Material.AIR){
				String id = "";
				ItemStack item = player.getPlayer().getItemInHand();
				id += item.getTypeId();
				if(item.getDurability() != 0 || !item.getEnchantments().isEmpty()){
					id += ":" + item.getDurability();
				}
				if(!item.getEnchantments().isEmpty()){
					for(Enchantment en : item.getEnchantments().keySet()){
						id += ":" + en.getId();
						if(item.getEnchantments().get(en) != 1){
							id += "," + item.getEnchantments().get(en);
						}
					}
				}
				
				if(item.getAmount() > 1){
					id += " " + item.getAmount();
				}
				sign.setLine(3, id);
				sign.update();
				return true;
			}
		}
		return false;
	}

}
