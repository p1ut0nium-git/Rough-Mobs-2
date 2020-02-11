package de.lellson.roughmobs2.compat;

import net.minecraftforge.fml.common.Loader;

public class CompatHandler {
	
	private static Boolean isGameStagesLoaded;
	private static Boolean isSereneSeasonsLoaded;
	
	public static void registerGameStages() {
		if (Loader.isModLoaded("gamestages")) {
			isGameStagesLoaded = true;
			GameStagesCompat.register();
		}
	}
	
	public static void registerSereneSeasons() {
		if (Loader.isModLoaded("sereneseasons")) {
			isSereneSeasonsLoaded = true;
			SereneSeasonsCompat.register();
		}
	}

	public static Boolean isGameStagesLoaded() {
		return isGameStagesLoaded && GameStagesCompat.isStagesEnabled();
	}

	public static Boolean isSereneSeasonsLoaded() {
		return isSereneSeasonsLoaded && SereneSeasonsCompat.isSereneSeasonsEnabled();
	}
}
