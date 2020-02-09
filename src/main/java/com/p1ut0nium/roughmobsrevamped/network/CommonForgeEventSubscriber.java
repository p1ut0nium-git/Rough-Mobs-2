package com.p1ut0nium.roughmobsrevamped.network;

import com.p1ut0nium.roughmobsrevamped.RoughApplier;
import com.p1ut0nium.roughmobsrevamped.misc.Constants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Subscribe to events from the FORGE EventBus that should be handled on the PHYSICAL SERVER side in this class
 *
 * @author p1ut0nium_94
 */
@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.FORGE, value = Dist.DEDICATED_SERVER)
public final class CommonForgeEventSubscriber {
	@SubscribeEvent
	public static void onFMLCommonSetupEvent(final FMLCommonSetupEvent event ) {

		System.out.println("Server Event Bus: COMMON FORGE SETUP");	
	}
}
