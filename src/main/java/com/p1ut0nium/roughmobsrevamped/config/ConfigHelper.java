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
package com.p1ut0nium.roughmobsrevamped.config;

import net.minecraftforge.fml.config.ModConfig;

public class ConfigHelper {
	private static ModConfig modCompatConfig;
	private static ModConfig spawnConditionsConfig;
	private static ModConfig equipmentConfig;
	private static ModConfig featuresConfig;
	private static ModConfig fogConfig;

	// Mod Compatibility
	public static void bakeModCompat(final ModConfig config) {
		modCompatConfig = config;

		RoughConfig.useAllStages = ConfigHolder.MODCOMPAT.useAllStages.get();
		RoughConfig.useAbilitiesStage = ConfigHolder.MODCOMPAT.useAbilitiesStage.get();
		RoughConfig.useBossStage = ConfigHolder.MODCOMPAT.useBossStage.get();
		RoughConfig.useEnchantStage = ConfigHolder.MODCOMPAT.useEnchantStage.get();
		RoughConfig.useEquipmentStage = ConfigHolder.MODCOMPAT.useEquipmentStage.get();
		
		RoughConfig.seasonWhiteList = ConfigHolder.MODCOMPAT.seasonWhiteList.get();
	}
	
	// Spawn Conditions
	public static void bakeSpawnConditions(final ModConfig config) {
		spawnConditionsConfig = config;

		RoughConfig.minPlayerLevel = ConfigHolder.SPAWNCONDITIONS.minPlayerLevel.get();
		RoughConfig.mustBeUnderground = ConfigHolder.SPAWNCONDITIONS.mustBeUnderground.get();
		RoughConfig.maxSpawnHeight = ConfigHolder.SPAWNCONDITIONS.maxSpawnHeight.get();
		RoughConfig.minDistFromSpawn = ConfigHolder.SPAWNCONDITIONS.minDistFromSpawn.get();
	}
	
	// Equipment
	public static void bakeEquipment(final ModConfig config) {
		equipmentConfig = config;

		RoughConfig.chancePerWeapon = ConfigHolder.EQUIPMENT.chancePerWeapon.get();
		RoughConfig.chancePerArmor = ConfigHolder.EQUIPMENT.chancePerArmor.get();
		RoughConfig.chancePerEnchantment = ConfigHolder.EQUIPMENT.chancePerEnchantment.get();
		RoughConfig.enchantMultiplier = ConfigHolder.EQUIPMENT.enchantMultiplier.get().floatValue();
		RoughConfig.dropChance = ConfigHolder.EQUIPMENT.dropChance.get().floatValue();
		
		RoughConfig.chanceTimeMultiplier = ConfigHolder.EQUIPMENT.chanceTimeMultiplier.get();
		RoughConfig.chanceDistMultiplier = ConfigHolder.EQUIPMENT.chanceDistMultiplier.get();
		RoughConfig.distThreshold = ConfigHolder.EQUIPMENT.distThreshold.get();
		RoughConfig.disableBabyZombieEquipment = ConfigHolder.EQUIPMENT.disableBabyZombieEquipment.get();

		RoughConfig.equipMainhand = ConfigHolder.EQUIPMENT.equipMainhand.get();
		RoughConfig.equipOffhand = ConfigHolder.EQUIPMENT.equipOffhand.get();
		RoughConfig.equipHelmet = ConfigHolder.EQUIPMENT.equipHelmet.get();
		RoughConfig.equipChestplate = ConfigHolder.EQUIPMENT.equipChestplate.get();
		RoughConfig.equipLeggings = ConfigHolder.EQUIPMENT.equipLeggings.get();
		RoughConfig.equipBoots = ConfigHolder.EQUIPMENT.equipBoots.get();
		RoughConfig.equipWeaponEnchants = ConfigHolder.EQUIPMENT.equipWeaponEnchants.get();
		RoughConfig.equipArmorEnchants = ConfigHolder.EQUIPMENT.equipArmorEnchants.get();
	}
	
	// Features
	public static void bakeFeatures(final ModConfig config) {
		featuresConfig = config;
		
		RoughConfig.horseFeaturesEnabled = ConfigHolder.FEATURES.horseFeaturesEnabled.get();
		RoughConfig.hostileHorseEntities = ConfigHolder.FEATURES.hostileHorseEntities.get();
		RoughConfig.hostileHorseBurn = ConfigHolder.FEATURES.hostileHorseBurn.get();
		RoughConfig.hostileHorseCanDespawn = ConfigHolder.FEATURES.hostileHorseCanDespawn.get();
		RoughConfig.hostileHorseRiderChance = ConfigHolder.FEATURES.hostileHorseRiderChance.get();
		
		RoughConfig.spiderFeaturesEnabled = ConfigHolder.FEATURES.spiderFeaturesEnabled.get();
		RoughConfig.spiderEntities = ConfigHolder.FEATURES.spiderEntities.get();
		RoughConfig.spiderIgnoreFallDamage = ConfigHolder.FEATURES.spiderIgnoreFallDamage.get().floatValue();
		RoughConfig.spiderRiderChance = ConfigHolder.FEATURES.spiderRiderChance.get();
		RoughConfig.spiderRiderChanceRandom = ConfigHolder.FEATURES.spiderRiderChanceRandom.get();
		RoughConfig.spiderRiderEntities = ConfigHolder.FEATURES.spiderRiderEntities.get();
		RoughConfig.spiderSlownessChance = ConfigHolder.FEATURES.spiderSlownessChance.get();
		RoughConfig.spiderSlownessCreateWeb = ConfigHolder.FEATURES.spiderSlownessCreateWeb.get();
		RoughConfig.spiderSlownessDuration = ConfigHolder.FEATURES.spiderSlownessDuration.get();
		
		RoughConfig.zombieFeaturesEnabled = ConfigHolder.FEATURES.zombieFeaturesEnabled.get();
		RoughConfig.zombieEntities = ConfigHolder.FEATURES.zombieEntities.get();
		RoughConfig.zombieBabyBurn = ConfigHolder.FEATURES.zombieBabyBurn.get();
		RoughConfig.zombieHelmetBurn = ConfigHolder.FEATURES.zombieHelmetBurn.get();
		RoughConfig.zombieHorseChance = ConfigHolder.FEATURES.zombieHorseChance.get();
		RoughConfig.zombieHorseMinY = ConfigHolder.FEATURES.zombieHorseMinY.get();
		RoughConfig.zombieHungerChance = ConfigHolder.FEATURES.zombieHungerChance.get();
		RoughConfig.zombieHungerDuration = ConfigHolder.FEATURES.zombieHungerDuration.get();
		RoughConfig.zombieLeapChance = ConfigHolder.FEATURES.zombieLeapChance.get();
		RoughConfig.zombieChampionChance = ConfigHolder.FEATURES.zombieChampionChance.get();
		RoughConfig.zombieLeapHeight = ConfigHolder.FEATURES.zombieLeapHeight.get().floatValue();
		RoughConfig.zombieBreakBlocks = ConfigHolder.FEATURES.zombieBreakBlocks.get();
		RoughConfig.zombieChampionNames = ConfigHolder.FEATURES.zombieChampionNames.get();
	}
	
	// Fog
	public static void bakeFog(final ModConfig config) {
		fogConfig = config;

		RoughConfig.bossFogEnabled = ConfigHolder.FOG.bossFogEnabled.get();
		RoughConfig.bossFogColor = ConfigHolder.FOG.bossFogColor.get();
		RoughConfig.bossFogMaxDist = ConfigHolder.FOG.bossFogMaxDist.get();
		RoughConfig.bossFogStartDist = ConfigHolder.FOG.bossFogStartDist.get();
		RoughConfig.bossFogFarPlane = ConfigHolder.FOG.bossFogFarPlane.get();
		RoughConfig.bossFogFarPlaneScale = ConfigHolder.FOG.bossFogFarPlaneScale.get().floatValue();

		RoughConfig.bossFogDoTEnabled = ConfigHolder.FOG.bossFogDoTEnabled.get();
		RoughConfig.bossFogDoTWarning = ConfigHolder.FOG.bossFogDoTWarning.get();
		RoughConfig.bossFogPlayerCough = ConfigHolder.FOG.bossFogPlayerCough.get();
		RoughConfig.bossFogDoTDelay= ConfigHolder.FOG.bossFogDoTDelay.get();
		RoughConfig.bossFogDoTWarningTime = ConfigHolder.FOG.bossFogDoTWarningTime.get();
		RoughConfig.bossFogDoTDamage = ConfigHolder.FOG.bossFogDoTDamage.get();
	}

	/**
	 * Helper method to set a value on a config and then save the config.
	 *
	 * @param modConfig The ModConfig to change and save
	 * @param path      The name/path of the config entry
	 * @param newValue  The new value of the config entry
	 */
	public static void setValueAndSave(final ModConfig modConfig, final String path, final Object newValue) {
		modConfig.getConfigData().set(path, newValue);
		modConfig.save();
	}
}
