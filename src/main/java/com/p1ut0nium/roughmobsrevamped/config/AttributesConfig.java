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

import com.p1ut0nium.roughmobsrevamped.misc.AttributeHelper;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class AttributesConfig {
	
	final ConfigValue<List<? extends String>> options;
	
	AttributesConfig(final ForgeConfigSpec.Builder builder) {
		
		// Hostile Horse Features
		
		builder.push("Attributes");
		builder.comment("Add attribute modifiers to entities to change their stats. Takes 4-6 values seperated by a semicolon.",
				"Format:        entity;attribute;operator;value;dimension;child",
				"entity:        entity name",
				"attribute:     attribute name (Possible attributes: " + AttributeHelper.getAttributesString() + ")",
				"operator:      0 = add new value to original attribute (x + y)",
				"               1 = multiply original attribute by new value and add the result to the original attribute ((x * y) + x)",
				"               2 = multiply original attribute by new value (x * y)",
				"value:         value which will be used for the calculation",
				"dimension:     dimension (ID) in which the entity should get the boost (optional! Leave this blank or use a \"/\" for any dimension)",
				"child:         0 or blank = adults and children",
				"               1 = adults only",
				"               2 = children only");
		options = builder
				.defineList("Attributes", Arrays.asList(Constants.ATTRIBUTE_DEFAULT), RoughConfig.ELEMENT_STRING_VALIDATOR);
		builder.pop();
	}
}
