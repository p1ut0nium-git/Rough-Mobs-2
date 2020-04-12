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

import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.entity.ai.goal.RoughAIFlameTouchGoal;
import com.p1ut0nium.roughmobsrevamped.misc.FeatureHelper;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.Explosion.Mode;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class BlazeFeatures extends EntityFeatures {

	private boolean pushAttackersAway;
	private boolean flameTouch;
	private Mode deathExplosionType;
	private float pushStrength;
	private float deathExplosionStrength;
	
	public BlazeFeatures() {
		super("blaze", Constants.BLAZES);
	}
	
	@Override
	public void initConfig() {
		super.initConfig();
		
		pushAttackersAway = RoughConfig.blazePushAttackersAway;
		pushStrength = RoughConfig.blazePushDamage;
		flameTouch = RoughConfig.blazeFlameTouch;
		deathExplosionStrength = RoughConfig.blazeDeathExplosionStrength;
		deathExplosionType = RoughConfig.blazeDeathExplosionType;
	}
	
	@Override
	public void addAI(EntityJoinWorldEvent event, MobEntity entity, GoalSelector goalSelector, GoalSelector targetSelector) {
		if (entity instanceof MobEntity && flameTouch)
			goalSelector.addGoal(1, new RoughAIFlameTouchGoal(entity));
	}
	
	@Override
	public void onDefend(LivingEntity target, Entity attacker, Entity immediateAttacker, LivingAttackEvent event) {
		if (pushAttackersAway && attacker instanceof LivingEntity && attacker == immediateAttacker) {
			FeatureHelper.knockback(target, (LivingEntity) attacker, 1F, 0.05F);
			attacker.attackEntityFrom(DamageSource.GENERIC, pushStrength);
			attacker.setFire(8);
			
			FeatureHelper.playSound(target, SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.7f, 1.0f);
		}
	}
	
	@Override
	public void onDeath(LivingEntity deadEntity, DamageSource source) {
		if (deathExplosionStrength > 0 && !(source.getTrueSource() instanceof FakePlayer)) {;
			deadEntity.world.createExplosion(deadEntity, deadEntity.posX, deadEntity.posY, deadEntity.posZ, deathExplosionStrength, deathExplosionType);
		}
	}
}
