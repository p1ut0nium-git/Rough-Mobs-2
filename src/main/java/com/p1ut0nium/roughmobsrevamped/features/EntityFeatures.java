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

import java.util.Random;

import com.p1ut0nium.roughmobsrevamped.compat.GameStagesCompat;
import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

public abstract class EntityFeatures {
	
	public static final Random RND = new Random();
	public static final int MAX = Short.MAX_VALUE;
	
	//Default
	public String name;
	protected EntityType[] entityTypes;
	
	public EntityFeatures(String name, EntityType[] entityTypes) {
		this.name = name;
		this.entityTypes = entityTypes;
	}

	public boolean isEntity(Entity creature) {;
		EntityType<?> entityType = creature.getType();
		ResourceLocation loc = EntityType.getKey(entityType);
		return RoughConfig.zombieFeaturesEnabled && loc != null && RoughConfig.zombieEntities.contains(loc.toString());
	}
	
	public void initConfig() {
		
		if (!hasDefaultConfig())
			return;
	}
	
	private boolean hasDefaultConfig() {
		return true;
	}
	
	public void addFeatures(EntityJoinWorldEvent event, Entity entity) {
	}
	
	public boolean bossesEnabled(Entity entity) {

		boolean bossStageEnabled = GameStagesCompat.useBossStage();	
		
		if (bossStageEnabled) {
			PlayerEntity playerClosest = entity.world.getClosestPlayer(entity, -1.0D);
			return (GameStageHelper.hasAnyOf(playerClosest, Constants.ROUGHMOBSALL, Constants.ROUGHMOBSBOSS));
		}
		
		// If boss game stage isn't enabled, then it is ok to spawn bosses
		return true;
	}

	public void preInit() {
	}

	public void postInit() {
		
	}

	public void onAttack(Entity attacker, Entity immediateAttacker, Entity target, LivingAttackEvent event) {
	}

	public void onDefend(LivingEntity target, Entity trueSource, Entity immediateSource, LivingAttackEvent event) {
	}

	public void onDeath(LivingEntity deadEntity, DamageSource source) {
	}

	public void onFall(LivingEntity entity, LivingFallEvent event) {
	}

	public void onBlockBreak(PlayerEntity player, BreakEvent event) {
	}
}
