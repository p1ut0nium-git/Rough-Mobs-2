package com.p1ut0nium.roughmobsrevamped.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

/**
 * Subscribe to events from the MOD EventBus that should be handled on the PHYSICAL SERVER side in this class
 *
 * @author p1ut0nium_94
 */
@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.MOD, value=Dist.DEDICATED_SERVER)
public final class ServerModEventSubscriber {
	
	private static final Logger LOGGER = LogManager.getLogger(Constants.MODID + " Server Mod Event Subscriber");

	@SubscribeEvent
	public static void onServerSetup(final FMLDedicatedServerSetupEvent event) {
		LOGGER.info(Constants.MODID + ": #3 Dedicated Server Setup");
	}	

}
