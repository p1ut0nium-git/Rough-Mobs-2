package com.p1ut0nium.roughmobsrevamped.features;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.p1ut0nium.roughmobsrevamped.RoughMobs;
import com.p1ut0nium.roughmobsrevamped.ai.combat.RoughAIFlameTouch;
import com.p1ut0nium.roughmobsrevamped.compat.GameStagesCompat;
import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.misc.FeatureHelper;
import com.p1ut0nium.roughmobsrevamped.util.Constants;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

public abstract class EntityFeatures {

	private boolean flameTouch;
	private boolean pushAttackersAway;
	private float deathExplosionStrength;
	private boolean deathExplosionDestroyBlocks;
	private float pushStrength;

	public static final Random RND = new Random();
	public static final int MAX = Short.MAX_VALUE;
	
	//Default
	public String name;
	protected List<Class<? extends Entity>> entityClasses;

	//Config
	protected boolean featuresEnabled;
	protected List<String> entityNames;
	
	@SuppressWarnings("unchecked")
	public EntityFeatures(String name, Class<? extends Entity>... entityClasses) {
		this.name = name;
		this.entityClasses = Arrays.asList(entityClasses);
	}
	
	public boolean isEntity(Entity creature) {
		ResourceLocation loc = EntityList.getKey(creature);
		return featuresEnabled && loc != null && entityNames.contains(loc.toString());
	}
	
	public void initConfig() {
		
		if (!hasDefaultConfig())
			return;
	
		RoughConfig.getConfig().addCustomCategoryComment(name, "Configuration options which affect " + name + " features");
		
		featuresEnabled = RoughConfig.getBoolean(name, "FeaturesEnabled", true, "Set to false to disable ALL %s features", true);
		entityNames = Arrays.asList(RoughConfig.getStringArray(name, "Entities", Constants.getRegNames(entityClasses).toArray(new String[0]), "Entities which count as %s entities"/*, EntityList.getEntityNameList().toArray(new String[0])*/));

		pushAttackersAway = RoughConfig.getBoolean(name, "PushAttackersAway", true, "Set to false to prevent %ss from pushing attackers away");
		pushStrength = RoughConfig.getFloat(name, "PushStrength", 1.0F, 0F, MAX, "Amount of damage done to attacker when pushed away.");
		deathExplosionStrength = RoughConfig.getFloat(name, "DeathExplosionStrength", 1.0F, 0F, MAX, "Explosion strength of the explosions, which %ss create on death\nSet to 0 to disable this feature");
		deathExplosionDestroyBlocks = RoughConfig.getBoolean(name, "DeathExplosionDestroyBlocks", false, "Set to true to enable destoying blocks when a Blaze explodes on death.");

	}
	
	private boolean hasDefaultConfig() {
		return true;
	}
	
	public void addFeatures(EntityJoinWorldEvent event, Entity entity) {
	}
	
	public boolean bossesEnabled(Entity entity) {

		boolean bossStageEnabled = GameStagesCompat.useBossStage();	
		
		if (bossStageEnabled) {
			EntityPlayer playerClosest = entity.world.getClosestPlayerToEntity(entity, -1.0D);
			return (GameStageHelper.hasAnyOf(playerClosest, Constants.ROUGHMOBSALL, Constants.ROUGHMOBSBOSS));
		}
		
		// If boss game stage isn't enabled, then its ok to spawn bosses
		return true;
	}
	
	public void addAI(EntityJoinWorldEvent event, Entity entity, EntityAITasks tasks, EntityAITasks targetTasks) {
		if (entity instanceof EntityLiving && flameTouch)
			tasks.addTask(1, new RoughAIFlameTouch((EntityLiving) entity));
	}

	public void onAttack(Entity attacker, Entity immediateAttacker, Entity target, LivingAttackEvent event) {
	}

	public void onDefend(Entity target, Entity attacker, Entity immediateAttacker, LivingAttackEvent event) {
		if (flameTouch)
			attacker.setFire(8);
		if (pushAttackersAway && attacker instanceof EntityLivingBase && attacker == immediateAttacker) {
			FeatureHelper.knockback(target, (EntityLivingBase) attacker, 1F, 0.05F);
			attacker.attackEntityFrom(DamageSource.GENERIC, pushStrength);

			FeatureHelper.playSound(target, SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.7f, 1.0f);
		}
	}

	public void onDeath(Entity deadEntity, DamageSource source) {
		if (deathExplosionStrength > 0 && !(source.getTrueSource() instanceof FakePlayer)) {
			deadEntity.world.createExplosion(deadEntity, deadEntity.posX, deadEntity.posY, deadEntity.posZ, deathExplosionStrength, deathExplosionDestroyBlocks);
		}
		// If the mob was killed by a player, update the player's kill count.
		if (source.getTrueSource() instanceof EntityPlayer) {
			//TODO PlayerHelper.setPlayerMobKills();
		}
	}
	
	public void onFall(Entity entity, LivingFallEvent event) {
	}
	
	public void onBlockBreak(EntityPlayer player, BreakEvent event) {
	}
	
	public void preInit() {
	}

	public void postInit() {
	}
}
