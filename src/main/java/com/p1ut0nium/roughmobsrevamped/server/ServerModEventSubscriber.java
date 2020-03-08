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
 */
@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.MOD, value=Dist.DEDICATED_SERVER)
public final class ServerModEventSubscriber {
	
	private static final Logger LOGGER = LogManager.getLogger(Constants.MODID + ": Server ModEventSubscriber");

	@SubscribeEvent
	public static void onServerSetup(final FMLDedicatedServerSetupEvent event) {
		LOGGER.info(Constants.MODID + ": SERVER Setup");
	}	

}
