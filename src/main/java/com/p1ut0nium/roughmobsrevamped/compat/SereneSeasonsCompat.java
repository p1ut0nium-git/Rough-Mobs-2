package com.p1ut0nium.roughmobsrevamped.compat;

import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.util.Constants;

import net.minecraft.world.World;
import sereneseasons.api.season.SeasonHelper;

public abstract class SereneSeasonsCompat {
	
	//public static final SereneSeasonsCompat INSTANCE = new SereneSeasonsCompat();
	
	private SereneSeasonsCompat() {}
	
	private static boolean registered;
	private static String[] seasonWhiteList;
	
	public static void register() {
		if (registered)
			return;
		registered = true;
		preInit();
	}
	
	private static void preInit() {
		if (!hasDefaultConfig())
			return;
		
		RoughConfig.getConfig().addCustomCategoryComment("SereneSeasons", "Configuration options for enabling/disabling Serene Seasons support.\n"
				+ "The mod Serene Seasons must be installed for these features to work.\n");

				seasonWhiteList = RoughConfig.getStringArray("SereneSeasons", "_WhiteList", Constants.SEASONS, "Whitelist of all seasons that Rough Mobs can spawn in.");
	}
	
	private static boolean hasDefaultConfig() {
		return true;
	}

	/*
	 * Getters
	 */
	
	public static String getSeason(World world) {
		if (registered)
			return SeasonHelper.getSeasonState(world).getSeason().toString();
		return null;
	}
	
	public static String[] getSeasonWhitelist() {
		if (registered)
			return seasonWhiteList;
		return null;
	}
}
