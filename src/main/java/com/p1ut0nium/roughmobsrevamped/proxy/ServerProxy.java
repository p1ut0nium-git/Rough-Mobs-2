package com.p1ut0nium.roughmobsrevamped.proxy;

import com.p1ut0nium.roughmobsrevamped.RoughApplier;
import com.p1ut0nium.roughmobsrevamped.compat.CompatHandler;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/*
 * Server side only event handling
 */

public class ServerProxy extends CommonProxy {
	
	private RoughApplier applier;
	
	public void preInit(FMLPreInitializationEvent event) {
		
		super.preInit(event);
		
		// Initialize 3rd party mod support
		CompatHandler.registerGameStages();
		CompatHandler.registerSereneSeasons();
		
		applier = new RoughApplier();
		applier.preInit();
	}
	
	public void init(FMLInitializationEvent e) {
	}
	
	public void postInit(FMLPostInitializationEvent event) {

		applier.postInit();
	}
}
