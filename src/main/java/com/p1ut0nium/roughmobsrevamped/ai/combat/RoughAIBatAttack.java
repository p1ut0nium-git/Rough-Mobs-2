package com.p1ut0nium.roughmobsrevamped.ai.combat;

import com.p1ut0nium.roughmobsrevamped.entities.EntityHostileBat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.DamageSource;

public class RoughAIBatAttack extends EntityAIBase {

	private static int BAT_DAMAGE = 1;
	
    private final EntityHostileBat hostileBat;
    private Entity entityTarget;
    private int attackTick;
    
    public RoughAIBatAttack(EntityLivingBase bat) {
        hostileBat = (EntityHostileBat) bat;
        attackTick = 0;
    }
    
    //works
    @Override
    public boolean shouldExecute() {
    	
        if (hostileBat.getAttackTarget() != null && hostileBat.getAttackTarget().isEntityAlive()) {
            entityTarget = hostileBat.getAttackTarget();
            return true;
        }

        return false;
    }
    
    //works
    @Override
    public boolean shouldContinueExecuting() {
        return entityTarget != null && entityTarget.isEntityAlive() || super.shouldContinueExecuting();
    }
    
    @Override
    public void startExecuting()
    {
        super.startExecuting();
    }
    
    @Override
    public void resetTask() {
        entityTarget = null;
        attackTick = 0;
        super.resetTask();
    }
    
    @Override
    public void updateTask() {
        hostileBat.getLookHelper().setLookPositionWithEntity(entityTarget, 30.0F, 30.0F);

        attackTick = Math.max(attackTick - 1, 0);

        double maxReach = hostileBat.width * hostileBat.width * 5.0D;
        
        if (hostileBat.getDistanceSq(entityTarget.posX, entityTarget.getEntityBoundingBox().maxY, entityTarget.posZ) <= maxReach
                || (entityTarget.getEntityBoundingBox() != null && hostileBat.getEntityBoundingBox().intersects(entityTarget.getEntityBoundingBox()))) {
 
            if (attackTick == 0) {
                attackTick = 20;
                hostileBat.attackEntityAsMob(entityTarget);

                double xKnock = entityTarget.posX - hostileBat.posX;
                double zKnock = entityTarget.posZ - hostileBat.posZ;
                
                for (; xKnock * xKnock + zKnock * zKnock < 1.0E-4D; zKnock = (Math.random() - Math.random()) * 0.01D) {
                    xKnock = (Math.random() - Math.random()) * 0.01D;
                }
                hostileBat.knockBack(entityTarget, 0, xKnock, zKnock);
                
                entityTarget.attackEntityFrom(DamageSource.causeMobDamage(hostileBat), BAT_DAMAGE);
            }
        }
    }
}
