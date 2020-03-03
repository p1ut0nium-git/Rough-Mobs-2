package com.p1ut0nium.roughmobsrevamped.entities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBoss {
	
	World getEntityWorld();
	AxisAlignedBB getEntityBoundingBox();
	NBTTagCompound getEntityData();
	
	void onLivingUpdate();
	void onAddedToWorld();
	void onDeath(DamageSource cause);
	
	void playSound(SoundEvent soundIn, float volume, float pitch);

	String getName();
	String getCustomNameTag();
	void setCustomNameTag(String name);
	float getMaxHealth();
	BlockPos getPosition();
	void setPosition(double x, double y, double z);
	// TODO void setBossColorTheme(String[] bossColorTheme);
	// TODO double[] getBossColorTheme();
}
