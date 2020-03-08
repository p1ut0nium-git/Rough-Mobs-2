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

import com.p1ut0nium.roughmobsrevamped.config.ModCompatConfig;

public abstract class GameStagesCompat {
	
	//public static final GameStagesCompat INSTANCE = new GameStagesCompat();
	
	private GameStagesCompat() {}
	
	private static boolean registered;

	private static Boolean useEquipmentStage;
	private static Boolean useAllStages;
	private static Boolean useBossStage;
	private static Boolean useEnchantStage;
	private static Boolean useAbilitiesStage;

	public static void register() {
		if (registered)
			return;
		registered = true;
		preInit();
	}
	
	public static void preInit() {
		if (!hasDefaultConfig())
			return;
	
		useAllStages = ModCompatConfig.useAllStages.get();
		useEquipmentStage = ModCompatConfig.useEquipmentStage.get();
		useBossStage = ModCompatConfig.useBossStage.get();
		useAbilitiesStage = ModCompatConfig.useAbilitiesStage.get();
		useEnchantStage = ModCompatConfig.useEnchantStage.get();
		
		// If useAllStages is true, then all other stages should also be set to true
		if (useAllStages) {
			useEquipmentStage = useBossStage = useAbilitiesStage = useEnchantStage = true;
		}
	}
	
	public static void initConfig() {	
	}
	
	public static void postInit() {
	}
	
	private static boolean hasDefaultConfig() {
		return true;
	}
	
	public static Boolean useEquipmentStage() {
		if (registered) {
			return useEquipmentStage;
		}
		return false;
	}

	public static Boolean useAllStages() {
		if (registered) {
			return useAllStages;
		}
		return false;
	}

	public static Boolean useBossStage() {
		if (registered) {
			return useBossStage;
		} 
		return false;
	}

	public static Boolean useEnchantStage() {
		if (registered) {
			return useEnchantStage;
		}
		return false;
	}

	public static Boolean useAbilitiesStage() {
		if (registered) {
			return useAbilitiesStage;
		}
		return false;
	}
}
