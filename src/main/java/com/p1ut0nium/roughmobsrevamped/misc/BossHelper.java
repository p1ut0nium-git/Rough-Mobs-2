package com.p1ut0nium.roughmobsrevamped.misc;

import java.util.Random;

import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.entities.BossSkeleton;
import com.p1ut0nium.roughmobsrevamped.entities.BossZombie;
import com.p1ut0nium.roughmobsrevamped.entities.IBoss;
import com.p1ut0nium.roughmobsrevamped.misc.EquipHelper.EquipmentApplier;
import com.p1ut0nium.roughmobsrevamped.util.handlers.EffectsHandler;
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
	
	public static abstract class BossApplier {
		
		private EquipmentApplier equipApplier;
		
		private final String name;
		private final int defaultBossChance;
		private final float defaultEnchMultiplier;
		private final float defaultDropChance;
		private final String[] defaultBossNames;
		
		private int bossChance;
		private boolean bossWarning;
		private int bossWarningDist;

		private boolean bossWarningSound;
		private String[] bossNames;
		
		public BossApplier(String name, int defaultBossChance, float defaultEnchMultiplier, float defaultDropChance, String[] defaultBossNames) {
			
			this.name = name;
			this.defaultBossChance = defaultBossChance;
			this.defaultEnchMultiplier = defaultEnchMultiplier;
			this.defaultDropChance = defaultDropChance;
			this.defaultBossNames = defaultBossNames;

			equipApplier = new EquipmentApplier(name + " boss", 1, 1, 1, this.defaultEnchMultiplier, this.defaultDropChance);
		}	
		
		public void initConfig() {

			equipApplier.initConfig(Constants.DEFAULT_BOSS_MAINHAND, 
									Constants.DEFAULT_BOSS_OFFHAND, 
									Constants.DEFAULT_BOSS_HELMETS, 
									Constants.DEFAULT_BOSS_CHESTPLATES, 
									Constants.DEFAULT_BOSS_LEGGINGS, 
									Constants.DEFAULT_BOSS_BOOTS, 
									Constants.DEFAULT_WEAPON_ENCHANTS, 
									Constants.DEFAULT_ARMOR_ENCHANTS, 
									true);
			
			bossWarning = RoughConfig.getBoolean(name, "BossWarning", true, "Enable this to have a chat message warning of a boss spawn.");
			bossWarningDist = RoughConfig.getInteger(name, "BossWarningDistance", 50, 0, Short.MAX_VALUE, "Max bos spawn distance from a player that will trigger a warning.\nUsed for both chat messages and sounds.");
			bossWarningSound = RoughConfig.getBoolean(name, "BossWarningSound", true, "Play a warning sound when a boss spawns.");
			bossChance = RoughConfig.getInteger(name, "BossChance", defaultBossChance, 0, Short.MAX_VALUE, "Chance (1 in X) for a newly spawned " + name + " to become a boss " + name);
			bossNames = RoughConfig.getStringArray(name, "BossNames", defaultBossNames, name + " boss names. Please be more creative than I am... :P");
		}

		public void postInit() {
			equipApplier.createPools();
		}
		
		public EntityLiving trySetBoss(EntityLiving entity) {
			
			if (bossChance <= 0 || RND.nextInt(bossChance) != 0 || (entity instanceof EntityZombie && ((EntityZombie)entity).isChild()))
				return null;
			
			// Despawn normal skeletons and zombies and replace with BossSkeleton or BossZombie respectively
			IBoss boss = null;
			
			String entityTypeName = entity.getName();
			
			switch (entityTypeName) {
				case "Zombie":
					boss = new BossZombie(entity.world);
					((BossZombie) boss).setPosition(entity.posX, entity.posY, entity.posZ);
					entity.world.spawnEntity((BossZombie) boss);
					entity.setDead();
					break;
				case "Skeleton":
					boss = new BossSkeleton(entity.world);
					((BossSkeleton) boss).setPosition(entity.posX, entity.posY, entity.posZ);
					entity.world.spawnEntity((BossSkeleton) boss);
					entity.setDead();
					break;					
			}
			
			if(boss != null) {
			
				// Add custom attributes
				AttributeHelper.applyAttributeModifier((EntityLiving)boss, SharedMonsterAttributes.MAX_HEALTH, name + "BossHealth", 0, ((EntityLiving)boss).getMaxHealth()*2);
				AttributeHelper.applyAttributeModifier((EntityLiving)boss, SharedMonsterAttributes.KNOCKBACK_RESISTANCE, name + "BossKnock", 1, 1);
				
				// Add equipment
				boolean isBoss = true;
				equipApplier.equipEntity((EntityLiving)boss, isBoss);
				
				// Set Bosses name
				String bossName = bossNames[RND.nextInt(bossNames.length)];
				((EntityLiving)boss).setCustomNameTag(bossName);
				
				// Play special effects on spawn
				if (((EntityLiving)boss).getEntityWorld().canBlockSeeSky(((EntityLiving)boss).getPosition())) {
					EffectsHandler.lightningStrikeOn((EntityLiving)boss);
				}
				
				// Add chat message warning of new boss
				// TODO - move to network packets?
				if (bossWarning) {
					TextComponentString bossWarningMsg = new TextComponentString(bossName + ", a powerful " + entityTypeName + " warlord, has joined the battlefield.");
					bossWarningMsg.getStyle().setColor(TextFormatting.RED);
					bossWarningMsg.getStyle().setBold(true);
					
					// TODO Change to all players within boosWarningDist
					EntityPlayer closestPlayer = ((EntityLiving)boss).world.getClosestPlayerToEntity((EntityLiving)boss, bossWarningDist);
					if (closestPlayer != null) {
						closestPlayer.sendMessage(bossWarningMsg);
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
		
		public abstract void addBossFeatures(EntityLiving boss);
	}

	public static boolean isBoss(Entity entity) {
		return entity.getEntityData() != null && entity.getEntityData().getBoolean(BOSS);
	}
}
