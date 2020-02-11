package de.lellson.roughmobs2.compat;

import de.lellson.roughmobs2.config.RoughConfig;

public class GameStagesCompat {
	
	public static final GameStagesCompat INSTANCE = new GameStagesCompat();
	
	private GameStagesCompat() {}
	
	private static boolean registered;

	private static Boolean useEquipmentStage;
	private static Boolean useAllStages;
	private static Boolean useBossStage;
	private static Boolean useEnchantStage;
	private static Boolean useAbilitiesStage;
	private static Boolean useGameStagesMod;

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
																		+ "Stages: \n"
																		+ "\n"
																		+ "roughmobsall - gives all stages at once.\n"
																		+ "roughmobsequip - allows mobs to spawn with equipment.\n"
																		+ "roughmobsenchant - allows mob gear to be enchanted.\n"
																		+ "roughmobsboss - allows some mobs to be bosses.\n"
																		+ "roughmobsabils - allows mobs to have special abilities.");
		
		useAllStages = RoughConfig.getBoolean("GameStages", "_AllStages", false, "Enable this Game Stage to allow all Rough Mobs stages at once. Or turn them on individually");		
		useEquipmentStage = RoughConfig.getBoolean("GameStages", "_Equipment", false, "Enable this Game stage for Rough Mobs to have equipment.");
		useBossStage = RoughConfig.getBoolean("GameStages", "_Bosses", false, "Enable this Game Stage needed for Rough Mob Bosses to spawn. Must also enable Equipment stage for this to work.");
		useAbilitiesStage = RoughConfig.getBoolean("GameStages", "_Abilities", false, "Enable this Game Stage for Rough Mobs to have special combat AI and attributes.");
		useEnchantStage = RoughConfig.getBoolean("GameStages", "_Enchantments", false, "Enable this Game Stage for Rough Mob equipment to be enchanted.");		
		useGameStagesMod = RoughConfig.getBoolean("GameStages", "_Enabled", false, "Set to true to enable using Game Stages.", true);
		
		// If useAllStages is true, then all other stages should also be set to true
		if (useAllStages) {
			useEquipmentStage = useBossStage = useAbilitiesStage = useEnchantStage = true;
		}
	}
	
	public void initConfig() {	
	}
	
	public void postInit() {
	}
	
	public static boolean hasDefaultConfig() {
		return true;
	}

	/*
	 * Getters & Setters
	 */
	
	public static boolean isStagesEnabled() {
		return useGameStagesMod;
	}
	
	public static Boolean useEquipmentStage() {
		return useEquipmentStage;
	}

	public static Boolean useAllStages() {
		return useAllStages;
	}

	public static Boolean useBossStage() {
		return useBossStage;
	}

	public static Boolean useEnchantStage() {
		return useEnchantStage;
	}

	public static Boolean useAbilitiesStage() {
		return useAbilitiesStage;
	}
}
