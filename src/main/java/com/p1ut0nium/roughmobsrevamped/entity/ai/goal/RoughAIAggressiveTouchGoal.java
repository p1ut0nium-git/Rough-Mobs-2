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
import java.util.List;

import com.p1ut0nium.roughmobsrevamped.misc.FeatureHelper;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;

public class RoughAIAggressiveTouchGoal extends Goal {
	
	protected MobEntity entity;
	
	private List<PlayerEntity> players;
	
	public RoughAIAggressiveTouchGoal(MobEntity entity) {
		this.entity = entity;
	    this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.TARGET));
		//this.setMutexBits(4);
	}

	@Override
	public boolean shouldExecute() {
		return !(players = entity.world.getEntitiesWithinAABB(PlayerEntity.class, entity.getBoundingBox().expand(0.1, 0.1, 0.1))).isEmpty();
	}
	
	@Override
	public void tick() {
		for (PlayerEntity player : players) {
			entity.setRevengeTarget(player);
			entity.setAttackTarget(player);
			FeatureHelper.playSound(entity, SoundEvents.ENTITY_ZOMBIE_PIGMAN_ANGRY);
			break;
		}
	}
}
