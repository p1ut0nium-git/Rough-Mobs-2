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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.entity.ai.goal.RoughAISearchForRiderGoal;
import com.p1ut0nium.roughmobsrevamped.features.HostileHorseFeatures;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.entity.passive.horse.ZombieHorseEntity;
import net.minecraft.world.World;

public class MountHelper {
	
	public static final Random RND = new Random();
	
	public static class Rider {
		
		public static final String RIDER = Constants.unique("isrider");
		
		private final String name;
		private final List<String> defaultRiders;
		private final int defaultChance;
		
		private List<String> riderEntities;
		private int chance;
		private int randomRiderChance;
		
		private List<EntityType<?>> riderList;
		
		public Rider(String name, List<String> defaultRiders, int defaultChance) {
			this.name = name;
			this.defaultRiders = defaultRiders;
			this.defaultChance = defaultChance;
		}
		
		public void initConfigs() {

			chance = RoughConfig.spiderRiderChance;
			riderEntities = RoughConfig.spiderRiderEntities;
			randomRiderChance = RoughConfig.spiderRiderChanceRandom;
		}
		
		public void postInit() {
			riderList = FeatureHelper.getEntitiesFromNames(riderEntities);
		}
		
		public void addAI(LivingEntity mount) {
			if (randomRiderChance > 0)
				((MobEntity)mount).goalSelector.addGoal(1, new RoughAISearchForRiderGoal(mount, getPossibleRiders(), 32, randomRiderChance));
		}
		
		public void tryAddRider(LivingEntity mount) {
			
			if (chance <= 0 || mount == null || riderList.isEmpty() || mount.getPersistentData().getBoolean(RIDER) || RND.nextInt(chance) != 0)
				return;
			
			EntityType<?> riderType = riderList.get(RND.nextInt(riderList.size()));
			
			// TODO verify riderType.create works - 1.12.2 version -> Entity entity = entry.newInstance(mount.getEntityWorld());
			MobEntity rider = (MobEntity) riderType.create(mount.getEntityWorld());
			rider.setPosition(mount.getPosX(), mount.getPosY(), mount.getPosZ());
			rider.hurtResistantTime = 60;
			rider.getPersistentData().putBoolean(RIDER, true);
			
			mount.getEntityWorld().addEntity(rider);
			
			if (!rider.isPassenger() && !rider.isBeingRidden() && !mount.isPassenger() && !mount.isBeingRidden())
				rider.startRiding(mount);
		}
		
		public boolean isPossibleRider(MobEntity entity) {
			
			for (EntityType<?> entityType : riderList)
				if (entityType == entity.getType())
					return true;
			
			return false;
		}
		
		public List<EntityType<?>> getPossibleRiders() {
			
			List<EntityType<?>> possibleRidersList = new ArrayList<EntityType<?>>();
			
			for (EntityType<?> entityType : riderList)
				possibleRidersList.add(entityType);
			
			return possibleRidersList;
		}
	}
	
	public enum HorseType {
		NORMAL, ZOMBIE, SKELETON;
		
		public AbstractHorseEntity createInstance(World world) {
			
			switch(this) {
				case ZOMBIE: return new ZombieHorseEntity(EntityType.ZOMBIE_HORSE, world);
				case SKELETON: return new SkeletonHorseEntity(EntityType.SKELETON_HORSE, world);
				default: return new HorseEntity(EntityType.HORSE, world);
			}
		}
	}
	
	public static AbstractHorseEntity createHorse(World world, Entity owner, HorseType type) {
		
		AbstractHorseEntity horse = type.createInstance(world);
		horse.setPosition(owner.getPosX(), owner.getPosY(), owner.getPosZ());
		horse.onInitialSpawn(world, world.getDifficultyForLocation(owner.getPosition()), SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, null);
		horse.hurtResistantTime = 60;
        horse.setHorseTamed(true);
        horse.setGrowingAge(0);
        horse.getPersistentData().putBoolean(HostileHorseFeatures.ROUGH_HORSE, true);
		
		world.addEntity(horse);
		return horse;
	}
	
	public static boolean tryMountHorse(MobEntity rider, HorseType type, int chance, int minY) {
		
		if (rider.getPosY() < minY)
			return false;

		if (!BossHelper.isBoss(rider) && (chance <= 0 || RND.nextInt(chance) != 0 || rider.isPassenger() || (rider instanceof ZombieEntity && ((ZombieEntity)rider).isChild())))
			return false;
		
		if (rider.getPersistentData().getBoolean(Rider.RIDER))
			return false;
		
		AbstractHorseEntity mount = createHorse(rider.world, rider, type);
		rider.startRiding(mount);
		return true;
	}
}
