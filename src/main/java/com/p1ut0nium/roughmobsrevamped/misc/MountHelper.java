package com.p1ut0nium.roughmobsrevamped.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.p1ut0nium.roughmobsrevamped.features.HostileHorseFeatures;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
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
		private final String[] defaultEntities;
		private final int defaultChance;
		
		private String[] entities;
		private int chance;
		private int randomRiderChance;
		
		private List<EntityType> entries;
		
		public Rider(String name, String[] defaultEntities, int defaultChance) {
			this.name = name;
			this.defaultEntities = defaultEntities;
			this.defaultChance = defaultChance;
		}
		
		public void initConfigs() {
			
			chance = RoughConfig.getInteger(name, "RiderChance", defaultChance, 0, Short.MAX_VALUE, "Chance (1 in X) for a " + name + " to spawn with another entity riding it\nSet to 0 to disable this feature");
			entities = RoughConfig.getStringArray(name, "RiderEntities", defaultEntities, "Entities which may ride on " + name + "s");
			randomRiderChance = RoughConfig.getInteger(name, "RiderChanceRandom", 10, 0, Short.MAX_VALUE, "Chance (1 in X) that a randomly spawned entity from the RiderEntities list can start riding on random " + name + "s\nSet to 0 to disable this feature");
		}
		
		public void postInit() {
			entries = FeatureHelper.getEntitiesFromNames(entities);
		}
		
		public void addAI(LivingEntity mount) {
			if (randomRiderChance > 0)
				mount.tasks.addTask(1, new RoughAISearchForRider(mount, getPossibleRiders(), 32, randomRiderChance));
		}
		
		public void tryAddRider(LivingEntity mount) {
			
			if (chance <= 0 || mount == null || entries.isEmpty() || mount.getEntityData().getBoolean(RIDER) || RND.nextInt(chance) != 0)
				return;
			
			EntityEntry entry = entries.get(RND.nextInt(entries.size()));
			
			Entity entity = entry.newInstance(mount.getEntityWorld());
			entity.setPosition(mount.posX, mount.posY, mount.posZ);
			entity.hurtResistantTime = 60;
			entity.getEntityData().setBoolean(RIDER, true);
			
			mount.getEntityWorld().spawnEntity(entity);
			if (!entity.isRiding() && !entity.isBeingRidden() && !mount.isRiding() && !mount.isBeingRidden())
				entity.startRiding(mount);
		}
		
		public boolean isPossibleRider(Entity entity) {
			
			for (EntityType entry : entries)
				if (entry.getEntityClass() == entity.getClass())
					return true;
			
			return false;
		}
		
		public List<Class<? extends Entity>> getPossibleRiders() {
			
			List<Class<? extends Entity>> list = new ArrayList<Class<? extends Entity>>();
			
			for (EntityType entry : entries)
				list.add(entry.getEntityClass());
			
			return list;
		}
	}
	
	public enum HorseType {
		NORMAL, ZOMBIE, SKELETON;
		
		public AbstractHorseEntity createInstance(World world) {
			
			switch(this) 
			{
				case ZOMBIE: return new ZombieHorseEntity(EntityType.ZOMBIE_HORSE, world);
				case SKELETON: return new SkeletonHorseEntity(EntityType.SKELETON_HORSE, world);
				default: return new HorseEntity(EntityType.HORSE, world);
			}
		}
	}
	
	public static AbstractHorseEntity createHorse(World world, Entity owner, HorseType type) {
		
		AbstractHorseEntity horse = type.createInstance(world);
		horse.setPosition(owner.posX, owner.posY, owner.posZ);
		horse.onInitialSpawn(world, world.getDifficultyForLocation(owner.getPosition()), SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, null);
		horse.hurtResistantTime = 60;
        horse.setHorseTamed(true);
        horse.setGrowingAge(0);
        horse.getEntityData().setBoolean(HostileHorseFeatures.ROUGH_HORSE, true);
		
		world.spawnEntity(horse);
		return horse;
	}
	
	public static boolean tryMountHorse(Entity rider, HorseType type, int chance, int minY) {
		
		if (rider.posY < minY)
			return false;
		
		if (!BossHelper.isBoss(rider) && (chance <= 0 || RND.nextInt(chance) != 0 || rider.isRiding() || (rider instanceof ZombieEntity && ((ZombieEntity)rider).isChild())))
			return false;
		
		if (rider.getEntityData().getBoolean(Rider.RIDER))
			return false;
		
		AbstractHorseEntity mount = createHorse(rider.world, rider, type);
		rider.startRiding(mount);
		return true;
	}
}
