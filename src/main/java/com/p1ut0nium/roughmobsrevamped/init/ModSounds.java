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
package com.p1ut0nium.roughmobsrevamped.init;

import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds {
	
	public static SoundEvent ENTITY_BOSS_SPAWN;
	public static SoundEvent ENTITY_BOSS_IDLE;
	public static SoundEvent ENTITY_BOSS_DEATH;
	public static SoundEvent ENTITY_BOSS_BATSWARM;
	public static SoundEvent ENTITY_PLAYER_COUGH;
		
	public static void registerSounds() {
		ENTITY_BOSS_SPAWN = registerSound("entity.boss.boss_spawn");
		ENTITY_BOSS_IDLE = registerSound("entity.boss.boss_idle");
		ENTITY_BOSS_DEATH = registerSound("entity.boss.boss_death");
		ENTITY_BOSS_BATSWARM = registerSound("entity.boss.boss_batswarm");
		ENTITY_PLAYER_COUGH = registerSound("entity.player.player_cough");
	}
	
	private static SoundEvent registerSound(String name) {
		ResourceLocation location = new ResourceLocation(Constants.MODID, name);
		SoundEvent event = new SoundEvent(location);
		event.setRegistryName(name);
		ForgeRegistries.SOUND_EVENTS.register(event);
		return event;
	}
}
