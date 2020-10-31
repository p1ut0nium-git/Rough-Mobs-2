package com.p1ut0nium.roughmobsrevamped.features;

import java.util.ArrayList;
import java.util.List;

import com.p1ut0nium.roughmobsrevamped.RoughMobs;
import com.p1ut0nium.roughmobsrevamped.ai.misc.RoughAIDespawn;
import com.p1ut0nium.roughmobsrevamped.ai.misc.RoughAISearchForRider;
import com.p1ut0nium.roughmobsrevamped.ai.misc.RoughAISunlightBurn;
import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.util.Constants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class HostileHorseFeatures extends EntityFeatures {

	public static final String ROUGH_HORSE = Constants.unique("roughhorse");
	
	private boolean horseBurn;
	private int randomRiderChance; 
	private boolean canDespawn;
	private int rangeToSearchRider;
	
	@SuppressWarnings("unchecked")
	public HostileHorseFeatures() {
		super("hostileHorse", EntityZombieHorse.class, EntitySkeletonHorse.class);
	}
	
	@Override
	public void initConfig() {
		super.initConfig();
		
		horseBurn = RoughConfig.getBoolean(name, "Burn", true, "Set this to false to prevent undead horses from burning in sunlight (as long as they have no rider)");
		randomRiderChance = RoughConfig.getInteger(name, "RandomRiderChance", 3, 0, MAX, "Chance (1 in X) that a random skeleton or zombie starts riding unmounted hostile horses around it");
		canDespawn = RoughConfig.getBoolean(name, "CanDespawn", true, "Set to false to prevent undead horses summoned through this mod from despawning");
		rangeToSearchRider = RoughConfig.getInteger(name, "RangeToSearchRider", 32, 0, Short.MAX_VALUE, "Range for " + name + " to search rider");
	}
	
	@Override
	public void addAI(EntityJoinWorldEvent event, Entity entity, EntityAITasks tasks, EntityAITasks targetTasks) {
		
		if (horseBurn && !entity.isImmuneToFire() && entity instanceof EntityLiving)
			tasks.addTask(0, new RoughAISunlightBurn((EntityLiving) entity, false) {
				@Override
				public boolean shouldExecute() {
					return super.shouldExecute() && !entity.isBeingRidden();
				}
			});
		
		if (entity instanceof EntityLiving && randomRiderChance > 0)
			tasks.addTask(1, new RoughAISearchForRider((EntityLiving) entity, getRiders(entity), rangeToSearchRider, randomRiderChance));
		
		if (entity instanceof EntityLivingBase && shouldDespawn(entity))
			tasks.addTask(1, new RoughAIDespawn((EntityLivingBase) entity));
	}
	
	@Override
	public void addFeatures(EntityJoinWorldEvent event, Entity entity) {
		if (entity instanceof EntityPlayer) {
			RoughMobs.logger.debug("Entity is player...skipping addFeatures");
			return;
		}
		if (entity instanceof EntityLiving && shouldDespawn(entity))
			ReflectionHelper.setPrivateValue(EntityLiving.class, (EntityLiving)entity, false, 17);
	}
	
	private boolean shouldDespawn(Entity entity) {
		return canDespawn && entity.getEntityData().getBoolean(ROUGH_HORSE);
	}

	private List<Class<? extends Entity>> getRiders(Entity entity) {

		List<Class<? extends Entity>> riders = new ArrayList<>();
		
		if (entity instanceof EntitySkeletonHorse)
			riders.add(AbstractSkeleton.class);
		else
			riders.add(EntityZombie.class);
		
		return riders;
	}
	
	
}
