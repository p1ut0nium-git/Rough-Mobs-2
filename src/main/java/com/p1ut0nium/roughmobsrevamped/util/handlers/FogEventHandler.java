package com.p1ut0nium.roughmobsrevamped.util.handlers;

import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.p1ut0nium.roughmobsrevamped.entities.BossZombie;
import com.p1ut0nium.roughmobsrevamped.misc.BossHelper.BossApplier;
import com.p1ut0nium.roughmobsrevamped.misc.Constants;

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
	
	private static double FOG_MAX_PLANE = 100;
	private static double FOG_ORIGINAL_MAX = 0.0001F;

    //private static double closestEntityDistance = -1;
    private static double entityDistance;
    private static BossZombie closestEntity;
    
    private static double entityFogStart = BossApplier.bossFogStartDistance;
    private static double maxFogDist = BossApplier.bossFogMaxDistance;
    private static double fogFarPlane = BossApplier.bossFogFarPlane;
    private static double fogStrength = BossApplier.bossFogStrength;
    private static double fogColorRed = Float.parseFloat(BossApplier.bossFogColor[0]);
    private static double fogColorGreen = Float.parseFloat(BossApplier.bossFogColor[1]);
    private static double fogColorBlue = Float.parseFloat(BossApplier.bossFogColor[2]);

    @SubscribeEvent
    public static void onGetFogColor(EntityViewRenderEvent.FogColors event) {
       	
    	// Set original fog color
       	if (closestEntity == null) {
       		FOG_RED = event.getRed();
       		FOG_GREEN = event.getGreen();
       		FOG_BLUE = event.getBlue();
       	}

       	// Get closest boss
        if (event.getEntity() instanceof EntityPlayer) {
        	
            EntityPlayer player = (EntityPlayer) event.getEntity();
            World world = player.world;

            /*
             * TODO - Make the test work for both skeleton and zombie bosses, and make the iterator work so the closest boss in range is the one counted
             */
            if (BossApplier.bossFogEnabled) {
	            List<BossZombie> entities = world.getEntitiesWithinAABB(BossZombie.class, player.getEntityBoundingBox().grow(maxFogDist));
	            	           
	           // if (Helpers.containsInstance(entities, IBoss.class))
	           // 	System.out.println("Has IBoss");

	            Iterator<BossZombie> iterator = entities.iterator();

	            while (iterator.hasNext()) {

	            	BossZombie entity = (BossZombie) iterator.next();
		            
		            if((int) player.getDistance((BossZombie)entity) <= maxFogDist)
		            	entityDistance = player.getDistance((BossZombie)entity);
		            
		            if (closestEntity == null || entityDistance < player.getDistance(closestEntity)) {
		            	closestEntity = entity;
		            	//closestEntityDistance = entityDistance;
		            } else if (entity == closestEntity && entityDistance > maxFogDist) {
		            	closestEntity = null;
		            	//closestEntityDistance = -1;
		            }
	            }
	            
	            if (entities.isEmpty()) {
	            	//closestEntityDistance = -1;
	            	closestEntity = null;
	            }

	            if (!(entities.isEmpty())) {
		            event.setRed((float) Helpers.scaleValue(Math.max(entityFogStart, entityDistance), entityFogStart, maxFogDist, fogColorRed, FOG_RED));
		            event.setGreen((float) Helpers.scaleValue(Math.max(entityFogStart, entityDistance), entityFogStart, maxFogDist, fogColorGreen, FOG_GREEN));
		            event.setBlue((float) Helpers.scaleValue(Math.max(entityFogStart, entityDistance), entityFogStart, maxFogDist, fogColorBlue, FOG_BLUE));
	            }
            }
        }
    }
    
    @SubscribeEvent
    public static void onRenderFog(EntityViewRenderEvent.RenderFogEvent event) {
    	
    	double farPlane;
    	double maxFogStrength;
    	
    	// Set original fog far plane distance
       	if (closestEntity == null) {
       		FOG_MAX_PLANE = event.getFarPlaneDistance();
       		maxFogStrength = FOG_ORIGINAL_MAX;
       	}
       	
        if (BossApplier.bossFogEnabled) {
	        farPlane = Helpers.scaleValue(Math.max(entityFogStart, entityDistance), entityFogStart, maxFogDist, fogFarPlane, FOG_MAX_PLANE);
	        maxFogStrength = Helpers.scaleValue(Math.max(entityFogStart, entityDistance), entityFogStart, maxFogDist, fogStrength, FOG_ORIGINAL_MAX);
	        renderFog(event.getFogMode(), farPlane, maxFogStrength);
        }
    }

    private static void renderFog(int fogMode, double farPlaneDistance, double farPlaneDistanceScale) {
        if (fogMode < 0) {
            GL11.glFogf(GL11.GL_FOG_START, 0.0F);
            GL11.glFogf(GL11.GL_FOG_END, (float) farPlaneDistance);
        } else {
            GL11.glFogf(GL11.GL_FOG_START, (float) farPlaneDistance * (float) farPlaneDistanceScale);
            GL11.glFogf(GL11.GL_FOG_END, (float) farPlaneDistance);
        }
    }
}
