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

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHolder {
	public static final ForgeConfigSpec MODCOMPAT_SPEC;
	static final ModCompatConfig MODCOMPAT;
	
	public static final ForgeConfigSpec SPAWNCONDITIONS_SPEC;
	static final SpawnConditionsConfig SPAWNCONDITIONS;
	
	public static final ForgeConfigSpec EQUIPMENT_SPEC;
	static final EquipmentConfig EQUIPMENT;
	
	public static final ForgeConfigSpec FEATURES_SPEC;
	static final FeaturesConfig FEATURES;
	
	public static final ForgeConfigSpec ATTRIBUTES_SPEC;
	static final AttributesConfig ATTRIBUTES;
	
	public static final ForgeConfigSpec FOG_SPEC;
	static final FogConfig FOG;
	
	static {
		{
			final Pair<ModCompatConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ModCompatConfig::new);
			MODCOMPAT = specPair.getLeft();
			MODCOMPAT_SPEC = specPair.getRight();
		}
		{
			final Pair<SpawnConditionsConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(SpawnConditionsConfig::new);
			SPAWNCONDITIONS = specPair.getLeft();
			SPAWNCONDITIONS_SPEC = specPair.getRight();
		}
		{
			final Pair<EquipmentConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(EquipmentConfig::new);
			EQUIPMENT = specPair.getLeft();
			EQUIPMENT_SPEC = specPair.getRight();
		}
		{
			final Pair<FeaturesConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(FeaturesConfig::new);
			FEATURES = specPair.getLeft();
			FEATURES_SPEC = specPair.getRight();
		}
		{
			final Pair<AttributesConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(AttributesConfig::new);
			ATTRIBUTES = specPair.getLeft();
			ATTRIBUTES_SPEC = specPair.getRight();
		}
		{
			final Pair<FogConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(FogConfig::new);
			FOG = specPair.getLeft();
			FOG_SPEC = specPair.getRight();
		}
	}
}
