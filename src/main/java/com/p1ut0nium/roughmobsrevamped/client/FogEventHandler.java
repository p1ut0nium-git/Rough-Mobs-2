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
package com.p1ut0nium.roughmobsrevamped.client;

import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.p1ut0nium.roughmobsrevamped.entity.boss.SkeletonChampionEntity;
import com.p1ut0nium.roughmobsrevamped.entity.boss.ZombieChampionEntity;
import com.p1ut0nium.roughmobsrevamped.misc.BossHelper;
import com.p1ut0nium.roughmobsrevamped.util.Utilities;

import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.extensions.IForgeEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class FogEventHandler {
	
	private static double FOG_RED = 0.7116436;
	private static double FOG_GREEN = 0.81895345;
	private static double FOG_BLUE = 0.99999976;
	
	private static float FOG_MAX_FARPLANE = 192;
	private static float FOG_MAX_FARPLANE_SCALE = 1.0F;
	
	private static int FOG_FADE_TIME = 60;
	private static int fog_colorfade_tick = 0;
	private static int fog_farplane_tick = 0;

    private static LivingEntity closestBoss;
    private static float closestBossDistance;

    private static int fogNearPlaneConfig = BossHelper.bossFogStartDistance;
    private static int fogMaxDistConfig = BossHelper.bossFogMaxDistance;
    private static int fogFarPlaneConfig = BossHelper.bossFogFarPlane;
    private static float fogFarPlaneScaleConfig = BossHelper.bossFogFarPlaneScale;
    private static float fogColorConfigRed = Float.parseFloat(BossHelper.bossFogColor[0]);
    private static float fogColorConfigGreen = Float.parseFloat(BossHelper.bossFogColor[1]);
    private static float fogColorConfigBlue = Float.parseFloat(BossHelper.bossFogColor[2]);
    
    private static float currentFogColorRed;
    private static float currentFogColorGreen;
    private static float currentFogColorBlue;
    private static float addToRed = 0.0F;
    private static float addToGreen = 0.0F;
    private static float addToBlue = 0.0F;
    
    private static int currentFarPlane;
    private static float currentFarPlaneScale;
    private static float addToFarPlane = 0.0F;
    private static float addToFarPlaneScale = 0.0F;

    private static boolean outOfRange = true;
    public static boolean bossDied;
    public static boolean playerRespawned;

    @SubscribeEvent
    public static void onGetFogColor(EntityViewRenderEvent.FogColors event) {
       	
    	// Set original fog color if no bosses are in range
   		FOG_RED = event.getRed();
   		FOG_GREEN = event.getGreen();
   		FOG_BLUE = event.getBlue();
   		
   		// If player just died and respawned, reset fog color to normal
   		if (((IForgeEntity) event).getEntity() instanceof PlayerEntity && playerRespawned) {
   			event.setRed((float)FOG_RED);
	        event.setGreen((float)FOG_GREEN);
	        event.setBlue((float)FOG_BLUE);
   		}

       	// If player, then check for bosses in range.
        if (((IForgeEntity) event).getEntity() instanceof PlayerEntity && BossHelper.bossFogEnabled) {

            LivingEntity boss = checkForBossInRange(event);
     
        	// If there are bosses, then set the fog color based on the distance
        	if (boss != null && !outOfRange && !bossDied) {

        		currentFogColorRed = (float) Utilities.scaleValue(Math.max(fogNearPlaneConfig, closestBossDistance), fogNearPlaneConfig, fogMaxDistConfig, fogColorConfigRed, FOG_RED);
        		currentFogColorGreen = (float) Utilities.scaleValue(Math.max(fogNearPlaneConfig, closestBossDistance), fogNearPlaneConfig, fogMaxDistConfig, fogColorConfigGreen, FOG_GREEN);
        		currentFogColorBlue = (float) Utilities.scaleValue(Math.max(fogNearPlaneConfig, closestBossDistance), fogNearPlaneConfig, fogMaxDistConfig, fogColorConfigBlue, FOG_BLUE);

        		event.setRed(currentFogColorRed);
		        event.setGreen(currentFogColorGreen);
		        event.setBlue(currentFogColorBlue);
	        }
        	else if(bossDied && boss == closestBoss) {
    			fadeOutFogColor(event);
    		}
        }
    }

    @SubscribeEvent
    public static void onRenderFog(EntityViewRenderEvent.RenderFogEvent event) {

    	// Set original fog far plane distance & scale
   		FOG_MAX_FARPLANE = event.getFarPlaneDistance();
   		
   		// If player just died and respawned, reset fog density to normal
   		if (((IForgeEntity) event).getEntity() instanceof PlayerEntity && playerRespawned) {
   			renderFog(event.getFogMode(), FOG_MAX_FARPLANE, FOG_MAX_FARPLANE_SCALE);
   			playerRespawned = false;
   		}

        if (((IForgeEntity) event).getEntity() instanceof PlayerEntity && BossHelper.bossFogEnabled) {

        	LivingEntity boss = checkForBossInRange(event);
            
        	// If there are bosses, then set the fog far plane based on the distance
        	if (boss != null && !outOfRange && !bossDied) {
		        currentFarPlane = (int) Utilities.scaleValue(Math.max(fogNearPlaneConfig, closestBossDistance), fogNearPlaneConfig, fogMaxDistConfig, fogFarPlaneConfig, FOG_MAX_FARPLANE);
		        currentFarPlaneScale = (float) Utilities.scaleValue(Math.max(fogNearPlaneConfig, closestBossDistance), fogNearPlaneConfig, fogMaxDistConfig, fogFarPlaneScaleConfig, FOG_MAX_FARPLANE_SCALE);

		        renderFog(event.getFogMode(), currentFarPlane, currentFarPlaneScale);
        	}
        	else if(bossDied && boss == closestBoss) {
        		adjustFogFarPlane(event);
        	}
        }
    }

    /*
     * Updates the fog's far plane distance
     */
    private static void renderFog(int fogMode, float farPlaneDistance, float farPlaneDistanceScale) {
        if (fogMode < 0) {
            GL11.glFogf(GL11.GL_FOG_START, 0.0F);
            GL11.glFogf(GL11.GL_FOG_END, farPlaneDistance);
        } else {
            GL11.glFogf(GL11.GL_FOG_START, farPlaneDistance * farPlaneDistanceScale);
            GL11.glFogf(GL11.GL_FOG_END, farPlaneDistance);
        }
    }

    /*
     * Finds the closest boss to the player
     */
    private static LivingEntity checkForBossInRange(EntityViewRenderEvent event) {
    	
    	PlayerEntity player = (PlayerEntity) ((IForgeEntity) event).getEntity();
        World world = player.world;
    		
		List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, player.getBoundingBox().grow(fogMaxDistConfig));
		
		// If there are no bosses in the list, then exit the function and reset distances
		if (!(Utilities.containsInstance(entities, ZombieChampionEntity.class, SkeletonChampionEntity.class))) {
			closestBoss = null;
			closestBossDistance = fogMaxDistConfig + 1;
			outOfRange = true;
			return closestBoss;
		}
		
		Iterator<LivingEntity> iterator = entities.iterator();
		
		// Do while there are bosses in range of the player
    	while (iterator.hasNext()) {
    		
    		LivingEntity entity = iterator.next();
    		
    		// Only test bosses in the list, ignore all other entity types
    		if(entity.getClass() == ZombieChampionEntity.class || entity.getClass() == SkeletonChampionEntity.class) {

			    float entityDistance = player.getDistance(entity);
			    
			    // Is this boss closer than the last closest one and within the max fog distance?
			    // If it is, then make it the new closest boss
			    if (closestBoss == null && entityDistance <= fogMaxDistConfig || closestBoss != null && closestBoss != entity && entityDistance < player.getDistance(closestBoss)) {
			    	closestBoss = entity;
			    	outOfRange = false;	
			    	
			    	return closestBoss;
			    } 
			    // Is this the current closest boss and is it now out of range?
			    // If it is, then reset the closest entity to null
			    else if (entity == closestBoss && entityDistance > fogMaxDistConfig) {
			    	closestBoss = null;
			    	closestBossDistance = fogMaxDistConfig + 1;
	        		outOfRange = true;
	        		
	        		return closestBoss;
			    }
    		}
    	}
    	
    	// Update the distance of the closest boss
    	if (closestBoss != null)
    		closestBossDistance = player.getDistance(closestBoss);

    	return closestBoss;
    }

    /*
     * This fades the fog color from the current color to the normal fog color over FOG_FADE_TIME ticks
     */
    private static void fadeOutFogColor(EntityViewRenderEvent.FogColors event) {
    	
    	fog_colorfade_tick += 1;
    	
    	// On the first tick, figure out how much should be added to each color channel per tick
    	if (!outOfRange && fog_colorfade_tick == 1) {

    		float diff = (event.getRed() - currentFogColorRed);
    		addToRed = diff / FOG_FADE_TIME;
    		
    		diff = (event.getGreen() - currentFogColorGreen);
    		addToGreen = diff / FOG_FADE_TIME;
    		
    		diff = (event.getBlue() - currentFogColorBlue);
    		addToBlue = diff / FOG_FADE_TIME;
    	}

    	// On all ticks, begin updating the current fog color for each color channel
    	if (!outOfRange && fog_colorfade_tick <= FOG_FADE_TIME) {
    		
    		currentFogColorRed += addToRed;
			event.setRed(currentFogColorRed);
			
			currentFogColorGreen += addToGreen;
			event.setGreen(currentFogColorGreen);
			
			currentFogColorBlue += addToBlue;
			event.setBlue(currentFogColorBlue);
    	}
    	
    	// After the last tick, reset
    	else if (fog_colorfade_tick > FOG_FADE_TIME) {
    		fog_colorfade_tick = 0;
    		outOfRange = true;
    		bossDied = false;
    	}
    }
    
    /*
     * Fades fog far plane from current distance to normal distance over FOG_FADE_TIME ticks
     */
    private static void adjustFogFarPlane(EntityViewRenderEvent.RenderFogEvent event) {
    	
    	fog_farplane_tick += 1;
    	
    	if (!outOfRange && fog_farplane_tick == 1) {
    		float diff = (event.getFarPlaneDistance() - currentFarPlane);
    		addToFarPlane = diff / FOG_FADE_TIME;
    		
    		diff = (FOG_MAX_FARPLANE_SCALE - currentFarPlaneScale);
    		addToFarPlaneScale = diff / FOG_FADE_TIME;
    	}
       
        if (!outOfRange && fog_farplane_tick <= FOG_FADE_TIME) {
        	currentFarPlane += addToFarPlane;
        	currentFarPlaneScale += addToFarPlaneScale;
        	
        	renderFog(event.getFogMode(), currentFarPlane, Math.min(FOG_MAX_FARPLANE_SCALE, currentFarPlaneScale));
        }
        
    	// After the last tick, reset
        else if (fog_farplane_tick > FOG_FADE_TIME) {
        	fog_farplane_tick = 0;
    		outOfRange = true;
    		bossDied = false;
    		
    		renderFog(event.getFogMode(), FOG_MAX_FARPLANE, FOG_MAX_FARPLANE_SCALE);
    	}  
    }
}
