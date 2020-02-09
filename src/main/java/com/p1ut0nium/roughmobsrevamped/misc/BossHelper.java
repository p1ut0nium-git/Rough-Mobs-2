package com.p1ut0nium.roughmobsrevamped.misc;

import java.util.Random;
import com.p1ut0nium.roughmobsrevamped.misc.EquipHelper.EquipmentApplier;
import net.minecraft.entity.LivingEntity;

public class BossHelper {
	
	public static final Random RND = new Random();
	public static final String ISBOSS = Constants.unique("isBoss");
	
	public static abstract class BossApplier {
		
		private EquipmentApplier equipApplier;
		
		private final String name;
		private final float defaultDropChance;
		private final String[] defaultBossNames;
		
		private int bossChance;
		private String[] bossNames;
		
		public BossApplier(String name, float defaultDropChance, String[] defaultBossNames) {
			
			this.name = name;
			this.defaultDropChance = defaultDropChance;
			this.defaultBossNames = defaultBossNames;
			
			equipApplier = new EquipmentApplier(this.name + " boss", 4, 4, this.defaultDropChance);
		}	
		
		public void initConfig() {
			equipApplier.initConfig(Constants.DEFAULT_BOSS_MAINHAND, 
									Constants.DEFAULT_BOSS_OFFHAND, 
									Constants.DEFAULT_BOSS_HELMETS, 
									Constants.DEFAULT_BOSS_CHESTPLATES, 
									Constants.DEFAULT_BOSS_LEGGINGS, 
									Constants.DEFAULT_BOSS_BOOTS);
			
			bossChance = 100;
			bossNames = defaultBossNames;
		}

		public void postInit() {
			equipApplier.createPools();
		}
		
		public boolean trySetBoss(LivingEntity entity) {
			
			if (bossChance <= 0 || RND.nextInt(bossChance) != 0)
				return false;
			
			//final DataParameter<Boolean> ISBOSS = entity.getDataManager().createKey(LivingEntity.class, DataSerializers.BOOLEAN);
			//entity.getDataManager().register(ISBOSS, true);
			
			equipApplier.equipEntity(entity);
			
			//TODO Set Boss Name Tag
			//ITextComponent bossNameTag = null;
			//bossNameTag.appendText(bossNames[RND.nextInt(bossNames.length)]);
			//entity.setCustomName(bossNameTag);
			
			return true;
		}
		
		public abstract void addBossFeatures(LivingEntity entity);
	}
}
