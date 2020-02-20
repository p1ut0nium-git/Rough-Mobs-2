package com.p1ut0nium.roughmobsrevamped.compat;

import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;

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
	
		// Get/Set config file values for Game Stages
		RoughConfig.getConfig().addCustomCategoryComment("GameStages", "Configuration options for enabling/disabling Game Stages.\n"
																		+ "The mod Game Stages must be installed for these features to work.\n"
																		+ "\n"
																		+ "Stages to give player in-game: \n"
																		+ "\n"
																		+ "roughmobsall - gives all stages at once.\n"
																		+ "roughmobsequip - allows mobs to spawn with equipment.\n"
																		+ "roughmobsenchant - allows mob gear to be enchanted.\n"
																		+ "roughmobsboss - allows some mobs to be bosses.\n"
																		+ "roughmobsabils - allows mobs to have special abilities.");
		
		useAllStages = RoughConfig.getBoolean("GameStages", "_All", false, "Enable this Game Stage to allow all Rough Mobs stages at once. Or turn them on individually");		
		useEquipmentStage = RoughConfig.getBoolean("GameStages", "_Equipment", false, "Enable this Game stage for Rough Mobs to have equipment.");
		useBossStage = RoughConfig.getBoolean("GameStages", "_Bosses", false, "Enable this Game Stage needed for Rough Mob Bosses to spawn. Must also enable Equipment stage for this to work.");
		useAbilitiesStage = RoughConfig.getBoolean("GameStages", "_Abilities", false, "Enable this Game Stage for Rough Mobs to have special combat AI and attributes.");
		useEnchantStage = RoughConfig.getBoolean("GameStages", "_Enchantments", false, "Enable this Game Stage for Rough Mob equipment to be enchanted.");		
		
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

	/*
	 * Getters
	 */
	
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
