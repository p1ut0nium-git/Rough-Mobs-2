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

import java.util.List;

import com.p1ut0nium.roughmobsrevamped.misc.BossHelper.BossApplier;
import com.p1ut0nium.roughmobsrevamped.misc.EquipHelper.EquipmentApplier;
import com.p1ut0nium.roughmobsrevamped.misc.FeatureHelper;
import com.p1ut0nium.roughmobsrevamped.misc.MountHelper;
import com.p1ut0nium.roughmobsrevamped.misc.MountHelper.HorseType;
import com.p1ut0nium.roughmobsrevamped.misc.SpawnHelper;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class ZombieFeatures extends EntityFeatures {
	
	private static final String BOSS_MINION = Constants.unique("bossMinion");
	private CompoundNBT nbt = new CompoundNBT();
	
	private float leapHeight; 
	private int leapChance;
	
	private int hungerDuration;
	private int hungerChance;
	
	private int horseChance;
	private int horseMinY;
	
	private boolean babyBurn;
	private boolean helmetBurn;
	
	private EquipmentApplier equipApplier;
	private BossApplier bossApplier;

	private String[] breakBlocks;
	private List<Block> allowedBreakBlocks;
	
	@SuppressWarnings("unchecked")
	public ZombieFeatures() {
		super("zombie", ZombieEntity.class, ZombieVillagerEntity.class, HuskEntity.class, ZombiePigmanEntity.class);
	}
	
	@Override
	public void preInit() {
		nbt.putBoolean(BOSS_MINION, true);
		equipApplier = new EquipmentApplier(name, 3, 4, 8, 0.5f, 0.085F);
		bossApplier = new BossApplier(name, 200, 1F, 0.2F, new String[]{"Zombie King", "Flesh King", "Dr. Zomboss", "Azog", "Zon-Goku", "Amy", "Z0mb3y"}) {
			
			@Override
			public void addBossFeatures(LivingEntity entity) {
				if (SpawnHelper.disableBabyZombies() == false) {
					for (int i = 0; i < 4; i++) 
					{
						ZombieEntity zombieMinion = new ZombieEntity(entity.getEntityWorld());
						zombieMinion.setPosition(entity.posX + Math.random() - Math.random(), entity.posY + Math.random(), entity.posZ + Math.random() - Math.random());
						zombieMinion.onInitialSpawn(zombieMinion.world, entity.getEntityWorld().getDifficultyForLocation(entity.getPosition()), SpawnReason.MOB_SUMMONED, null, nbt);
						zombieMinion.setChild(true);
						
						// TODO Not needed - move this to code samples
						CompoundNBT nbtMinion = zombieMinion.getPersistentData();
						System.out.println("Zombie is a boss minion? " + nbtMinion.getBoolean(BOSS_MINION));

						entity.world.addEntity(zombieMinion);
					}
				}
			}
		};
	}
	
	@Override
	public void postInit() {
		equipApplier.createPools();
		bossApplier.postInit();
		allowedBreakBlocks = FeatureHelper.getBlocksFromNames(breakBlocks);
	}
	
	@Override
	public void initConfig() {
		super.initConfig();
		
		/* TODO Config
		leapChance = RoughConfig.getInteger(name, "LeapChance", 5, 0, MAX, "Chance (1 in X) for a %s to leap to the target\nSet to 0 to disable this feature");
		leapHeight = RoughConfig.getFloat(name, "LeapHeight", 0.2F, 0, MAX, "Amount of blocks the %s jumps on leap attack");
		
		hungerDuration = RoughConfig.getInteger(name, "HungerDuration", 200, 1, MAX, "Duration in ticks of the applied hunger effect (20 ticks = 1 second)");
		hungerChance = RoughConfig.getInteger(name, "HungerChance", 1, 0, MAX, "Chance (1 in X) for a %s to apply the hunger effect on attack\nSet to 0 to disable this feature");
		
		horseChance = RoughConfig.getInteger(name, "HorseChance", 10, 0, MAX, "Chance (1 in X) that a %s spawns riding a %s horse\nSet to 0 to disable this feature");
		horseMinY = RoughConfig.getInteger(name, "HorseMinY", 63, 0, MAX, "Minimal Y position above %s horses may spawn");
		
		babyBurn = RoughConfig.getBoolean(name, "BabyBurn", true, "Set this to false to prevent baby %ss from burning in sunlight");
		helmetBurn = RoughConfig.getBoolean(name, "HelmetBurn", false, "Set this to true to make all %ss burn in sunlight even if they wear a helmet");
		
		breakBlocks = RoughConfig.getStringArray(name, "BreakBlocks", Constants.DEFAULT_DESTROY_BLOCKS, "Blocks which can be destroyed by %ss if they have no attack target\nDelete all lines to disable this feature");
		*/

		equipApplier.initConfig(
			Constants.DEFAULT_MAINHAND,
			Constants.DEFAULT_OFFHAND,
			Constants.DEFAULT_HELMETS,
			Constants.DEFAULT_CHESTPLATES,
			Constants.DEFAULT_LEGGINGS,
			Constants.DEFAULT_BOOTS,
			Constants.DEFAULT_WEAPON_ENCHANTS,
			Constants.DEFAULT_ARMOR_ENCHANTS,
			false
		);

		bossApplier.initConfig();
	}
	
	/* TODO Add AI
	@Override
	public void addAI(EntityJoinWorldEvent event, Entity entity, EntityAITasks tasks, EntityAITasks targetTasks) {
		
		if (!(entity instanceof LivingEntity))
			return;
		
		if (leapChance > 0)
			tasks.addTask(1, new RoughAILeapAtTargetChanced((LivingEntity) entity, leapHeight, leapChance));
		
		if (babyBurn && entity instanceof ZombieEntity && ((ZombieEntity)entity).isChild() && !entity.isImmuneToFire())
			tasks.addTask(0, new RoughAISunlightBurn((LivingEntity) entity, false));
		
		if (helmetBurn)
			tasks.addTask(0, new RoughAISunlightBurn((LivingEntity) entity, true));
		
		if (allowedBreakBlocks.size() > 0)
			tasks.addTask(1, new RoughAIBreakBlocks((LivingEntity) entity, 8, allowedBreakBlocks));
	}
	*/
	
	@Override
	public void addFeatures(EntityJoinWorldEvent event, Entity entity) {
		
		if (!(entity instanceof LivingEntity) || (entity.getPersistentData()).getBoolean(BOSS_MINION))
			return;

		if (super.bossesEnabled(entity)) {
			Entity boss = bossApplier.trySetBoss((LivingEntity) entity);
			if (boss != null) {
				entity = boss;
				event.setCanceled(true);
			}
			else
				equipApplier.equipEntity((LivingEntity) entity);
		}

		// Attempt to spawn zombie on a horse
		MountHelper.tryMountHorse(entity, HorseType.ZOMBIE, horseChance, horseMinY);
	}
	
	@Override
	public void onAttack(Entity attacker, Entity immediateAttacker, Entity target, LivingAttackEvent event) {
		
		if (target instanceof LivingEntity && hungerChance > 0)
			FeatureHelper.addEffect((LivingEntity)target, Effects.HUNGER, hungerDuration, 0, hungerChance, true, 4);
	}
}
