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

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.p1ut0nium.roughmobsrevamped.core.RoughMobsRevamped;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod.EventBusSubscriber
public class RoughConfig {
	
	// Mod Compat Config
	public static boolean useAllStages;
	public static boolean useEquipmentStage;
	public static boolean useBossStage;
	public static boolean useAbilitiesStage;
	public static boolean useEnchantStage;
	
	public static List<String> seasonWhiteList;
	
	// Spawn Conditions Config
	public static int minPlayerLevel;
	public static boolean mustBeUnderground;
	public static int maxSpawnHeight;
	public static int minDistFromSpawn;
	
	// Equipment Config
	public static int chancePerWeapon;
	public static int chancePerArmor;
	public static int chancePerEnchantment;
	public static float enchantMultiplier;
	public static float dropChance;
	
	public static List<String> equipMainhand;
	public static List<String> equipOffhand;
	public static List<String> equipHelmet;
	public static List<String> equipChestplate;
	public static List<String> equipLeggings;
	public static List<String> equipBoots;
	public static List<String> equipWeaponEnchants;
	public static List<String> equipArmorEnchants;
	
	public static boolean chanceTimeMultiplier;
	public static boolean chanceDistMultiplier;
	public static int distThreshold;
	public static boolean disableBabyZombieEquipment;
	
	// Features Config
	public static boolean featuresEnabled;
	public static List<? extends String> entityNames;
	
	// Fog Config
	public static boolean bossFogEnabled;
	public static List<Float> bossFogColor;
	public static int bossFogMaxDist;
	public static int bossFogStartDist;
	public static int bossFogFarPlane;
	public static float bossFogFarPlaneScale;
	
	public static boolean bossFogDoTEnabled;
	public static boolean bossFogDoTWarning;
	public static boolean bossFogPlayerCough;
	public static int bossFogDoTDelay;
	public static int bossFogDoTWarningTime;
	public static int bossFogDoTDamage;

	public static void init() {
		
		Path configPath = FMLPaths.CONFIGDIR.get();
		Path roughConfigPath = Paths.get(configPath.toAbsolutePath().toString(), Constants.MOD_CONFIG_DIRECTORY);
		
		try {
			Files.createDirectory(roughConfigPath);
		} catch (FileAlreadyExistsException e) {
			// Do nothing
		} catch (IOException e) {
			RoughMobsRevamped.LOGGER.error("Failed to create config directory.", e);
		}
	}
	
    public static void register(final ModLoadingContext context) {
        context.registerConfig(ModConfig.Type.COMMON, ConfigHolder.MODCOMPAT_SPEC, Constants.MOD_CONFIG_DIRECTORY + "/" + Constants.MOD_COMPAT_CONFIG_FILENAME);
        context.registerConfig(ModConfig.Type.COMMON, ConfigHolder.SPAWNCONDITIONS_SPEC, Constants.MOD_CONFIG_DIRECTORY + "/" + Constants.SPAWN_CONDITIONS_CONFIG_FILENAME);
        context.registerConfig(ModConfig.Type.COMMON, ConfigHolder.EQUIPMENT_SPEC, Constants.MOD_CONFIG_DIRECTORY + "/" + Constants.EQUIPMENT_CONFIG_FILENAME);
        context.registerConfig(ModConfig.Type.COMMON, ConfigHolder.FEATURES_SPEC, Constants.MOD_CONFIG_DIRECTORY + "/" + Constants.FEATURES_CONFIG_FILENAME);
        context.registerConfig(ModConfig.Type.COMMON, ConfigHolder.FOG_SPEC, Constants.MOD_CONFIG_DIRECTORY + "/" + Constants.FOG_CONFIG_FILENAME);
    }
}
