package com.p1ut0nium.roughmobsrevamped.client.effects;

import com.p1ut0nium.roughmobsrevamped.network.PacketDispatcher;
import com.p1ut0nium.roughmobsrevamped.network.PacketLightning;
import com.p1ut0nium.roughmobsrevamped.network.PacketParticles;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//@SideOnly(Side.CLIENT)
public class SpecialEffects {
	
	public static void lightningStrikeOn(Entity entity) {
		PacketLightning lightningPacket = new PacketLightning(entity.posX, entity.posY, entity.posZ);
		PacketDispatcher.DISPATCHER.sendToAll(lightningPacket);	
	}
	
	public static void particlesOn(Entity entity) {
		World world = entity.getEntityWorld();
		
		PacketParticles particlePacket = new PacketParticles(EnumParticleTypes.REDSTONE.getParticleID(), entity.posX, entity.posY, entity.posZ, 1.0D, 0.0D, 0.0D);
		NetworkRegistry.TargetPoint target = new TargetPoint(world.provider.getDimension(), entity.posX, entity.posY, entity.posZ, 60.0D);
		PacketDispatcher.DISPATCHER.sendToAllAround(particlePacket, target);
	}
}
