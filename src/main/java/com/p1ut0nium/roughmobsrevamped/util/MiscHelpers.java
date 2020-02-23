package com.p1ut0nium.roughmobsrevamped.util;

import com.p1ut0nium.roughmobsrevamped.misc.SpawnHelper;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class MiscHelpers {
	
	/*
	 * This function is used to determine if a piece of equipment or an enchantment should be given to a spawned mob.
	 */
	public static boolean getChance(Entity entity, int chance, boolean chanceTimeMultiplier, boolean chanceDistanceMultiplier, int distThreshold) {
		
		if (chance <= 0) {
			return false;
		}
		
		float distanceChanceIncrease = 0;
		float timeChanceIncrease = 0;
		
		// Increase chance the closer it is to midnight.
		if (chanceTimeMultiplier) {
			long currentTime = entity.getEntityWorld().getWorldTime();
			
			// Ensure the current time is in the range of 0 to 24000
			currentTime = currentTime % 24000;
			
			// Convert time in ticks to hours
			byte currentHour = (byte) Math.floor(currentTime / 1000);

			// Add additional 16% bonus every hour from 8 PM to midnight
			if (currentHour >= 13 && currentHour <= 18)
				timeChanceIncrease = (float)((currentHour - 12) * 0.16);
			// Remove 25% bonus every hour from 1 AM to 6 AM
			else if (currentHour > 18 && currentHour <= 22)
				timeChanceIncrease = (float)(Math.abs(currentHour - 23) * 0.25);
		}
	
		// Increase chance the farther from world spawn the mob is.
		if (chanceDistanceMultiplier) {
			World world = entity.getEntityWorld();
			double distanceToSpawn = entity.getDistance(world.getSpawnPoint().getX(), world.getSpawnPoint().getY(), world.getSpawnPoint().getZ());

			if (distanceToSpawn >= SpawnHelper.getMinDistFromSpawn()) {
				distanceToSpawn = distanceToSpawn - SpawnHelper.getMinDistFromSpawn();
				distanceChanceIncrease = Math.min(1, (float)distanceToSpawn / distThreshold);
			}
		}
		
		// Add time and distance bonuses to chance
		float adjustedChance = Math.min(1, (float)1/(float)chance + Math.min(1, (((float)1/(float)chance * distanceChanceIncrease) + ((float)1/(float)chance * timeChanceIncrease))));
		
		return Math.random() <= adjustedChance;
	}
}
