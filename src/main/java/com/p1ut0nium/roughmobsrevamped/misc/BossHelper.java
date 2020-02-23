package com.p1ut0nium.roughmobsrevamped.misc;

import java.util.Random;

import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.misc.EquipHelper.EquipmentApplier;
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
			bossWarningDist = RoughConfig.getInteger(name, "BossWarningDistance", 50, 0, 1000, "Max distance from a player that a Boss mob spawn will trigger a warning message.");
			bossWarningSound = RoughConfig.getBoolean(name, "BossWarningSound", true, "Play a warning sound when a boss spawns.");
			bossChance = RoughConfig.getInteger(name, "BossChance", defaultBossChance, 0, Short.MAX_VALUE, "Chance (1 in X) for a newly spawned " + name + " to become a boss " + name);
			bossNames = RoughConfig.getStringArray(name, "BossNames", defaultBossNames, name + " boss names. Please be more creative than I am... :P");
		}

		public void postInit() {
			equipApplier.createPools();
		}
		
		public boolean trySetBoss(EntityLiving entity) {
			
			if (bossChance <= 0 || RND.nextInt(bossChance) != 0 || (entity instanceof EntityZombie && ((EntityZombie)entity).isChild()))
				return false;
			
			// Add custom attributes
			AttributeHelper.applyAttributeModifier(entity, SharedMonsterAttributes.MAX_HEALTH, name + "BossHealth", 0, entity.getMaxHealth()*2);
			AttributeHelper.applyAttributeModifier(entity, SharedMonsterAttributes.KNOCKBACK_RESISTANCE, name + "BossKnock", 1, 1);
			
			// Add equipment
			boolean isBoss = true;
			equipApplier.equipEntity(entity, isBoss);
			
			// Set Bosses name
			String entityType = entity.getName();
			String bossName = bossNames[RND.nextInt(bossNames.length)];
			entity.setCustomNameTag(bossName);
			
			// Add chat message warning of new boss
			if (bossWarning) {
				TextComponentString bossWarningMsg = new TextComponentString(bossName + ", a powerful " + entityType + " warlord, has joined the battlefield.");
				bossWarningMsg.getStyle().setColor(TextFormatting.RED);
				bossWarningMsg.getStyle().setBold(true);
				
				EntityPlayer closestPlayer = entity.world.getClosestPlayerToEntity(entity, bossWarningDist);
				if (closestPlayer != null) {
					closestPlayer.sendMessage(bossWarningMsg);
				}
			}
			
			// Play warning sound of boss spawn
			if (bossWarningSound) {
				entity.playSound(SoundHandler.ENTITY_BOSS_SPAWN, 10.0F, 0.5F);
			}
			
			// Add Boss features
			entity.getEntityData().setBoolean(BOSS, true);
			addBossFeatures(entity);
			
			return true;
		}
		
		public abstract void addBossFeatures(EntityLiving entity); {} // TODO
	}

	public static boolean isBoss(Entity entity) {
		return entity.getEntityData() != null && entity.getEntityData().getBoolean(BOSS);
	}
}
