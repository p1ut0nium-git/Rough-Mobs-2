package com.p1ut0nium.roughmobsrevamped.entities;

import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;

public interface IBoss {
	void onLivingUpdate();
	void onAddedToWorld();
	void onDeath(DamageSource cause);
	BlockPos getPosition();
}
