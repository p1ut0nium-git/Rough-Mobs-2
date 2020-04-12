/*
 * Rough Mobs Revamped for Minecraft Forge 1.15.2
 * 
 * This is a complete revamp of Lellson's Rough Mobs 2
 * 
 * Author: p1ut0nium_94
 * Website: https://www.curseforge.com/minecraft/mc-mods/rough-mobs-revamped
 * Source: https://github.com/p1ut0nium-git/Rough-Mobs-Revamped/tree/1.15.2
 * 
 */
package com.p1ut0nium.roughmobsrevamped.init;

import com.p1ut0nium.roughmobsrevamped.entity.boss.HuskChampionEntity;
import com.p1ut0nium.roughmobsrevamped.entity.boss.SkeletonChampionEntity;
import com.p1ut0nium.roughmobsrevamped.entity.boss.ZombieChampionEntity;
import com.p1ut0nium.roughmobsrevamped.entity.boss.ZombiePigmanChampionEntity;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraft.client.renderer.entity.HuskRenderer;
import net.minecraft.client.renderer.entity.PigZombieRenderer;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
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
	public static final String ZOMBIE_CHAMPION_NAME = "zombie_champion";
	public static final RegistryObject<EntityType<ZombieChampionEntity>> ZOMBIE_CHAMPION = ENTITY_TYPES.register(ZOMBIE_CHAMPION_NAME, () ->
					EntityType.Builder.<ZombieChampionEntity>create(ZombieChampionEntity::new, EntityClassification.MONSTER)
					.size(EntityType.ZOMBIE.getWidth(), EntityType.ZOMBIE.getHeight())
					.build(new ResourceLocation(Constants.MODID, ZOMBIE_CHAMPION_NAME).toString())
	);
	
	// Champion Zombie Pigman
	public static final String ZOMBIE_PIGMAN_CHAMPION_NAME = "zombie_pigman_champion";
	public static final RegistryObject<EntityType<ZombiePigmanChampionEntity>> ZOMBIE_PIGMAN_CHAMPION = ENTITY_TYPES.register(ZOMBIE_PIGMAN_CHAMPION_NAME, () ->
					EntityType.Builder.<ZombiePigmanChampionEntity>create(ZombiePigmanChampionEntity::new, EntityClassification.MONSTER)
					.size(EntityType.ZOMBIE_PIGMAN.getWidth(), EntityType.ZOMBIE_PIGMAN.getHeight())
					.build(new ResourceLocation(Constants.MODID, ZOMBIE_PIGMAN_CHAMPION_NAME).toString())
	);
	
	// Champion Husk
	public static final String HUSK_CHAMPION_NAME = "husk_champion";
	public static final RegistryObject<EntityType<HuskChampionEntity>> HUSK_CHAMPION = ENTITY_TYPES.register(HUSK_CHAMPION_NAME, () ->
					EntityType.Builder.<HuskChampionEntity>create(HuskChampionEntity::new, EntityClassification.MONSTER)
					.size(EntityType.HUSK.getWidth(), EntityType.HUSK.getHeight())
					.build(new ResourceLocation(Constants.MODID, HUSK_CHAMPION_NAME).toString())
	);
	
	// Champion Skeleton
	public static final String SKELETON_CHAMPION_NAME = "skeleton_champion";
	public static final RegistryObject<EntityType<SkeletonChampionEntity>> SKELETON_CHAMPION = ENTITY_TYPES.register(SKELETON_CHAMPION_NAME, () ->
					EntityType.Builder.<SkeletonChampionEntity>create(SkeletonChampionEntity::new, EntityClassification.MONSTER)
					.size(EntityType.SKELETON.getWidth(), EntityType.SKELETON.getHeight())
					.build(new ResourceLocation(Constants.MODID, SKELETON_CHAMPION_NAME).toString())
	);
	
	@OnlyIn(Dist.CLIENT)
	public static void registerRendering() {
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ZOMBIE_CHAMPION.get(), ZombieRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ZOMBIE_PIGMAN_CHAMPION.get(), PigZombieRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SKELETON_CHAMPION.get(), SkeletonRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.HUSK_CHAMPION.get(), HuskRenderer::new);
	}
}
