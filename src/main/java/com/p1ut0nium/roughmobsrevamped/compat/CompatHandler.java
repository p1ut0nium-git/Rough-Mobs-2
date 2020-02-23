package com.p1ut0nium.roughmobsrevamped.compat;

import net.minecraftforge.fml.common.Loader;

public abstract class CompatHandler {
	
	private static Boolean isGameStagesLoaded;
	private static Boolean isSereneSeasonsLoaded;
	
	public static void registerGameStages() {
		if (Loader.isModLoaded("gamestages")) {
			isGameStagesLoaded = true;
			GameStagesCompat.register();
		} else {
			isGameStagesLoaded = false;
		}
	}
	
	public static void registerSereneSeasons() {
		if (Loader.isModLoaded("sereneseasons")) {
			isSereneSeasonsLoaded = true;
			SereneSeasonsCompat.register();
		} else {
			isSereneSeasonsLoaded = false;
		}
	}

	public static Boolean isGameStagesLoaded() {
		return isGameStagesLoaded;
	}

	public static Boolean isSereneSeasonsLoaded() {
		return isSereneSeasonsLoaded;
	}
}
