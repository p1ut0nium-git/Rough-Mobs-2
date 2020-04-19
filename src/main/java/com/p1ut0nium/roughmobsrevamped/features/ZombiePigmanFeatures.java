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
package com.p1ut0nium.roughmobsrevamped.features;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.entity.ai.goal.RoughAIAggressiveTouchGoal;
import com.p1ut0nium.roughmobsrevamped.entity.ai.goal.RoughAIAlwaysAggressiveGoal;
import com.p1ut0nium.roughmobsrevamped.misc.FeatureHelper;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

public class ZombiePigmanFeatures extends EntityFeatures {
	
	private boolean aggressiveTouch;
	private boolean alwaysAggressive;
	private int aggressiveRange;
	private float aggressiveBlockRange;
	private int aggressiveBlockChance;
	
	public ZombiePigmanFeatures() {
		super("zombie_pigman", Constants.ZOMBIE_PIGMEN);
	}

	@Override
	public void initConfig() {
		
		super.initConfig();
		
		aggressiveTouch = RoughConfig.zombiePigmanAggressiveTouch;
		alwaysAggressive = RoughConfig.zombiePigmanAlwaysAggressive;
		aggressiveRange = RoughConfig.zombiePigmanAggressiveRange;
		aggressiveBlockRange = RoughConfig.zombiePigmanAggressiveBlockRange;
		aggressiveBlockChance = RoughConfig.zombiePigmanAggressiveBlockChance;
	}
	
	@Override
	public void addAI(EntityJoinWorldEvent event, MobEntity entity, GoalSelector goalSelector, GoalSelector targetSelector) {

		if (!(entity instanceof MobEntity))
			return;

		if (aggressiveTouch) {
			goalSelector.addGoal(1, new RoughAIAggressiveTouchGoal(entity));
		}
	
		if (alwaysAggressive)
			goalSelector.addGoal(3, new RoughAIAlwaysAggressiveGoal(entity, aggressiveRange));
	}
	
	@Override
	public void onBlockBreak(PlayerEntity player, BreakEvent event) {
		
		for (EntityType<? extends Entity> entityType : entityTypes) {
			List<Entity> entities = player.world.getEntitiesWithinAABB(entityType, player.getBoundingBox().expand(aggressiveBlockRange, aggressiveBlockRange, aggressiveBlockRange), ENTITY_TYPE_VALIDATOR);
			
			for (Entity entity : entities) {
				if (aggressiveBlockChance > 0 && entity instanceof MobEntity && player.world.rand.nextInt(aggressiveBlockChance) == 0) {
					MobEntity mob = (MobEntity) entity;
					mob.setAttackTarget(player);
					mob.setRevengeTarget(player);
					FeatureHelper.playSound(mob, SoundEvents.ENTITY_ZOMBIE_PIGMAN_ANGRY);
				}
			}
		}
	}
	
    public static final Predicate<Entity> ENTITY_TYPE_VALIDATOR = (entity) -> {
    	List<EntityType<? extends Entity>> entityTypes = Arrays.asList(entity.getType());
        try {
        	entityTypes.stream()
        	.allMatch(e -> e.equals(EntityType.ZOMBIE_PIGMAN));
        }
        catch (Exception e) {
            return false;
        }
        return true;
    };
}
