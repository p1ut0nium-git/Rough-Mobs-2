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

public class AttributesConfig {
	
	final ForgeConfigSpec.ConfigValue<List<String>> options;
	
	AttributesConfig(final ForgeConfigSpec.Builder builder) {
		
		// Hostile Horse Features
		
		builder.push("Attributes");
		builder.comment("Add attribute modifiers to entities to change their stats. Takes 4-6 values seperated by a semicolon:",
				"Format: entity;attribute;operator;value;dimension;child",
				"entity:\\t\\tentity name",
				"attribute:\\tattribute name (Possible attributes: " + AttributeHelper.getAttributesString() + ")",
				"operator:\\t\\toperator type (0 = add, 1 = multiply and add)",
				"value:\\t\\tvalue which will be used for the calculation",
				"dimension:\\tdimension (ID) in which the entity should get the boost (optional! Leave this blank or use a \\\"/\\\" for any dimension)",
				"child:\\t0 = the modifier doesn't care if the entity is a child or not, 1 = adults only, 2 = childs only (optional! Leave this blank for 0)");
		options = builder
				.define("Attributes", Arrays.asList(Constants.ATTRIBUTE_DEFAULT));
		builder.pop();
	}
}

/*
fillMap(options);
*/
