package com.p1ut0nium.roughmobsrevamped.proxy;

import com.google.common.util.concurrent.ListenableFuture;
import com.p1ut0nium.roughmobsrevamped.client.model.render.RenderHostileBat;
import com.p1ut0nium.roughmobsrevamped.entities.EntityHostileBat;
import com.p1ut0nium.roughmobsrevamped.util.Constants;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
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
        
        // Minecraft mc = Minecraft.getMinecraft();

        RenderingRegistry.registerEntityRenderingHandler(EntityHostileBat.class, new IRenderFactory<EntityHostileBat>() {
            @Override
            public Render<? super EntityHostileBat> createRenderFor(RenderManager manager)
            {
                return new RenderHostileBat(manager);
            }
        });
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
        
        // Minecraft mc = Minecraft.getMinecraft();
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
        
        // Minecraft mc = Minecraft.getMinecraft();
	}
	
	@Override
	public EntityPlayer getPlayerEntityFromContext(MessageContext ctx) {

        return (ctx.side.isClient() ? Minecraft.getMinecraft().player : ctx.getServerHandler().player);
	}
	
	@Override
	public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule, MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft().addScheduledTask(runnableToSchedule) : null);
	}
}
