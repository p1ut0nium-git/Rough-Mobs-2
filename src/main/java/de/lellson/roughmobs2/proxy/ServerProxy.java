package de.lellson.roughmobs2.proxy;

import de.lellson.roughmobs2.RoughApplier;
import de.lellson.roughmobs2.compat.GameStages;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/*
 * Server side only event handling
 */

public class ServerProxy extends CommonProxy {
	
	private RoughApplier applier;
	private GameStages gameStage;
	
	public void preInit(FMLPreInitializationEvent event) {
		
		super.preInit(event);
		
		gameStage = new GameStages();
		gameStage.preInit();
		
		applier = new RoughApplier();
		applier.preInit();
	}
	
	public void init(FMLInitializationEvent e) {
	}
	
	public void postInit(FMLPostInitializationEvent event) {

		gameStage.postInit();
		applier.postInit();
	}
}
