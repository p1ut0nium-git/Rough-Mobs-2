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

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;

public class RoughAILeapAtTargetChancedGoal extends LeapAtTargetGoal {
   
   private int chance;
   protected LivingEntity leaperProtected;

   public RoughAILeapAtTargetChancedGoal(MobEntity leapingEntity, float leapMotionYIn, int chance) {
	   super(leapingEntity, leapMotionYIn); 
	   this.leaperProtected = leapingEntity;
	   this.chance = chance;
   }
   
   @Override
   public boolean shouldExecute() {
	   return super.shouldExecute() && chance > 0 && leaperProtected.getRNG().nextInt(chance) == 0;
   }

   public boolean shouldContinueExecuting() {
	   return super.shouldContinueExecuting();
   }

   public void startExecuting() {
	   super.startExecuting();
   }
}
