package com.p1ut0nium.roughmobsrevamped.ai.misc;

import java.util.Random;

import com.p1ut0nium.roughmobsrevamped.entities.EntityHostileBat;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class RoughAIBatFlight extends EntityAIBase {

	private final int BAT_OWNER_FOLLOW_Y_OFFSET = 3;
	
	private final EntityHostileBat hostileBat;
	private EntityLiving boss;
	private BlockPos currentFlightTarget;
	private Random rnd;
	
	public RoughAIBatFlight(EntityHostileBat hostileBat) {
		this.hostileBat = hostileBat;
		rnd = hostileBat.getRNG();
	}

	@Override
	public boolean shouldExecute() {
		return true;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return true;
	}
	
	@Override
	public void startExecuting() {
		this.boss = hostileBat.getBoss();
		super.startExecuting();
	}
	
	@Override
	public void resetTask() {
		super.resetTask();
	}
	
	@Override
	public void updateTask() {
		updateFlightTarget();
		performFlightMovement();
		super.updateTask();
	}
	
	private void updateFlightTarget() {
        // target invalid or no free block
        if (currentFlightTarget != null && !(hostileBat.getAttackTarget() instanceof EntityPlayer)
                && (!hostileBat.world.isAirBlock(currentFlightTarget) || currentFlightTarget.getY() < 1)) {
            currentFlightTarget = null;
        }

        // finding a new target, randomly
        if (currentFlightTarget == null || rnd.nextInt(30) == 0
                || currentFlightTarget.distanceSq(hostileBat.posX, hostileBat.posY, hostileBat.posZ) < 4.0F) {
            currentFlightTarget = getRandomFlightCoordinates();
        }
	}

	private void performFlightMovement() {
        double diffX, diffY, diffZ;
        if (hostileBat.getAttackTarget() == null || !hostileBat.getAttackTarget().isEntityAlive()) {
            if (currentFlightTarget != null) {
                // go for ChunkCoords flight target!
                diffX = (double) currentFlightTarget.getX() + 0.5D - hostileBat.posX;
                diffY = (double) currentFlightTarget.getY() + 0.1D - hostileBat.posY;
                diffZ = (double) currentFlightTarget.getZ() + 0.5D - hostileBat.posZ;
            } else {
                diffX = diffY = diffZ = 0D;
            }
        }
        else {
            // Attack the target!
            diffX = hostileBat.getAttackTarget().posX - hostileBat.posX;
            diffY = hostileBat.getAttackTarget().posY - hostileBat.posY + 1.5D;
            diffZ = hostileBat.getAttackTarget().posZ - hostileBat.posZ;
        }

        hostileBat.motionX += (Math.signum(diffX) * 0.5D - hostileBat.motionX) * 0.1D;
        hostileBat.motionY += (Math.signum(diffY) * 0.7D - hostileBat.motionY) * 0.1D;
        hostileBat.motionZ += (Math.signum(diffZ) * 0.5D - hostileBat.motionZ) * 0.1D;
        float var7 = (float) (Math.atan2(hostileBat.motionZ, hostileBat.motionX) * 180.0D / Math.PI) - 90.0F;
        float var8 = MathHelper.wrapDegrees(var7 - hostileBat.rotationYaw);
        hostileBat.setMoveForward(0.5F);
        hostileBat.rotationYaw += var8;
    }

    private BlockPos getRandomFlightCoordinates() {
        int x = 0;
        int y = 0;
        int z = 0;

        Vec3d orig;
        Vec3d dest;
        RayTraceResult RayTraceResult;
        
        // Follow the boss around when a player is not in range
        if (boss != null) {
	        for (int i = 0; i < 10; i++) {
	            x = boss.getPosition().getX() + rnd.nextInt(7) - rnd.nextInt(7);
	            y = boss.getPosition().getY() + rnd.nextInt(6) - 2 + BAT_OWNER_FOLLOW_Y_OFFSET;
	            z = boss.getPosition().getZ() + rnd.nextInt(7) - rnd.nextInt(7);
	
	            orig = new Vec3d(hostileBat.posX, hostileBat.posY, hostileBat.posZ);
	            dest = new Vec3d(x + 0.5D, y + 0.5D, z + 0.5D);
	            RayTraceResult = hostileBat.world.rayTraceBlocks(orig, dest, false, true, false);
	            if (RayTraceResult == null) { // No collision detected - path is free to traverse
	                break;
	            }
	        }
        }

        return new BlockPos(x, y, z);
    }
}
