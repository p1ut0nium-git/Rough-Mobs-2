package com.p1ut0nium.roughmobsrevamped.features;

import java.util.ArrayList;
import java.util.List;

import com.p1ut0nium.roughmobsrevamped.reference.Constants;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.entity.passive.horse.ZombieHorseEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class HostileHorseFeatures extends EntityFeatures {

	public static final String ROUGH_HORSE = Constants.unique("roughhorse");
	
	private boolean horseBurn;
	private int randomRiderChance; 
	private boolean canDespawn;
	
	@SuppressWarnings("unchecked")
	public HostileHorseFeatures() {
		super("hostileHorse", ZombieHorseEntity.class, SkeletonHorseEntity.class);
	}
	
	@Override
	public void initConfig() {
		super.initConfig();
		
		horseBurn = RoughConfig.getBoolean(name, "Burn", true, "Set this to false to prevent undead horses from burning in sunlight (as long as they have no rider)");
		randomRiderChance = RoughConfig.getInteger(name, "RandomRiderChance", 3, 0, MAX, "Chance (1 in X) that a random skeleton or zombie starts riding unmounted hostile horses around it");
		canDespawn = RoughConfig.getBoolean(name, "CanDespawn", true, "Set to false to prevent undead horses summoned through this mod from despawning");
	}
	
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
	
	@Override
	public void addFeatures(EntityJoinWorldEvent event, Entity entity) {
		if (entity instanceof LivingEntity && shouldDespawn(entity))
			ReflectionHelper.setPrivateValue(LivingEntity.class, (LivingEntity)entity, false, 17);
	}
	
	private boolean shouldDespawn(Entity entity) {
		return canDespawn && entity.getEntityData().getBoolean(ROUGH_HORSE);
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
