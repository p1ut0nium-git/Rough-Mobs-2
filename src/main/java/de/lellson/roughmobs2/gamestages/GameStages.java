package de.lellson.roughmobs2.gamestages;

import java.util.Arrays;
import java.util.List;

import de.lellson.roughmobs2.config.RoughConfig;
import de.lellson.roughmobs2.misc.Constants;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Loader;

public class GameStages {
	
	private static String gameStage;
	private static Boolean isGameStagesLoaded;
	private static Boolean isGameStagesEnabled;

	public void preInit() {
		if (!hasDefaultConfig())
			return;
	
		RoughConfig.getConfig().addCustomCategoryComment("GameStages", "Configuration options which affect GameStages features");
			
		gameStage = RoughConfig.getString("GameStages", "Name", "RoughMobs", "Game stage for Rough Mobs to have equipment.");
		isGameStagesEnabled = RoughConfig.getBoolean("GameStages", "FeaturesEnabled", false, "Set to false to disable ALL GameStages features. (GameStages must be installed)", true);
	}
	
	public void postInit() {
		isGameStagesLoaded = Loader.isModLoaded("gamestages");
	}
	
	public void initConfig() {
		
	}
	
	public boolean hasDefaultConfig() {
		return true;
	}
	
	public static void addGameStage(EntityPlayer player, String stageToAdd) {
		GameStageHelper.addStage(player, stageToAdd);
	}
	
	public static Boolean hasGameStage(EntityPlayer player) {
		if (isGameStagesLoaded) {
			return GameStageHelper.hasStage(player, gameStage);
		} else {
			return false;
		}
		
	}
	
	public static String getStages(EntityPlayer player) {
		return gameStage;
	}
	
	public static boolean isStagesEnabled() {
		if (isGameStagesLoaded && isGameStagesEnabled) {
			return true;
		} else {
			return false;
		}
	}
}
