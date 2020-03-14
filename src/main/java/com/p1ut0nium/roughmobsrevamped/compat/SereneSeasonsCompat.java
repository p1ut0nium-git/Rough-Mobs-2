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
	
	private SereneSeasonsCompat() {}
	
	private static boolean registered;
	private static List<String> seasonWhiteList;
	
	public static void register() {
		if (registered)
			return;
		registered = true;
		preInit();
	}
	
	public static void preInit() {
		
		// Get season white list from config and convert to an array of strings
		seasonWhiteList = RoughConfig.seasonWhiteList;

	}
	
	public static String getSeason(World world) {
		if (registered)
			return SeasonHelper.getSeasonState(world).getSeason().toString();
		return null;
	}
	
	public static List<String> getSeasonWhitelist() {
		return seasonWhiteList;
	}
}
