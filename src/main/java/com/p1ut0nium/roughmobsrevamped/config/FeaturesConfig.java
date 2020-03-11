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

public class FeaturesConfig {
	
	final ForgeConfigSpec.BooleanValue featuresEnabled;
	final ForgeConfigSpec.ConfigValue<List<String>> entityNames;

	@SuppressWarnings("unchecked")
	FeaturesConfig(final ForgeConfigSpec.Builder builder) {
		
		// Zombie Features
		
		builder.comment("Config options for Zombies.");
		builder.push("Zombie Features");
		featuresEnabled = builder
				.comment("Enable this to use all Zombie features.")
				.define("Features Enabled", true);
		entityNames = builder
				.comment("Entities which count as Zombies.")
				.define("Zombie Entities", Constants.getRegNames(Arrays.asList(Constants.ZOMBIES)));
		builder.pop();
	}
	
    private static final Predicate<Object> ENTITY_CLASS_VALIDATOR = (obj) -> {
    	List<Object> entityClasses = Arrays.asList(obj);
        try {
        	entityClasses.stream()
        	.filter(e -> e.getClass().equals(String.class))
        	.allMatch(e -> e.toString().endsWith(".class"));
        }
        catch (Exception e) {
            // Not all elements are a class, so return invalid
            return false;
        }

        System.out.println("All classes");
        return true;
    };
}