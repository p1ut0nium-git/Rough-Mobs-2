package com.p1ut0nium.roughmobsrevamped.entities;

import javax.annotation.Nullable;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;

public interface IBoss {
	IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata);
	void onLivingUpdate();
}
