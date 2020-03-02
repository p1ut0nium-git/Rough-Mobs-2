package com.p1ut0nium.roughmobsrevamped.util.handlers;

import com.p1ut0nium.roughmobsrevamped.compat.CompatHandler;
import com.p1ut0nium.roughmobsrevamped.init.EntityInit;

import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RegistryHandler {
	
	public static void preInitRegistries() {
		
		// Initialize 3rd party mod support
		CompatHandler.registerGameStages();
		CompatHandler.registerSereneSeasons();
	
		SoundHandler.registerSounds();
		EntityInit.registerEntities();
	}
}
