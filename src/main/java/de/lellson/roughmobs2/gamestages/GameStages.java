package de.lellson.roughmobs2.gamestages;

import de.lellson.roughmobs2.config.RoughConfig;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Loader;

public class GameStages {
	
	public static Boolean enableEquipmentStage;
	public static Boolean enableAllStages;
	public static Boolean enableBossStage;
	public static Boolean enableEnchantStage;
	public static Boolean enableAbilitiesStage;
	
	private static Boolean isGameStagesLoaded;
	private static Boolean isGameStagesEnabled;

	public void preInit() {
		if (!hasDefaultConfig())
			return;
	
		RoughConfig.getConfig().addCustomCategoryComment("GameStages", "Configuration options for enabling/disabling Game Stages.\n"
																		+ "The mod Game Stages must be installed for these features to work.\n"
																		+ "Stages: \n"
																		+ "roughmobsall - gives all stages at once.\n"
																		+ "roughmobsequip - allows mobs to spawn with equipment.\n"
																		+ "roughmobsenchant - allows mob gear to be enchanted.\n"
																		+ "roughmobsboss - allows some mobs to be bosses.\n"
																		+ "roughmobsabils - allows mobs to have special abilities.");
		
		enableAllStages = RoughConfig.getBoolean("GameStages", "_AllStages", false, "Enable this Game Stage to allow all Rough Mobs stages at once. Or turn them on individually");
		enableEquipmentStage = RoughConfig.getBoolean("GameStages", "_Equipment", false, "Enable Game stage for Rough Mobs to have equipment.");
		enableBossStage = RoughConfig.getBoolean("GameStages", "_Bosses", false, "Enable Game Stage needed for Rough Mob Bosses to spawn. Must also enable Equipment stage for this to work.");
		enableAbilitiesStage = RoughConfig.getBoolean("GameStages", "_Abilities", false, "Enable Game Stage needed for Rough Mobs to have abilities.");
		enableEnchantStage = RoughConfig.getBoolean("GameStages", "_Enchantments", false, "Enable Game Stage needed for Rough Mob equipment to be enchanted.");		
		isGameStagesEnabled = RoughConfig.getBoolean("GameStages", "_FeaturesEnabled", false, "Set to false to disable using Game Stages.", true);
	}
	
	public void postInit() {
		isGameStagesLoaded = Loader.isModLoaded("gamestages");
	}
	
	public void initConfig() {
		
	}
	
	public boolean hasDefaultConfig() {
		return true;
	}
	
	public static boolean isStagesEnabled() {
		if (isGameStagesLoaded && isGameStagesEnabled) {
			return true;
		} else {
			return false;
		}
	}
}
