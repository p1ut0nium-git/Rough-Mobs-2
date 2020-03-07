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
package com.p1ut0nium.roughmobsrevamped.entity.boss;

import net.minecraft.util.DamageSource;

public interface IChampion {
	
	void livingTick();
	void onAddedToWorld();
	void onDeath(DamageSource cause);

	// TODO void setBossColorTheme(String[] bossColorTheme);
	// TODO double[] getBossColorTheme();
}
