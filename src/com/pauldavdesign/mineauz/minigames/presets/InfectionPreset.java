package com.pauldavdesign.mineauz.minigames.presets;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.PlayerLoadout;

public class InfectionPreset implements BasePreset {

	
	public String getName() {
		return "Infection";
	}

	
	public String getInfo() {
		return "Creates balanced settings for an Infection game. This is based off the \"Introducing Infection\" video on the Minigames 1.5 release. " +
				"It gives survivors a stone knockback 2 sword and power 10 bow with 24 arrows (insta kill) and give the Infected a sharpness 3 sword (2 hit kill), jump boost 2 " +
				"and speed 2 for unlimited time (also a zombie head). The games timer is 5 minutes.";
	}

	
	public void execute(Minigame minigame) {
		//Loadouts
		minigame.addLoadout("red");
		minigame.addLoadout("blue");
		PlayerLoadout red = minigame.getLoadout("red");
		PlayerLoadout blue = minigame.getLoadout("blue");
		
		ItemStack zsword = new ItemStack(Material.DIAMOND_SWORD);
		ItemStack zhead = new ItemStack(Material.SKULL_ITEM);
		ItemStack ssword = new ItemStack(Material.STONE_SWORD);
		ItemStack sbow = new ItemStack(Material.BOW);
		ItemStack sarrows = new ItemStack(Material.ARROW, 24);
		
		zsword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
		zhead.setDurability((short)2);
		ssword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
		sbow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 10);
		
		red.addItemToLoadout(zsword);
		red.addItemToLoadout(zhead);
		blue.addItemToLoadout(ssword);
		blue.addItemToLoadout(sbow);
		blue.addItemToLoadout(sarrows);
		
		red.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200000, 2, true));
		red.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 200000, 2, true));
		
		//Settings
		minigame.setScoreType("infection");
		minigame.setDefaultWinner("blue");
		minigame.setType("teamdm");
		minigame.setMinPlayers(4);
		minigame.setMaxPlayers(16);
		minigame.setTimer(300);
		minigame.saveMinigame();
	}

}
