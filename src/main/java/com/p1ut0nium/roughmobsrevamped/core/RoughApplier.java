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
package com.p1ut0nium.roughmobsrevamped.core;

import java.util.ArrayList;
import java.util.List;

import com.p1ut0nium.roughmobsrevamped.compat.CompatHandler;
import com.p1ut0nium.roughmobsrevamped.compat.GameStagesCompat;
import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.entity.boss.IChampion;
import com.p1ut0nium.roughmobsrevamped.features.EntityFeatures;
import com.p1ut0nium.roughmobsrevamped.features.HostileHorseFeatures;
import com.p1ut0nium.roughmobsrevamped.features.SpiderFeatures;
import com.p1ut0nium.roughmobsrevamped.features.ZombieFeatures;
import com.p1ut0nium.roughmobsrevamped.misc.AttributeHelper;
import com.p1ut0nium.roughmobsrevamped.misc.SpawnHelper;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RoughApplier {
	
	public static final String FEATURES_APPLIED = Constants.unique("featuresApplied");
	
	public static final List<EntityFeatures> FEATURES = new ArrayList<EntityFeatures>();
	
	public RoughApplier() {
		MinecraftForge.EVENT_BUS.register(this);
		
		FEATURES.add(new ZombieFeatures());
		FEATURES.add(new HostileHorseFeatures());
		FEATURES.add(new SpiderFeatures());
		
		/* TODO
		FEATURES.add(new SkeletonFeatures());
		FEATURES.add(new CreeperFeatures());
		FEATURES.add(new SlimeFeatures());
		FEATURES.add(new EndermanFeatures());
		FEATURES.add(new WitchFeatures().addPotionHandler(FEATURES));
		FEATURES.add(new SilverfishFeatures());
		FEATURES.add(new ZombiePigmanFeatures());
		FEATURES.add(new BlazeFeatures());
		FEATURES.add(new GhastFeatures());
		FEATURES.add(new MagmaCubeFeatures());
		FEATURES.add(new WitherFeatures());
		FEATURES.add(new EndermiteFeatures());
		FEATURES.add(new GuardianFeatures());
		*/
	}
	
	public void init() {
		
		for (EntityFeatures features : FEATURES) 
			features.preInit();
		
		// TODO RoughConfig.loadFeatures();
		for (EntityFeatures features : FEATURES) 
			features.initConfig();	
		
		for (EntityFeatures features : FEATURES) 
			features.postInit();
		
		//AttributeHelper.initAttributeOption();
		
		// SpawnHelper.initSpawnOption();
		// TODO SpawnHelper.addEntries();
		
		//BossHelper.initGlobalBossConfig();
		
		//TargetHelper.init();
		
		// TODO RoughConfig.saveFeatures();
	}

	/*
	 * Add custom attributes (health, damage, etc.)
	 */
	private static void addAttributes(Entity entity) {
		if (entity instanceof LivingEntity)
			AttributeHelper.addAttributes((LivingEntity)entity);
	}
	
	/*
	 * Add equipment and enchantments, and AI
	 * Also try to create bosses
	 */
	private static void addFeatures(EntityJoinWorldEvent event, MobEntity entity) {
		
		// Loop through the features list and add equipment and AI to the entity
		for (EntityFeatures features : FEATURES) {
			if (features.isEntity(entity)) {
				// Don't attempt to add equipment to a boss. It has already been given equipment in the BossApplier class
				if (!(entity instanceof IChampion)) {
					// Also test if baby zombies should have equipment
					if (!((LivingEntity)entity).isChild() || ((LivingEntity)entity).isChild() && !RoughConfig.disableBabyZombieEquipment) {
						if (!entity.getPersistentData().getBoolean(FEATURES_APPLIED)) {
							features.addFeatures(event, entity);
						}
					}
				}
			}
		}
	}
	
	/*
	 * Add custom AI goals to entity
	 */
	private static void addAI(EntityJoinWorldEvent event, MobEntity entity) {
		for (EntityFeatures features : FEATURES) {
			if (features.isEntity(entity)) {
				if (entity instanceof MobEntity)
					features.addAI(event, entity, entity.goalSelector, entity.targetSelector);
			}
		}
	}
	
	/*
	 * When an entity spawns, we do all the magic, such as adding equipment and AI, trying to turn it into a boss, etc.
	 */
	@SubscribeEvent
	public void onEntitySpawn(EntityJoinWorldEvent event) {
		
		/* TODO
		if (event.getEntity() instanceof PlayerEntity)
			FogEventHandler.playerRespawned = true;
		*/
		
		// Ignore spawn if on the client side, or if entity is the player.
		if (event.getWorld().isRemote || event.getEntity() instanceof PlayerEntity)
			return;
		
		MobEntity entity = null;
		
		if (event.getEntity() instanceof MobEntity)
			entity = (MobEntity) event.getEntity();
		
		boolean isBoss = entity instanceof IChampion;
		boolean canSpawn = SpawnHelper.checkSpawnConditions(event);
		
		// If enabled and the entity can spawn, add additional targets from config
		// Also add additional targets if the entity is ignoring spawn conditions
		/* TODO AI
		if (TargetHelper.targetAttackerEnabled() && canSpawn || TargetHelper.targetAttackerEnabled() && TargetHelper.ignoreSpawnConditions)
			TargetHelper.setTargets(entity);
		*/
		
		// If entity failed spawn conditions, and isn't a boss, then exit event and spawn vanilla mob with no features added
		if (!isBoss && !canSpawn)
			return;
		
		PlayerEntity closestPlayer = entity.world.getClosestPlayer(entity, -1.0D);
		GameStagesCompat.syncPlayerGameStages(closestPlayer);
		
		// If the Abilities Game Stage isn't enabled, or it is and player has the Abilities stage, then add attributes and AI to mob
		if (!GameStagesCompat.useAbilitiesStage() || GameStagesCompat.useAbilitiesStage() && GameStagesCompat.players.get(closestPlayer).get(Constants.PLAYER_ABILITIES_STAGE)) {
			// TODO addAttributes(entity);
			addAI(event, entity);
		}
		
		// If the Equipment Game Stage isn't enabled, or it is and player has the Equipment stage, then add equipment and other features to mob
		if (!GameStagesCompat.useEquipmentStage() || GameStagesCompat.useEquipmentStage() && GameStagesCompat.players.get(closestPlayer).get(Constants.PLAYER_EQUIPMENT_STAGE))
			addFeatures(event, entity);

		entity.getPersistentData().putBoolean(RoughApplier.FEATURES_APPLIED, true);
	}
	
	@SubscribeEvent
	public void onAttack(LivingAttackEvent event) {
		
		Entity trueSource = event.getSource().getTrueSource();
		Entity immediateSource = event.getSource().getImmediateSource();
		Entity target = event.getEntity();
		
		if (trueSource == null || target == null || target instanceof FakePlayer || trueSource instanceof FakePlayer || immediateSource instanceof FakePlayer || target.world.isRemote)
			return;

		for (EntityFeatures features : FEATURES) {
			if (trueSource instanceof LivingEntity && features.isEntity((LivingEntity)trueSource) && !(target instanceof PlayerEntity && ((PlayerEntity)target).isCreative())) {
				features.onAttack((LivingEntity)trueSource, immediateSource, target, event);
			}
			
			if (target instanceof LivingEntity && features.isEntity((LivingEntity)target) && !(trueSource instanceof PlayerEntity && ((PlayerEntity)trueSource).isCreative())) {
				features.onDefend((LivingEntity)target, trueSource, immediateSource, event);
			}
		}
	}
	
	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		
		Entity deadEntity = event.getEntity();
		
		if (deadEntity == null || deadEntity.world.isRemote || !(deadEntity instanceof LivingEntity))
			return;
		
		for (EntityFeatures features : FEATURES) {
			if (features.isEntity((LivingEntity)deadEntity)) {
				features.onDeath((LivingEntity)deadEntity, event.getSource());
			}
		}
	}
	
	@SubscribeEvent
	public void onFall(LivingFallEvent event) {
		
		Entity entity = event.getEntity();
		
		if (entity == null || entity.world.isRemote || !(entity instanceof LivingEntity))
			return;
		
		for (EntityFeatures features : FEATURES) {
			if (features.isEntity((LivingEntity)entity)) {
				features.onFall((LivingEntity)entity, event);
			}
		}
	}
	
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event) {
		
		PlayerEntity player = event.getPlayer();
		
		if (player == null || player.world.isRemote || player.isCreative())
			return;
		
		for (EntityFeatures features : FEATURES) {
			features.onBlockBreak(player, event);
		}
	}
	
	/* TODO
	@SubscribeEvent
	public void onTarget(LivingSetAttackTargetEvent event) {

		if (!TargetHelper.targetBlockerEnabled() || event.getTarget() == null || !(event.getTarget() instanceof MobEntity) || !(event.getEntityLiving() instanceof MobEntity))
			return;
		
		Class<? extends Entity> validAttacker = TargetHelper.getBlockerEntityForTarget(event.getTarget());
		
		if (validAttacker != null && validAttacker.isInstance(event.getEntityLiving())) {
			PlayerEntity player = event.getEntityLiving().getEntityWorld().getClosestPlayer(event.getEntityLiving(), 32);
			
			if (player != null && player.isOnSameTeam(event.getEntityLiving()))
				return;
			
			event.getEntityLiving().setRevengeTarget(player);
			((MobEntity)event.getEntityLiving()).setAttackTarget(player);
		}
	}
	*/
}