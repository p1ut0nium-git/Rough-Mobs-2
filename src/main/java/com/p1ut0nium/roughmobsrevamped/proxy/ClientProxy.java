package com.p1ut0nium.roughmobsrevamped.proxy;

import com.google.common.util.concurrent.ListenableFuture;
import com.p1ut0nium.roughmobsrevamped.misc.Constants;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/*
 * Client side only event handling
 */

@EventBusSubscriber(value = Side.CLIENT, modid = Constants.MODID)
public class ClientProxy implements IProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
        System.out.println("preInit() on Client side");
        
        // Minecraft mc = Minecraft.getMinecraft();
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
        System.out.println("init() on Client side");
        
        // Minecraft mc = Minecraft.getMinecraft();
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
        System.out.println("postInit() on Client side");
        
        // Minecraft mc = Minecraft.getMinecraft();
	}
	
	@Override
	public EntityPlayer getPlayerEntityFromContext(MessageContext ctx) {
		System.out.println("CLIENT: getPlayer");

        return (ctx.side.isClient() ? Minecraft.getMinecraft().player : ctx.getServerHandler().player);
	}
	
	@Override
	public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule, MessageContext ctx) {
		System.out.println("CLIENT: addScheduledTask");
		return (ctx.side.isClient() ? Minecraft.getMinecraft().addScheduledTask(runnableToSchedule) : null);
	}
}
