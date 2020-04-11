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
import java.util.List;
import java.util.UUID;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.core.RoughMobsRevamped;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

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
	
	private static Multimap<EntityType<?>, AttributeEntry> map;

	public static final String KEY_ATTRIBUTES = Constants.unique("attributesApplied");
	
	public static class AttributeEntry {
		
		private UUID uuid;
		private String attribute;
		private Operation operator;
		private double value;
		private int dimension;
		private int child;
		
		public AttributeEntry(UUID uuid, String attribute, Operation operator2, double value, int dimension, int child) {
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
		
		public Operation getOperator() {
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
		fillMap(RoughConfig.attributes);
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
		
		Collection<AttributeEntry> attributes = map.get(entity.getType());
		
		int i = 0;
		for (AttributeEntry attribute : attributes) 
		{
			if (!attribute.checkChild(entity))
				continue;
			
			IAttributeInstance instance = entity.getAttributes().getAttributeInstanceByName(attribute.attribute);
			if (instance != null) 
			{
				AttributeModifier modifier = new AttributeModifier(attribute.getUuid(), Constants.unique("mod" + i), attribute.getValue(), attribute.getOperator());
				instance.applyModifier(modifier);
				
				if (instance.getAttribute() == SharedMonsterAttributes.MAX_HEALTH)
					entity.setHealth(entity.getMaxHealth());
			}
			else
				RoughMobsRevamped.LOGGER.error("Error on attribute modification: \"" + attribute.attribute + "\" is not a valid attribute. Affected Entity: " + entity);
			
			i++;
		}

		entity.getPersistentData().putBoolean(KEY_ATTRIBUTES, true);
	}

	private static void fillMap(List<String> attributes) {	
		map = ArrayListMultimap.create();
		
		for (String line : attributes) 
		{
			String[] pars = line.split(";");
			
			if (pars.length >= 4) 
			{		
				// OLD Class<? extends Entity> entityType = EntityList.getClass(new ResourceLocation(pars[0]));
				EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(pars[0]));
				if (entityType != null) 
				{
					try 
					{
						Operation operator = Operation.values()[Integer.parseInt(pars[2])];
						double value = Double.parseDouble(pars[3]);
						int dimension = pars.length >= 5 && !pars[4].equals("/") ? Integer.parseInt(pars[4]) : Integer.MIN_VALUE;
						int child = pars.length >= 6 ? Integer.parseInt(pars[5]) : 0;
						
						if (child < 0 || child > 2)
						{
							RoughMobsRevamped.LOGGER.error("Error on attribute initialization: child is not between 0 and 2: " + line);
							continue;
						}
						
						map.put(entityType, new AttributeEntry(UUID.randomUUID(), pars[1], operator, value, dimension, child));
					}
					catch(NumberFormatException e) 
					{
						RoughMobsRevamped.LOGGER.error("Error on attribute initialization: Invalid numbers: " + line);
					}
				}
				else
					RoughMobsRevamped.LOGGER.error("Error on attribute initialization: Entity " + pars[0] + " does not exist");
			}
			else
				RoughMobsRevamped.LOGGER.error("Error on attribute initialization: Wrong amount of arguments: " + line);
		}
	}

	public static String getAttributesString() {
		
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
	}
}
