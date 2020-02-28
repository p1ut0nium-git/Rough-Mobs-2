package com.p1ut0nium.roughmobsrevamped.ai.combat;

import com.p1ut0nium.roughmobsrevamped.entities.EntityHostileBat;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

public class RoughAIBatTarget extends EntityAITarget {

	private static double MAX_TARGET_RANGE = 10;
	
    private EntityHostileBat hostileBat;
    private EntityLivingBase theTarget;
    private EntityLiving boss;

    public RoughAIBatTarget(EntityHostileBat bat)
    {
        super(bat, false);
        this.hostileBat = bat;
        this.setMutexBits(1);
    }

    // This is tested first. If it succeeds, then it runs startExecuting()
    // After that, it gets tested again after shouldContinueExecuting() has run.
    @Override
    public boolean shouldExecute()
    {   

    	if (this.boss != null) {
    		theTarget = hostileBat.world.getNearestPlayerNotCreative(this.boss, MAX_TARGET_RANGE);
    	}
    	else {
    		theTarget = hostileBat.world.getNearestPlayerNotCreative(hostileBat, MAX_TARGET_RANGE);
    	}
	    return this.isSuitableTarget(theTarget, false);
    }

    // After startExecuting(), if this succeeds, then run shouldExecute again()
    @Override
    public boolean shouldContinueExecuting() {
    	if (this.boss != null)
    		theTarget = hostileBat.world.getNearestPlayerNotCreative(this.boss, MAX_TARGET_RANGE);
        return (theTarget != null && theTarget.isEntityAlive());
    }

    // When the player is no longer in range of the boss, clear bat's attack target
    @Override
    public void resetTask() {
        this.theTarget = null;
        this.hostileBat.setAttackTarget(null);
    }

    // If shouldExecute() is true, then do this
    @Override
    public void startExecuting() {
    	this.boss = hostileBat.getBoss();
        this.hostileBat.setAttackTarget(theTarget);

        super.startExecuting();
    }

}
