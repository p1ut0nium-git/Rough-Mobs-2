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

import java.util.List;

import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.pathfinding.Path;

public class RoughAISearchForRiderGoal extends Goal {

	public static final String MOUNT_SEARCHER = Constants.unique("mountSearcher");
	public static final int IS_SEARCHER = 2;
	public static final int NO_SEARCHER = 1;
	
	protected LivingEntity mount;
	protected List<EntityType<?>> possibleRiders;
	protected int range;
	protected int chance;
	
	protected MobEntity mountSearcher;
	private Path path;
	
	public RoughAISearchForRiderGoal(LivingEntity mount, List<EntityType<?>> possibleRiders, int range, int chance) {
		this.mount = mount;
		this.possibleRiders = possibleRiders;
		this.range = range;
		this.chance = chance;
	}

	@Override
	public boolean shouldExecute() {
		
		if (!this.mount.isAlive() || this.mount.isBeingRidden()) {
			this.mountSearcher = null;
			return false;
		}

		List<MobEntity> entities = this.mount.world.getEntitiesWithinAABB(MobEntity.class, ((Entity)this.mount).getBoundingBox().grow(range));
		
		for (MobEntity entity : entities)
			if (entity.isAlive() && this.mount != entity && isPossibleRider(entity)) {
				if (entity.getPersistentData().getInt(MOUNT_SEARCHER) == 0)
					entity.getPersistentData().putInt(MOUNT_SEARCHER, entity.world.rand.nextInt(chance) <= 1 || entity.isPassenger() ? IS_SEARCHER : NO_SEARCHER);
				
				if (entity.getPersistentData().getInt(MOUNT_SEARCHER) == IS_SEARCHER && !entity.isPassenger()) {
					this.mountSearcher = entity;
					return true;
				}
			}
		
		this.mountSearcher = null;
		return false;
	}
	
	@Override
	public void tick() {
		this.path = this.mountSearcher.getNavigator().func_75494_a(this.mount, 0);
		this.mountSearcher.getNavigator().setPath(this.path, 1.0D);
		
		if (this.mount.getDistanceSq(this.mountSearcher) <= 2 && this.mountSearcher != this.mount) {
			if (this.mount instanceof AbstractHorseEntity) {
				AbstractHorseEntity horse = (AbstractHorseEntity) this.mount;
				horse.hurtResistantTime = 60;
		        horse.enablePersistence();
		        horse.setHorseTamed(true);
			}
			
			this.mountSearcher.startRiding(this.mount);
			this.mountSearcher.getNavigator().clearPath();
		}
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return (!(this.mountSearcher instanceof CreatureEntity) || ((CreatureEntity)this.mountSearcher).isWithinHomeDistanceFromPosition(this.mount.getPosition())) && this.mountSearcher.getAttackTarget() == null && this.mount.isAlive() && !this.mount.isBeingRidden() && !this.mountSearcher.isPassenger() && this.mountSearcher != this.mount;
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
