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
package com.p1ut0nium.roughmobsrevamped.entity.ai.goal;

import java.lang.reflect.Field;
import java.util.List;

import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.TrackedEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class RoughAISearchForRiderGoal extends Goal {

	public static final String MOUNT_SEARCHER = Constants.unique("mountSearcher");
	public static final int IS_SEARCHER = 2;
	public static final int NO_SEARCHER = 1;
	
	protected LivingEntity entity;
	protected List<EntityType<?>> possibleRiders;
	protected int range;
	protected int chance;
	
	protected MobEntity mountSearcher;
	
	public RoughAISearchForRiderGoal(LivingEntity entity, List<EntityType<?>> possibleRiders, int range, int chance) {
		this.entity = entity;
		this.possibleRiders = possibleRiders;
		this.range = range;
		this.chance = chance;
	}

	@Override
	public boolean shouldExecute() {
		
		if (!this.entity.isAlive() || entity.isBeingRidden()) {
			mountSearcher = null;
			return false;
		}

		List<MobEntity> entities = entity.world.getEntitiesWithinAABB(MobEntity.class, ((Entity)entity).getBoundingBox().grow(range));
		
		for (MobEntity entity : entities)
			if (entity.isAlive() && this.entity != entity && isPossibleRider(entity)) {
				if (entity.getPersistentData().getInt(MOUNT_SEARCHER) == 0)
					entity.getPersistentData().putInt(MOUNT_SEARCHER, entity.world.rand.nextInt(chance) == 0 || entity.isPassenger() ? IS_SEARCHER : NO_SEARCHER);
				
				if (entity.getPersistentData().getInt(MOUNT_SEARCHER) == IS_SEARCHER && !entity.isPassenger()) {
					mountSearcher = entity;
					return true;
				}
			}
		
		mountSearcher = null;
		return false;
	}
	
	@Override
	public void tick() {
		
		//TODO this.mountSearcher.getNavigator().setPath(mountSearcher.getNavigator().getPathToEntityLiving(entity), 1);
		
		if (this.entity.getDistanceSq(this.mountSearcher) <= 2 && this.mountSearcher != this.entity)
		{
			if (this.entity instanceof AbstractHorseEntity)
			{
				AbstractHorseEntity horse = (AbstractHorseEntity) this.entity;
				horse.hurtResistantTime = 60;
		        horse.enablePersistence();
		        horse.setHorseTamed(true);
			}
			
			this.mountSearcher.startRiding(this.entity);
			this.mountSearcher.getNavigator().clearPath();
		}
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return (!(this.mountSearcher instanceof CreatureEntity) || ((CreatureEntity)this.mountSearcher).isWithinHomeDistanceFromPosition(entity.getPosition())) && this.mountSearcher.getAttackTarget() == null && this.entity.isAlive() && !this.entity.isBeingRidden() && !this.mountSearcher.isPassenger() && this.mountSearcher != this.entity;
    }
	
	@Override
	public void resetTask() {
		this.mountSearcher.getNavigator().clearPath();
		this.mountSearcher = null;
	}
	
	private boolean isPossibleRider(MobEntity entity) {
		
		for (EntityType<?> entityType : possibleRiders)
			if (entity.getType().equals(entityType))
				return true;
		
		return false;
	}
}
