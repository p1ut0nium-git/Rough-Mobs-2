/*
 * Rough Mobs Revamped for Minecraft Forge 1.14.4
 * 
 * This is a complete revamp of Lellson's Rough Mobs 2
 * 
 * Author: p1ut0nium_94
 * Website: https://www.curseforge.com/minecraft/mc-mods/rough-mobs-revamped
 * Source: https://github.com/p1ut0nium-git/Rough-Mobs-Revamped/tree/1.14.4
 * 
 */
package com.p1ut0nium.roughmobsrevamped.compat;

import java.util.List;

import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;

import net.minecraft.world.World;
import sereneseasons.api.season.SeasonHelper;

public abstract class SereneSeasonsCompat {
	
	//public static final SereneSeasonsCompat INSTANCE = new SereneSeasonsCompat();
	
	private SereneSeasonsCompat() {}
	
	private static boolean registered;
	private static List<? extends String> seasonWhiteList;
	
	public static void register() {
		if (registered)
			return;
		registered = true;
	}
	
	public static void preInit() {
		if (!hasDefaultConfig())
			return;
		
		// Get season white list from config and convert to an array of strings
		seasonWhiteList = RoughConfig.seasonWhiteList;
		
		// TODO - may be able to delete this
		// Object[] whiteListConfig = ModCompatConfig.seasonWhiteList.get().toArray();
		// seasonWhiteList = Arrays.copyOf(whiteListConfig, whiteListConfig.length, String[].class);
	}
	
	private static boolean hasDefaultConfig() {
		return true;
	}
	
	public static String getSeason(World world) {
		if (registered)
			return SeasonHelper.getSeasonState(world).getSeason().toString();
		return null;
	}
	
	public static List<? extends String> getSeasonWhitelist() {
		return seasonWhiteList;
	}
}
