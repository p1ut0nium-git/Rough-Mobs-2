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
import com.p1ut0nium.roughmobsrevamped.misc.FeatureHelper;
import com.p1ut0nium.roughmobsrevamped.misc.MountHelper.Rider;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;

public class SpiderFeatures extends EntityFeatures {

	private float ignoreFallDamageMult;
	
	private int slownessChance;
	private int slownessDuration;
	private boolean slownessCreateWeb;
	
	private Rider rider;

	public SpiderFeatures() {
		super("spider", Constants.SPIDERS);
	}
	
	@Override
	public void preInit() {
		rider = new Rider(name, RoughConfig.spiderRiderEntities, 10);
	}
	
	@Override
	public void initConfig() {
		super.initConfig();
		
		ignoreFallDamageMult = RoughConfig.spiderIgnoreFallDamage;
		
		slownessDuration = RoughConfig.spiderSlownessDuration * 20;
		slownessChance = RoughConfig.spiderSlownessChance;
		slownessCreateWeb = RoughConfig.spiderSlownessCreateWeb;
	
		rider.initConfigs();
	}
	
	@Override
	public void postInit() {
		rider.postInit();
	}

	@Override
	public void addAI(EntityJoinWorldEvent event, Entity entity, GoalSelector goalSelector, GoalSelector targetSelector) {
		if (entity instanceof LivingEntity)
			rider.addAI((LivingEntity) entity);
	}
	
	@Override
	public void addFeatures(EntityJoinWorldEvent event, Entity spider) {
		if (spider instanceof LivingEntity)
			rider.tryAddRider((LivingEntity) spider);
	}
	
	@Override
	public void onFall(LivingEntity entity, LivingFallEvent event) {
		
		if (ignoreFallDamageMult == 1)
			return;
		
		if (ignoreFallDamageMult == 0)
			event.setCanceled(true);
		
		event.setDamageMultiplier(event.getDamageMultiplier() * ignoreFallDamageMult);
	}
	
	@Override
	public void onAttack(Entity attacker, Entity immediateAttacker, Entity target, LivingAttackEvent event) {
		
		if (target instanceof LivingEntity && slownessChance > 0)
		{
			LivingEntity living = (LivingEntity)target;
			int maxAmp = 4;
			
			FeatureHelper.addEffect(living, Effects.SLOWNESS, slownessDuration, 0, slownessChance, true, maxAmp);
			EffectInstance active = living.getActivePotionEffect(Effects.SLOWNESS);
			
			if (slownessCreateWeb && active != null && active.getAmplifier() >= maxAmp && RND.nextInt(slownessChance) == 0 && target.getEntityWorld().getBlockState(target.getPosition()).getBlock() == Blocks.AIR)
			{
				target.getEntityWorld().setBlockState(target.getPosition(), Blocks.COBWEB.getDefaultState());
			}
		}
	}
}	
