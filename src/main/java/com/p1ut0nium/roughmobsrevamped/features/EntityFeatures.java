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
import java.util.Random;

import com.p1ut0nium.roughmobsrevamped.compat.GameStagesCompat;
import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
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
	protected List<EntityType<? extends Entity>> entityTypes;
	
	//Config
	protected boolean featuresEnabled;
	protected List<String> entityNames;
	
	public EntityFeatures(String name, EntityType<? extends Entity>[] entityTypes) {
		this.name = name;
		this.entityTypes = Arrays.asList(entityTypes);
	}

	public boolean isEntityType(MonsterEntity mobEntity) {;
		EntityType<?> entityType = mobEntity.getType();
		ResourceLocation loc = EntityType.getKey(entityType);

		return featuresEnabled && loc != null && entityNames.contains(loc.toString());
	}
	
	public void initConfig() {
		
		if (!hasDefaultConfig())
			return;
		
		featuresEnabled = RoughConfig.featuresEnabled.get(name);
		entityNames = RoughConfig.entities.get(name);
	}
	
	private boolean hasDefaultConfig() {
		return true;
	}
	
	public void addFeatures(EntityJoinWorldEvent event, MobEntity entity) {
	}
	
	public boolean bossesEnabled(MobEntity entity) {
		
		if (GameStagesCompat.useBossStage()) {
			PlayerEntity playerClosest = entity.world.getClosestPlayer(entity, -1.0D);
			return (GameStageHelper.hasAnyOf((ServerPlayerEntity) playerClosest, Constants.ROUGHMOBSALL, Constants.ROUGHMOBSBOSS));
		}
		
		// If boss game stage isn't enabled, then it is ok to spawn bosses
		return true;
	}

	public void preInit() {
	}

	public void postInit() {	
	}
	
	public void addAI(EntityJoinWorldEvent event, MobEntity entity, GoalSelector goalSelector, GoalSelector targeSelector) {
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
