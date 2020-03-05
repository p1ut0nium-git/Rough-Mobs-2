package com.p1ut0nium.roughmobsrevamped.proxy;

import com.google.common.util.concurrent.ListenableFuture;
import com.p1ut0nium.roughmobsrevamped.entities.BossZombie;
import com.p1ut0nium.roughmobsrevamped.misc.BossHelper;
import com.p1ut0nium.roughmobsrevamped.util.DamageSourceFog;
import com.p1ut0nium.roughmobsrevamped.util.handlers.SoundHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/*
 * Server side only event handling
 */

public class ServerProxy implements IProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
	}
	
    @Override
    public EntityPlayer getPlayerEntityFromContext(MessageContext ctx) {
        return ctx.getServerHandler().player;
    }
	
	public void serverStarting(FMLServerStartingEvent event) {
		//event.registerServerCommand(new CommandStructureCapture());
	}

	@Override
	public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule, MessageContext ctx) {
		return ctx.getServerHandler().player.getServer().addScheduledTask(runnableToSchedule);
	}
}
