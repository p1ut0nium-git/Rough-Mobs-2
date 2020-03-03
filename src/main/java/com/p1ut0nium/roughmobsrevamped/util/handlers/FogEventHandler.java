package com.p1ut0nium.roughmobsrevamped.util.handlers;

import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.p1ut0nium.roughmobsrevamped.entities.BossSkeleton;
import com.p1ut0nium.roughmobsrevamped.entities.BossZombie;
import com.p1ut0nium.roughmobsrevamped.misc.BossHelper;
import com.p1ut0nium.roughmobsrevamped.misc.Constants;
import com.p1ut0nium.roughmobsrevamped.util.Helpers;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = Constants.MODID, value = {Side.CLIENT})
public class FogEventHandler {
	
	private static double FOG_RED = 0.7116436;
	private static double FOG_GREEN = 0.81895345;
	private static double FOG_BLUE = 0.99999976;
	
	private static float FOG_MAX_FARPLANE = 192;
	private static float FOG_MAX_FARPLANE_SCALE = 1.0F;
	
	private static int FOG_FADE_TIME = 60;
	private static int fog_colorfade_tick = 0;
	private static int fog_farplane_tick = 0;

    private static EntityLiving closestBoss;
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

    @SubscribeEvent
    public static void onGetFogColor(EntityViewRenderEvent.FogColors event) {
       	
    	// Set original fog color if no bosses are in range
   		FOG_RED = event.getRed();
   		FOG_GREEN = event.getGreen();
   		FOG_BLUE = event.getBlue();

       	// If player, then check for bosses in range.
        if (event.getEntity() instanceof EntityPlayer && BossHelper.bossFogEnabled) {

            EntityLiving boss = checkForBossInRange(event);
     
        	// If there are bosses, then set the fog color based on the distance
        	if (boss != null && !outOfRange && !bossDied) {

        		currentFogColorRed = (float) Helpers.scaleValue(Math.max(fogNearPlaneConfig, closestBossDistance), fogNearPlaneConfig, fogMaxDistConfig, fogColorConfigRed, FOG_RED);
        		currentFogColorGreen = (float) Helpers.scaleValue(Math.max(fogNearPlaneConfig, closestBossDistance), fogNearPlaneConfig, fogMaxDistConfig, fogColorConfigGreen, FOG_GREEN);
        		currentFogColorBlue = (float) Helpers.scaleValue(Math.max(fogNearPlaneConfig, closestBossDistance), fogNearPlaneConfig, fogMaxDistConfig, fogColorConfigBlue, FOG_BLUE);

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
   		// currentFarPlaneScale = FOG_MAX_FARPLANE_SCALE;

        if (event.getEntity() instanceof EntityPlayer && BossHelper.bossFogEnabled) {

        	EntityLiving boss = checkForBossInRange(event);
            
        	// If there are bosses, then set the fog far plane based on the distance
        	if (boss != null && !outOfRange && !bossDied) {
		        currentFarPlane = (int) Helpers.scaleValue(Math.max(fogNearPlaneConfig, closestBossDistance), fogNearPlaneConfig, fogMaxDistConfig, fogFarPlaneConfig, FOG_MAX_FARPLANE);
		        currentFarPlaneScale = (float) Helpers.scaleValue(Math.max(fogNearPlaneConfig, closestBossDistance), fogNearPlaneConfig, fogMaxDistConfig, fogFarPlaneScaleConfig, FOG_MAX_FARPLANE_SCALE);

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
    private static EntityLiving checkForBossInRange(EntityViewRenderEvent event) {
    	
    	EntityPlayer player = (EntityPlayer) event.getEntity();
        World world = player.world;
    		
		List<EntityLiving> entities = world.getEntitiesWithinAABB(EntityLiving.class, player.getEntityBoundingBox().grow(fogMaxDistConfig));
		
		// If there are no bosses in the list, then exit the function and reset distances
		if (!(Helpers.containsInstance(entities, BossZombie.class, BossSkeleton.class))) {
			closestBoss = null;
			closestBossDistance = fogMaxDistConfig + 1;
			outOfRange = true;
			return closestBoss;
		}
		
		Iterator<EntityLiving> iterator = entities.iterator();
		
		// Do while there are bosses in range of the player
    	while (iterator.hasNext()) {
    		
    		EntityLiving entity = iterator.next();
    		
    		// Only test bosses in the list, ignore all other entity types
    		if(entity.getClass() == BossZombie.class || entity.getClass() == BossSkeleton.class) {

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
