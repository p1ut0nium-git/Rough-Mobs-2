package com.p1ut0nium.roughmobsrevamped.proxy;

import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/*
 * The Common Proxy is needed for events that occur on both the client and the server.
 */

@EventBusSubscriber
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {}
	
	public void init(FMLInitializationEvent event) {}
	
	public void postInit(FMLPostInitializationEvent event) {}

	public void serverStarting(FMLServerStartingEvent event) {}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {}

	public ListenableFuture<Object> addScheduledTaskClient(Runnable runnableToSchedule) {
		throw new IllegalStateException("This should only be called from client side");
	}
	
	public EntityPlayer getPlayerEntityFromContext(MessageContext ctx) {
		throw new IllegalStateException("This should only be called from client side");
	}

}
