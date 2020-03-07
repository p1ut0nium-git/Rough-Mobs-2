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
package com.p1ut0nium.roughmobsrevamped.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.p1ut0nium.roughmobsrevamped.RoughApplier;
import com.p1ut0nium.roughmobsrevamped.compat.CompatHandler;
import com.p1ut0nium.roughmobsrevamped.config.ConfigHelper;
import com.p1ut0nium.roughmobsrevamped.config.ConfigHolder;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

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
public final class CommonModEventSubscriber {
	
	private static RoughApplier applier;

	private static final Logger LOGGER = LogManager.getLogger(Constants.MODID + " Mod Event Subscriber");

	// Pre-Initialization
	@SubscribeEvent
	public static void onCommonSetup(final FMLCommonSetupEvent event) {
		LOGGER.info(Constants.MODID + " Common ModSetupEvent");

		// Register Config
		
		// Initialize 3rd party mod support
		CompatHandler.registerGameStages();
		CompatHandler.registerSereneSeasons();
		
		// Begin adding features, etc.
		applier = new RoughApplier();
		applier.preInit();
		applier.postInit();
	}


	/**
	 * This method will be called by Forge when a config changes.
	 */

	@SubscribeEvent
	public static void onModConfigEvent(final ModConfig.ModConfigEvent event) {
		
		LOGGER.info(Constants.MODID + ": #1 COMMON - ModConfig Event");
		
		final ModConfig config = event.getConfig();
		// Rebake the configs when they change
		if (config.getSpec() == ConfigHolder.CLIENT_SPEC) {
			ConfigHelper.bakeClient(config);
			LOGGER.debug("Baked client config");
		} else if (config.getSpec() == ConfigHolder.SERVER_SPEC) {
			ConfigHelper.bakeServer(config);
			LOGGER.debug("Baked server config");
		}
	}
	
	@SubscribeEvent
	public void onPlayerLogIn(PlayerLoggedInEvent event) {
		LOGGER.info(Constants.MODID + ": Player logged in.");
	}
	
	@SubscribeEvent
	public void onPlayerSleep(PlayerSleepInBedEvent event) {
		LOGGER.info(Constants.MODID + ": Player sleep.");
	}
	
	@SubscribeEvent
	public void onPlayerFalls(LivingFallEvent event) {
		LOGGER.info(Constants.MODID + ": Player Falls.");
	}
}
