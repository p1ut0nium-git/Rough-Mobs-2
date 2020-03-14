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
package com.p1ut0nium.roughmobsrevamped.misc;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.UUID;

import com.google.common.collect.Multimap;
import com.p1ut0nium.roughmobsrevamped.core.RoughMobsRevamped;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;
import com.google.common.collect.ArrayListMultimap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class AttributeHelper {
	
	private static Multimap<Class<? extends Entity>, AttributeEntry> map;

	public static final String KEY_ATTRIBUTES = Constants.unique("attributesApplied");
	
	public static class AttributeEntry {
		
		private UUID uuid;
		private String attribute;
		private int operator;
		private double value;
		private int dimension;
		private int child;
		
		public AttributeEntry(UUID uuid, String attribute, int operator2, double value, int dimension, int child) {
			this.uuid = uuid;
			this.attribute = attribute;
			this.operator = operator2;
			this.value = value;
			this.dimension = dimension;
			this.child = child;
		}
		
		public UUID getUuid() {
			return uuid;
		}
		
		public String getAttribute() {
			return attribute;
		}
		
		public int getOperator() {
			return operator;
		}
		
		public double getValue() {
			return value;
		}
		
		public int getDimension() {
			return dimension;
		}
		
		public int getChild() {
			return child;
		}
		
		public boolean hasDimension() {
			return dimension != Integer.MIN_VALUE;
		}

		public boolean checkChild(LivingEntity entity) {
			return child == 0 || (child == 1 && !entity.isChild()) || (child == 2 && entity.isChild());
		}
	}

	public static void initAttributeOption() {
		
		/* TODO Config
		RoughConfig.getConfig().addCustomCategoryComment("attributes", "Add attribute modifiers to entities to change their stats. Takes 4-6 values seperated by a semicolon:\n"
																		+ "Format: entity;attribute;operator;value;dimension;child\n"
																		+ "entity:\t\tentity name\n"
																		+ "attribute:\tattribute name (Possible attributes: " + getAttributesString() + ")\n"
																		+ "operator:\t\toperator type (0 = add, 1 = multiply and add)\n"
																		+ "value:\t\tvalue which will be used for the calculation\n"
																		+ "dimension:\tdimension (ID) in which the entity should get the boost (optional! Leave this blank or use a \"/\" for any dimension)\n"
																		+ "child:\t0 = the modifier doesn't care if the entity is a child or not, 1 = adults only, 2 = childs only (optional! Leave this blank for 0)");
		
		String[] options = RoughConfig.getStringArray("attributes", "Modifier", Constants.ATTRIBUTE_DEFAULT, "Attributes:");
		fillMap(options);
		*/
	}
	
	public static boolean applyAttributeModifier(LivingEntity entity, IAttribute attribute, String name, Operation operator, double amount) {
		
		if (amount != 0) 
		{
			IAttributeInstance instance = entity.getAttribute(attribute);
			AttributeModifier modifier = new AttributeModifier(Constants.unique(name), amount, operator);
			
			if (instance != null && !instance.hasModifier(modifier)) 
			{
				instance.applyModifier(modifier);
				if (attribute == SharedMonsterAttributes.MAX_HEALTH) 
					entity.setHealth(entity.getMaxHealth());	
				return true;
			}
		}
		
		return false;
	}
	
	
	public static void addAttributes(LivingEntity entity) {
		
		if (entity.getPersistentData().getBoolean(KEY_ATTRIBUTES))
			return;
		
		Collection<AttributeEntry> attributes = map.get(entity.getClass());
		
		int i = 0;
		for (AttributeEntry attribute : attributes) 
		{
			if (!attribute.checkChild(entity))
				continue;
			
			IAttributeInstance instance = entity.getAttributes().getAttributeInstanceByName(attribute.attribute);
			if (instance != null) 
			{
				/* TODO Operator
				AttributeModifier modifier = new AttributeModifier(attribute.getUuid(), Constants.unique("mod" + i), attribute.getValue(), attribute.getOperator());
				instance.applyModifier(modifier);
				
				if (instance.getAttribute() == SharedMonsterAttributes.MAX_HEALTH)
					entity.setHealth(entity.getMaxHealth());
				*/
			}
			else
				RoughMobsRevamped.LOGGER.error("Error on attribute modification: \"" + attribute.attribute + "\" is not a valid attribute. Affected Entity: " + entity);
			
			i++;
		}

		entity.getPersistentData().putBoolean(KEY_ATTRIBUTES, true);
	}

	private static void fillMap(String[] options) {	
		map = ArrayListMultimap.create();
		
		for (String line : options) 
		{
			String[] pars = line.split(";");
			
			if (pars.length >= 4) 
			{
				/* TODO getClass using resource location
				Class<? extends Entity> entityClass = EntityList.getClass(new ResourceLocation(pars[0]));
				if (entityClass != null) 
				{
					try 
					{
						int operator = Integer.parseInt(pars[2]);
						double value = Double.parseDouble(pars[3]);
						int dimension = pars.length >= 5 && !pars[4].equals("/") ? Integer.parseInt(pars[4]) : Integer.MIN_VALUE;
						int child = pars.length >= 6 ? Integer.parseInt(pars[5]) : 0;
						
						if (child < 0 || child > 2)
						{
							RoughMobsRevamped.LOGGER.error("Error on attribute initialization: child is not between 0 and 2: " + line);
							continue;
						}
						
						map.put(entityClass, new AttributeEntry(UUID.randomUUID(), pars[1], operator, value, dimension, child));
					}
					catch(NumberFormatException e) 
					{
						RoughMobsRevamped.LOGGER.error("Error on attribute initialization: Invalid numbers: " + line);
					}
				}
				else
					RoughMobsRevamped.LOGGER.error("Error on attribute initialization: Entity " + pars[0] + " does not exist");
				*/
			}
			else
				RoughMobsRevamped.LOGGER.error("Error on attribute initialization: Wrong amount of arguments: " + line);
		}
	}

	private static String getAttributesString() {
		
		String attributes = "";
		
		try
		{
			for (Field field : SharedMonsterAttributes.class.getFields()) 
			{
				field.setAccessible(true);
				Object obj = field.get(null);
				if (obj != null && obj instanceof IAttribute) 
				{
					attributes += ", " + ((IAttribute)obj).getName();
				}
			}
		}
		catch(Exception e) {}
		
		return attributes.length() > 2 ? attributes.substring(2) : "";	
	    //return "generic.maxHealth, generic.followRange, generic.knockbackResistance, generic.movementSpeed, generic.flyingSpeed, generic.attackDamage, generic.attackSpeed, generic.armor, generic.armorToughness, generic.luck";
	}
}
