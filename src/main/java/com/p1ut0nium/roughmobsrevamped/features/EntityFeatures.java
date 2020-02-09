package com.p1ut0nium.roughmobsrevamped.features;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public abstract class EntityFeatures {
	
	public static final Random RND = new Random();
	public static final int MAX = Short.MAX_VALUE;
	
	//Default
	public String name;
	protected List<Class<? extends Entity>> entityClasses;
	//Config
	protected boolean featuresEnabled;
	protected List<String> entityNames;
	
	public EntityFeatures(String name, Class<? extends Entity>... entityClasses) {
		this.name = name;
		this.entityClasses = Arrays.asList(entityClasses);
	}
	
	public boolean isEntity(Entity entity) {

				//ResourceLocation loc = ForgeRegistries.ENTITIES.getValue(entity);
				//getValue(new ResourceLocation(entity);
				//ResourceLocation loc = EntityList.getKey(creature);

				//System.out.println("ENTITY NAME " + entity.getName());
				//System.out.println("ENTITY TO STRING " + entity.toString());
				//System.out.println("ENTITY " + ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entity.toString())));
				return true;
				// return featuresEnabled && loc != null && entityNames.contains(loc.toString());
	}
	
	public void initConfig() {
		featuresEnabled = true;
		//TODO entityNames = Constants.getRegNames(entityClasses).toArray(new String[0]);
	}
	
	public boolean hasDefaultConfig() {
		return true;
	}
	
	public void addFeatures(EntityJoinWorldEvent event, Entity entity) {
	}

	public void onDeath(Entity deadEntity, DamageSource source) {
		// If the mob was killed by a player, update the player's kill count.
		if (source.getTrueSource() instanceof PlayerEntity) {
			//TODO PlayerHelper.setPlayerMobKills();
		}
	}
	
	public void preInit() {
	}

	public void postInit() {
	}
}
