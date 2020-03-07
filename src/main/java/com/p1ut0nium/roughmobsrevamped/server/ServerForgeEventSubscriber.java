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
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.FORGE, value=Dist.DEDICATED_SERVER)
public final class ServerForgeEventSubscriber {

	private static final Logger LOGGER = LogManager.getLogger(Constants.MODID + ": Dedicated Server ForgeEventSubscriber");
	
    // Server About to Start
    @SubscribeEvent
    public void onServerAboutToStart(final FMLServerAboutToStartEvent event) {
        LOGGER.info(Constants.MODID + ": SERVER About to Start");
    }
    
    // Server Starting
    @SubscribeEvent
    public void onServerStarting(final FMLServerStartingEvent event) {
        LOGGER.info(Constants.MODID + ": SERVER Starting");
    }
    
	// Server Started
	@SubscribeEvent
	public static void onServerStartedEvent(final FMLServerStartedEvent event) {
        LOGGER.info(Constants.MODID + ": SERVER Started");
	}
	
	// Tick Event
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
	}
	
	// World Loaded
	@SubscribeEvent
	public void onServerWorldLoad(final WorldEvent.Load event) {
		LOGGER.info(Constants.MODID + ": SERVER World Loaded -> " + event.getWorld().getDimension());

	}
	
	// Chunk Loaded
	@SubscribeEvent
	public void onServerChunkLoad(final ChunkEvent.Load event) {
	}
}
