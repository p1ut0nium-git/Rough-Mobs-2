package com.p1ut0nium.roughmobsrevamped.misc;

import java.util.List;
import java.util.Random;

import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.entities.BossSkeleton;
import com.p1ut0nium.roughmobsrevamped.entities.BossZombie;
import com.p1ut0nium.roughmobsrevamped.entities.IBoss;
import com.p1ut0nium.roughmobsrevamped.misc.EquipHelper.EquipmentApplier;
import com.p1ut0nium.roughmobsrevamped.util.Constants;
import com.p1ut0nium.roughmobsrevamped.util.handlers.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

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
		
		RoughConfig.getConfig().addCustomCategoryComment("BossGlobal", "Miscellaneous config options which affect all bosses");
		
		bossWarning = RoughConfig.getBoolean("BossGlobal", "_SpawnWarning", true, "Enable this to have a chat message warning of a boss spawn.");
		bossWarningDist = RoughConfig.getInteger("BossGlobal", "_SpawnWarningDistance", 50, 0, Short.MAX_VALUE, "Max bos spawn distance from a player that will trigger a warning.\nUsed for both chat messages and sounds.");
		bossWarningSound = RoughConfig.getBoolean("BossGlobal", "_SpawnWarningSound", true, "Play a warning sound when a boss spawns.");
		
		RoughConfig.getConfig().addCustomCategoryComment("BossFog", "Configuration options for adjusting the poisonous fog that shrouds all bosses.");
		
		bossFogEnabled = RoughConfig.getBoolean("BossFog", "_Enabled", true, "Enable this to have thick colored fog around bosses.", true);
		bossFogColor = RoughConfig.getStringArray("BossFog", "_FogColor", Constants.FOG_COLORS, "Change these three values between 0.0 and 1.0 to change the fog color.\nRed, Green, Blue\n");
		bossFogMaxDistance = RoughConfig.getInteger("BossFog", "_FogMaxDist", 20, 0, 100, "Max distance from boss for fog to render.\nFog will only occur if you are within this distance");
		bossFogStartDistance = RoughConfig.getInteger("BossFog", "_FogStartDist", 1, 0, 50, "How far away from boss before fog begins to fade from maximum density.\nMust be a value lower than BossFogMaxDist");
		bossFogFarPlane = RoughConfig.getInteger("BossFog", "_FogFarPlane", 10, 0, 192, "This effects how far away from you before the fog is at maximum thickness.");
		bossFogFarPlaneScale = RoughConfig.getFloat("BossFog", "_FogFarPlaneScale", 0.2F, 0.0F, 0.8F, "This controls how thick/strong the fog is.");
		
		bossFogDoTEnabled = RoughConfig.getBoolean("BossFog", "_DoTEnabled", true, "If enabled, boss fog will cause poison damage over time.", true);
		bossFogDoTDelay = RoughConfig.getInteger("BossFog", "_DoTCoolDown", 10, 0, Short.MAX_VALUE, "The cooldown (in seconds) between each damage event while inside the fog.");
		bossFogDoTWarning = RoughConfig.getBoolean("BossFog", "_DoTWarning", true, "Should the player recieve a chat warning message when entering the poisonous fog?");
		bossFogDoTWarningTime = RoughConfig.getInteger("BossFog", "_DoTWarningTime", 60, 1, Short.MAX_VALUE, "Controls how frequent (in seconds) a player can be warned when entering boss fog.");
		bossFogDoTDamage = RoughConfig.getInteger("BossFog", "_DoTDamage", 1, 1, 127, "How many half hearts the fog DoT does per hit.");
		bossFogPlayerCough = RoughConfig.getBoolean("BossFog", "_PlayerCough", true, "Disable this if you find the player cough sound annoying.\nOnly happens in poisonous fog.");
		
		RoughConfig.getConfig().addCustomCategoryComment("BatSwarm", "Configuration options for the Bat Swarm attack.");
		
		bossBatSwarmEnabled = RoughConfig.getBoolean("BatSwarm", "_Enabled", true, "Disable this if you don't want bosses to use Bat Swarm attacks.", true);
		bossBatSwarmCount = RoughConfig.getInteger("BatSwarm", "_BatCount", 3, 0, Short.MAX_VALUE, "The number of bats that attack when a boss fires his Bat Swarm ability.");
		bossBatSwarmDelay = RoughConfig.getInteger("BatSwarm", "_CoolDown", 3, 0, Short.MAX_VALUE, "The cooldown (in seconds) before the boss can fire Bat Swarm again.");
		bossBatSwarmRange = RoughConfig.getInteger("BatSwarm", "_UseRange", 20, 0, Short.MAX_VALUE, "How close a player must be to the boss before it will fire off a Bat Swarm attack.");
		bossBatSwarmAttackRange = RoughConfig.getInteger("BatSwarm", "_BatAttackRange", 20, 0, Short.MAX_VALUE, "How close a player must be before the bat swarm attacks.");
		bossBatSwarmDamage = RoughConfig.getInteger("BatSwarm", "_Damage", 1, 0, Short.MAX_VALUE, "How many half hearts (minus damage immunity) each bat does on attack.");
		bossBatSwarmHealth = RoughConfig.getInteger("BatSwarm", "_Health", 6, 1, Short.MAX_VALUE, "How much health each bat in the swarm has.");
	}
	
	public static boolean hasDefaultConfig() {
		return true;
	}
	
	public static abstract class BossApplier {
		
		private EquipmentApplier equipApplier;
		
		private final String name;
		private final int defaultBossChance;
		private final float defaultEnchMultiplier;
		private final float defaultDropChance;
		private final String[] defaultBossNames;
		
		private int bossChance;
		private String[] bossNames;
		// TODO private String[] bossColors;
		
		public BossApplier(String name, int defaultBossChance, float defaultEnchMultiplier, float defaultDropChance, String[] defaultBossNames) {
			
			this.name = name;
			this.defaultBossChance = defaultBossChance;
			this.defaultEnchMultiplier = defaultEnchMultiplier;
			this.defaultDropChance = defaultDropChance;
			this.defaultBossNames = defaultBossNames;

			equipApplier = new EquipmentApplier(name + " boss", 1, 1, 1, this.defaultEnchMultiplier, this.defaultDropChance);
		}	
		
		public void initConfig() {

			// TODO - add boss equipment to config
			equipApplier.initConfig(Constants.DEFAULT_BOSS_MAINHAND, 
									Constants.DEFAULT_BOSS_OFFHAND, 
									Constants.DEFAULT_BOSS_HELMETS, 
									Constants.DEFAULT_BOSS_CHESTPLATES, 
									Constants.DEFAULT_BOSS_LEGGINGS, 
									Constants.DEFAULT_BOSS_BOOTS, 
									Constants.DEFAULT_WEAPON_ENCHANTS, 
									Constants.DEFAULT_ARMOR_ENCHANTS, 
									true);
			
			bossChance = RoughConfig.getInteger(name, "_BossChance", defaultBossChance, 0, Short.MAX_VALUE, "Chance (1 in X) for a newly spawned " + name + " to become a boss " + name);
			bossNames = RoughConfig.getStringArray(name, "_BossNames", defaultBossNames, name + " boss names. Please be more creative than I am... :P");
			//TODO bossColors = RoughConfig.getStringArray(name, "_BossColors", Constants.BOSS_COLORS, "(WIP) Color theme for boss particles, etc.\nValues are Red, Green, Blue from 0.0 to 1.0");
		}

		public void postInit() {
			equipApplier.createPools();
		}
		
		public EntityLiving trySetBoss(Entity entity) {
			
			if (bossChance <= 0 || RND.nextInt(bossChance) != 0 || (entity instanceof EntityZombie && ((EntityZombie)entity).isChild()))
				return null;
			
			// Despawn normal skeletons and zombies and replace with BossSkeleton or BossZombie respectively
			IBoss boss = null;
			
			String entityTypeName = entity.getName();
			
			switch (entityTypeName) {
				case "Zombie":
					boss = new BossZombie(entity.world);
					((EntityLiving)boss).setPosition(entity.posX, entity.posY, entity.posZ);
					entity.world.spawnEntity((BossZombie) boss);				
					break;
				case "Skeleton":
					boss = new BossSkeleton(entity.world);
					((EntityLiving)boss).setPosition(entity.posX, entity.posY, entity.posZ);
					entity.world.spawnEntity((BossSkeleton) boss);
					break;				
			}
			
			// Remove vanilla mob
			entity.setDead();
			
			if(boss != null) {
				
				//TODO boss.setBossColorTheme(bossColors);
			
				// Add custom attributes
				AttributeHelper.applyAttributeModifier((EntityLiving)boss, SharedMonsterAttributes.MAX_HEALTH, name + "BossHealth", 0, ((EntityLiving)boss).getMaxHealth()*2);
				AttributeHelper.applyAttributeModifier((EntityLiving)boss, SharedMonsterAttributes.KNOCKBACK_RESISTANCE, name + "BossKnock", 1, 1);
				
				// Add equipment
				boolean isBoss = true;
				equipApplier.equipEntity((EntityLiving)boss, isBoss);
				
				// Set Bosses name
				String bossName = bossNames[RND.nextInt(bossNames.length)];
				((EntityLiving)boss).setCustomNameTag(bossName);
				
				// Add chat message warning of new boss
				// TODO - move to network packets?
				if (bossWarning) {
					TextComponentString bossWarningMsg = new TextComponentString(bossName + ", a powerful " + entityTypeName + " warlord, has joined the battlefield.");
					bossWarningMsg.getStyle().setColor(TextFormatting.RED);
					bossWarningMsg.getStyle().setBold(true);
					
					// TODO Change to all players within boosWarningDist
					List<EntityPlayer> players = ((EntityLiving)boss).world.getEntitiesWithinAABB(EntityPlayer.class, (((EntityLiving)boss).getEntityBoundingBox().grow(bossWarningDist)));
					if (players != null) {
						for (EntityPlayer player : players)
							player.sendMessage(bossWarningMsg);
					}
				}
				
				// Play warning sound of boss spawn
				if (bossWarningSound) {
					((EntityLiving)boss).playSound(SoundHandler.ENTITY_BOSS_SPAWN, (bossWarningDist / 16), 0.5F);
				}
				
				// Add Boss features
				((EntityLiving)boss).getEntityData().setBoolean(BOSS, true);
				addBossFeatures((EntityLiving)boss);
				
				return (EntityLiving)boss;
			}

			return null;
		}


		public abstract void addBossFeatures(Entity boss);
	}

	public static boolean isBoss(Entity entity) {
		return entity.getEntityData() != null && entity.getEntityData().getBoolean(BOSS);
	}
}
