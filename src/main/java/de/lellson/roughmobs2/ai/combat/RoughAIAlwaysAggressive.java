package de.lellson.roughmobs2.ai.combat;

import de.lellson.roughmobs2.misc.FeatureHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;

public class RoughAIAlwaysAggressive extends EntityAIBase {
	
	public RoughAIAlwaysAggressive(EntityLiving entity, boolean alwaysAggressive, int aggressiveRange) {
		this.entity = entity;
		this.alwaysAggressive = alwaysAggressive;
		this.aggressiveRange = aggressiveRange;
		this.setMutexBits(4);
	}

	protected EntityLiving entity;
	
	private boolean alwaysAggressive;
	private int aggressiveRange;
	private EntityPlayer closestPlayer;
	


	@Override
	public boolean shouldExecute() {
		closestPlayer = entity.world.getClosestPlayerToEntity(entity, aggressiveRange);
		if (alwaysAggressive && closestPlayer != null) {
			return true;
		}
		return false;
	}
	
	@Override
	public void updateTask() {
		closestPlayer = entity.world.getClosestPlayerToEntity(entity, aggressiveRange);
		if (closestPlayer == null) {
			entity.setRevengeTarget(null);
			entity.setAttackTarget(null);
		}
		if (entity.getAttackTarget() != closestPlayer) {
			entity.setRevengeTarget(closestPlayer);
			entity.setAttackTarget(closestPlayer);
		}

	}
	
	@Override
	public void startExecuting() {
		if (closestPlayer == null || closestPlayer.getDistanceSq(entity) <= aggressiveRange) {
			return;
		}
		FeatureHelper.playSound(entity, SoundEvents.ENTITY_ZOMBIE_PIG_ANGRY);
		entity.setRevengeTarget(closestPlayer);
		entity.setAttackTarget(closestPlayer);
	}
}
