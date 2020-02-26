package com.p1ut0nium.roughmobsrevamped.network;

import com.p1ut0nium.roughmobsrevamped.RoughMobs;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketLightning implements IMessage {

	private double x, y, z;
	private boolean isMessageValid;
	
	// Default constructor must always be included
	public PacketLightning() {
		this.isMessageValid = false;
	}
	
	public PacketLightning(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.isMessageValid = true;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			this.x = buf.readDouble();
			this.y = buf.readDouble();
			this.z = buf.readDouble();
		} catch(IndexOutOfBoundsException ioe) {
			return;
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		if(!this.isMessageValid)
			return;
		
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
	}
	
	public static class Handler implements IMessageHandler<PacketLightning, IMessage> {

		@Override
		public IMessage onMessage(PacketLightning message, MessageContext ctx) {

			if (!message.isMessageValid && ctx.side != Side.CLIENT) {
				return null;
			}
			
			RoughMobs.proxy.addScheduledTask(() -> processMessage(message, ctx), ctx);
			return null;
		}

		void processMessage(PacketLightning message, MessageContext ctx) {
			EntityPlayer player = RoughMobs.proxy.getPlayerEntityFromContext(ctx);
			player.world.addWeatherEffect(new EntityLightningBolt(player.world, message.x, message.y, message.z, true));
			
			SoundEvent soundEvent = new SoundEvent(new ResourceLocation("entity.lightning.thunder"));
			player.world.playSound(message.x, message.y, message.z, soundEvent, SoundCategory.AMBIENT, 100.0F, 1.0F, true);

		}
	}

}
