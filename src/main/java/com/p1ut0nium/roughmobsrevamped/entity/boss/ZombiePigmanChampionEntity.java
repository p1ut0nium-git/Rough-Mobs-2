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
package com.p1ut0nium.roughmobsrevamped.entity.boss;

import com.p1ut0nium.roughmobsrevamped.init.ModEntityTypes;
import com.p1ut0nium.roughmobsrevamped.init.ModSounds;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ZombiePigmanChampionEntity extends ZombiePigmanEntity implements IChampion {
	
	public ZombiePigmanChampionEntity(EntityType<? extends ZombiePigmanEntity> zombie, World worldIn) {
		super(zombie, worldIn);
        this.experienceValue = 100;
	}
	
	public ZombiePigmanChampionEntity(World worldIn) {
	    this((EntityType<? extends ZombiePigmanEntity>) ModEntityTypes.ZOMBIE_PIGMAN_CHAMPION.get(), worldIn);
	}

	@Override
	public void onAddedToWorld() {
    	super.onAddedToWorld();
    	
        if (this.getPosY() >= world.getSeaLevel() && this.world.canBlockSeeSky(this.getPosition()))
        	((ServerWorld)this.world).addLightningBolt(new LightningBoltEntity((ServerWorld)this.world, this.getPosX(), this.getPosY(), this.getPosZ(), true));
    }

	@Override
    public void livingTick() {

        if (this.world.isRemote) {
            for (int i = 0; i < 2; ++i) {
                this.world.addParticle(ParticleTypes.DRIPPING_LAVA, this.getPosX() + (this.rand.nextDouble() - 0.5D) * (double)this.getWidth(), this.getPosY() + this.rand.nextDouble() * (double)this.getHeight(), this.getPosZ() + (this.rand.nextDouble() - 0.5D) * (double)this.getWidth(), 0.0D, 0.0D, 0.0D);
            }
        }

        super.livingTick();
    }

	@Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
    }
	
	protected boolean canDespawn() {
		return false;
	}

    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY_BOSS_IDLE;
    }
    
    protected SoundEvent getDeathSound() {
        return ModSounds.ENTITY_BOSS_DEATH;
    }
}
