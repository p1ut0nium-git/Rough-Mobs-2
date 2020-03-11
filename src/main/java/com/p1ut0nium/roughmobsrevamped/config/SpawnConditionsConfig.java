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

import net.minecraftforge.common.ForgeConfigSpec;

public class SpawnConditionsConfig {
	
	final ForgeConfigSpec.IntValue minPlayerLevel;
	final ForgeConfigSpec.BooleanValue mustBeUnderground;
	final ForgeConfigSpec.IntValue maxSpawnHeight;
	final ForgeConfigSpec.IntValue minDistFromSpawn;

	SpawnConditionsConfig(final ForgeConfigSpec.Builder builder) {
		
		builder.comment("Config options for determining if a Rough Mob can spawn.");
		builder.push("Spawn Conditions");
		minPlayerLevel = builder
				.comment("Player's Minecraft Experience Level required before a Rough Mob will spawn.")
				.defineInRange("Player_XP_Level", 0, 0, Short.MAX_VALUE);
		maxSpawnHeight = builder
				.comment("Set maximum height for Rough Mobs to spawn. Works in conjunction with MustBeUnderground.")
				.defineInRange("Max_Spawn_Height", 256, 0, 256);
		mustBeUnderground = builder
				.comment("Enable this to require Rough Mobs be underground in order to spawn.")
				.define("Spawn_Underground", false);
		minDistFromSpawn = builder
				.comment("Set the minimum distance from the world spawn before a Rough Mob can spawn.")
				.defineInRange("Min_Distance_From_Spawn", 0, 0, Short.MAX_VALUE);
		builder.pop();
	}
}