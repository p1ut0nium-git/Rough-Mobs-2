package com.p1ut0nium.roughmobsrevamped.init;

import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

public class ModSounds {
	private static void initialize() {
	}
	
	@Mod.EventBusSubscriber(modid = Constants.MODID)
	public static class RegistrationHandler {
		
		@SubscribeEvent
		public static void onEvent(final RegistryEvent.Register<SoundEvent> event) {
			final String[] arraySoundEvents = {
					
				// bosses
				"entity.boss.boss_spawn",
				"entity.boss.boss_idle",
				"entity.boss.boss_death",
				"entity.boss.bpss_batswarm",
				
				// player
				"entity.player.player_cough"
			};
			
			final IForgeRegistry<SoundEvent> registry = event.getRegistry();
			
			System.out.println("Registering sound events.");
			
			for (final String soundName : arraySoundEvents) {
				registry.register(new SoundEvent(new ResourceLocation(Constants.MODID, soundName)).setRegistryName(soundName));
			}
			
			initialize();
		}
		
	}
}
