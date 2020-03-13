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
package com.p1ut0nium.roughmobsrevamped.init;

import com.p1ut0nium.roughmobsrevamped.entity.boss.ZombieChampionEntity;
import com.p1ut0nium.roughmobsrevamped.client.renderer.entity.HostileBatRenderer;
import com.p1ut0nium.roughmobsrevamped.entity.boss.SkeletonChampionEntity;
import com.p1ut0nium.roughmobsrevamped.entity.monster.HostileBatEntity;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityTypes {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Constants.MODID);
	
	// Champion Zombie
	public static final String ZOMBIE_CHAMPION_NAME = "zombie_champion_entity";
	public static final RegistryObject<EntityType<ZombieChampionEntity>> ZOMBIE_CHAMPION = ENTITY_TYPES.register(ZOMBIE_CHAMPION_NAME, () ->
					EntityType.Builder.<ZombieChampionEntity>create(ZombieChampionEntity::new, EntityClassification.MONSTER)
					.size(EntityType.ZOMBIE.getWidth(), EntityType.ZOMBIE.getHeight())
					.build(new ResourceLocation(Constants.MODID, ZOMBIE_CHAMPION_NAME).toString())
	);
	
	// Champion Skeleton
	public static final String SKELETON_CHAMPION_NAME = "skeleton_champion_entity";
	public static final RegistryObject<EntityType<SkeletonChampionEntity>> SKELETON_CHAMPION = ENTITY_TYPES.register(SKELETON_CHAMPION_NAME, () ->
					EntityType.Builder.<SkeletonChampionEntity>create(SkeletonChampionEntity::new, EntityClassification.MONSTER)
					.size(EntityType.SKELETON.getWidth(), EntityType.SKELETON.getHeight())
					.build(new ResourceLocation(Constants.MODID, SKELETON_CHAMPION_NAME).toString())
	);
	
	// Hostile Bat
	public static final String HOSTILE_BAT_NAME = "hostile_bat_entity";
	public static final RegistryObject<EntityType<HostileBatEntity>> HOSTILE_BAT = ENTITY_TYPES.register(HOSTILE_BAT_NAME, () ->
					EntityType.Builder.<HostileBatEntity>create(HostileBatEntity::new, EntityClassification.MONSTER)
					.size(EntityType.BAT.getWidth(), EntityType.BAT.getHeight())
					.build(new ResourceLocation(Constants.MODID, HOSTILE_BAT_NAME).toString())
	);
	
	@OnlyIn(Dist.CLIENT)
	public static void registerRendering() {
		RenderingRegistry.registerEntityRenderingHandler(HostileBatEntity.class, HostileBatRenderer::new);
	}
}
