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

import com.p1ut0nium.roughmobsrevamped.core.RoughMobsRevamped;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraftforge.fml.ModList;

public abstract class CompatHandler {
	
	private static boolean isGameStagesLoaded;
	private static boolean isSereneSeasonsLoaded;
	
	public static void registerModCompatibility() {
		isGameStagesLoaded = registerGameStages();
		isSereneSeasonsLoaded = registerSereneSeasons();
	}
	
	private static boolean registerGameStages() {
		if (ModList.get().isLoaded("gamestages")) {
			RoughMobsRevamped.LOGGER.info(Constants.MODID + ": Found Game Stages. Setting Up Compatibility...");
			GameStagesCompat.register();
			return true;
		}
		
		return false;
	}
	
	private static boolean registerSereneSeasons() {
		if (ModList.get().isLoaded("sereneseasons")) {
			RoughMobsRevamped.LOGGER.info(Constants.MODID + ": Found Serene Seasons. Setting Up Compatibility...");
			SereneSeasonsCompat.register();
			return true;
		}
		
		return false;
	}

	public static boolean isGameStagesLoaded() {
		return isGameStagesLoaded;
	}

	public static boolean isSereneSeasonsLoaded() {
		return isSereneSeasonsLoaded;
	}
}
