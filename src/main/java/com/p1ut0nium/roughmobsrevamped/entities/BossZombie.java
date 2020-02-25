package com.p1ut0nium.roughmobsrevamped.entities;

import javax.annotation.Nullable;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class BossZombie extends EntityZombie implements IBoss {

	public BossZombie(World worldIn) {
		super(worldIn);
	}
	
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        super.onInitialSpawn(difficulty, livingdata);
        
        return livingdata;
    }
	
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            for (int i = 0; i < 2; ++i) {
                this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0.0D, 0.0D, 0.0D);
            }
            
            /* TODO: Change sky/fog color to red if player is in range
            if (this.canEntityBeSeen(this.world.getClosestPlayerToEntity(this, 50))) {
            }
            */
        }

        super.onLivingUpdate();
    }

}
