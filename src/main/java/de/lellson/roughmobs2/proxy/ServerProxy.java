package de.lellson.roughmobs2.proxy;

import de.lellson.roughmobs2.RoughApplier;
import de.lellson.roughmobs2.gamestages.GameStages;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy {
	
	private RoughApplier applier;
	private GameStages gameStage;
	
	public void preInit(FMLPreInitializationEvent event) {
		
		applier = new RoughApplier();
		applier.preInit();
		
		gameStage = new GameStages();
		gameStage.preInit();
		
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		
		applier.postInit();
		gameStage.postInit();
	}
}
