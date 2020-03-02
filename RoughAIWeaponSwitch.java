package com.p1ut0nium.roughmobsrevamped.ai.combat;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class RoughAIWeaponSwitch extends EntityAIBase{

	protected EntityLiving entity;
	protected double rangeNear, rangeFar;
	protected ItemStack melee, ranged;
	
	public RoughAIWeaponSwitch(EntityLiving entity, double range) {
        this.entity = entity;

        rangeNear = range * 0.9;
        rangeFar = range * 1.1;

        ranged = entity.getHeldItemMainhand();
        melee = entity.getHeldItemOffhand();

        if (melee.getItem() == Items.BOW) {
            ItemStack swap = melee;
            melee = ranged;
            ranged = swap;
        }
	}
	
	@Override
	public boolean shouldExecute() {
		
		return melee != ItemStack.EMPTY && ranged != ItemStack.EMPTY;
	}
	
	@Override
	public void startExecuting() {
		
        EntityLivingBase target = entity.getAttackTarget();
        if (target == null || !entity.getEntitySenses().canSee(target)) return;

        double distSq = entity.getDistanceSq(target);
        if (distSq < rangeNear)
        {
            if (entity.getHeldItemMainhand() == ranged)
            {
                entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, melee);
                entity.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ranged);
            }
        }
        else if (distSq > rangeFar)
        {
            if (entity.getHeldItemMainhand() == melee)
            {
                entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ranged);
                entity.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, melee);
            }
        }
	}
	
    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }
}
