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

import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraftforge.common.ForgeConfigSpec;

public class EquipmentConfig {
	
	final ForgeConfigSpec.IntValue chancePerWeapon;
	final ForgeConfigSpec.IntValue chancePerArmor;
	final ForgeConfigSpec.IntValue chancePerEnchantment;
	final ForgeConfigSpec.DoubleValue enchantMultiplier;
	final ForgeConfigSpec.DoubleValue dropChance;

	final ForgeConfigSpec.ConfigValue<List<? extends String>> equipMainhand;
	final ForgeConfigSpec.ConfigValue<List<? extends String>> equipOffhand;
	final ForgeConfigSpec.ConfigValue<List<? extends String>> equipHelmet;
	final ForgeConfigSpec.ConfigValue<List<? extends String>> equipChestplate;
	final ForgeConfigSpec.ConfigValue<List<? extends String>> equipLeggings;
	final ForgeConfigSpec.ConfigValue<List<? extends String>> equipBoots;
	final ForgeConfigSpec.ConfigValue<List<? extends String>> equipWeaponEnchants;
	final ForgeConfigSpec.ConfigValue<List<? extends String>> equipArmorEnchants;
	
	final ForgeConfigSpec.BooleanValue chanceTimeMultiplier;
	final ForgeConfigSpec.BooleanValue chanceDistMultiplier;
	final ForgeConfigSpec.IntValue distThreshold;
	final ForgeConfigSpec.BooleanValue disableBabyZombieEquipment;

	EquipmentConfig(final ForgeConfigSpec.Builder builder) {
		
		// Global Equipment Options
		
		builder.push("Global Equipment Options");
		builder.comment("Options to control equipment spawning across all mobs that can wear equipment.");
		chanceTimeMultiplier = builder
				.comment("Should Rough Mobs get more gear as it gets closer to midnight?")
				.define("Time_Multiplier", true);
		chanceDistMultiplier = builder
				.comment("Should Rough Mobs get more gear based upon distance from world spawn?")
				.define("Distance_Multiplier", true);
		distThreshold = builder
				.comment("The distance threshold used to calculate the Distance Multiplier.",
						"A shorter distance here means mobs will have more gear closer to the World Spawn.")
				.defineInRange("Distance_Threshold", 1000, 0, Short.MAX_VALUE);
		disableBabyZombieEquipment = builder
				.comment("Set to true to disable baby zombies getting equipment.")
				.define("Disable_BabyZombie_Equipment", true);
		builder.pop();
		
		// Zombie Equipment
		// TODO make this work for both zombies and skeletons without code duplication
		
		builder.push("Zombie Equipment Chances");
		chancePerWeapon = builder
				.comment("Chance (1 in X per hand) to give a Zombie a weapon on spawn.",
						"NOTE: Champions always spawn with weapons.",
						"Set to 0 to disable.")
				.defineInRange("Weapon_Chance", Constants.CHANCE_PER_WEAPON_DEFAULT, 0, Short.MAX_VALUE);
		chancePerArmor = builder
				.comment("Chance (1 in X per piece) to give a Zombie a piece of armor on spawn.",
						"NOTE: Champions always spawn with all armor pieces.",
						"Set to 0 to disable.")
				.defineInRange("Armor_Chance", Constants.CHANCE_PER_ARMOR_DEFAULT, 0, Short.MAX_VALUE);
		chancePerEnchantment = builder
				.comment("Chance (1 in X per item) to enchant any given weapons or armor.",
						"NOTE: Champions always spawn with enchanted weapons and armor.",
						"Set to 0 to disable.")
				.defineInRange("Enchantment_Chance", Constants.CHANCE_PER_ENCHANT_DEFAULT, 0, Short.MAX_VALUE);
		enchantMultiplier = builder
				.comment("Multiplier for the applied enchantment level with the max level. The level can still be a bit lower.", 
						"e.g. 0.5 would make sharpness to be at most level 3 (5 x 0.5 = 2.5 and [2.5] = 3) and fire aspect would always be level 1 (2 x 0.5 = 1).")
				.defineInRange("Enchantment_Multiplier", Constants.ENCHANT_MULTIPLIER_DEFAULT, 0F, 1F);
		dropChance = builder
				.comment("Chance (per slot) that the Zombie drops the equipped item (1 = 100%, 0 = 0%).")
				.defineInRange("Drop_Chance", Constants.DROP_CHANCE_DEFAULT, 0F, 1F);
		builder.pop();
		
		builder.push("Zombie Equipment");
		builder.comment("Add enchanted armor and weapons to a newly spawned zombie. Takes 2-3 values seperated by a semicolon.",
				"Format:              item or enchantment;chance;dimension",
				"item or enchantment: the item/enchantment id", 
				"chance:              the higher this number the more this item/enchantment gets selected",
				"dimension:           dimension (ID) in which the item/enchantment can be selected (optional! Leave this blank for any dimension)");
		equipMainhand = builder
				.defineList("Mainhand_Weapons", Arrays.asList(Constants.DEFAULT_MAINHAND), RoughConfig.ELEMENT_STRING_VALIDATOR);
		equipOffhand = builder
				.defineList("Offhand_Weapons", Arrays.asList(Constants.DEFAULT_OFFHAND), RoughConfig.ELEMENT_STRING_VALIDATOR);
		equipHelmet = builder
				.defineList("Helmets", Arrays.asList(Constants.DEFAULT_HELMETS), RoughConfig.ELEMENT_STRING_VALIDATOR);
		equipChestplate = builder
				.defineList("Chestplates", Arrays.asList(Constants.DEFAULT_CHESTPLATES), RoughConfig.ELEMENT_STRING_VALIDATOR);
		equipLeggings = builder
				.defineList("Leggings", Arrays.asList(Constants.DEFAULT_LEGGINGS), RoughConfig.ELEMENT_STRING_VALIDATOR);
		equipBoots = builder
				.defineList("Boots", Arrays.asList(Constants.DEFAULT_BOOTS), RoughConfig.ELEMENT_STRING_VALIDATOR);
		equipWeaponEnchants = builder
				.defineList("Weapon_Enchantments", Arrays.asList(Constants.DEFAULT_WEAPON_ENCHANTS), RoughConfig.ELEMENT_STRING_VALIDATOR);
		equipArmorEnchants = builder
				.defineList("Armor_Enchantments", Arrays.asList(Constants.DEFAULT_ARMOR_ENCHANTS), RoughConfig.ELEMENT_STRING_VALIDATOR);
		builder.pop();
		
		// TODO Skeleton Equipment
		
	}
}
