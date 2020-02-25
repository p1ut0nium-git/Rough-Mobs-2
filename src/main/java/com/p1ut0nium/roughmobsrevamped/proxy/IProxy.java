package com.p1ut0nium.roughmobsrevamped.proxy;

import com.google.common.util.concurrent.ListenableFuture;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public interface IProxy {

		void preInit(FMLPreInitializationEvent even);
		
		void init(FMLInitializationEvent event);
		
		void postInit(FMLPostInitializationEvent event);
		
		// EntityPlayer getPlayerEntityFromContext(MessageContext parContext);

		void serverStarting(FMLServerStartingEvent event);

		public ListenableFuture<Object> addScheduledTaskClient(Runnable runnableToSchedule);
		
}
