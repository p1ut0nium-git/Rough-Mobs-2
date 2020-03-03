package com.p1ut0nium.roughmobsrevamped.misc;

import java.util.ArrayList;
import java.util.List;

import com.p1ut0nium.roughmobsrevamped.RoughMobs;
import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class TargetHelper {
	
	public static final String CATEGORY = "targetBlocker";
	private static boolean enableTargetBlock;
	private static final List<TargetEntry> BlockerList = new ArrayList<TargetEntry>();
	
	public static boolean enableTargetAlways;
	public static boolean ignoreSpawnConditions;
	private static final List<TargetEntry> AttackerList = new ArrayList<TargetEntry>();
	
	public static void init() {
		
		RoughConfig.getConfig().addCustomCategoryComment(CATEGORY, "Entities which can't be targeted by other entities."
																+ "\ne.g. Skeletons can't target other Skeletons by shooting them accidentally"
																+ "\nTakes 2 arguments divided by a semicolon per entry. victim;attacker"
																+ "\nvictim: The entity which should not be targeted if attacked by the attacker (entity name)"
																+ "\nattacker: the attacker entity which can't target the victim (entity name)"
																+ "\nUse \"*\" instead of the victim or attacker if you want this for all entities except players");
		String[] blockers = RoughConfig.getStringArray(CATEGORY, "_List", Constants.DEFAULT_TARGET_BLOCKER, "");
		enableTargetBlock = RoughConfig.getBoolean(CATEGORY, "_Enabled", false, "Set to true to enable the target blocker feature.");
		
		fillList(blockers, "targetblocker");
		
		RoughConfig.getConfig().addCustomCategoryComment("targetAttacker", "Entities always attack these targets."
																+ "\ne.g. Zombies always attack pigs."
																+ "\nTakes 2 arguments divided by a semicolon per entry. victim;attacker"
																+ "\nvictim: The entity which should be targeted (entity name)"
																+ "\nattacker: the attacker entity (entity name)"
																+ "\nMultiple entires for the same victim or attacker are allowed"
																+ "\nUse \"*\" instead of the victim or attacker if you want this for all entities except players");
		
		String[] attackers = RoughConfig.getStringArray("targetAttacker", "_List", Constants.DEFAULT_TARGETS, "");
		enableTargetAlways = RoughConfig.getBoolean("targetAttacker", "_Enabled", false, "Set to true to enable the target always feature.");
		ignoreSpawnConditions = RoughConfig.getBoolean("targetAttacker", "_IgnoreSpawnConditions", true, "Disable to require spawn conditions be met in order for target attacker feature to work.");
		
		fillList(attackers, "targetattacker");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void fillList(String[] options, String listType) {
		
		for (String option : options) 
		{
			String[] split = option.split(";");
			if (split.length >= 2) 
			{
				Class[] entities = new Class[2];
				boolean success = true;
				for (int i = 0; i < 2; i++)
				{
					if (split[i].trim().equals("*"))
					{
						entities[i] = Entity.class;
					}
					else
					{
						Class<? extends Entity> clazz = EntityList.getClass(new ResourceLocation(split[i].trim()));
						if (clazz == null)
						{
							if (listType == "targetblock")
							RoughMobs.logError(listType + ": \"" + split[1] + "\" is not a valid entity!");
							success = false;
							break;
						}
						entities[i] = clazz;
					}
				}
				
				if (success)
					if (listType == "targetblocker")
						BlockerList.add(new TargetEntry(entities[1], entities[0]));
					else if (listType == "targetattacker")
						AttackerList.add(new TargetEntry(entities[1], entities[0]));
			}
			else
				RoughMobs.logError(listType + ": each option needs at least 2 arguments! (" + option + ")");
		}
	}

	public static Class<? extends Entity> getBlockerEntityForTarget(Entity target) {

		for (TargetEntry entry : BlockerList) 
		{
			if (target.getClass().equals(entry.getTargetClass()))
				return entry.getAttackerClass();
		}
		
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setTargets(Entity attacker) {
		for (TargetEntry entry : AttackerList) {
			if (attacker.getClass().equals(entry.getAttackerClass()))
				((EntityLiving)attacker).targetTasks.addTask(1, new EntityAINearestAttackableTarget((EntityCreature) attacker, entry.getTargetClass(), true));
		}
	}
	
	public static boolean targetBlockerEnabled() {
		return enableTargetBlock;
	}
	
	public static boolean targetAttackerEnabled() {
		return enableTargetAlways;
	}
	
	static class TargetEntry {
		
		private final Class<? extends Entity> attackerClass;
		private final Class<? extends Entity> targetClass;
		
		public TargetEntry(Class<? extends Entity> attackerClass, Class<? extends Entity> targetClass) {
			this.attackerClass = attackerClass;
			this.targetClass = targetClass;
		}
		
		public Class<? extends Entity> getAttackerClass() {
			return attackerClass;
		}
		
		public Class<? extends Entity> getTargetClass() {
			return targetClass;
		}
	}
}
