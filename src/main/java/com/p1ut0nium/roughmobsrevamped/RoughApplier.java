package com.p1ut0nium.roughmobsrevamped;

import java.util.ArrayList;
import java.util.List;

import com.mojang.datafixers.types.templates.Tag;
import com.p1ut0nium.roughmobsrevamped.features.EntityFeatures;
import com.p1ut0nium.roughmobsrevamped.features.ZombieFeatures;
import com.p1ut0nium.roughmobsrevamped.misc.Constants;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class RoughApplier {
	
	public static final String FEATURES_APPLIED = Constants.unique("featuresApplied");
	public static final List<EntityFeatures> FEATURES = new ArrayList<EntityFeatures>();
	
	public RoughApplier() {
		MinecraftForge.EVENT_BUS.register(this);
		
		FEATURES.add(new ZombieFeatures());
	}
	
	public void preInit() {

		for (EntityFeatures features : FEATURES) {
			features.preInit();
			features.initConfig();
		}

	}		
	
	public void postInit() {
		
		for (EntityFeatures features : FEATURES)
			features.postInit();
	}
	
	/*
	 * This method intercepts spawn events, and adds features to the entities.
	 */
	@SubscribeEvent
	public void onEntitySpawn(EntityJoinWorldEvent event) {
		
		// Only execute on the server
		if (event.getWorld().isRemote) {
			return;
		}
		
		Entity entity = event.getEntity();

		//TODO Need validation
		//System.out.println("Entity Spawned: " + event.getEntity());
		//System.out.println("Entity Spawned Name: " + event.getEntity().getName());
		
		
		for (EntityFeatures features : FEATURES)  {
			//if (features.isEntity(entity) == true) {
				
					features.addFeatures(event, entity);
			
			//}
		}
		
		
		//entity.getDataManager().set(HASEQUIPMENT, true);
		// entity.getEntityData().setBoolean(FEATURES_APPLIED, true);
		entity.addTag(FEATURES_APPLIED);
		//System.out.println("TAGS AFTER: " + entity.getTags());

		
		/* Code snippet for adding CompoundNBT
		CompoundNBT tag = new CompoundNBT();
		tag.putBoolean("hasFeatures", true);
		???
		*/

	}
	
	/* TODO - Add capabiliites
	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof LivingEntity) {
			System.out.println("[" + Constants.MODID.toUpperCase() + "] ATTACHING ATTACK TRACKER CAPABILITY.");
			//event.addCapability(key, cap);
		}
		
	}
	*/
}
