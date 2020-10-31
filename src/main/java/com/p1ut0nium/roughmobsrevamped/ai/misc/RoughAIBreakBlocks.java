package com.p1ut0nium.roughmobsrevamped.ai.misc;

import com.p1ut0nium.roughmobsrevamped.util.Constants;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RoughAIBreakBlocks extends EntityAIBase {

    public static final String BANS = Constants.unique("breakbans");
    public static final String LONG_POS = Constants.unique("breakpos");

    protected EntityLiving breaker;
    protected int range;
    protected World world;
    protected Set<Block> allowedBlocks;
    protected BlockPos target;
    protected int breakingTime;
    protected int previousBreakProgress = -1;
    protected Block block;
    protected int neededTime;
    protected int idleTime;
    protected int curDistance;

    protected static Map<BlockPos, Boolean> blockCache = new HashMap<>();

    public RoughAIBreakBlocks(EntityLiving breaker, int range, Set<Block> allowedBlocks) {
        this.breaker = breaker;
        this.range = range;
        this.world = breaker.world;
        this.allowedBlocks = allowedBlocks;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {

        if (!world.getGameRules().getBoolean("mobGriefing") || this.breaker.getAttackTarget() != null) { return false; }

        BlockPos target = getFirstTarget();
        if (target != null) {
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
        this.breaker.getNavigator().setPath(this.breaker.getNavigator().getPathToPos(target), 1);
    }

    private int getBreakSpeed() {

        float multiplier = 100;
        ItemStack held = breaker.getHeldItemMainhand();
        IBlockState state = world.getBlockState(target);

        if (held != null && held.getItem() instanceof ItemTool) {
            multiplier -= held.getItem().getDestroySpeed(held, state) * 10;
        }

        return (int) (state.getBlockHardness(world, target) * Math.max(0, multiplier) + 10);
    }

    @Override
    public void updateTask() {

        Entity breakerEntity = breaker;
        while (breakerEntity.isRiding()) {
            breakerEntity = breakerEntity.getRidingEntity();
        }

        ++this.idleTime;

        double distance = this.target.distanceSq(breakerEntity.posX, breakerEntity.posY, breakerEntity.posZ);

        if ((int) Math.round(distance) == curDistance) { this.idleTime++; } else { this.idleTime = 0; }

        if (this.idleTime >= 160) { banishTarget(); }

        curDistance = (int) Math.round(distance);

        if (this.target != null) {
            if (distance <= 2) {
                ++this.breakingTime;
                int i = (int) ((float) this.breakingTime / (float) this.neededTime * 10.0F);

                if (i != this.previousBreakProgress) {
                    this.world.sendBlockBreakProgress(this.breaker.getEntityId(), this.target, i);
                    this.previousBreakProgress = i;
                }

                if (this.breakingTime >= this.neededTime) {
                    this.world.setBlockToAir(this.target);
                    this.world.playEvent(2001, this.target, Block.getIdFromBlock(this.block));
                    this.breakingTime = 0;
                }
            } else if (world.getWorldTime() % 40 == 0) {
                this.breaker.getNavigator().setPath(this.breaker.getNavigator().getPathToPos(target), 1);
            }
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return target != null && (!(this.breaker instanceof EntityCreature) || ((EntityCreature) this.breaker)
            .isWithinHomeDistanceFromPosition(target)) && world.getBlockState(target).getBlock() == this.block
            && this.breaker.getAttackTarget() == null;
    }

    @Override
    public void resetTask() {
        this.breaker.getNavigator().clearPath();
    }

    private BlockPos getFirstTarget() {

        List<BlockPos> positions = new ArrayList<>();
        BlockPos curPos, target = null;

        Entity breakerEntity = breaker;
        while (breakerEntity.isRiding()) {
            breakerEntity = breakerEntity.getRidingEntity();
        }
        int j, k;
        for (int i = -range; i <= range; i++) {
            for (j = -range; j <= range; j++) {
                for (k = -range; k <= range; k++) {
                    curPos = new BlockPos(breakerEntity.posX + i, breakerEntity.posY + j, breakerEntity.posZ + k);
                    if (!blockCache.containsKey(curPos) || blockCache.get(curPos)) {
                        Block block = world.getBlockState(curPos).getBlock();

                        if (block instanceof BlockMobSpawner) { return null; }

                        if (blockCache.size() >= 256) blockCache.clear();

                        if (allowedBlocks.contains(block) && !isBanned(curPos)) {
                            positions.add(curPos);
                            blockCache.put(curPos, true);
                        } else {
                            blockCache.put(curPos, false);
                        }
                    }
                }
            }
        }

        Entity finalBreakerEntity = breakerEntity;

        target = positions.stream()
            .min(Comparator.comparingDouble(blockPos -> blockPos.distanceSq(finalBreakerEntity.getPosition())))
            .orElse(null);

        return target;
    }

    private void banishTarget() {

        if (breaker.getEntityData().getTag(BANS) == null) { breaker.getEntityData().setTag(BANS, new NBTTagList()); }

        NBTTagCompound comp = new NBTTagCompound();
        comp.setLong(LONG_POS, target.toLong());

        ((NBTTagList) breaker.getEntityData().getTag(BANS)).appendTag(comp);

        this.target = null;
        resetTask();
    }

    private boolean isBanned(BlockPos pos) {

        long l = pos.toLong();
        NBTTagList list = (NBTTagList) breaker.getEntityData().getTag(BANS);

        if (list == null) { return false; }

        for (int i = 0; i < list.tagCount(); i++) {
            if (list.getCompoundTagAt(i).getLong(LONG_POS) == l) { return true; }
        }

        return false;
    }
}
