package com.p1ut0nium.roughmobsrevamped.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerHandlerDummy implements IMessageHandler<PacketParticles, IMessage>{

	@Override
	public IMessage onMessage(PacketParticles message, MessageContext ctx) {
		return null;
	}

}
