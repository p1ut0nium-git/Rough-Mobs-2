package com.p1ut0nium.roughmobsrevamped.network;

import com.p1ut0nium.roughmobsrevamped.RoughApplier;
import com.p1ut0nium.roughmobsrevamped.misc.Constants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Subscribe to events from the MOD EventBus that should be handled on both sides in this class
 *
 * @author p1ut0nium_94
 */
@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class ClientModEventSubscriber {

	private static final Logger LOGGER = LogManager.getLogger(Constants.MODID + " Server Mod Event Subscriber");

	/**
	 * This method will be called by Forge when it is time for the mod to do its server-side setup
	 * This method will always be called after the Registry events.
	 * This means that all Blocks, Items, TileEntityTypes, etc. will all have been registered already
	 */
	
	@SubscribeEvent
	public static void onFMLClientSetupEvent(final FMLClientSetupEvent event ) {
		// #2
		System.out.println("Server Event Bus: CLIENT MOD SETUP");	
	}

}
