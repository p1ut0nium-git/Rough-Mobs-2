package com.p1ut0nium.roughmobsrevamped.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.p1ut0nium.roughmobsrevamped.RoughMobsRevamped;

import net.minecraft.block.Block;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.entity.EntityType;

public class FeatureHelper {
	
	public static boolean removeTask(CreatureEntity entity, Class<? extends EntityAIBase> aiClass) {
		return tryRemoveTask(entity, entity.task, aiClass) || tryRemoveTask(entity, entity.targetTasks, aiClass);
	}
	
	private static boolean tryRemoveTask(CreatureEntity entity, EntityAITasks tasks, Class<? extends EntityAIBase> aiClass) {
		
		for (EntityAITaskEntry ai : tasks.taskEntries) 
		{
			if (aiClass.isInstance(ai.action)) 
			{
				tasks.removeTask(ai.action);
				return true;
			}
		}
		
		return false;
	}

	public static boolean addEffect(LivingEntity entity, Effect effect, int duration, int startAmplifier, int chance, boolean isIncreasing, int maxAmplifier) {
		
		if (entity == null || effect == null || startAmplifier < 0 || chance <= 0 || duration <= 0 || entity.getRNG().nextInt(chance) != 0)
			return false;
		
		int amplifier = startAmplifier;
		if (isIncreasing) 
		{
			EffectInstance active = entity.getActivePotionEffect(effect);
			if (active != null) 
			{
				if (active.getDuration() == duration)
					return false;
				
				amplifier = Math.min(startAmplifier + active.getAmplifier() + 1, maxAmplifier);
			}
		}
		
		entity.addPotionEffect(new EffectInstance(effect, duration, amplifier));
		return true;
	}
	
	public static boolean addEffect(LivingEntity entity, Effect effect, int duration, int amplifier, int chance) {
		return addEffect(entity, effect, duration, amplifier, chance, false, 127);
	}
	
	public static boolean addEffect(LivingEntity entity, Effect effect, int duration, int amplifier) {
		return addEffect(entity, effect, duration, amplifier, 1);
	}
	
	public static boolean addEffect(LivingEntity entity, Effect effect, int duration) {
		return addEffect(entity, effect, duration, 0);
	}
	
	public static void spawnParticle(Entity entity, ParticleType type, float spread, int amount) {
		
		Random rnd = entity.world.rand; 
		for (int i = 0; i < amount; i++) 
		{
			double moveX = (rnd.nextDouble() - 0.5D) * 2.0D * spread;
			double moveY = -rnd.nextDouble() * spread;
			double moveZ = (rnd.nextDouble() - 0.5D) * 2.0D * spread;
			((ServerWorld)entity.world).spawnParticle(type, entity.posX + (rnd.nextDouble() - 0.5D) * (double)entity.getWidth(), entity.posY + rnd.nextDouble() * (double)entity.getHeight() - 0.25D, entity.posZ + (rnd.nextDouble() - 0.5D) * (double)entity.getWidth(), 1, moveX, moveY, moveZ, 0.0D);
		}
	}
	
	public static void spawnParticle(Entity entity, ParticleType type, int amount) {
		spawnParticle(entity, type, 1, amount);
	}
	
	public static void spawnParticle(Entity entity, ParticleType type) {
		spawnParticle(entity, type, 1);
	}
	
	public static void playSound(Entity entity, SoundEvent event, float volume, float pitch) {
		
		entity.world.playSound((PlayerEntity)null, entity.prevPosX, entity.prevPosY, entity.prevPosZ, event, entity.getSoundCategory(), volume, pitch);
    	entity.playSound(event, volume, pitch);
	}
	
	public static void playSound(Entity entity, SoundEvent event) {
		playSound(entity, event, 1.0F, 1.0F);
	}
	
	public static void knockback(Entity attacker, LivingEntity target, float strength, float extraLeap) {
		
		double xRatio = attacker.posX - target.posX;
		double zRatio = attacker.posZ - target.posZ;
		target.knockBack(attacker, strength, xRatio, zRatio);
		target.motionY += extraLeap;
	}

	public static List<Block> getBlocksFromNames(String[] breakBlocks) {

		List<Block> blocks = new ArrayList<Block>();
		
		for (String name : breakBlocks) 
		{
			// TODO Block block = Block.getBlockFromItem(itemIn)
			Block block = Block.getBlockFromName(name);
			if (block == null) 
				RoughMobsRevamped.LOGGER.error(name + " isn't a valid block!");
			else
				blocks.add(block);
		}
		
		return blocks;
	}
	
	public static List<EntityType> getEntitiesFromNames(String[] entitieNames) {

		List<EntityType> entities = new ArrayList<EntityType>();
		
		for (String name : entitieNames) 
		{
			EntityType entity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(name));
			if (entity == null) 
				RoughMobsRevamped.LOGGER.error(name + " isn't a valid entity!");
			else
				entities.add(entity);
		}
		
		return entities;
	}
	
	public static Map<Potion, Integer> getPotionsFromNames(String[] potionNames) {

		Map<Potion, Integer> potions = new HashMap<Potion, Integer>();
		
		for (String name : potionNames) 
		{
			String[] parts = name.split(";");
			
			if (parts.length < 2)
				continue;
			
			Potion potion = Potion.getPotionTypeForName(parts[0]);
			// TODO Potion potion = Potion.getPotionFromResourceLocation(parts[0]);
			if (potion == null) 
				RoughMobsRevamped.LOGGER.error(parts[0] + " isn't a valid potion effect!");
			else
			{
				try
				{
					potions.put(potion, Integer.parseInt(parts[1]));
				}
				catch(NumberFormatException e)
				{
					RoughMobsRevamped.LOGGER.error(parts[1] + " isn't a valid number!");
				}
			}
		}
		
		return potions;
	}
}
