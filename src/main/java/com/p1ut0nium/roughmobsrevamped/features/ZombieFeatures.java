package com.p1ut0nium.roughmobsrevamped.features;

import com.p1ut0nium.roughmobsrevamped.misc.BossHelper.BossApplier;
import com.p1ut0nium.roughmobsrevamped.misc.Constants;
import com.p1ut0nium.roughmobsrevamped.misc.EquipHelper.EquipmentApplier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class ZombieFeatures extends EntityFeatures {
	
	private EquipmentApplier equipApplier;
	private BossApplier bossApplier;
	
	public ZombieFeatures() {
		// Sends name and list of classes back to EntityFeatures
	
		super("zombie", ZombieEntity.class, ZombieVillagerEntity.class, HuskEntity.class, ZombiePigmanEntity.class);
	}
	
	@Override
	public void preInit() {
		super.preInit();
		equipApplier = new EquipmentApplier(name, 4, 4, 8);
		bossApplier = new BossApplier(name, 0.2F, new String[]{"Zombie King", "Flesh King", "Dr. Zomboss", "Azog", "Zon-Goku", "Amy", "Z0mb3y"}) {
			@Override
			public void addBossFeatures(LivingEntity entity) {
				//TODO: Baby Zombies
			}
		};
	}
	
	@Override
	public void postInit() {
		super.postInit();
		equipApplier.createPools();
		bossApplier.postInit();
	}
	
	@Override
	public void initConfig() {
		super.initConfig();

		equipApplier.initConfig(
			Constants.DEFAULT_MAINHAND,
			Constants.DEFAULT_OFFHAND,
			Constants.DEFAULT_HELMETS,
			Constants.DEFAULT_CHESTPLATES,
			Constants.DEFAULT_LEGGINGS,
			Constants.DEFAULT_BOOTS
		);

		bossApplier.initConfig();
	}
	
	@Override
	public void addFeatures(EntityJoinWorldEvent event, Entity entity) {
		
		if (!(entity instanceof LivingEntity))
			return;

		// Test to see if mob is a boss
		boolean isBoss = bossApplier.trySetBoss((LivingEntity) entity);
		
		// If mob is not a boss, use normal equipment
		if (!isBoss) {
			equipApplier.equipEntity((LivingEntity) entity);
		}
	}
}
