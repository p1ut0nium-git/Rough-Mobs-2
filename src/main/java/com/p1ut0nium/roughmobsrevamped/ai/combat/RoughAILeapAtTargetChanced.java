package com.p1ut0nium.roughmobsrevamped.ai.combat;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAILeapAtTarget;

public class RoughAILeapAtTargetChanced extends EntityAILeapAtTarget {

	private int chance;
	protected EntityLiving leaperProtected;
	
	public RoughAILeapAtTargetChanced(EntityLiving leapingEntity, float leapMotionYIn, int chance) {
		super(leapingEntity, leapMotionYIn);
		this.leaperProtected = leapingEntity;
		this.chance = chance;
	}
	
	@Override
	public boolean shouldExecute() {
		return super.shouldExecute() && chance > 0 && leaperProtected.getRNG().nextInt(chance) == 0;
	}
}
