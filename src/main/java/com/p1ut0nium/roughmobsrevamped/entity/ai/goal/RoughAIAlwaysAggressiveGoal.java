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
import java.lang.reflect.Method;
import java.util.EnumSet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.entity.player.PlayerEntity;

public class RoughAIAlwaysAggressiveGoal extends Goal {
	
	private MobEntity entity;   
	private LivingEntity targetEntity;

	private int aggressiveRange;
	private boolean isAngry;
	
	//private Method becomeAngryMethod;
	//private Field angerLevelField;
	
	public RoughAIAlwaysAggressiveGoal(MobEntity entity, int aggressiveRange) {

		this.entity = entity;
		this.aggressiveRange = aggressiveRange;
		this.isAngry = true;
	    this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.TARGET));
		//this.setMutexBits(4);

		//becomeAngryMethod = ReflectionHelper.findMethod(EntityPigZombie.class, "becomeAngryAt", "func_70835_c", Entity.class);
		//becomeAngryMethod.setAccessible(true);

		//angerLevelField = ReflectionHelper.findField(EntityPigZombie.class, "field_70837_d");
		//angerLevelField.setAccessible(true);
	}

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
	@Override
	public boolean shouldExecute() {
		
		// Set target to the closest player
		if (entity.getAttackTarget() == null || entity.getAttackTarget() != entity.world.getClosestPlayer(entity, aggressiveRange)) {
	        this.targetEntity = entity.world.getClosestPlayer(entity, aggressiveRange);
		} else {
			this.targetEntity = entity.getAttackTarget();
		}

		// Test to see if target is null, too far away, can't be seen, or is a player in creative mode.
        if (this.targetEntity == null) {
            return false; 
        } else if (this.targetEntity.getDistanceSq(this.entity) > (double)(this.aggressiveRange * this.aggressiveRange)) {
        	return false; 
        } else if (!entity.canEntityBeSeen(targetEntity)) {
        	return false;
        } else if (((PlayerEntity)this.targetEntity).isCreative()) {
        	return false;
        } else {
        	return true;
        }
	}
	
    /**
     * Execute a one shot task or start executing a continuous task
     */
	@Override
	public void startExecuting() {
		entity.getNavigator().tryMoveToEntityLiving(this.targetEntity, 1);

    	setAngry(true);
	}
	
    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
	@Override
    public boolean shouldContinueExecuting() {
    	
    	 if (!this.entity.getNavigator().noPath() && this.targetEntity.isAlive() && this.targetEntity.getDistanceSq(this.entity) < (double)(this.aggressiveRange * this.aggressiveRange) && this.isAngry) {
    		 return true;
    	 } 
    	 return false;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
	@Override
    public void resetTask() {
    	this.targetEntity = null;
    	
    	setAngry(false);
    }
	
	private void setAngry(boolean isAngry) {
		
		if (isAngry) {	
			try {
				Method becomeAngryAt = ZombiePigmanEntity.class.getDeclaredMethod("becomeAngryAt", Entity.class);
				becomeAngryAt.setAccessible(true);		
				becomeAngryAt.invoke(entity, entity.world.getClosestPlayer(entity, aggressiveRange));
			}
			catch (Exception e) {
				try {
					Method becomeAngryAt = ZombiePigmanEntity.class.getDeclaredMethod("func_70835_c", Entity.class);
					becomeAngryAt.setAccessible(true);
					becomeAngryAt.invoke(entity, entity.world.getClosestPlayer(entity, aggressiveRange));
				} 
				catch (Exception e2) {
					e2.printStackTrace();
				} 
			}	
		} else {
			try {
				Field angerLevel = ZombiePigmanEntity.class.getDeclaredField("angerLevel");
				angerLevel.setAccessible(true);
				angerLevel.setInt(entity, 0);
			} catch (Exception e) {
				try {
					Field angerLevel = ZombiePigmanEntity.class.getDeclaredField("field_70837_d");
					angerLevel.setAccessible(true);;
					angerLevel.setInt(entity, 0);
				}
				catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		
		this.isAngry = isAngry;
	}
}
