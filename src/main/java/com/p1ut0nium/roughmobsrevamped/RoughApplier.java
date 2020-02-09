package com.p1ut0nium.roughmobsrevamped;

import java.util.ArrayList;
import java.util.List;
import com.p1ut0nium.roughmobsrevamped.features.EntityFeatures;
import com.p1ut0nium.roughmobsrevamped.features.ZombieFeatures;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class RoughApplier {
	
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
	
	@SubscribeEvent
	public void onEntitySpawn(EntityJoinWorldEvent event) {
		
		if (event.getWorld().isRemote)
			return;
		
		Entity entity = event.getEntity();

		//TODO Need validation
		//System.out.println("Entity Spawned: " + event.getEntity());
		//System.out.println("Entity Spawned Name: " + event.getEntity().getName());
		
		//final DataParameter<Boolean> HASEQUIPMENT = entity.getDataManager().createKey(LivingEntity.class, DataSerializers.BOOLEAN);
		//entity.getDataManager().register(HASEQUIPMENT, false);
		
		for (EntityFeatures features : FEATURES)  {
			//if (features.isEntity(entity) == true) {
				
				//if (!entity.getDataManager().get(HASEQUIPMENT)) {
					features.addFeatures(event, entity);
				//}
			//}
		}
		
		//entity.getDataManager().set(HASEQUIPMENT, true);
	}
}
