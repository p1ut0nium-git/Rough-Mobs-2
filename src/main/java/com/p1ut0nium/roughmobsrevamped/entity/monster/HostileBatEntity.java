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
package com.p1ut0nium.roughmobsrevamped.entity.monster;

import javax.annotation.Nullable;

import com.p1ut0nium.roughmobsrevamped.entity.boss.ZombieChampionEntity;
import com.p1ut0nium.roughmobsrevamped.misc.BossHelper;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class HostileBatEntity extends MonsterEntity {
	private static final DataParameter<Byte> HANGING = EntityDataManager.createKey(BatEntity.class, DataSerializers.BYTE);
	private static final EntityPredicate field_213813_c = (new EntityPredicate()).setDistance(4.0D).allowFriendlyFire();
	private BlockPos spawnPosition;
	
	private static double BAT_DAMAGE = BossHelper.bossBatSwarmDamage;
	private static double BAT_HEALTH = BossHelper.bossBatSwarmHealth;

    private LivingEntity boss;

	public HostileBatEntity(EntityType<? extends HostileBatEntity> type, final World worldIn) {
        super(type, worldIn);
        //this.setSize(0.5F, 0.9F);
        this.setIsBatHanging(false);
        this.experienceValue = 0;
        
        // TODO AI
        //tasks.addTask(1, new RoughAIBatAttack(this));
        //tasks.addTask(2, new RoughAIBatFlight(this));
		//targetTasks.addTask(1, new RoughAIBatTarget(this));
	}

	protected void registerGoals() {
	}
	
	protected void applyEntityAI() {	
	}
	
	protected void registerAttributes() {
		super.registerAttributes();
		
    	this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(BAT_DAMAGE);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(BAT_HEALTH);
	}
	
	/**
	 * Called on the logical server to get a packet to send to the client containing data necessary to spawn your entity.
	 * Using Forge's method instead of the default vanilla one allows extra stuff to work such as sending extra data,
	 * using a non-default entity factory and having {@link IEntityAdditionalSpawnData} work.
	 *
	 * It is not actually necessary for our WildBoarEntity to use Forge's method as it doesn't need any of this extra
	 * functionality, however, this is an example mod and many modders are unaware that Forge's method exists.
	 *
	 * @return The packet with data about your entity
	 * @see FMLPlayMessages.SpawnEntity
	 */
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	protected void registerData() {
		super.registerData();
		this.dataManager.register(HANGING, (byte)0);
	}
    
    protected int getExperiencePoints(PlayerEntity player) {
        return super.getExperiencePoints(player);
    }

	public void setBoss(ZombieChampionEntity boss) {
		this.boss = boss;
	}
	
	public LivingEntity getBoss() {
		return this.boss;
	}
	
	protected boolean canDespawn() {
		return true;
	}

    protected float getSoundVolume() {
        return 0.1F;
    }

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

    public boolean canBePushed() {
        return false;
    }

    protected void collideWithEntity(Entity entityIn) {
    }

    protected void collideWithNearbyEntities() {
    }

    public boolean getIsBatHanging() {
    	return (this.dataManager.get(HANGING) & 1) != 0;
    }

    public void setIsBatHanging(boolean isHanging) {
    	byte b0 = this.dataManager.get(HANGING);
        if (isHanging) {
           this.dataManager.set(HANGING, (byte)(b0 | 1));
        } else {
           this.dataManager.set(HANGING, (byte)(b0 & -2));
        }
    }

    public void tick() {
    	super.tick();
        if (this.getIsBatHanging()) {
        	this.setMotion(Vec3d.ZERO);
            this.posY = (double)MathHelper.floor(this.posY) + 1.0D - (double)this.getHeight();
        } else {
        	this.setMotion(this.getMotion().mul(1.0D, 0.6D, 1.0D));
        }
    }

    protected void updateAITasks() {
        super.updateAITasks();
        BlockPos blockpos = new BlockPos(this);
        BlockPos blockpos1 = blockpos.up();
        if (this.getIsBatHanging()) {
           if (this.world.getBlockState(blockpos1).isNormalCube(this.world, blockpos)) {
              if (this.rand.nextInt(200) == 0) {
                 this.rotationYawHead = (float)this.rand.nextInt(360);
              }

              if (this.world.getClosestPlayer(field_213813_c, this) != null) {
                 this.setIsBatHanging(false);
                 this.world.playEvent((PlayerEntity)null, 1025, blockpos, 0);
              }
           } else {
              this.setIsBatHanging(false);
              this.world.playEvent((PlayerEntity)null, 1025, blockpos, 0);
           }
        } else {
           if (this.spawnPosition != null && (!this.world.isAirBlock(this.spawnPosition) || this.spawnPosition.getY() < 1)) {
              this.spawnPosition = null;
           }

           if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.withinDistance(this.getPositionVec(), 2.0D)) {
              this.spawnPosition = new BlockPos(this.posX + (double)this.rand.nextInt(7) - (double)this.rand.nextInt(7), this.posY + (double)this.rand.nextInt(6) - 2.0D, this.posZ + (double)this.rand.nextInt(7) - (double)this.rand.nextInt(7));
           }

           double d0 = (double)this.spawnPosition.getX() + 0.5D - this.posX;
           double d1 = (double)this.spawnPosition.getY() + 0.1D - this.posY;
           double d2 = (double)this.spawnPosition.getZ() + 0.5D - this.posZ;
           Vec3d vec3d = this.getMotion();
           Vec3d vec3d1 = vec3d.add((Math.signum(d0) * 0.5D - vec3d.x) * (double)0.1F, (Math.signum(d1) * (double)0.7F - vec3d.y) * (double)0.1F, (Math.signum(d2) * 0.5D - vec3d.z) * (double)0.1F);
           this.setMotion(vec3d1);
           float f = (float)(MathHelper.atan2(vec3d1.z, vec3d1.x) * (double)(180F / (float)Math.PI)) - 90.0F;
           float f1 = MathHelper.wrapDegrees(f - this.rotationYaw);
           this.moveForward = 0.5F;
           this.rotationYaw += f1;
           if (this.rand.nextInt(100) == 0 && this.world.getBlockState(blockpos1).isNormalCube(this.world, blockpos1)) {
              this.setIsBatHanging(true);
           }
        }

    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public void fall(float distance, float damageMultiplier) {
    }

    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }
     
    public boolean attackEntityFrom(DamageSource source, float amount) {
    	if (this.isInvulnerableTo(source)) {
    		return false;
        } else {
            if (!this.world.isRemote && this.getIsBatHanging()) {
                this.setIsBatHanging(false);
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    public void readAdditional(CompoundNBT compound) {
    	super.readAdditional(compound);
        this.dataManager.set(HANGING, compound.getByte("BatFlags"));
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putByte("BatFlags", this.dataManager.get(HANGING));
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height / 2.0F;
    }
}
