package com.p1ut0nium.roughmobsrevamped.proxy;

import com.google.common.util.concurrent.ListenableFuture;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/*
 * Server side only event handling
 */

public class ServerProxy implements IProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		System.out.println("SERVER: preInit()");
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		System.out.println("SERVER: init()");
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		System.out.println("SERVER: postInit()");
	}
	
    @Override
    public EntityPlayer getPlayerEntityFromContext(MessageContext ctx) {
		System.out.println("SERVER: getPlayer");
        return ctx.getServerHandler().player;
    }
	
	public void serverStarting(FMLServerStartingEvent event) {
		//event.registerServerCommand(new CommandStructureCapture());
	}

	@Override
	public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule, MessageContext ctx) {
		System.out.println("SERVER: addScheduledTask");
		return ctx.getServerHandler().player.getServer().addScheduledTask(runnableToSchedule);
	}
}
