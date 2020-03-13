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

import com.p1ut0nium.roughmobsrevamped.config.ConfigHelper;
import com.p1ut0nium.roughmobsrevamped.config.ConfigHolder;
import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.init.ModSounds;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;
import com.p1ut0nium.roughmobsrevamped.util.DamageSourceFog;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Subscribe to events from the MOD EventBus that should be handled on both PHYSICAL sides
 */
@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class CommonModEvents {

	// Pre-Initialization
	@SubscribeEvent
	public static void onCommonSetup(final FMLCommonSetupEvent event) {
		RoughMobsRevamped.LOGGER.debug(Constants.MODID + ": Common ModSetupEvent");
	}
	
	@SubscribeEvent
	public void onPlayerLogIn(PlayerLoggedInEvent event) {
		RoughMobsRevamped.LOGGER.debug(Constants.MODID + ": Player logged in.");
	}
	
	@SubscribeEvent
	public void onPlayerSleep(PlayerSleepInBedEvent event) {
		RoughMobsRevamped.LOGGER.debug(Constants.MODID + ": Player sleep.");
	}
	
	@SubscribeEvent
	public void onPlayerFalls(LivingFallEvent event) {
		RoughMobsRevamped.LOGGER.debug(Constants.MODID + ": Player Falls.");
	}
	
	@SubscribeEvent
	public void onEntityHurt(LivingAttackEvent event) {
		RoughMobsRevamped.LOGGER.debug(Constants.MODID + ": LivingAttackEVent");
		
		if (event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntity();
			if (!player.isCreative()) {
				if (event.getSource().equals(DamageSourceFog.POISONOUS_FOG)) {
					
					if (RoughConfig.bossFogPlayerCough)
						playHurtSound(player);
				
					player.setHealth(player.getHealth() - RoughConfig.bossFogDoTDamage);
					event.setCanceled(true);
				}
			}
		}
	}
	
	private void playHurtSound(PlayerEntity player) {
		player.world.playSound(null, player.getPosition(), ModSounds.ENTITY_PLAYER_COUGH, SoundCategory.PLAYERS, 1.0F, (float)Math.max(0.75, Math.random()));
	}
	
	@SubscribeEvent
	public static void onModConfigEvent(final ModConfig.ModConfigEvent event) {
		final ModConfig config = event.getConfig();
		// Rebake the configs when they change
		if (config.getSpec() == ConfigHolder.MODCOMPAT_SPEC) {
			ConfigHelper.bakeModCompat(config);
			RoughMobsRevamped.LOGGER.debug("Baked mod compatibility config");
		} else if (config.getSpec() == ConfigHolder.SPAWNCONDITIONS_SPEC) {
			ConfigHelper.bakeSpawnConditions(config);
			RoughMobsRevamped.LOGGER.debug("Baked spawn conditions config");
		} else if (config.getSpec() == ConfigHolder.EQUIPMENT_SPEC) {
			ConfigHelper.bakeEquipment(config);
			RoughMobsRevamped.LOGGER.debug("Baked equipment config");
		} else if (config.getSpec() == ConfigHolder.FEATURES_SPEC) {
			ConfigHelper.bakeFeatures(config);
			RoughMobsRevamped.LOGGER.debug("Baked features config");
		} else if (config.getSpec() == ConfigHolder.FOG_SPEC) {
			ConfigHelper.bakeFog(config);
			RoughMobsRevamped.LOGGER.debug("Baked fog config");
		}
	}
}
