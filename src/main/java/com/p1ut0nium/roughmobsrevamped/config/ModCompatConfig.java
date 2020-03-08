package com.p1ut0nium.roughmobsrevamped.config;

import java.util.Arrays;
import java.util.List;

import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModCompatConfig {
	
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	
	public static final ForgeConfigSpec.BooleanValue useAllStages;
	public static final ForgeConfigSpec.BooleanValue useEquipmentStage;
	public static final ForgeConfigSpec.BooleanValue useBossStage;
	public static final ForgeConfigSpec.BooleanValue useAbilitiesStage;
	public static final ForgeConfigSpec.BooleanValue useEnchantStage;
	
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> seasonWhiteList;

	static {
		
		// Game Stages
		
		BUILDER.comment("Config options for enabling/disabling Game Stages support.\n"
				+ "The mod Game Stages must be installed for these features to work.\n"
				+ "\n"
				+ "Stages to give player in game: \n"
				+ "\n"
				+ "roughmobsall - gives all stages below at once.\n"
				+ "roughmobsequip - allows mobs to spawn with equipment.\n"
				+ "roughmobsenchant - allows mob gear to be enchanted.\n"
				+ "roughmobsboss - allows some mobs to be bosses.\n"
				+ "roughmobsabils - allows mobs to have special abilities.");
		BUILDER.push("Game Stages");
		useAllStages = BUILDER
				.comment("Require this Game Stage to allow all Rough Mobs stages below to be given at once."
						+ "\nRequires the player to have 'roughmobsall'")
				.define("GameStages_AllStages", false);
		useEquipmentStage = BUILDER
				.comment("Require this Game Stage for Rough Mobs to have equipment."
						+ "\nRequires the player to have 'roughmobsequip'")
				.define("GameStages_Equipment", false);
		useBossStage = BUILDER
				.comment("Require this Game Stage for Rough Mob Bosses to spawn. \nMust also enable Equipment stage for this to work."
						+ "\nRequires the player to have 'roughmobsboss'")
				.define("GameStages_Bosses", false);
		useAbilitiesStage = BUILDER
				.comment("Require this Game Stage for Rough Mobs to have special combat AI and attributes."
						+ "\nRequired the player to have roughmobsabils")
				.define("GameStages_Abilities", false);
		useEnchantStage = BUILDER
				.comment("Require this Game Stage for Rough Mob equipment to be enchanted."
						+ "\nRequires the player to have roughmobsenchant")
				.define("GameStages_Enchantments", false);
		BUILDER.pop();
		
		// Serene Seasons
		
		BUILDER.comment("Config options for enabling/disabling Serene Seasons support.\n"
				+ "The mod Game Stages must be installed for these features to work.\n");
		BUILDER.push("Serene Seasons");
		seasonWhiteList = BUILDER
				.comment("Whitelist of all seasons that Rough Mobs can spawn in.")
				.defineList("Season_Whitelist", Arrays.asList(Constants.SEASONS), null);
		
		SPEC = BUILDER.build();
	}
}

