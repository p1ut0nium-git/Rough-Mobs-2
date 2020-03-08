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

public class FogConfig {
	
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	
	public static final ForgeConfigSpec.BooleanValue bossFogEnabled;
	
	public static final ForgeConfigSpec.BooleanValue bossFogDoTEnabled;
	public static final ForgeConfigSpec.BooleanValue bossFogDoTWarning;
	public static final ForgeConfigSpec.BooleanValue bossFogPlayerCough;
	public static final ForgeConfigSpec.IntValue bossFogDoTDelay;
	public static final ForgeConfigSpec.IntValue bossFogDoTWarningTime;
	public static final ForgeConfigSpec.IntValue bossFogDoTDamage;

	static {
		
		// Base Fog Settings
		
		BUILDER.comment("Config options for controlling how boss fog looks.");
		BUILDER.push("Boss Fog");
		bossFogEnabled = BUILDER
				.comment("Enable this to have thick colored fog around bosses.")
				.define("Fog_Enabled", true);
		BUILDER.pop();
		
		// Fog DoT Settings
		
		BUILDER.comment("Config options for enabling and adjusting the poisonous version of boss fog.");
		BUILDER.push("DoT Fog");
		bossFogDoTEnabled = BUILDER
				.comment("If enabled, boss fog will cause poison damage over time.")
				.define("DoT_Enabled", true);
		bossFogDoTDelay = BUILDER
				.comment("The cooldown (in seconds) between each time the player receives damage while inside the fog.")
				.defineInRange("DoT_Cooldown", 10, 1, Short.MAX_VALUE);
		bossFogDoTDamage = BUILDER
				.comment("How many half hearts the fog DoT does per hit.")
				.defineInRange("DoT_Damage", 1, 1, Short.MAX_VALUE);
		bossFogDoTWarning = BUILDER
				.comment("Should the player recieve a chat warning message when entering the poisonous fog?")
				.define("DoT_Warning", true);
		bossFogDoTWarningTime = BUILDER
				.comment("Controls how frequently (in seconds) a player can be warned when entering boss fog.")
				.defineInRange("DoT_Warning_Timer", 60, 1, Short.MAX_VALUE);
		bossFogPlayerCough = BUILDER
				.comment("Disable this if you find the player cough sound annoying. \nOnly happens in poisonous fog.")
				.define("DoT_Coughing", true);
		BUILDER.pop();
		
		SPEC = BUILDER.build();
	}
}

/* TODO
bossFogColor = RoughConfig.getStringArray("BossFog", "_FogColor", Constants.FOG_COLORS, "Change these three values between 0.0 and 1.0 to change the fog color.\nRed, Green, Blue\n");
bossFogMaxDistance = RoughConfig.getInteger("BossFog", "_FogMaxDist", 20, 0, 100, "Max distance from boss for fog to render.\nFog will only occur if you are within this distance");
bossFogStartDistance = RoughConfig.getInteger("BossFog", "_FogStartDist", 1, 0, 50, "How far away from boss before fog begins to fade from maximum density.\nMust be a value lower than BossFogMaxDist");
bossFogFarPlane = RoughConfig.getInteger("BossFog", "_FogFarPlane", 10, 0, 192, "This effects how far away from you before the fog is at maximum thickness.");
bossFogFarPlaneScale = RoughConfig.getFloat("BossFog", "_FogFarPlaneScale", 0.2F, 0.0F, 0.8F, "This controls how thick/strong the fog is.");
*/