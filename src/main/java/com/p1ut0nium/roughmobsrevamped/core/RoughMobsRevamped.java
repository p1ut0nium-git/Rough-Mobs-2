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
package com.p1ut0nium.roughmobsrevamped.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.p1ut0nium.roughmobsrevamped.client.ClientModEvents;
import com.p1ut0nium.roughmobsrevamped.compat.CompatHandler;
import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.init.ModEntityTypes;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MODID)
public final class RoughMobsRevamped {
	
	public static RoughMobsRevamped INSTANCE;
	public static final Logger LOGGER = LogManager.getLogger(Constants.MODID);
	public static RoughApplier applier;
	
	public RoughMobsRevamped() {
		
		LOGGER.debug("Starting up...");
		
		INSTANCE = this;

        // Register the setup methods for mod loading
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(CommonModEvents::onCommonSetup);
        modEventBus.addListener(ClientModEvents::onClientSetup);
        modEventBus.addListener(this::loadComplete);

        // Setup config
        LOGGER.debug("Initializing Config");
        RoughConfig.init();
        RoughConfig.register(ModLoadingContext.get());

		// Register entities
        LOGGER.debug("Registering entities...");
		ModEntityTypes.ENTITY_TYPES.register(modEventBus);
	}
    
    private void loadComplete(final FMLLoadCompleteEvent event) {
    	LOGGER.debug("Load Complete...Registering mod compatibility");
    	
		// Initialize 3rd party mod support
		CompatHandler.registerGameStages();
		CompatHandler.registerSereneSeasons();
    	
		LOGGER.debug("Initializing RoughApplier");
		
		// Begin adding features, etc.
		applier = new RoughApplier();
		applier.preInit();
		applier.postInit();

    }
}
