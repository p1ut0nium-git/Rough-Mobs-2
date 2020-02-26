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
	
	private static int FOG_MAX_PLANE = 100;
	private static float FOG_MAX_STRENGTH = 0.8F;

    private static int closestEntityDistance = -1;
    private static int entityDistance;
    private static BossZombie closestEntity;
    
    private static int entityFogStart = BossApplier.bossFogStartDistance;
    private static int maxFogDist = BossApplier.bossFogMaxDistance;
    private static int fogFarPlane = BossApplier.bossFogFarPlane;
    private static float fogStrength = BossApplier.bossFogStrength;
    private static float fogColorRed = Float.parseFloat(BossApplier.bossFogColor[0]);
    private static float fogColorGreen = Float.parseFloat(BossApplier.bossFogColor[1]);
    private static float fogColorBlue = Float.parseFloat(BossApplier.bossFogColor[2]);

    @SubscribeEvent
    public static void onGetFogColor(EntityViewRenderEvent.FogColors event) {
    	
    	System.out.println("Red: " + event.getRed());
       	System.out.println("Green: " + event.getGreen());
       	System.out.println("Blue: " + event.getBlue());

        if (event.getEntity() instanceof EntityPlayer) {
        	
            EntityPlayer player = (EntityPlayer) event.getEntity();
            World world = player.world;

            /*
             * TODO - Make the test work for both skeleton and zombie bosses, and make the iterator work so the closest boss in range is counted
             */
            if (BossApplier.bossFogEnabled) {
	            List<BossZombie> entities = world.getEntitiesWithinAABB(BossZombie.class, player.getEntityBoundingBox().grow(maxFogDist));
	            	           
	           // if (Helpers.containsInstance(entities, IBoss.class))
	           // 	System.out.println("Has IBoss");

	            Iterator<BossZombie> iterator = entities.iterator();

	            while (iterator.hasNext()) {

	            	BossZombie entity = (BossZombie) iterator.next();
		            
		            if((int) player.getDistance((BossZombie)entity) <= maxFogDist)
		            	entityDistance = (int) player.getDistance((BossZombie)entity);
		            
		            if (closestEntity == null || entityDistance < (int) player.getDistance(closestEntity)) {
		            	closestEntity = entity;
		            	closestEntityDistance = entityDistance;
		            } else if (entity == closestEntity && entityDistance > maxFogDist) {
		            	closestEntity = null;
		            	//closestEntityDistance = -1;
		            }
		            
		           // System.out.println("Entity Distance: " + entityDistance);
	            }
	            
	            if (entities.isEmpty()) {
	            	//closestEntityDistance = -1;
	            	closestEntity = null;
	            }

	            //if (closestEntityDistance != -1) {
	            	
	            	// System.out.println("closestEntityDistance: " + closestEntityDistance);
	    	    	
		            if (!(entities.isEmpty())) {
			            event.setRed(Helpers.scaleValue(Math.max(entityFogStart, entityDistance), entityFogStart, maxFogDist, fogColorRed, FOG_RED));
			            event.setGreen(Helpers.scaleValue(Math.max(entityFogStart, entityDistance), entityFogStart, maxFogDist, fogColorGreen, FOG_GREEN));
			            event.setBlue(Helpers.scaleValue(Math.max(entityFogStart, entityDistance), entityFogStart, maxFogDist, fogColorBlue, FOG_BLUE));
		            }
	          //  }
            }
        }
    }
    
    @SubscribeEvent
    public static void onRenderFog(EntityViewRenderEvent.RenderFogEvent event) {
        if (BossApplier.bossFogEnabled) {
	  
	        int farPlane = (int) Helpers.scaleValue(Math.max(entityFogStart, entityDistance), entityFogStart, maxFogDist, fogFarPlane, FOG_MAX_PLANE);
	        float maxFogStrength = Helpers.scaleValue(Math.max(entityFogStart, entityDistance), entityFogStart, maxFogDist, fogStrength, FOG_MAX_STRENGTH);
	        renderFog(event.getFogMode(), farPlane, maxFogStrength);
        }
    }

    private static void renderFog(int fogMode, float farPlaneDistance, float farPlaneDistanceScale) {
        if (fogMode < 0) {
            GL11.glFogf(GL11.GL_FOG_START, 0.0F);
            GL11.glFogf(GL11.GL_FOG_END, farPlaneDistance);
        } else {
            GL11.glFogf(GL11.GL_FOG_START, farPlaneDistance * farPlaneDistanceScale);
            GL11.glFogf(GL11.GL_FOG_END, farPlaneDistance);
        }
    }
}
