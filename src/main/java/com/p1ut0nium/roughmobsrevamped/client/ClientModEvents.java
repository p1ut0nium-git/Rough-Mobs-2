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
package com.p1ut0nium.roughmobsrevamped.client;

import com.p1ut0nium.roughmobsrevamped.core.RoughMobsRevamped;
import com.p1ut0nium.roughmobsrevamped.init.ModEntityTypes;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Subscribe to events from the MOD EventBus that should be handled on the PHYSICAL CLIENT
 */
@Mod.EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
	
	@SubscribeEvent
	public static void onClientSetup(final FMLClientSetupEvent event) {;
		
		// Register Entity Renderers
		ModEntityTypes.registerRendering();

		RoughMobsRevamped.LOGGER.debug("Registered Entity Renderers");
	}

}
