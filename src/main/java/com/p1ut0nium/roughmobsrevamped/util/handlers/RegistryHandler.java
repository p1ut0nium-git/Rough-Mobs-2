package com.p1ut0nium.roughmobsrevamped.util.handlers;

import com.p1ut0nium.roughmobsrevamped.RoughMobs;
import com.p1ut0nium.roughmobsrevamped.compat.CompatHandler;
import com.p1ut0nium.roughmobsrevamped.init.EntityInit;
import com.p1ut0nium.roughmobsrevamped.misc.Constants;
import com.p1ut0nium.roughmobsrevamped.network.PacketDispatcher;

import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RegistryHandler {
	
	public static void preInitRegistries() {
		
		// Initialize 3rd party mod support
		CompatHandler.registerGameStages();
		CompatHandler.registerSereneSeasons();

		PacketDispatcher.registerPackets(Constants.MODID);
	
		SoundHandler.registerSounds();
		EntityInit.registerEntities();
	}
}
