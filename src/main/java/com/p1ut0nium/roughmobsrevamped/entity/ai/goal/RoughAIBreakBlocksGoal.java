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
package com.p1ut0nium.roughmobsrevamped.entity.ai.goal;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class RoughAIBreakBlocksGoal extends Goal {
	
	public static final String BANS = Constants.unique("breakbans");
	public static final String LONG_POS = Constants.unique("breakpos");
	
	protected CreatureEntity breaker;
	protected int range;
	protected World world;
	protected List<Block> allowedBlocks;
	protected BlockPos target;
	protected int breakingTime;
	protected int previousBreakProgress = -1;
	protected Block block;
	protected int neededTime;
	protected int idleTime;
	protected int curDistance;

	public RoughAIBreakBlocksGoal(LivingEntity breaker, int range, List<Block> allowedBlocks) {
		this.breaker = (CreatureEntity) breaker;
		this.range = range;
		this.world = breaker.world;
		this.allowedBlocks = allowedBlocks;
	    this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.TARGET));
		// TODO setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {

		if (!world.getGameRules().getBoolean(GameRules.MOB_GRIEFING) || this.breaker.getAttackTarget() != null)
			return false;
		
		BlockPos target = getFirstTarget();
		if (target != null) 
		{
			this.target = target;
			return true;
		}
        
        return false;
	}
	
	@Override
	public void startExecuting() {
		
		this.breakingTime = 0;
		this.idleTime = 0;
		this.block = world.getBlockState(target).getBlock();
		this.neededTime = getBreakSpeed(); 
	}
	
	private int getBreakSpeed() {

		float multiplier = 100;
		ItemStack held = breaker.getHeldItemMainhand();
		BlockState state = world.getBlockState(target);
		
		if (held != null && held.getItem() instanceof ToolItem) 
		{
			multiplier -= ((ToolItem)held.getItem()).getDestroySpeed(held, state)*10;
		}
		
		return (int)(state.getBlockHardness(world, target) * Math.max(0, multiplier) + 10);
	}

	@Override
	public void tick() {
		
		this.breaker.getNavigator().setPath(breaker.getNavigator().tryMoveToXYZ(x, y, z, speedIn)(target), 1);
		
		Entity breakerEntity = breaker;
		while (breakerEntity.isPassenger()) {
			breakerEntity = breakerEntity.getRidingEntity();
		}
		
		++this.idleTime;
		
		double distance = this.target.distanceSq(breakerEntity.posX, breakerEntity.posY, breakerEntity.posZ, false);
		
		if ((int)Math.round(distance) == curDistance)
			this.idleTime++;
		else
			this.idleTime = 0;
		
		if (this.idleTime >= 160)
			banishTarget();
		
		curDistance = (int) Math.round(distance);
		
		if (distance <= 2 && this.target != null) 
		{
	        ++this.breakingTime;
	        int i = (int)((float)this.breakingTime / (float)this.neededTime * 10.0F);

	        if (i != this.previousBreakProgress)
	        {
	            this.world.sendBlockBreakProgress(this.breaker.getEntityId(), this.target, i);
	            this.previousBreakProgress = i;
	        }

	        if (this.breakingTime >= this.neededTime) {

	            // TODO - old - this.world.setBlockToAir(this.target);
	        	// new method - this.world.setBlockState(this.target, Blocks.AIR.getDefaultState());
	            // new method - this.world.playEvent(2001, this.target, Block.getStateId(this.world.getBlockState(this.target));

	        	// Or maybe this works?
	        	this.world.destroyBlock(this.target, true);

	            this.breakingTime = 0;
	        }
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		return target != null && (!(this.breaker instanceof CreatureEntity) || ((CreatureEntity)this.breaker).isWithinHomeDistanceFromPosition(target)) && world.getBlockState(target).getBlock() == this.block && this.breaker.getAttackTarget() == null;
    }
	
	@Override
	public void resetTask() {
		this.breaker.getNavigator().clearPath();
	}
	
	private BlockPos getFirstTarget() {

		List<BlockPos> positions = new ArrayList<BlockPos>();
		BlockPos curPos, target = null;
		double distance, bestDistance = Double.MAX_VALUE;
		
		Entity breakerEntity = breaker;
		while (breakerEntity.isPassenger()) 
		{
			breakerEntity = breakerEntity.getRidingEntity();
		}
		
		for (int i = -range; i <= range; i++) 
		{
			for (int j = -range; j <= range; j++) 
			{
				for (int k = -range; k <= range; k++) 
				{
					curPos = new BlockPos(breakerEntity.posX + i, breakerEntity.posY + j, breakerEntity.posZ + k);
					Block block = world.getBlockState(curPos).getBlock();
					
					if (block instanceof SpawnerBlock)
						return null;
					
					if (allowedBlocks.contains(block) && !isBanned(curPos)) 
					{
						positions.add(curPos);
					}
				}
			}
		}

		for (BlockPos pos : positions)
		{
			distance = pos.distanceSq(breakerEntity.posX, breakerEntity.posY, breakerEntity.posZ, false);
			if (distance < bestDistance) 
			{
				target = pos;
				bestDistance = distance;
			}
		}
		
		return target;
	}
	
	private void banishTarget() {
		
		if (breaker.getEntityData().(BANS) == null)
			breaker.getEntityData().setTag(BANS, new ListNBT());
		
		CompoundNBT comp = new CompoundNBT();
		comp.putLong(LONG_POS, target.toLong());
		
		((ListNBT)breaker.getEntityData().getTag(BANS)).appendTag(comp);
		
		this.target = null;
		resetTask();
	}

	private boolean isBanned(BlockPos pos) {
		
		long l = pos.toLong();
		ListNBT list = (ListNBT) breaker.getPersistentData().getString(BANS);
		
		if (list == null)
			return false;
		
		for (int i = 0; i < list.tagCount(); i++)
		{
			if (list.getCompound(i).getLong(LONG_POS) == l)
				return true;
		}

		return false;
	}
}
