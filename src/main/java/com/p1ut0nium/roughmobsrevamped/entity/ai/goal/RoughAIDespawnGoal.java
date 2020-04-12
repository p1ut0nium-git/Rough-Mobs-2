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
package com.p1ut0nium.roughmobsrevamped.entity.ai.goal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

public class RoughAIDespawnGoal extends Goal {

	protected LivingEntity entity;
	protected Entity player = null;
	
	public RoughAIDespawnGoal(LivingEntity entity) {
		this.entity = entity;
	}
	
	@Override
	public boolean shouldExecute() {
		
		PlayerEntity playerNew = entity.world.getClosestPlayer(entity, -1.0D);
		if (player == null || playerNew != null)
			player = playerNew;
		
		return player != null;
	}
	
	@Override
	public void tick() {
		
	    if (player != null) {
	        double d0 = player.getPosX() - entity.getPosX();
	        double d1 = player.getPosY() - entity.getPosY();
	        double d2 = player.getPosZ() - entity.getPosZ();
	        double d3 = d0 * d0 + d1 * d1 + d2 * d2;

	        if (d3 > 16384.0D) {
	        	entity.remove();
	        }

	        if (entity.getIdleTime() > 600 && entity.world.rand.nextInt(800) == 0 && d3 > 1024.0D) {
	        	entity.remove();
	        }
	        else if (d3 < 1024.0D) {
	        	entity.setIdleTime(0);
	        }
	    }
	}
}
