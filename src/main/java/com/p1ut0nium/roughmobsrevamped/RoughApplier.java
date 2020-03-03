package com.p1ut0nium.roughmobsrevamped;

import java.util.ArrayList;
import java.util.List;

import com.p1ut0nium.roughmobsrevamped.compat.CompatHandler;
import com.p1ut0nium.roughmobsrevamped.compat.GameStagesCompat;
import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.entities.IBoss;
import com.p1ut0nium.roughmobsrevamped.features.BlazeFeatures;
import com.p1ut0nium.roughmobsrevamped.features.CreeperFeatures;
import com.p1ut0nium.roughmobsrevamped.features.EndermanFeatures;
import com.p1ut0nium.roughmobsrevamped.features.EndermiteFeatures;
import com.p1ut0nium.roughmobsrevamped.features.EntityFeatures;
import com.p1ut0nium.roughmobsrevamped.features.GhastFeatures;
import com.p1ut0nium.roughmobsrevamped.features.GuardianFeatures;
import com.p1ut0nium.roughmobsrevamped.features.HostileHorseFeatures;
import com.p1ut0nium.roughmobsrevamped.features.MagmaCubeFeatures;
import com.p1ut0nium.roughmobsrevamped.features.SilverfishFeatures;
import com.p1ut0nium.roughmobsrevamped.features.SkeletonFeatures;
import com.p1ut0nium.roughmobsrevamped.features.SlimeFeatures;
import com.p1ut0nium.roughmobsrevamped.features.SpiderFeatures;
import com.p1ut0nium.roughmobsrevamped.features.WitchFeatures;
import com.p1ut0nium.roughmobsrevamped.features.WitherFeatures;
import com.p1ut0nium.roughmobsrevamped.features.ZombieFeatures;
import com.p1ut0nium.roughmobsrevamped.features.ZombiePigmanFeatures;
import com.p1ut0nium.roughmobsrevamped.misc.AttributeHelper;
import com.p1ut0nium.roughmobsrevamped.misc.BossHelper;
import com.p1ut0nium.roughmobsrevamped.misc.Constants;
import com.p1ut0nium.roughmobsrevamped.misc.SpawnHelper;
import com.p1ut0nium.roughmobsrevamped.misc.TargetHelper;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
	}
	
	public void preInit() {
		
		for (EntityFeatures features : FEATURES) 
			features.preInit();
		
		
		
		RoughConfig.loadFeatures();
	}		
	
	public void postInit() {
		
		for (EntityFeatures features : FEATURES) 
			features.postInit();
		
		AttributeHelper.initAttributeOption();
		
		SpawnHelper.initSpawnOption();
		SpawnHelper.addEntries();
		
		BossHelper.initGlobalBossConfig();
		
		TargetHelper.init();
		
		RoughConfig.saveFeatures();
	}
	
	/*
	 * Test if Game Stages is loaded, if any stages are enabled, and if the player has the stages
	 */
	private void getGameStages(EntityPlayer player) {

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
			
			if (entity instanceof EntityLiving)
				AttributeHelper.addAttributes((EntityLiving)entity);
		}
	}
	
	/*
	 * Add equipment and enchantments, and AI
	 * Also try to create bosses
	 */
	private void addFeatures(EntityJoinWorldEvent event, Entity entity) {
		
		boolean isBoss = entity instanceof IBoss;
		
		// Loop through the features list and add equipment and AI to the entity
		for (EntityFeatures features : FEATURES) 
		{
			if (features.isEntity(entity))
			{
				// Don't attempt to add equipment to a boss. It has already been given equipment in the BossApplier class
				// Also test if baby zombies should have equipment
				if (!isBoss || ((EntityLiving)entity).isChild() && SpawnHelper.disableBabyZombieEquipment() != true) {
				// Test to see if equip stage is disabled or if it is enabled and player has it
					if (equipStageEnabled == false || equipStageEnabled && playerHasEquipStage) {
	
						if (!entity.getEntityData().getBoolean(FEATURES_APPLIED)) 
							features.addFeatures(event, entity);
					}
				}
				
				// Test to see if abilities stage is disabled or if it is enabled and player has it
				if (abilsStageEnabled == false || abilsStageEnabled && playerHasAbilsStage) {

					if (entity instanceof EntityLiving)
						features.addAI(event, entity, ((EntityLiving)entity).tasks, ((EntityLiving)entity).targetTasks);
				}
			}
		}
	}
	
	/*
	 * When an entity spawns, we do all the magic, such as adding equipment and AI, trying to turn it into a boss, etc.
	 */
	
	//TODO Move to SpawnHandler class?
	@SubscribeEvent
	public void onEntitySpawn(EntityJoinWorldEvent event) {
		
		// Ignore spawn if on the client side, or if entity is the player.
		if (event.getWorld().isRemote || event.getEntity() instanceof EntityPlayer)
			return;
		
		Entity entity = event.getEntity();
		boolean isBoss = entity instanceof IBoss;
		
		// If the entity is not a boss, then test spawn conditions
		if (!isBoss && !SpawnHelper.checkSpawnConditions(event))
			return;
			
		getGameStages(entity.world.getClosestPlayerToEntity(entity, -1.0D));
		addAttributes(entity);
		addFeatures(event, entity);
		
		// Add additional targets from config
		if (TargetHelper.targetAttackerEnabled())
			TargetHelper.setTargets(entity);
		
		entity.getEntityData().setBoolean(FEATURES_APPLIED, true);
	}
	
	@SubscribeEvent
	public void onAttack(LivingAttackEvent event) {
		
		Entity trueSource = event.getSource().getTrueSource();
		Entity immediateSource = event.getSource().getImmediateSource();
		Entity target = event.getEntity();
		
		if (trueSource == null || target == null || target instanceof FakePlayer || trueSource instanceof FakePlayer || immediateSource instanceof FakePlayer || target.world.isRemote)
			return;
		
		// boolean finish = false;
		for (EntityFeatures features : FEATURES) 
		{
			if (trueSource instanceof EntityLiving && features.isEntity((EntityLiving)trueSource) && !(target instanceof EntityPlayer && ((EntityPlayer)target).isCreative())) 
			{
				features.onAttack((EntityLiving)trueSource, immediateSource, target, event);
				// finish = true;
			}
			
			if (target instanceof EntityLiving && features.isEntity((EntityLiving)target) && !(trueSource instanceof EntityPlayer && ((EntityPlayer)trueSource).isCreative())) 
			{
				features.onDefend((EntityLiving)target, trueSource, immediateSource, event);
				// finish = true;
			}
		}
	}
	
	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		
		Entity deadEntity = event.getEntity();
		
		if (deadEntity == null || deadEntity.world.isRemote || !(deadEntity instanceof EntityLiving))
			return;
		
		for (EntityFeatures features : FEATURES) 
		{
			if (features.isEntity((EntityLiving)deadEntity)) 
			{
				features.onDeath((EntityLiving)deadEntity, event.getSource());
			}
		}
	}
	
	@SubscribeEvent
	public void onFall(LivingFallEvent event) {
		
		Entity entity = event.getEntity();
		
		if (entity == null || entity.world.isRemote || !(entity instanceof EntityLiving))
			return;
		
		for (EntityFeatures features : FEATURES) 
		{
			if (features.isEntity((EntityLiving)entity)) 
			{
				features.onFall((EntityLiving)entity, event);
			}
		}
	}
	
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event) {
		
		EntityPlayer player = event.getPlayer();
		
		if (player == null || player.world.isRemote || player.isCreative())
			return;
		
		for (EntityFeatures features : FEATURES) 
		{
			features.onBlockBreak(player, event);
		}
	}
	
	@SubscribeEvent
	public void onTarget(LivingSetAttackTargetEvent event) {
		
		if (!TargetHelper.targetBlockerEnabled() || event.getTarget() == null || !(event.getTarget() instanceof EntityMob) || !(event.getEntityLiving() instanceof EntityMob))
			return;
		
		Class<? extends Entity> validAttacker = TargetHelper.getBlockerEntityForTarget(event.getTarget());
		
		if (validAttacker != null && validAttacker.isInstance(event.getEntityLiving())) 
		{
			EntityPlayer player = event.getEntityLiving().getEntityWorld().getNearestAttackablePlayer(event.getEntityLiving(), 32, 32);
			
			if (player != null && player.isOnSameTeam(event.getEntityLiving()))
				return;
			
			event.getEntityLiving().setRevengeTarget(player);
			((EntityLiving)event.getEntityLiving()).setAttackTarget(player);
		}
	}
}
