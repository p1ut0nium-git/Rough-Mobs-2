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
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
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
	
	/* TODO AI
	@Override
	public void addAI(EntityJoinWorldEvent event, Entity entity, EntityAITasks tasks, EntityAITasks targetTasks) {
		
		if (horseBurn && !entity.isImmuneToFire() && entity instanceof LivingEntity)
			tasks.addTask(0, new RoughAISunlightBurn((LivingEntity) entity, false) {
				@Override
				public boolean shouldExecute() {
					return super.shouldExecute() && !entity.isBeingRidden();
				}
			});
		
		if (entity instanceof LivingEntity && randomRiderChance > 0)
			tasks.addTask(1, new RoughAISearchForRider((LivingEntity) entity, getRiders(entity), 32, randomRiderChance));
		
		if (entity instanceof LivingEntity && shouldDespawn(entity))
			tasks.addTask(1, new RoughAIDespawn((LivingEntity) entity));
	}
	*/
	
	@Override
	public void addFeatures(EntityJoinWorldEvent event, Entity entity) {
		if (entity instanceof LivingEntity && shouldDespawn(entity))
			ObfuscationReflectionHelper.setPrivateValue(MobEntity.class, (MobEntity)entity, false, "persistenceRequired");
			// TODO Deprecated - ObfuscationReflectionHelper.setPrivateValue(LivingEntity.class, (LivingEntity)entity, false, 17);
	}

	private boolean shouldDespawn(Entity entity) {
		return canDespawn && entity.getPersistentData().getBoolean(ROUGH_HORSE);
	}

	private List<Class<? extends Entity>> getRiders(Entity entity) {

		List<Class<? extends Entity>> riders = new ArrayList<>();
		
		if (entity instanceof SkeletonHorseEntity)
			riders.add(AbstractSkeletonEntity.class);
		else
			riders.add(ZombieEntity.class);
		
		return riders;
	}
	
	
}
