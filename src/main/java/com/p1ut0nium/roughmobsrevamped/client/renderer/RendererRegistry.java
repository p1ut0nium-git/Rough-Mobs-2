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
package com.p1ut0nium.roughmobsrevamped.client.renderer;

import com.p1ut0nium.roughmobsrevamped.client.renderer.entity.HostileBatRenderer;
import com.p1ut0nium.roughmobsrevamped.client.renderer.entity.HostileBatRenderer.RenderFactory;
import com.p1ut0nium.roughmobsrevamped.entity.monster.HostileBatEntity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

@OnlyIn(Dist.CLIENT)
public class RendererRegistry {
	public static void registryEntityRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(HostileBatEntity.class, new HostileBatRenderer.RenderFactory());
	}
}
