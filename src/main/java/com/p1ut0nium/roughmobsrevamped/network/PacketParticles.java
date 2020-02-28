package com.p1ut0nium.roughmobsrevamped.network;

import com.p1ut0nium.roughmobsrevamped.RoughMobs;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/*
 * This class is used to store a position for particles to be spawned on the client, which can be sent over the network. 
 * To send a message, use: ParticlePacket message = new ParticlePacket(x, y, z);
 */

public class PacketParticles implements IMessage {
	
	private boolean isMessageValid;
	private int particleType;
	private double x, y, z;
	private double red, green, blue;
	
	// Default constructor must always be included
	public PacketParticles() {
		this.isMessageValid = false;
	}
	
	// Overriden constructor for sending integers
	public PacketParticles(int particleType, double x, double y, double z, double red, double green, double blue) {
		
		this.particleType = particleType;
		this.x = x;
		this.y = y;
		this.z = z;
		this.red = red;
		this.green = green;
		this.blue = blue;
		
		this.isMessageValid = true;
	}

	// Reads the data back from the buffer. If you have multiple values, you must read in the same order you wrote.
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			//this.entityID = buf.readInt();
			this.particleType = buf.readInt();
			this.x = buf.readDouble();
			this.y = buf.readDouble();
			this.z = buf.readDouble();
			this.red = buf.readDouble();
			this.green = buf.readDouble();
			this.blue = buf.readDouble();
		} catch(IndexOutOfBoundsException ioe) {
			return;
		}
	}

	// Writes the data into the buffer
	@Override
	public void toBytes(ByteBuf buf) {
		if(!this.isMessageValid)
			return;
		
		//buf.writeInt(entity.getEntityId());
		buf.writeInt(particleType);
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeDouble(red);
		buf.writeDouble(green);
		buf.writeDouble(blue);
	}
	
	/*
	 * This class handles the PacketParticle message
	 * 
	 * IMessgeHandler params are Request, Reply 
	 * PacketParticles (Request) = packet you are receiving
	 * IMessage (Reply) = packet you are returning 
	 * Reply can be used as a response from a sent packet
	 */

	public static class Handler implements IMessageHandler<PacketParticles, IMessage> {

		@Override
		public IMessage onMessage(PacketParticles message, MessageContext ctx) {

			if (!message.isMessageValid && ctx.side != Side.CLIENT) {
				return null;
			}
			
			RoughMobs.proxy.addScheduledTask(() -> processMessage(message, ctx), ctx);
			return null;
		}

		void processMessage(PacketParticles message, MessageContext ctx) {
			EntityPlayer player = RoughMobs.proxy.getPlayerEntityFromContext(ctx);
			player.world.spawnParticle(EnumParticleTypes.getParticleFromId(message.particleType), message.x + 1.0D, message.y + 1.0D, message.z + 1.0D, message.red, message.blue, message.green);
		}
	}
}
