package com.p1ut0nium.roughmobsrevamped.entities;

import javax.annotation.Nullable;

import com.p1ut0nium.roughmobsrevamped.util.handlers.SoundHandler;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class BossZombie extends EntityZombie implements IBoss {

	// TODO: Blased fileball stuff
	// private static final DataParameter<Byte> ON_FIRE = EntityDataManager.<Byte>createKey(EntityBlaze.class, DataSerializers.BYTE);
	
	public BossZombie(World worldIn) {
		super(worldIn);
        this.experienceValue = 50;
	}
	
	/* TODO Add Blaze fireball attack
    protected void initEntityAI()
    {
       // this.tasks.addTask(4, new BossZombie.AIFireballAttack(this));
    }
    */
    
    public void onAddedToWorld() {
    	super.onAddedToWorld();
    	
        if (this.world.isRemote) {
			this.world.addWeatherEffect(new EntityLightningBolt(this.world, this.posX, this.posY, this.posZ, true));
			SoundEvent soundEvent = new SoundEvent(new ResourceLocation("entity.lightning.thunder"));
			this.world.playSound(this.posX, this.posY, this.posZ, soundEvent, SoundCategory.AMBIENT, 100.0F, 1.0F, true);
        }
    }

    public void onLivingUpdate() {
	
        if (this.world.isRemote) {
            for (int i = 0; i < 2; ++i) {
                this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0.0D, 0.0D, 0.0D);
            }
        }
        
        // List entities = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(20));

        super.onLivingUpdate();
    }
    
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        
        //TODO Add custom death effects
    }
    
    //TODO Add custom ambient sound
    protected SoundEvent getAmbientSound() {
        return SoundHandler.ENTITY_BOSS_IDLE;
    }
    
	//TODO Add custom death sound
    protected SoundEvent getDeathSound() {
        return SoundHandler.ENTITY_BOSS_DEATH;
    }
    
    //TODO Add custom loot table
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_ZOMBIE;
    }
    
    /* TODO - Blaze fireball stuff
    public void setOnFire(boolean onFire)
    {
        byte b0 = ((Byte)this.dataManager.get(ON_FIRE)).byteValue();

        if (onFire)
        {
            b0 = (byte)(b0 | 1);
        }
        else
        {
            b0 = (byte)(b0 & -2);
        }

        this.dataManager.set(ON_FIRE, Byte.valueOf(b0));
    }
    
    static class AIFireballAttack extends EntityAIBase
    {
        private final BossZombie bossZombie;
        private int attackStep;
        private int attackTime;

        public AIFireballAttack(BossZombie bossZombieIn)
        {
            this.bossZombie = bossZombieIn;
            this.setMutexBits(3);
        }

        public boolean shouldExecute()
        {
            EntityLivingBase entitylivingbase = this.bossZombie.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive();
        }

        public void startExecuting()
        {
            this.attackStep = 0;
        }

        public void resetTask()
        {
            this.bossZombie.setOnFire(false);
        }

        public void updateTask()
        {
            --this.attackTime;
            EntityLivingBase entitylivingbase = this.bossZombie.getAttackTarget();
            double d0 = this.bossZombie.getDistanceSq(entitylivingbase);

            if (d0 < 4.0D)
            {
                if (this.attackTime <= 0)
                {
                    this.attackTime = 20;
                    this.bossZombie.attackEntityAsMob(entitylivingbase);
                }

                this.bossZombie.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
            }
            else if (d0 < this.getFollowDistance() * this.getFollowDistance())
            {
                double d1 = entitylivingbase.posX - this.bossZombie.posX;
                double d2 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 2.0F) - (this.bossZombie.posY + (double)(this.bossZombie.height / 2.0F));
                double d3 = entitylivingbase.posZ - this.bossZombie.posZ;

                if (this.attackTime <= 0)
                {
                    ++this.attackStep;

                    if (this.attackStep == 1)
                    {
                        this.attackTime = 60;
                        this.bossZombie.setOnFire(true);
                    }
                    else if (this.attackStep <= 4)
                    {
                        this.attackTime = 6;
                    }
                    else
                    {
                        this.attackTime = 100;
                        this.attackStep = 0;
                        this.bossZombie.setOnFire(false);
                    }

                    if (this.attackStep > 1)
                    {
                        float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;
                        this.bossZombie.world.playEvent((EntityPlayer)null, 1018, new BlockPos((int)this.bossZombie.posX, (int)this.bossZombie.posY, (int)this.bossZombie.posZ), 0);

                        for (int i = 0; i < 1; ++i)
                        {
                            EntitySmallFireball entitysmallfireball = new EntitySmallFireball(this.bossZombie.world, this.bossZombie, d1 + this.bossZombie.getRNG().nextGaussian() * (double)f, d2, d3 + this.bossZombie.getRNG().nextGaussian() * (double)f);
                            entitysmallfireball.posY = this.bossZombie.posY + (double)(this.bossZombie.height / 2.0F) + 0.5D;
                            this.bossZombie.world.spawnEntity(entitysmallfireball);
                        }
                    }
                }

                this.bossZombie.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
            }
            else
            {
                this.bossZombie.getNavigator().clearPath();
                this.bossZombie.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
            }

            super.updateTask();
        }

        private double getFollowDistance()
        {
            IAttributeInstance iattributeinstance = this.bossZombie.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
            return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
        }
    }
*/

}
