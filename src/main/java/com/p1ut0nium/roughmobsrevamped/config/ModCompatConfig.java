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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraftforge.common.ForgeConfigSpec;

final class ModCompatConfig {
	
	final ForgeConfigSpec.BooleanValue useAllStages;
	final ForgeConfigSpec.BooleanValue useEquipmentStage;
	final ForgeConfigSpec.BooleanValue useBossStage;
	final ForgeConfigSpec.BooleanValue useAbilitiesStage;
	final ForgeConfigSpec.BooleanValue useEnchantStage;
	
	final ForgeConfigSpec.ConfigValue<List<String>> seasonWhiteList;

	ModCompatConfig(final ForgeConfigSpec.Builder builder) {
		
		// Game Stages
		
		builder.comment("Config options for enabling/disabling Game Stages support.");
		builder.push("Game Stages");
		useAllStages = builder
				.comment("Require this Game Stage to allow all Rough Mobs stages be given at once.")
				.define("GameStages_AllStages", false);		
		useEquipmentStage = builder
				.comment("Require this Game Stage for Rough Mobs to have equipment.")
				.define("GameStages_Equipment", false);
		useBossStage = builder
				.comment("Require this Game Stage for Rough Mob Bosses to spawn.")
				.define("GameStages_Bosses", false);
		useAbilitiesStage = builder
				.comment("Require this Game Stage for Rough Mobs to have special combat AI and attributes.")
				.define("GameStages_Abilities", false);
		useEnchantStage = builder
				.comment("Require this Game Stage for Rough Mob equipment to be enchanted.")
				.define("GameStages_Enchantments", false);
		builder.pop();
				
		// Serene Seasons
		
		builder.comment("Config options for enabling/disabling Serene Seasons support.");
		builder.push("Serene Seasons");
		seasonWhiteList = builder
				.comment("Whitelist of all seasons that Rough Mobs can spawn in.",
						"Default Seasons: SUMMER, AUTUMN, WINTER, SPRING")
				.define("Season_Whitelist", Arrays.asList(Constants.SEASONS), SEASON_VALIDATOR);
		builder.pop();
	}
	
    private static final Predicate<Object> SEASON_VALIDATOR = (obj) -> {
    	List<Object> seasons = Arrays.asList(obj);
        try {
        	seasons.stream().allMatch(e -> e.getClass().equals(String.class));
        }
        catch (Exception e) {
            // Not all elements are a string, so return invalid
            return false;
        }

        return true;
    };
}

