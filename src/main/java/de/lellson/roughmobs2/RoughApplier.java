package de.lellson.roughmobs2;

import java.util.ArrayList;
import java.util.List;

import de.lellson.roughmobs2.compat.GameStages;
import de.lellson.roughmobs2.config.RoughConfig;
import de.lellson.roughmobs2.features.BlazeFeatures;
import de.lellson.roughmobs2.features.CreeperFeatures;
import de.lellson.roughmobs2.features.EndermanFeatures;
import de.lellson.roughmobs2.features.EndermiteFeatures;
import de.lellson.roughmobs2.features.EntityFeatures;
import de.lellson.roughmobs2.features.GhastFeatures;
import de.lellson.roughmobs2.features.GuardianFeatures;
import de.lellson.roughmobs2.features.HostileHorseFeatures;
import de.lellson.roughmobs2.features.MagmaCubeFeatures;
import de.lellson.roughmobs2.features.SilverfishFeatures;
import de.lellson.roughmobs2.features.SkeletonFeatures;
import de.lellson.roughmobs2.features.SlimeFeatures;
import de.lellson.roughmobs2.features.SpiderFeatures;
import de.lellson.roughmobs2.features.WitchFeatures;
import de.lellson.roughmobs2.features.WitherFeatures;
import de.lellson.roughmobs2.features.ZombieFeatures;
import de.lellson.roughmobs2.features.ZombiePigmanFeatures;
import de.lellson.roughmobs2.misc.AttributeHelper;
import de.lellson.roughmobs2.misc.Constants;
import de.lellson.roughmobs2.misc.SpawnHelper;
import de.lellson.roughmobs2.misc.TargetHelper;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
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
	
	private static Boolean gameStagesEnabled;
	private static Boolean playerHasAbilsStage;
	private static Boolean abilsStageEnabled;
	private static Boolean equipStageEnabled;
	private static Boolean bossStageEnabled;
	private static Boolean playerHasEquipStage;
	private static Boolean playerHasBossStage;
	private static Boolean bossesCanSpawn;
	
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
		
		TargetHelper.init();
		
		RoughConfig.saveFeatures();
	}
	
	@SubscribeEvent
	public void onEntitySpawn(EntityJoinWorldEvent event) {
		
		if (event.getWorld().isRemote)
			return;
		
		Entity entity = event.getEntity();
		
		if (entity instanceof EntityPlayer)
			return;
		
		// Get nearest player to the spawned mob
		EntityPlayer playerClosest = entity.world.getClosestPlayerToEntity(entity, -1.0D);
		
		// Test spawn conditions (from config file)
		SpawnHelper spawnHelper = new SpawnHelper();
		if (!spawnHelper.canMobSpawn(entity, entity.world, playerClosest))
			return;
		
		// Get all Game Stage related info
		gameStagesEnabled = GameStages.isStagesEnabled();
		abilsStageEnabled = GameStages.useAbilitiesStage();
		equipStageEnabled = GameStages.useEquipmentStage();
		bossStageEnabled = GameStages.useBossStage();	
		
		// Test to see if player has these stages unlocked.
		if (gameStagesEnabled) {
			playerHasEquipStage = GameStageHelper.hasAnyOf(playerClosest, Constants.ROUGHMOBSALL, Constants.ROUGHMOBSEQUIP);
			playerHasAbilsStage = GameStageHelper.hasAnyOf(playerClosest, Constants.ROUGHMOBSALL, Constants.ROUGHMOBSABILS);
			playerHasBossStage = GameStageHelper.hasAnyOf(playerClosest, Constants.ROUGHMOBSALL, Constants.ROUGHMOBSBOSS);
		} else {
			playerHasEquipStage = playerHasAbilsStage = playerHasBossStage = false;
		}
		
		// Test to see if player has boss stage
		if (gameStagesEnabled == false || bossStageEnabled == false || bossStageEnabled && playerHasBossStage) {
			bossesCanSpawn = true;
		} else {
			bossesCanSpawn = false;
		}
		
		// Test to see if player has abilities game stage and if stages are enabled
		if (gameStagesEnabled == false || abilsStageEnabled == false || abilsStageEnabled && playerHasAbilsStage) {
			
			if (entity instanceof EntityLiving)
				AttributeHelper.addAttributes((EntityLiving)entity);
		}
		
		// Loop through each spawned entity and add features and AI
		for (EntityFeatures features : FEATURES) 
		{
			if (features.isEntity(entity))
			{

				// Test to see if player has equipment game stage
				if (gameStagesEnabled == false || equipStageEnabled == false || equipStageEnabled && playerHasEquipStage) {

					if (!entity.getEntityData().getBoolean(FEATURES_APPLIED)) 
						features.addFeatures(event, entity, bossesCanSpawn);
				}
				
				// Test to see if player has abilities game stage
				if (gameStagesEnabled == false || abilsStageEnabled == false || abilsStageEnabled && playerHasAbilsStage) {
					
					if (entity instanceof EntityLiving)
						features.addAI(event, entity, ((EntityLiving)entity).tasks, ((EntityLiving)entity).targetTasks);
				}
			}
		}
		
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
