/*
 * Rough Mobs Revamped for Minecraft Forge 1.14.4
 * 
 * This is a complete revamp of Lellson's Rough Mobs 2
 * 
 * Author: p1ut0nium_94
 * Website: https://www.curseforge.com/minecraft/mc-mods/rough-mobs-revamped
 * Source: https://github.com/p1ut0nium-git/Rough-Mobs-Revamped/tree/1.14.4
 * 
 */
package com.p1ut0nium.roughmobsrevamped.client;

import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * Subscribe to events from the FORGE EventBus that should be handled on the PHYSICAL CLIENT side in this class
 *
 * @author p1ut0nium_94
 */

@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class ClientForgeEventSubscriber {
}
