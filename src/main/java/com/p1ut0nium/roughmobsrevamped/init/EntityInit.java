package com.p1ut0nium.roughmobsrevamped.init;

import com.p1ut0nium.roughmobsrevamped.RoughMobs;
import com.p1ut0nium.roughmobsrevamped.entities.BossSkeleton;
import com.p1ut0nium.roughmobsrevamped.entities.BossZombie;
import com.p1ut0nium.roughmobsrevamped.entities.EntityHostileBat;
import com.p1ut0nium.roughmobsrevamped.util.Constants;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityInit {
	public static void registerEntities() {
		registerEntity("bosszombie", BossZombie.class, Constants.ENTITY_BOSSZOMBIE, 50);
		registerEntity("bossskeleton", BossSkeleton.class, Constants.ENTITY_BOSSSKELETON, 50);
		registerEntity("hostilebat", EntityHostileBat.class, Constants.ENTITY_HOSTILEBAT, 50);
	}
	
	private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range) {
		EntityRegistry.registerModEntity(new ResourceLocation(Constants.MODID + ":" + name), entity, name, id, RoughMobs.instance, range, 1, true);
	}
}
