package de.lellson.roughmobs2.ai.combat;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityPigZombie;

public class RoughAIAlwaysAggressive extends EntityAIBase {
	
	private EntityLiving entity;   
	private EntityLivingBase targetEntity;

	private int aggressiveRange;
	private boolean isAngry;
	
	//private Method becomeAngryMethod;
	//private Field angerLevelField;
	
	public RoughAIAlwaysAggressive(EntityLiving entity, int aggressiveRange) {

		this.entity = entity;
		this.aggressiveRange = aggressiveRange;
		this.isAngry = true;
		this.setMutexBits(4);

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
		if (entity.getAttackTarget() == null || entity.getAttackTarget() != entity.world.getClosestPlayerToEntity(entity, aggressiveRange)) {
	        this.targetEntity = entity.world.getClosestPlayerToEntity(entity, aggressiveRange);
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
        } else if (((EntityPlayer)this.targetEntity).isCreative()) {
        	return false;
        } else {
        	return true;
        }
	}
	
    /**
     * Execute a one shot task or start executing a continuous task
     */
	public void startExecuting() {
		entity.getNavigator().tryMoveToEntityLiving(this.targetEntity, 1);

    	setAngry(true);
	}
	
    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
    	
    	 if (!this.entity.getNavigator().noPath() && this.targetEntity.isEntityAlive() && this.targetEntity.getDistanceSq(this.entity) < (double)(this.aggressiveRange * this.aggressiveRange) && this.isAngry) {
    		 return true;
    	 } 
    	 return false;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
	public void updateTask() {
	}

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
    	this.targetEntity = null;
    	
    	setAngry(false);
    }
    
    /**
     * Determine if this AI Task is interruptible by a higher (= lower value) priority task. All vanilla AITask have
     * this value set to true.
     */
    public boolean isInterruptible() {
        return true;
    }
	
	private void setAngry(boolean isAngry) {
		
		if (isAngry) {	
			try {
				Method becomeAngryAt = EntityPigZombie.class.getDeclaredMethod("becomeAngryAt", Entity.class);
				becomeAngryAt.setAccessible(true);		
				becomeAngryAt.invoke(entity, entity.world.getClosestPlayerToEntity(entity, aggressiveRange));
			}
			catch (Exception e) {
				try {
					Method becomeAngryAt = EntityPigZombie.class.getDeclaredMethod("func_70835_c", Entity.class);
					becomeAngryAt.setAccessible(true);
					becomeAngryAt.invoke(entity, entity.world.getClosestPlayerToEntity(entity, aggressiveRange));
				} 
				catch (Exception e2) {
					e2.printStackTrace();
				} 
			}	
		} else {
			try {
				Field angerLevel = EntityPigZombie.class.getDeclaredField("angerLevel");
				angerLevel.setAccessible(true);
				angerLevel.setInt(entity, 0);
			} catch (Exception e) {
				try {
					Field angerLevel = EntityPigZombie.class.getDeclaredField("field_70837_d");
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
