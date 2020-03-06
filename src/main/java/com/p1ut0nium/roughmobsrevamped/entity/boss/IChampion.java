package com.p1ut0nium.roughmobsrevamped.entity.boss;

import net.minecraft.util.DamageSource;

public interface IChampion {
	
	void onLivingUpdate();
	void onAddedToWorld();
	void onDeath(DamageSource cause);

	// TODO void setBossColorTheme(String[] bossColorTheme);
	// TODO double[] getBossColorTheme();
}
