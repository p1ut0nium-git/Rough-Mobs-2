package com.p1ut0nium.roughmobsrevamped.init;

import com.p1ut0nium.roughmobsrevamped.RoughMobs;
import com.p1ut0nium.roughmobsrevamped.entities.BossSkeleton;
import com.p1ut0nium.roughmobsrevamped.entities.BossZombie;
import com.p1ut0nium.roughmobsrevamped.misc.Constants;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityInit {
	public static void registerEntities() {
		registerEntity("bosszombie", BossZombie.class, Constants.ENTITY_BOSSZOMBIE, 50, 65280, 33280);
		registerEntity("bossskeleton", BossSkeleton.class, Constants.ENTITY_BOSSSKELETON, 50, 65280, 33280);
	}
	
	private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range, int color1, int color2) {
		EntityRegistry.registerModEntity(new ResourceLocation(Constants.MODID + ":" + name), entity, name, id, RoughMobs.instance, range, 1, true, color1, color2);
	}
}
