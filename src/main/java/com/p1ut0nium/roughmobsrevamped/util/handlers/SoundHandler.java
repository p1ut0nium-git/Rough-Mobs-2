package com.p1ut0nium.roughmobsrevamped.util.handlers;

import com.p1ut0nium.roughmobsrevamped.misc.Constants;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundHandler {

	public static SoundEvent ENTITY_BOSS_SPAWN;
	
	public static void registerSounds() {
		ENTITY_BOSS_SPAWN = registerSound("entity.boss.boss_spawn");
	}
	
	private static SoundEvent registerSound(String name) {
		ResourceLocation location = new ResourceLocation(Constants.MODID, name);
		SoundEvent event = new SoundEvent(location);
		event.setRegistryName(name);
		ForgeRegistries.SOUND_EVENTS.register(event);
		return event;
	}
}
