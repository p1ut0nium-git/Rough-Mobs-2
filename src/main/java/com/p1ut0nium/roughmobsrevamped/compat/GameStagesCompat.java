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

import java.util.HashMap;

import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

public abstract class GameStagesCompat {
	
	private GameStagesCompat() {}
	
	private static boolean registered;

	private static Boolean useEquipmentStage;
	private static Boolean useAllStages;
	private static Boolean useBossStage;
	private static Boolean useEnchantStage;
	private static Boolean useAbilitiesStage;

	private static HashMap<String, Boolean> playerStages = new HashMap<>();
	public static HashMap<PlayerEntity, HashMap<String, Boolean>> players = new HashMap<>();

	public static void register() {
		if (registered)
			return;
		registered = true;
		preInit();
	}
	
	public static void preInit() {
	
		useAllStages = RoughConfig.useAllStages;

		// If useAllStages is true, then all other stages should also be set to true
		if (useAllStages) {
			useEquipmentStage = useBossStage = useAbilitiesStage = useEnchantStage = true;
		}
		else {
			useEquipmentStage = RoughConfig.useEquipmentStage;
			useBossStage = RoughConfig.useBossStage;
			useAbilitiesStage = RoughConfig.useAbilitiesStage;
			useEnchantStage = RoughConfig.useEnchantStage;
		}
	}
	
	public static void syncPlayerGameStages(ServerPlayerEntity player) {
		
		if (registered) {
			
			GameStageHelper.syncPlayer(player);
			
			// put stuff into inner map
			playerStages.put(Constants.PLAYER_EQUIPMENT_STAGE, GameStageHelper.hasAnyOf(player, Constants.ROUGHMOBSALL, Constants.ROUGHMOBSEQUIP));
			playerStages.put(Constants.PLAYER_ABILITIES_STAGE, GameStageHelper.hasAnyOf(player, Constants.ROUGHMOBSALL, Constants.ROUGHMOBSABILS));
			playerStages.put(Constants.PLAYER_BOSS_STAGE, GameStageHelper.hasAnyOf(player, Constants.ROUGHMOBSALL, Constants.ROUGHMOBSBOSS));
			playerStages.put(Constants.PLAYER_ENCHANT_STAGE, GameStageHelper.hasAnyOf(player, Constants.ROUGHMOBSALL, Constants.ROUGHMOBSENCHANT));
			
			// put players into outer map
			players.put(player, playerStages);
		}
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
