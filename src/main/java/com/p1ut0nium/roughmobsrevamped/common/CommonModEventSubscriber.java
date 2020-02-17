package com.p1ut0nium.roughmobsrevamped.common;

import com.p1ut0nium.roughmobsrevamped.RoughApplier;
import com.p1ut0nium.roughmobsrevamped.compat.CompatHandler;
import com.p1ut0nium.roughmobsrevamped.config.ConfigHelper;
import com.p1ut0nium.roughmobsrevamped.config.ConfigHolder;
import com.p1ut0nium.roughmobsrevamped.misc.Constants;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Subscribe to events from the MOD EventBus that should be handled on both PHYSICAL sides in this class
 *
 * @author p1ut0nium_94
 */

@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class CommonModEventSubscriber {
	
	private static RoughApplier applier;

	private static final Logger LOGGER = LogManager.getLogger(Constants.MODID + " Mod Event Subscriber");

	// Pre-Initialization
	@SubscribeEvent
	public static void onCommonSetup(final FMLCommonSetupEvent event) {
		LOGGER.info(Constants.MODID + ": #2 COMMON - Mod Setup Event");

		// Register Config
		
		// TODO Config initialization
		
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
}
