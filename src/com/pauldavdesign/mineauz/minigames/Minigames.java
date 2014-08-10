package com.pauldavdesign.mineauz.minigames;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.pauldavdesign.mineauz.minigames.commands.CommandDispatcher;
import com.pauldavdesign.mineauz.minigames.gametypes.DMMinigame;
import com.pauldavdesign.mineauz.minigames.gametypes.RaceMinigame;
import com.pauldavdesign.mineauz.minigames.gametypes.SPMinigame;
import com.pauldavdesign.mineauz.minigames.gametypes.TeamDMMinigame;
import com.pauldavdesign.mineauz.minigames.scoring.ScoreTypes;
import com.pauldavdesign.mineauz.minigames.signs.SignBase;

public class Minigames extends JavaPlugin{
	static Logger log = Logger.getLogger("Minecraft");
	public PlayerData pdata;
	public MinigameData mdata;
	public static Minigames plugin;
    private static Economy econ = null;
	private SQLDatabase sql = null;
	private static ScoreTypes scoretypes;
	private static SignBase minigameSigns;
	
	private long lastUpdateCheck = 0;

	public void onEnable(){
		plugin = this;
		PluginDescriptionFile desc = this.getDescription();
		log.info(desc.getName() + " �� ���������� Ȱ��ȭ �Ǿ����ϴ�.");
		
		mdata = new MinigameData();
		pdata = new PlayerData();
		
		mdata.addMinigameType(new SPMinigame());
		mdata.addMinigameType(new RaceMinigame());
		mdata.addMinigameType(new DMMinigame());
		mdata.addMinigameType(new TeamDMMinigame());
		
		if(!pdata.invsave.getConfig().contains("inventories")){
			pdata.invsave.getConfig().createSection("inventories");
		}
		try{
			Set<String> set = pdata.invsave.getConfig().getConfigurationSection("inventories").getKeys(false);
			ItemStack[] items = getServer().createInventory(null, InventoryType.PLAYER).getContents();
			ItemStack[] armour = new ItemStack[4];
			int health;
			int food;
			float saturation;
			GameMode lastGM;
			Location loginLocation;
			
			for(String player : set){
				health = pdata.invsave.getConfig().getInt("inventories." + player + ".health");
				food = pdata.invsave.getConfig().getInt("inventories." + player + ".food");
				saturation = Float.parseFloat(pdata.invsave.getConfig().getString("inventories." + player + ".saturation"));
				if(pdata.invsave.getConfig().contains("inventories." + player + ".lastGM")){ //TODO remove this next release version, for old compatibility.
					lastGM = GameMode.getByValue(pdata.invsave.getConfig().getInt("inventories." + player + ".lastGM"));
				}
				else{
					lastGM = GameMode.SURVIVAL;
				}
				if(pdata.invsave.getConfig().contains("inventories." + player + ".location")){ //TODO remove this next release version, for old compatibility.
					int x = pdata.invsave.getConfig().getInt("inventories." + player + ".location.x");
					int y = pdata.invsave.getConfig().getInt("inventories." + player + ".location.y");
					int z = pdata.invsave.getConfig().getInt("inventories." + player + ".location.z");
					float yaw = new Float(pdata.invsave.getConfig().getString("inventories." + player + ".location.yaw"));
					float pitch = new Float(pdata.invsave.getConfig().getString("inventories." + player + ".location.pitch"));
					World world = getServer().getWorld(pdata.invsave.getConfig().getString("inventories." + player + ".location.world"));
					loginLocation = new Location(world, x, y, z, yaw, pitch);
				}
				else{
					loginLocation = getServer().getWorlds().get(0).getSpawnLocation();
				}
				//log.info("Restoring " + player + "'s Items"); DEBUG
				for(int i = 0; i < items.length; i++){
					if(pdata.invsave.getConfig().contains("inventories." + player + "." + i)){
						items[i] = pdata.invsave.getConfig().getItemStack("inventories." + player + "." + i);
					}
				}
				for(int i = 0; i < 4; i++){
					armour[i] = pdata.invsave.getConfig().getItemStack("inventories." + player + ".armour." + i);
				}
				
//				pdata.storePlayerInventory(player, items, armour, health, food, saturation);
				pdata.addOfflineMinigamePlayer(new OfflineMinigamePlayer(player, items, armour, food, health, saturation, lastGM, loginLocation));
				items = getServer().createInventory(null, InventoryType.PLAYER).getContents();
			}
		}
		catch(Exception e){
			log.log(Level.SEVERE, "����� �κ��丮�� �ε����� ���߽��ϴ�!");
			e.printStackTrace();
		}
		
		MinigameSave completion = new MinigameSave("completion");
		mdata.addConfigurationFile("completion", completion.getConfig());
		
		getServer().getPluginManager().registerEvents(new Events(), this);
		
		
		try{
			this.getConfig().load(this.getDataFolder() + "/config.yml");
			List<String> mgs = new ArrayList<String>();
			if(getConfig().contains("minigames")){
				mgs = getConfig().getStringList("minigames");
			}
			final List<String> allMGS = new ArrayList<String>();
			allMGS.addAll(mgs);
			
			if(!mgs.isEmpty()){
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
					public void run() {
						for(String minigame : allMGS){
							Minigame game = new Minigame(minigame);
							game.loadMinigame();
							mdata.addMinigame(game);
						}
					}
				}, 1L);
			}
		}
		catch(FileNotFoundException ex){
			log.info("���� �ε忡 �����Ͽ����ϴ�. ���� ����ϴ�.");
			try{
				this.getConfig().save(this.getDataFolder() + "/config.yml");
			} 
			catch(IOException e){
				log.log(Level.SEVERE, "config.yml �� ������ �� �����ϴ�!");
				e.printStackTrace();
			}
		}
		catch(Exception e){
			log.log(Level.SEVERE, "���� �ε忡 �����Ͽ����ϴ�!");
			e.printStackTrace();
		}
		
		if(!setupEconomy()){
	        getLogger().info("Vault �÷������� �����ϴ�! �����۸� �������� �� �� �ֽ��ϴ�.");
		 }
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		if(getConfig().getBoolean("use-sql"))
			loadSQL();
		
		Calendar cal = Calendar.getInstance();
		if(cal.get(Calendar.DAY_OF_MONTH) == 21 && cal.get(Calendar.MONTH) == 8 ||
				cal.get(Calendar.DAY_OF_MONTH) == 25 && cal.get(Calendar.MONTH) == 11 ||
				cal.get(Calendar.DAY_OF_MONTH) == 1 && cal.get(Calendar.MONTH) == 0){
			getLogger().info(ChatColor.GREEN.name() + "��Ƽ ��� Ȱ��ȭ��!");
			pdata.setPartyMode(true);
		}
		
//		pdata.loadDCPlayers();
		pdata.loadDeniedCommands();
		
		scoretypes = new ScoreTypes();
		
		MinigameSave save = new MinigameSave("storedCheckpoints");
		for(String player : save.getConfig().getKeys(false)){
			StoredPlayerCheckpoints spc = new StoredPlayerCheckpoints(player);
			spc.loadCheckpoints();
			pdata.addStoredPlayerCheckpoints(player, spc);
		}
		
		MinigameSave globalLoadouts = new MinigameSave("globalLoadouts");
		Set<String> keys = globalLoadouts.getConfig().getKeys(false);
		for(String loadout : keys){
			mdata.addLoadout(loadout);
			Set<String> items = globalLoadouts.getConfig().getConfigurationSection(loadout).getKeys(false);
			for(int i = 0; i < items.size(); i++){
				if(globalLoadouts.getConfig().contains(loadout + "." + i))
					mdata.getLoadout(loadout).addItemToLoadout(globalLoadouts.getConfig().getItemStack(loadout + "." + i));
			}
			if(globalLoadouts.getConfig().contains(loadout + ".potions")){
				Set<String> pots = globalLoadouts.getConfig().getConfigurationSection(loadout + ".potions").getKeys(false);
				for(String eff : pots){
					if(PotionEffectType.getByName(eff) != null){
						PotionEffect effect = new PotionEffect(PotionEffectType.getByName(eff),
								globalLoadouts.getConfig().getInt(loadout + ".potions." + eff + ".dur"),
								globalLoadouts.getConfig().getInt(loadout + ".potions." + eff + ".amp"));
						mdata.getLoadout(loadout).addPotionEffect(effect);
					}
				}
			}
			if(globalLoadouts.getConfig().contains(loadout + ".usepermissions")){
				mdata.getLoadout(loadout).setUsePermissions(globalLoadouts.getConfig().getBoolean(loadout + ".usepermissions"));
			}
		}
		
		minigameSigns = new SignBase();
		
		getCommand("minigame").setExecutor(new CommandDispatcher());
		
		for(Player player : getServer().getOnlinePlayers()){
			pdata.addMinigamePlayer(player);
		}
	}

	public void onDisable(){
		PluginDescriptionFile desc = this.getDescription();
		log.info(desc.getName() + " �� ���������� ��Ȱ��ȭ �Ǿ����ϴ�!");
		
		for(Player p : getServer().getOnlinePlayers()){
			if(pdata.getMinigamePlayer(p).isInMinigame()){
				pdata.quitMinigame(pdata.getMinigamePlayer(p), true);
			}
		}
		Set<String> mgtreasure = mdata.getAllTreasureHuntLocation();
		for(String minigame : mgtreasure){
			if(mdata.getMinigame(minigame).getThTimer() != null) {
				mdata.getMinigame(minigame).getThTimer().stopTimer();
			}
			mdata.removeTreasureNoDelay(minigame);
		}
		for(Minigame mg : mdata.getAllMinigames().values()){
			mg.saveMinigame();
		}
		if(sql != null){
			getSQL().getSql().close();
		}
		
//		pdata.saveDCPlayers();
		pdata.saveDeniedCommands();
		
		MinigameSave globalLoadouts = new MinigameSave("globalLoadouts");
		if(mdata.hasLoadouts()){
			for(String loadout : mdata.getLoadouts()){
				for(int i = 0; i < mdata.getLoadout(loadout).getItems().size(); i++){
					globalLoadouts.getConfig().set(loadout + "." + i, mdata.getLoadout(loadout).getItems().get(i));
				}
				if(!mdata.getLoadout(loadout).getAllPotionEffects().isEmpty()){
					for(PotionEffect eff : mdata.getLoadout(loadout).getAllPotionEffects()){
						globalLoadouts.getConfig().set(loadout + ".potions." + eff.getType().getName() + ".amp", eff.getAmplifier());
						globalLoadouts.getConfig().set(loadout + ".potions." + eff.getType().getName() + ".dur", eff.getDuration());
					}
				}
				else{
					globalLoadouts.getConfig().set(loadout + ".potions", null);
				}
				if(mdata.getLoadout(loadout).getUsePermissions()){
					globalLoadouts.getConfig().set(loadout + ".usepermissions", true);
				}
				else{
					globalLoadouts.getConfig().set(loadout + ".usepermissions", null);
				}
			}
		}
		else{
			globalLoadouts.getConfig().set("globalloadouts", null);
		}
		globalLoadouts.saveConfig();
	}
	
	private boolean setupEconomy(){
        if(getServer().getPluginManager().getPlugin("Vault") == null){
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null){
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	public boolean hasEconomy(){
		if(econ != null){
			return true;
		}
		return false;
	}
	
	public Economy getEconomy(){
		return econ;
	}
	
	public PlayerData getPlayerData(){
		return pdata;
	}
	
	public void newPlayerData(){
		pdata = new PlayerData();
	}
	
	public MinigameData getMinigameData(){
		return mdata;
	}
	
	public void newMinigameData(){
		mdata = new MinigameData();
	}
	
	public SQLDatabase getSQL(){
		return sql;
	}
	
	public void loadSQL(){
		if(getServer().getPluginManager().getPlugin("SQLibrary") != null){
			sql = new SQLDatabase();
			sql.loadSQL();
		}
		else{
			getLogger().info("SQLibrary �� �������� �ʽ��ϴ�! SQL �� �̿��Ͽ� ����Ÿ�� ������ �� �����ϴ�!");
		}
	}
	
	public ScoreTypes getScoreTypes(){
		return scoretypes;
	}
	
	public long getLastUpdateCheck(){
		return lastUpdateCheck;
	}
	
	public void setLastUpdateCheck(long time){
		lastUpdateCheck = time;
	}
	
	public SignBase getMinigameSigns(){
		return minigameSigns;
	}
}
