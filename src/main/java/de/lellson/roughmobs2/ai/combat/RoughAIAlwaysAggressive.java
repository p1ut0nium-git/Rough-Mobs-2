package de.lellson.roughmobs2.ai.combat;

import de.lellson.roughmobs2.misc.FeatureHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;

public class RoughAIAlwaysAggressive extends EntityAIBase {
	
	protected EntityLiving entity;
	
	private boolean alwaysAggressive;
	private int aggressiveRange;
	private EntityPlayer player;
	
	public RoughAIAlwaysAggressive(EntityLiving entity, boolean alwaysAggressive, int aggressiveRange) {
		this.entity = entity;
		this.alwaysAggressive = alwaysAggressive;
		this.aggressiveRange = aggressiveRange;
		this.setMutexBits(4);
	}

	@Override
	public boolean shouldExecute() {
		if (alwaysAggressive) {
			FeatureHelper.playSound(entity, SoundEvents.ENTITY_ZOMBIE_PIG_ANGRY);
			player = entity.world.getClosestPlayerToEntity(entity, aggressiveRange);
			entity.setRevengeTarget(player);
			entity.setAttackTarget(player);
			return true;
		}
		return false;
	}
	
	@Override
	public void updateTask() {
	}
}
