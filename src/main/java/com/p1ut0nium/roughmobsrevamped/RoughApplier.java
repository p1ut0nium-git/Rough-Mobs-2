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
package com.p1ut0nium.roughmobsrevamped;

import java.util.ArrayList;
import java.util.List;

import com.p1ut0nium.roughmobsrevamped.client.FogEventHandler;
import com.p1ut0nium.roughmobsrevamped.compat.CompatHandler;
import com.p1ut0nium.roughmobsrevamped.compat.GameStagesCompat;
import com.p1ut0nium.roughmobsrevamped.config.FogConfig;
import com.p1ut0nium.roughmobsrevamped.entity.boss.IChampion;
import com.p1ut0nium.roughmobsrevamped.features.EntityFeatures;
import com.p1ut0nium.roughmobsrevamped.features.ZombieFeatures;
import com.p1ut0nium.roughmobsrevamped.init.ModSounds;
import com.p1ut0nium.roughmobsrevamped.misc.AttributeHelper;
import com.p1ut0nium.roughmobsrevamped.misc.BossHelper;
import com.p1ut0nium.roughmobsrevamped.misc.EquipHelper;
import com.p1ut0nium.roughmobsrevamped.misc.SpawnHelper;
import com.p1ut0nium.roughmobsrevamped.misc.TargetHelper;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;
import com.p1ut0nium.roughmobsrevamped.util.DamageSourceFog;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RoughApplier {
	
	public static final String FEATURES_APPLIED = Constants.unique("featuresApplied");
	
	public static final List<EntityFeatures> FEATURES = new ArrayList<EntityFeatures>();
	
	private static boolean gameStagesEnabled;
	private static boolean playerHasAbilsStage;
	private static boolean abilsStageEnabled;
	private static boolean equipStageEnabled;
	private static boolean playerHasEquipStage;
	
	public RoughApplier() {
		MinecraftForge.EVENT_BUS.register(this);
		
		FEATURES.add(new ZombieFeatures());
		/* TODO
		FEATURES.add(new SkeletonFeatures());
		FEATURES.add(new HostileHorseFeatures());
		FEATURES.add(new CreeperFeatures());
		FEATURES.add(new SlimeFeatures());
		FEATURES.add(new EndermanFeatures());
		FEATURES.add(new SpiderFeatures());
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
	
	public void preInit() {
		
		for (EntityFeatures features : FEATURES) 
			features.preInit();

		// TODO RoughConfig.loadFeatures();
	}		
	
	public void postInit() {
		
		for (EntityFeatures features : FEATURES) 
			features.postInit();
		
		AttributeHelper.initAttributeOption();
		
		SpawnHelper.initSpawnOption();
		// TODO SpawnHelper.addEntries();
		
		BossHelper.initGlobalBossConfig();
		
		TargetHelper.init();
		
		// TODO RoughConfig.saveFeatures();
	}
	
	/*
	 * Test if Game Stages is loaded, if any stages are enabled, and if the player has the stages
	 */
	private void getGameStages(PlayerEntity player) {

		gameStagesEnabled = CompatHandler.isGameStagesLoaded();
		abilsStageEnabled = GameStagesCompat.useAbilitiesStage();
		equipStageEnabled = GameStagesCompat.useEquipmentStage();
		
		// Test to see if player has these stages unlocked.
		if (gameStagesEnabled) {
			playerHasEquipStage = GameStageHelper.hasAnyOf(player, Constants.ROUGHMOBSALL, Constants.ROUGHMOBSEQUIP);
			playerHasAbilsStage = GameStageHelper.hasAnyOf(player, Constants.ROUGHMOBSALL, Constants.ROUGHMOBSABILS);
		} else {
			playerHasEquipStage = playerHasAbilsStage = false;
		}
	}
	
	private void addAttributes(Entity entity) {
		// Test to see if abilities stage is disabled or if it is enabled and player has it
		if (abilsStageEnabled == false || abilsStageEnabled && playerHasAbilsStage) {
			
			if (entity instanceof LivingEntity)
				AttributeHelper.addAttributes((LivingEntity)entity);
		}
	}
	
	/*
	 * Add equipment and enchantments, and AI
	 * Also try to create bosses
	 */
	private void addFeatures(EntityJoinWorldEvent event, Entity entity) {
		
		boolean isBoss = entity instanceof IChampion;
		
		// Loop through the features list and add equipment and AI to the entity
		for (EntityFeatures features : FEATURES) 
		{
			if (features.isEntity(entity))
			{
				// Don't attempt to add equipment to a boss. It has already been given equipment in the BossApplier class
				// Also test if baby zombies should have equipment
				if (!isBoss || ((LivingEntity)entity).isChild() && EquipHelper.disableBabyZombieEquipment() == false) {
				// Test to see if equip stage is disabled or if it is enabled and player has it
					if (equipStageEnabled == false || equipStageEnabled && playerHasEquipStage) {
						
						if (!entity.getPersistentData().getBoolean(FEATURES_APPLIED)) 
							features.addFeatures(event, entity);
					}
				}
				
				// Test to see if abilities stage is disabled or if it is enabled and player has it
				if (abilsStageEnabled == false || abilsStageEnabled && playerHasAbilsStage) {

					/* TODO AI
					if (entity instanceof LivingEntity)
						features.addAI(event, entity, ((LivingEntity)entity).tasks, ((LivingEntity)entity).targetTasks);
					*/
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityHurt(LivingAttackEvent event) {
		if (event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntity();
			if (!player.isCreative()) {
				if (event.getSource().equals(DamageSourceFog.POISONOUS_FOG)) {
					
					if (FogConfig.bossFogPlayerCough.get())
						playHurtSound(player);
				
					player.setHealth(player.getHealth() - FogConfig.bossFogDoTDamage.get());
					event.setCanceled(true);
				}
			}
		}
	}
	
	private void playHurtSound(PlayerEntity player) {
		player.world.playSound(null, player.getPosition(), ModSounds.ENTITY_PLAYER_COUGH, SoundCategory.PLAYERS, 1.0F, (float)Math.max(0.75, Math.random()));
	}
	
	/*
	 * When an entity spawns, we do all the magic, such as adding equipment and AI, trying to turn it into a boss, etc.
	 */
	@SubscribeEvent
	public void onEntitySpawn(EntityJoinWorldEvent event) {
		
		if (event.getEntity() instanceof PlayerEntity) {
			FogEventHandler.playerRespawned = true;
		}
		
		// Ignore spawn if on the client side, or if entity is the player.
		if (event.getWorld().isRemote || event.getEntity() instanceof PlayerEntity)
			return;
		
		Entity entity = event.getEntity();
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
		
		getGameStages(entity.world.getClosestPlayer(entity, -1.0D));
		addAttributes(entity);
		addFeatures(event, entity);

		entity.getPersistentData().putBoolean(FEATURES_APPLIED, true);
		// TODO entity.getEntityData().setBoolean(FEATURES_APPLIED, true);
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
}