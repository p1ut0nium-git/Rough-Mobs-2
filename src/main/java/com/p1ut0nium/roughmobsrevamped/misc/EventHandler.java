package com.p1ut0nium.roughmobsrevamped.misc;

import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {
	@SubscribeEvent
	public void onPlayerLogIn(PlayerLoggedInEvent event) {
	}
	
	@SubscribeEvent
	public void onPlayerSleep(PlayerSleepInBedEvent event) {
	}
	
	@SubscribeEvent
	public void onPlayerFalls(LivingFallEvent event) {
	}
}
