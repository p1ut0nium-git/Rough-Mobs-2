package com.p1ut0nium.roughmobsrevamped.ai.combat;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.p1ut0nium.roughmobsrevamped.misc.FeatureHelper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;

public class RoughAIMobBuff extends EntityAIBase {
	
	protected EntityLivingBase entity;
	protected Map<Potion, Integer> effects;
	protected float range;
	
	public RoughAIMobBuff(EntityLivingBase entity, Map<Potion, Integer> effects, float range) {
		this.entity = entity;
		this.effects = effects;
		this.range = range;
		this.setMutexBits(4);
	}
	
	@Override
	public boolean shouldExecute() {
		return !getEntitiesInRange().isEmpty() && !effects.isEmpty() && range != 0;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateTask() {
		
		Object[] entries = effects.entrySet().toArray();
		Random rnd = entity.getEntityWorld().rand;
		Map.Entry<Potion, Integer> entry;
		
		for (EntityMob mob : getEntitiesInRange())
		{
			if (mob == entity || isEffected(mob))
				continue;
			
			entry = (Map.Entry<Potion, Integer>) entries[rnd.nextInt(entries.length)];
			FeatureHelper.addEffect(mob, entry.getKey(), 600, entry.getValue()-1);
			
			FeatureHelper.spawnParticle(mob, EnumParticleTypes.SPELL_WITCH, 10);
		}
	}
	
	private boolean isEffected(EntityLivingBase mob) {
		
		for (Potion potion : effects.keySet())
		{
			for (PotionEffect active : mob.getActivePotionEffects())
			{
				if (potion.equals(active.getPotion()))
				{
					return true;
				}
			}
		}
		
		return false;
	}

	protected List<EntityMob> getEntitiesInRange() {
		return entity.getEntityWorld().getEntitiesWithinAABB(EntityMob.class, entity.getEntityBoundingBox().expand(range, range, range));
	}
}
