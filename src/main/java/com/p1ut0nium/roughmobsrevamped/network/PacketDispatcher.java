package com.p1ut0nium.roughmobsrevamped.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * This class is used to instantiate a SimpleNetworkWrapper object for handling network packets
 */

public class PacketDispatcher {
	public static SimpleNetworkWrapper DISPATCHER;
	
	private static int ID = 0;
	private static int nextID() { return ID++; }
	
	public static void registerPackets(String channelName) {
		DISPATCHER = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
		DISPATCHER.registerMessage(PacketParticles.Handler.class, PacketParticles.class, nextID(), Side.CLIENT);
	}
	
	public static void sendTo(IMessage message, EntityPlayerMP player) {
		DISPATCHER.sendTo(message, player);
	}

	public static void sendToAll(IMessage message) {
		DISPATCHER.sendToAll(message);
	}

	public static void sendToAllAround(IMessage message, TargetPoint targetPoint) {
		System.out.println("DISPATCHER sendToAllAround");
		DISPATCHER.sendToAllAround(message, targetPoint);
	}

	public static void sendToDimension(IMessage message, int dim) {
		DISPATCHER.sendToDimension(message, dim);
	}

	@SideOnly(Side.CLIENT)
	public static void sendToServer(IMessage message) {
		DISPATCHER.sendToServer(message);
	}
}
