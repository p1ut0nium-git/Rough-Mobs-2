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

import net.minecraftforge.fml.ModList;

public abstract class CompatHandler {
	
	private static Boolean isGameStagesLoaded;
	private static Boolean isSereneSeasonsLoaded;
	
	public static void registerGameStages() {
		if (ModList.get().isLoaded("gamestages")) {
			isGameStagesLoaded = true;
			GameStagesCompat.register();
		} else {
			isGameStagesLoaded = false;
		}
	}
	
	public static void registerSereneSeasons() {
		if (ModList.get().isLoaded("sereneseasons")) {
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
