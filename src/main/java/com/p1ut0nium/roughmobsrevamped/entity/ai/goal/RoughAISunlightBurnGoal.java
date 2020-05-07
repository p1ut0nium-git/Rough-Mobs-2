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

import java.util.EnumSet;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class RoughAISunlightBurnGoal extends Goal {

	protected LivingEntity entity;
	protected boolean helmetMode;
	
	public RoughAISunlightBurnGoal(LivingEntity entity, boolean helmetMode) {
		this.entity = entity;
		this.helmetMode = helmetMode;
	    this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.TARGET));
	}
	
	@Override
	public boolean shouldExecute() {

        float f = entity.getBrightness();
        boolean flag = entity.world.isDaytime() && f > 0.5F && entity.world.canBlockSeeSky(new BlockPos(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ));
        
        if (!flag)
        	return false;
		
        ItemStack itemstack = entity.getItemStackFromSlot(EquipmentSlotType.HEAD);
        if (itemstack != null && !itemstack.isEmpty()) {
        	if (helmetMode)
        		return !entity.isBurning();
        	
            if (itemstack.isDamageable()) {
                itemstack.setDamage(itemstack.getDamage() + entity.world.rand.nextInt(2));

                if (itemstack.getDamage() >= itemstack.getMaxDamage()) {
                	//TODO entity.renderBrokenItemStack(itemstack);
                	entity.setItemStackToSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
                }
            }
            return false;
        }
        
		return !helmetMode && !entity.isBurning();
	}
	
	@Override
	public void tick() {
		entity.setFire(8);
	}
}