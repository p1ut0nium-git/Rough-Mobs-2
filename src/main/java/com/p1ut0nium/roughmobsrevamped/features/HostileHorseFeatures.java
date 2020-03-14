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
package com.p1ut0nium.roughmobsrevamped.features;

import java.util.ArrayList;
import java.util.List;

import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.entity.ai.goal.RoughAIDespawnGoal;
import com.p1ut0nium.roughmobsrevamped.entity.ai.goal.RoughAISearchForRiderGoal;
import com.p1ut0nium.roughmobsrevamped.entity.ai.goal.RoughAISunlightBurnGoal;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class HostileHorseFeatures extends EntityFeatures {

	public static final String ROUGH_HORSE = Constants.unique("roughhorse");
	
	private boolean horseBurn;
	private int randomRiderChance; 
	private boolean canDespawn;
	
	public HostileHorseFeatures() {
		super("hostileHorse", Constants.HOSTILE_HORSES);
	}
	
	@Override
	public void initConfig() {
		super.initConfig();

		horseBurn = RoughConfig.hostileHorseBurn;
		randomRiderChance = RoughConfig.hostileHorseRiderChance;
		canDespawn = RoughConfig.hostileHorseCanDespawn;
	}
	
	@Override
	public void addAI(EntityJoinWorldEvent event, Entity entity, GoalSelector goalSelector, GoalSelector targetSelector) {
		
		if (horseBurn && !entity.isImmuneToFire() && entity instanceof LivingEntity)
			goalSelector.addGoal(0, new RoughAISunlightBurnGoal((LivingEntity) entity, false) {
				@Override
				public boolean shouldExecute() {
					return super.shouldExecute() && !entity.isBeingRidden();
				}
			});
		
		if (entity instanceof LivingEntity && randomRiderChance > 0)
			goalSelector.addGoal(1, new RoughAISearchForRiderGoal((LivingEntity) entity, getRiders(entity), 32, randomRiderChance));
		
		if (entity instanceof LivingEntity && shouldDespawn(entity))
			goalSelector.addGoal(1, new RoughAIDespawnGoal((LivingEntity) entity));
	}
	
	@Override
	public void addFeatures(EntityJoinWorldEvent event, Entity entity) {
		if (entity instanceof LivingEntity && shouldDespawn(entity))
			ObfuscationReflectionHelper.setPrivateValue(MobEntity.class, (MobEntity)entity, false, "field_82179_bU"); // persistenceRequired
	}

	private boolean shouldDespawn(Entity entity) {
		return canDespawn && entity.getPersistentData().getBoolean(ROUGH_HORSE);
	}

	private List<EntityType<?>> getRiders(Entity entity) {

		List<EntityType<?>> riders = new ArrayList<>();
		
		if (entity instanceof SkeletonHorseEntity)
			riders.add(EntityType.SKELETON);
		else
			riders.add(EntityType.ZOMBIE);
		
		return riders;
	}
	
	
}
