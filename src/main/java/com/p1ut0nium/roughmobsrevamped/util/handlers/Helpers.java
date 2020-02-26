package com.p1ut0nium.roughmobsrevamped.util.handlers;

import java.util.List;

import com.p1ut0nium.roughmobsrevamped.entities.BossZombie;

public abstract class Helpers {
	
	/*
	 * Takes a number from a source range of numbers, then scales that number to fit into a new range of numbers
	 * 
	 * Example:
	 * 500 in the range of 100 to 1000 becomes 0.5 in the range of 0 to 1.0
	 */
	public static float scaleValue(int currentDistance, int oldRangMin, int oldRangeMax, float newRangeMin, double newRangeMax) {
		double oldPercent = (double) (currentDistance - oldRangMin) / (double) (oldRangeMax - oldRangMin);
		float newResult = (float)((newRangeMax - newRangeMin) * oldPercent) + newRangeMin;
		return newResult;
	}
	
	/*
	 * Returns true if a class is contained within a List
	 * 
	 * Example usage:
	 * List entities = world.getEntitiesWithinAABB(BossZombie.class, player.getEntityBoundingBox().grow(20)); 
	 * containsInstance(entities, BossZombie.class);
	 */
	public static <E> boolean containsInstance(List<E> list, Class<? extends E> clazz) {
		for (E element : list) {
			if (clazz.isInstance(element)) {
				return true;
			}
		}
		return false;
	}
}
