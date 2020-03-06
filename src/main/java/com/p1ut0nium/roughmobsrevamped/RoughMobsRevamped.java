/*
 * Rough Mobs Revamped for Minecraft Forge 1.14.4
 * 
 * This is a complete revamp of Lellson's Rough Mobs 2
 * 
 * Author: p1ut0nium_94
 * Website: https://www.curseforge.com/minecraft/mc-mods/rough-mobs-revamped
 * 
 */
package com.p1ut0nium.roughmobsrevamped;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.p1ut0nium.roughmobsrevamped.client.ClientModEventSubscriber;
import com.p1ut0nium.roughmobsrevamped.common.CommonModEventSubscriber;
import com.p1ut0nium.roughmobsrevamped.config.ConfigHolder;
import com.p1ut0nium.roughmobsrevamped.init.ModEntityTypes;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;
import com.p1ut0nium.roughmobsrevamped.server.ServerForgeEventSubscriber;
import com.p1ut0nium.roughmobsrevamped.server.ServerModEventSubscriber;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MODID)
public final class RoughMobsRevamped {
	
	public static final Logger LOGGER = LogManager.getLogger(Constants.MODID);
	
	// This keeps us from running code on the wrong side
	//public static final IProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
	
	public RoughMobsRevamped() {
		
		LOGGER.debug("Rough Mobs Revamped starting up...");
		
		final ModLoadingContext modLoadingContext = ModLoadingContext.get();
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		
        // Register for server and other game events on the Forge Event Bus
        MinecraftForge.EVENT_BUS.register(new ServerForgeEventSubscriber());
		
        // Register the setup methods for mod loading
        modEventBus.addListener(CommonModEventSubscriber::onCommonSetup);
        modEventBus.addListener(ClientModEventSubscriber::onClientSetup);
        modEventBus.addListener(ServerModEventSubscriber::onServerSetup);

		// Register stuff...
		ModEntityTypes.ENTITY_TYPES.register(modEventBus);
		// Register sounds?
		
		// Register Configs
		modLoadingContext.registerConfig(ModConfig.Type.CLIENT, ConfigHolder.CLIENT_SPEC);
		modLoadingContext.registerConfig(ModConfig.Type.SERVER, ConfigHolder.SERVER_SPEC);
	}
    
    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class FutureExpansion {
    }
}
