package de.lellson.roughmobs2.compat;

import de.lellson.roughmobs2.config.RoughConfig;
import de.lellson.roughmobs2.misc.Constants;
import net.minecraft.world.World;
import sereneseasons.api.season.SeasonHelper;

public class SereneSeasonsCompat {
	
	public static final SereneSeasonsCompat INSTANCE = new SereneSeasonsCompat();
	
	private SereneSeasonsCompat() {}
	
	private static boolean registered;
	
	private static Boolean useSereneSeasonsMod;
	private static String[] seasonWhiteList;
	
	public static void register() {
		if (registered)
			return;
		registered = true;
		preInit();
	}
	
	public static void preInit() {
		if (!hasDefaultConfig())
			return;
		
		RoughConfig.getConfig().addCustomCategoryComment("SereneSeasons", "Configuration options for enabling/disabling Serene Seasons support.\n"
				+ "The mod Serene Seasons must be installed for these features to work.\n");

				seasonWhiteList = RoughConfig.getStringArray("SereneSeasons", "_WhiteList", Constants.SEASONS, "Whitelist of all seasons that Rough Mobs can spawn in.");
				useSereneSeasonsMod = RoughConfig.getBoolean("SereneSeasons", "_Enabled", true, "Set to true to enable using Serene Seasons.", true);
	}
	
	public static boolean hasDefaultConfig() {
		return true;
	}
	
	public static boolean isSereneSeasonsEnabled() {
		return useSereneSeasonsMod;
	}
	
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
