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
package com.p1ut0nium.roughmobsrevamped.misc;

import java.util.Random;

import com.p1ut0nium.roughmobsrevamped.entity.boss.IChampion;
import com.p1ut0nium.roughmobsrevamped.entity.boss.SkeletonChampionEntity;
import com.p1ut0nium.roughmobsrevamped.entity.boss.ZombieChampionEntity;
import com.p1ut0nium.roughmobsrevamped.misc.EquipHelper.EquipmentApplier;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.text.StringTextComponent;

public class BossHelper {
	
	public static final Random RND = new Random();
	public static final String BOSS = Constants.unique("isBoss");
	
	private static boolean bossWarning;
	private static boolean bossWarningSound;
	private static int bossWarningDist;
	
	public static boolean bossFogEnabled;
	public static String[] bossFogColor;
	public static int bossFogMaxDistance;
	public static int bossFogFarPlane;
	public static float bossFogFarPlaneScale;
	public static int bossFogStartDistance;
	public static boolean bossFogPlayerCough;
	
	public static boolean bossFogDoTEnabled;
	public static int bossFogDoTDelay;
	public static boolean bossFogDoTWarning;
	public static int bossFogDoTWarningTime;
	public static int bossFogDoTDamage;
	
	public static boolean bossBatSwarmEnabled;
	public static int bossBatSwarmCount;
	public static int bossBatSwarmDelay;
	public static int bossBatSwarmRange;
	public static int bossBatSwarmDamage;
	public static int bossBatSwarmHealth;
	public static int bossBatSwarmAttackRange;

	public static void initGlobalBossConfig() {
		if (!hasDefaultConfig())
			return;
		
		/* TODO Config
		RoughConfig.getConfig().addCustomCategoryComment("BossGlobal", "Miscellaneous config options which affect all bosses");
		
		bossWarning = RoughConfig.getBoolean("BossGlobal", "_SpawnWarning", true, "Enable this to have a chat message warning of a boss spawn.");
		bossWarningDist = RoughConfig.getInteger("BossGlobal", "_SpawnWarningDistance", 50, 0, Short.MAX_VALUE, "Max bos spawn distance from a player that will trigger a warning.\nUsed for both chat messages and sounds.");
		bossWarningSound = RoughConfig.getBoolean("BossGlobal", "_SpawnWarningSound", true, "Play a warning sound when a boss spawns.");
		
		RoughConfig.getConfig().addCustomCategoryComment("BatSwarm", "Configuration options for the Bat Swarm attack.");
		
		bossBatSwarmEnabled = RoughConfig.getBoolean("BatSwarm", "_Enabled", true, "Disable this if you don't want bosses to use Bat Swarm attacks.", true);
		bossBatSwarmCount = RoughConfig.getInteger("BatSwarm", "_BatCount", 3, 0, Short.MAX_VALUE, "The number of bats that attack when a boss fires his Bat Swarm ability.");
		bossBatSwarmDelay = RoughConfig.getInteger("BatSwarm", "_CoolDown", 3, 0, Short.MAX_VALUE, "The cooldown (in seconds) before the boss can fire Bat Swarm again.");
		bossBatSwarmRange = RoughConfig.getInteger("BatSwarm", "_UseRange", 20, 0, Short.MAX_VALUE, "How close a player must be to the boss before it will fire off a Bat Swarm attack.");
		bossBatSwarmAttackRange = RoughConfig.getInteger("BatSwarm", "_BatAttackRange", 20, 0, Short.MAX_VALUE, "How close a player must be before the bat swarm attacks.");
		bossBatSwarmDamage = RoughConfig.getInteger("BatSwarm", "_Damage", 1, 0, Short.MAX_VALUE, "How many half hearts (minus damage immunity) each bat does on attack.");
		bossBatSwarmHealth = RoughConfig.getInteger("BatSwarm", "_Health", 6, 1, Short.MAX_VALUE, "How much health each bat in the swarm has.");
		*/
	}
	
	public static boolean hasDefaultConfig() {
		return true;
	}
		
	public static abstract class BossApplier {
			
		private EquipmentApplier equipApplier;
			
		private final String name;
		private final int defaultChampionChance;
		private final float defaultEnchMultiplier;
		private final float defaultDropChance;
		private final String[] defaultChampionNames;

		// TODO private String[] bossColors;
			
		public BossApplier(String name, int defaultChampionChance, float defaultEnchMultiplier, float defaultDropChance, String[] championNames) {
				
			this.name = name;
			this.defaultChampionChance = defaultChampionChance;
			this.defaultEnchMultiplier = defaultEnchMultiplier;
			this.defaultDropChance = defaultDropChance;
			this.defaultChampionNames = championNames;

			equipApplier = new EquipmentApplier(name + " boss", 1, 1, 1, this.defaultEnchMultiplier, this.defaultDropChance);
		}	
			
		public void initConfig() {

			boolean isBoss = true;
			boolean useDefaultValues = true;
			equipApplier.initConfig(isBoss, useDefaultValues);
			//TODO bossColors = RoughConfig.getStringArray(name, "_BossColors", Constants.BOSS_COLORS, "(WIP) Color theme for boss particles, etc.\nValues are Red, Green, Blue from 0.0 to 1.0");
		}

		public void postInit() {
			equipApplier.createPools();
		}
			
		public LivingEntity trySetBoss(LivingEntity entity) {
				
			if (defaultChampionChance <= 0 || RND.nextInt(defaultChampionChance) != 0 || (entity instanceof ZombieEntity && ((ZombieEntity)entity).isChild()))
				return null;
				
			// Despawn normal skeletons and zombies and replace with BossSkeleton or BossZombie respectively
			IChampion boss = null;

			String entityTypeName = entity.getDisplayName().getUnformattedComponentText();
				
			switch (entityTypeName) {
				case "Zombie":
					boss = new ZombieChampionEntity(entity.world);
					((LivingEntity)boss).setPosition(entity.posX, entity.posY, entity.posZ);
					entity.world.addEntity((ZombieChampionEntity) boss);
					break;
				case "Skeleton":
					boss = new SkeletonChampionEntity(entity.world);
					((LivingEntity)boss).setPosition(entity.posX, entity.posY, entity.posZ);
					entity.world.addEntity((SkeletonChampionEntity) boss);
					break;				
			}
				
			// Remove vanilla mob
			entity.remove();
				
			if(boss != null) {
					
				//TODO boss.setBossColorTheme(bossColors);
			
				// TODO Fix attribute helper stuff
				// Add custom attributes
				//AttributeHelper.applyAttributeModifier((LivingEntity)boss, SharedMonsterAttributes.MAX_HEALTH, name + "BossHealth", 0, ((LivingEntity)boss).getMaxHealth()*2);
				//AttributeHelper.applyAttributeModifier((LivingEntity)boss, SharedMonsterAttributes.KNOCKBACK_RESISTANCE, name + "BossKnock", 1, 1);
				
				// Add equipment
				boolean isBoss = true;
				equipApplier.equipEntity((LivingEntity)boss, isBoss);
				
				// Set Bosses name
				StringTextComponent bossName = new StringTextComponent(defaultChampionNames[RND.nextInt(defaultChampionNames.length)]);
				((LivingEntity)boss).setCustomName(bossName);
				
				// Add chat message warning of new boss
				// TODO - move to network packets?
				/*
				if (bossWarning) {
					StringTextComponent bossWarningMsg = new StringTextComponent(bossName + ", a powerful " + entityTypeName + " warlord, has joined the battlefield.");
					bossWarningMsg.getStyle().setColor(TextFormatting.RED);
					bossWarningMsg.getStyle().setBold(true);
					
					List<PlayerEntity> players = ((LivingEntity)boss).world.getEntitiesWithinAABB(PlayerEntity.class, (((LivingEntity)boss).getBoundingBox().grow(bossWarningDist)));
					if (players != null) {
						for (PlayerEntity player : players)
							player.sendMessage(bossWarningMsg);
					}
				}
				
				// Play warning sound of boss spawn
				if (bossWarningSound) {
					((LivingEntity)boss).playSound(ModSounds.ENTITY_BOSS_SPAWN, (bossWarningDist / 16), 0.5F);
				}
				*/
				
				// Add Boss features
				((LivingEntity)boss).getPersistentData().putBoolean(BOSS, true);
				addBossFeatures((LivingEntity)boss);
				
				return (LivingEntity)boss;
			}

			return null;
		}
			
		public abstract void addBossFeatures(LivingEntity boss);
	}

	public static boolean isBoss(Entity entity) {
		return entity.getPersistentData() != null && (entity.getPersistentData()).getBoolean(BOSS);
	}
}
