package com.p1ut0nium.roughmobsrevamped.entities;

import net.minecraft.util.DamageSource;

public interface IBoss {
	
	void onLivingUpdate();
	void onAddedToWorld();
	void onDeath(DamageSource cause);

	// TODO void setBossColorTheme(String[] bossColorTheme);
	// TODO double[] getBossColorTheme();
}
