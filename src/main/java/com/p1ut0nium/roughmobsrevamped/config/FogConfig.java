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

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraftforge.common.ForgeConfigSpec;

public class FogConfig {
	
	final ForgeConfigSpec.BooleanValue bossFogEnabled;
	final ForgeConfigSpec.ConfigValue<List<Float>> bossFogColor;
	final ForgeConfigSpec.IntValue bossFogMaxDist;
	final ForgeConfigSpec.IntValue bossFogStartDist;
	final ForgeConfigSpec.IntValue bossFogFarPlane;
	final ForgeConfigSpec.DoubleValue bossFogFarPlaneScale;
	
	final ForgeConfigSpec.BooleanValue bossFogDoTEnabled;
	final ForgeConfigSpec.BooleanValue bossFogDoTWarning;
	final ForgeConfigSpec.BooleanValue bossFogPlayerCough;
	final ForgeConfigSpec.IntValue bossFogDoTDelay;
	final ForgeConfigSpec.IntValue bossFogDoTWarningTime;
	final ForgeConfigSpec.IntValue bossFogDoTDamage;
	
	FogConfig(final ForgeConfigSpec.Builder builder) {
		
		// Base Fog Settings
		
		builder.comment("Config options for controlling how champion fog looks.");
		builder.push("Boss Fog");
		bossFogEnabled = builder
				.comment("Enable this to have thick colored fog around champions.")
				.define("Fog_Enabled", true);
		bossFogColor = builder
				.comment("Change these three values between 0.0 and 1.0 to change the fog color.",
						"Red, Green, Blue")
				.define("Fog_Color", Arrays.asList(Constants.FOG_COLORS), FOG_COLOR_VALIDATOR);
		bossFogStartDist = builder
				.comment("How far away from a champion before fog begins to fade from maximum density.", 
						"Must be a value lower than Max_Fog_Distance")
				.defineInRange("Fog_Start_Distance", 1, 0, Short.MAX_VALUE);
		bossFogMaxDist = builder
				.comment("Max distance from champion for fog to render.", 
						"Fog will only occur if you are within this distance.")
				.defineInRange("Max_Fog_Distance", 20, 0, Short.MAX_VALUE);
		bossFogFarPlane = builder
				.comment("This effects how far away from you before the fog is at maximum thickness.")
				.defineInRange("Fog_FarPlane", 10, 0, 192);
		bossFogFarPlaneScale = builder
				.comment("This value works to scale the far plane distance, and effects the percieved thickness of the fog.")
				.defineInRange("Fog_FarPlane_Scale", 0.2F, 0.0F, 0.8F);
		builder.pop();
		
		// Fog DoT Settings
		
		builder.comment("Config options for enabling and adjusting the poisonous version of boss fog.");
		builder.push("DoT Fog");
		bossFogDoTEnabled = builder
				.comment("If enabled, boss fog will cause poison damage over time.")
				.define("DoT_Enabled", true);
		bossFogDoTDelay = builder
				.comment("The cooldown (in seconds) between each time the player receives damage while inside the fog.")
				.defineInRange("DoT_Cooldown", 10, 1, Short.MAX_VALUE);
		bossFogDoTDamage = builder
				.comment("How many half hearts the fog DoT does per hit.")
				.defineInRange("DoT_Damage", 1, 1, Short.MAX_VALUE);
		bossFogDoTWarning = builder
				.comment("Should the player recieve a chat warning message when entering the poisonous fog?")
				.define("DoT_Warning", true);
		bossFogDoTWarningTime = builder
				.comment("Controls how frequently (in seconds) a player can be warned when entering boss fog.")
				.defineInRange("DoT_Warning_Timer", 60, 1, Short.MAX_VALUE);
		bossFogPlayerCough = builder
				.comment("Disable this if you find the player cough sound annoying. \nOnly happens in poisonous fog.")
				.define("DoT_Coughing", true);
		builder.pop();
	}
	
	
	// private static Predicate<Object> isFloat = e -> e.getClass().equals(Float.class);
	
    private static final Predicate<Object> FOG_COLOR_VALIDATOR = (obj) -> {
    	List<Object> fogColor = Arrays.asList(obj);
        try {
        	fogColor.stream().allMatch(e -> e.getClass().equals(Float.class));
        }
        catch (Exception e) {
            // Not all elements are a string, so return invalid
            return false;
        }

        return true;
    };
}