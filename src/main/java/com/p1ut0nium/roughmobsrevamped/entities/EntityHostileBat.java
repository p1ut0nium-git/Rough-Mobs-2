package com.p1ut0nium.roughmobsrevamped.entities;

import javax.annotation.Nullable;

import com.p1ut0nium.roughmobsrevamped.ai.combat.RoughAIBatAttack;
import com.p1ut0nium.roughmobsrevamped.ai.combat.RoughAIBatTarget;
import com.p1ut0nium.roughmobsrevamped.ai.misc.RoughAIBatFlight;
import com.p1ut0nium.roughmobsrevamped.misc.BossHelper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityHostileBat extends EntityMob {
	
	private static double BAT_DAMAGE = BossHelper.bossBatSwarmDamage;
	private static double BAT_HEALTH = BossHelper.bossBatSwarmHealth;

    private static final DataParameter<Byte> HANGING = EntityDataManager.<Byte>createKey(EntityBat.class, DataSerializers.BYTE);
    /** Coordinates of where the bat spawned. */

    private EntityLiving boss;

	public EntityHostileBat(World worldIn) {
        super(worldIn);
        this.setSize(0.5F, 0.9F);
        this.setIsBatHanging(false);
        
        tasks.addTask(1, new RoughAIBatAttack(this));
        tasks.addTask(2, new RoughAIBatFlight(this));
		targetTasks.addTask(1, new RoughAIBatTarget(this));
	}
	
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(HANGING, Byte.valueOf((byte)0));
    }
    
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(BAT_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(BAT_HEALTH);
    }

	public void setBoss(BossZombie boss) {
		this.boss = boss;
	}
	
	public EntityLiving getBoss() {
		return this.boss;
	}
	
	protected boolean canDespawn() {
		return true;
	}

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume() {
        return 0.1F;
    }

    /**
     * Gets the pitch of living sounds in living entities.
     */
    protected float getSoundPitch() {
        return super.getSoundPitch() * 0.95F;
    }

    @Nullable
    public SoundEvent getAmbientSound() {
        return this.getIsBatHanging() && this.rand.nextInt(4) != 0 ? null : SoundEvents.ENTITY_BAT_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_BAT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_BAT_DEATH;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed() {
        return false;
    }

    protected void collideWithEntity(Entity entityIn) {
    }

    protected void collideWithNearbyEntities() {}

    public boolean getIsBatHanging() {
        return (((Byte)this.dataManager.get(HANGING)).byteValue() & 1) != 0;
    }

    public void setIsBatHanging(boolean isHanging) {
        byte b0 = ((Byte)this.dataManager.get(HANGING)).byteValue();

        if (isHanging) {
            this.dataManager.set(HANGING, Byte.valueOf((byte)(b0 | 1)));
        }
        else {
            this.dataManager.set(HANGING, Byte.valueOf((byte)(b0 & -2)));
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        super.onUpdate();

        if (this.getIsBatHanging()) {
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
            this.posY = (double)MathHelper.floor(this.posY) + 1.0D - (double)this.height;
        }
        else {
            this.motionY *= 0.6000000238418579D;
        }
    }

    protected void updateAITasks() {
        super.updateAITasks();
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking() {
        return false;
    }

    public void fall(float distance, float damageMultiplier) {}

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {}

    /**
     * Return whether this entity should NOT trigger a pressure plate or a tripwire.
     */
    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        else {
            if (!this.world.isRemote && this.getIsBatHanging()) {
                this.setIsBatHanging(false);
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    public static void registerFixesBat(DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityBat.class);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.dataManager.set(HANGING, Byte.valueOf(compound.getByte("BatFlags")));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setByte("BatFlags", ((Byte)this.dataManager.get(HANGING)).byteValue());
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere() {
    	return super.getCanSpawnHere();
    }

    public float getEyeHeight() {
        return this.height / 2.0F;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_BAT;
    }

}
