package de.lellson.roughmobs2.proxy;

import de.lellson.roughmobs2.RoughApplier;
import de.lellson.roughmobs2.gamestages.GameStages;
import de.lellson.roughmobs2.misc.PlayerHelper;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy {
	
	private RoughApplier applier;
	private GameStages gameStage;
	
	public void preInit(FMLPreInitializationEvent event) {
		
		gameStage = new GameStages();
		gameStage.preInit();
		
		applier = new RoughApplier();
		applier.preInit();
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		
		applier.postInit();
		gameStage.postInit();
	}
}
