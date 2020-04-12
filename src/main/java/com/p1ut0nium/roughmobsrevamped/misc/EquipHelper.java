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
package com.p1ut0nium.roughmobsrevamped.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.p1ut0nium.roughmobsrevamped.compat.GameStagesCompat;
import com.p1ut0nium.roughmobsrevamped.config.EquipmentConfig;
import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.core.RoughMobsRevamped;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class EquipHelper {
	
	private static final String KEY_APPLIED = Constants.unique("equipApplied");
	private static final Random RND = new Random();

	private static boolean playerHasEnchantStage;
	private static boolean enchantStageEnabled;
	
	public static class EquipmentApplier {
		
		private final String name;
		private final int chancePerWeaponDefault;
		private final int chancePerArmorDefault;
		private final int enchChanceDefault;
		private final float enchMultiplierDefault;
		private final float dropChanceDefault;
		
		private int chancePerWeapon;
		private int chancePerArmor;
		private int enchChance;
		private float enchMultiplier;
		private float dropChance;
		
		private EquipmentPool poolMainhand;
		private EquipmentPool poolOffhand;
		private EquipmentPool poolHelmet;
		private EquipmentPool poolChestplate;
		private EquipmentPool poolLeggings;
		private EquipmentPool poolBoots;

		private List<? extends String> equipMainhand;
		private List<? extends String> equipOffhand;
		private List<? extends String> equipHelmet;
		private List<? extends String> equipChestplate;
		private List<? extends String> equipLeggings;
		private List<? extends String> equipBoots;
		private List<? extends String> equipWeaponEnchants;
		private List<? extends String> equipArmorEnchants;
		
		public EquipmentApplier(String name, int chancePerWeaponDefault, int chancePerArmorDefault, int enchChanceDefault, float enchMultiplierDefault, float dropChanceDefault) {
			this.name = name;
			this.chancePerWeaponDefault = chancePerWeaponDefault;
			this.chancePerArmorDefault = chancePerArmorDefault;
			this.enchChanceDefault = enchChanceDefault;
			this.enchMultiplierDefault = enchMultiplierDefault;
			this.dropChanceDefault = dropChanceDefault;
		}
		
		public EquipmentPool getPoolMainhand() {
			return poolMainhand;
		}
		
		public EquipmentPool getPoolOffhand() {
			return poolOffhand;
		}
		
		public EquipmentPool getPoolHelmet() {
			return poolHelmet;
		}
		
		public EquipmentPool getPoolChestplate() {
			return poolChestplate;
		}
		
		public EquipmentPool getPoolLeggings() {
			return poolLeggings;
		}
		
		public EquipmentPool getPoolBoots() {
			return poolBoots;
		}
		
		public void setPoolMainhand(EquipmentPool poolMainhand) {
			this.poolMainhand = poolMainhand;
		}
		
		public void setPoolOffhand(EquipmentPool poolOffhand) {
			this.poolOffhand = poolOffhand;
		}
		
		public void setPoolHelmet(EquipmentPool poolHelmet) {
			this.poolHelmet = poolHelmet;
		}
		
		public void setPoolChestplate(EquipmentPool poolChestplate) {
			this.poolChestplate = poolChestplate;
		}
		
		public void setPoolLeggings(EquipmentPool poolLeggings) {
			this.poolLeggings = poolLeggings;
		}
		
		public void setPoolBoots(EquipmentPool poolBoots) {
			this.poolBoots = poolBoots;
		}
		
		public void equipEntity(MobEntity entity) {
			equipEntity(entity, false);
		}
		
		public void equipEntity(MobEntity boss, boolean isBoss) {
			
			if (boss == null || (boss.getPersistentData()).getBoolean(KEY_APPLIED) || !(boss instanceof MobEntity))
				return;
			
			// Get nearest player to the spawned mob
			PlayerEntity playerClosest = boss.world.getClosestPlayer(boss, -1.0D);
			
			// Get all Game Stage related info
			enchantStageEnabled = GameStagesCompat.useEnchantStage();			
			
			// Test to see if player has enchantment stage unlocked
			if (enchantStageEnabled) {
				playerHasEnchantStage = GameStageHelper.hasAnyOf((ServerPlayerEntity) playerClosest, Constants.ROUGHMOBSALL, Constants.ROUGHMOBSENCHANT);
			} else {
				playerHasEnchantStage = false;
			}
			
			EquipmentPool[] pools = new EquipmentPool[] {
				poolMainhand, poolOffhand, poolBoots, poolLeggings, poolChestplate, poolHelmet
			};
			
			// If getChance succeeds, then equip entity with complete set of armor
			boolean completeArmorSet;
			
			if (isBoss)
				completeArmorSet = true;
			else
				completeArmorSet = false;
			
			// Attempt to add weapons and armor)
			for (int i = 0; i < pools.length; i++) {
				EquipmentPool pool = pools[i];
				EquipmentSlotType slot = EquipmentSlotType.values()[i];
				
				// For slots 0 and 1, use chancePerWeapon, for all others, use chancePerPiece
				int chance = i <= 1 ? chancePerWeapon : chancePerArmor;
				
				// Test for each weapon and each piece of armor, or if entity should have complete armor set
				if (getChance(boss, chance) || (completeArmorSet && i > 1)) {
					ItemStack stack = pool.getRandom(boss, enchChance, enchMultiplier);
					
					if (stack != null) {
						boss.setItemStackToSlot(slot, stack);
						((MobEntity)boss).setDropChance(slot, dropChance);
					}
				}
			}
			
			(boss.getPersistentData()).putBoolean(KEY_APPLIED, true);
		}

		public void initConfig(boolean isBoss, boolean useDefaultValues) {

			String formatName = name.toLowerCase().replace(" ", "") + "Equipment";
			
			// TODO make this work for both skeletons and zombies without duplication of code			
			if (useDefaultValues) {
				chancePerWeapon = chancePerWeaponDefault;
				chancePerArmor= chancePerArmorDefault;
				enchChance = enchChanceDefault;
				enchMultiplier = enchMultiplierDefault;
				dropChance = dropChanceDefault;
			} else {
				chancePerWeapon = RoughConfig.chancePerWeapon;
				chancePerArmor = RoughConfig.chancePerArmor;
				enchChance = RoughConfig.chancePerEnchantment;
				enchMultiplier = RoughConfig.enchantMultiplier;
				dropChance = RoughConfig.dropChance;
			}

			// TODO make this work for isBoss
			equipMainhand = RoughConfig.equipMainhand;
			equipOffhand = RoughConfig.equipOffhand;
			equipHelmet = RoughConfig.equipHelmet;
			equipChestplate = RoughConfig.equipChestplate;
			equipLeggings = RoughConfig.equipLeggings;
			equipBoots = RoughConfig.equipBoots;
			
			equipWeaponEnchants = RoughConfig.equipWeaponEnchants;
			equipArmorEnchants = RoughConfig.equipArmorEnchants;
		}
		
		public void createPools() {
			
			setPoolMainhand(EquipmentPool.createEquipmentPool("mainhand", equipMainhand, equipWeaponEnchants));
			setPoolOffhand(EquipmentPool.createEquipmentPool("offhand", equipOffhand, equipWeaponEnchants));
			setPoolHelmet(EquipmentPool.createEquipmentPool("helmet", equipHelmet, equipArmorEnchants));
			setPoolChestplate(EquipmentPool.createEquipmentPool("chestplate", equipChestplate, equipArmorEnchants));
			setPoolLeggings(EquipmentPool.createEquipmentPool("leggings", equipLeggings, equipArmorEnchants));
			setPoolBoots(EquipmentPool.createEquipmentPool("boots", equipBoots, equipArmorEnchants));
		}
	}
	
	public static class EquipmentPool {
		
		public final EntryPool<ItemStack> ITEM_POOL = new EntryPool<ItemStack>();
		public final EntryPool<Enchantment> ENCHANTMENT_POOL = new EntryPool<Enchantment>();
		
		public static EquipmentPool createEquipmentPool(String name, List<? extends String> equipMainhand, List<? extends String> equipWeaponEnchants) {
			
			EquipmentPool pool = new EquipmentPool();
			
			List<String> errorItems = pool.addItemsFromNames(equipMainhand);
			if (!errorItems.isEmpty()) 
				RoughMobsRevamped.LOGGER.error(Constants.MODNAME + ": error on creating the " + name + " item pool! " + String.join(", ", errorItems));
			
			List<String> errorEnchants = pool.addEnchantmentsFromNames(equipWeaponEnchants);
			if (!errorEnchants.isEmpty()) 
				RoughMobsRevamped.LOGGER.error(Constants.MODNAME + ": error on creating the " + name + " enchantment pool! " + String.join(", ", errorEnchants));
			
			return pool;
		}
		
		public List<String> addEnchantmentsFromNames(List<? extends String> equipWeaponEnchants) {
			
			List<String> errors = new ArrayList<String>();
			for (String s : equipWeaponEnchants) {
				String error = addEnchantmentFromName(s);
				if (error != null)
					errors.add(error);
			}	
			
			return errors;
		}
		
		private String addEnchantmentFromName(String s) {
			
			String[] parts = s.split(";");
			
			if (parts.length >= 2) {
				try {
					// TODO Verify this replacement works - Enchantment ench = EnchantmentgetEnchantmentByLocation(parts[0]);
					Enchantment ench = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(parts[0]));
					int probability = Integer.parseInt(parts[1]);
					int dimension = parts.length > 2 ? Integer.parseInt(parts[2]) : Integer.MIN_VALUE;
					
					if (ench == null)
						return "Invalid enchantment: " + parts[0] + " in line: " + s;
					else	
						addEnchantment(ench, probability, dimension);
				}
				catch(NumberFormatException e) {
					return "Invalid numbers in line: " + s;
				}
			}
			else {
				return "Invalid format for line: \"" + s + "\" Please change to enchantment;probability;dimensionID";
			}
			
			return null;
		}

		public List<String> addItemsFromNames(List<? extends String> equipMainhand) {
			
			List<String> errors = new ArrayList<String>();
			for (String s : equipMainhand) {
				String error = addItemFromName(s);
				if (error != null)
					errors.add(error);
			}
			
			return errors;
		}
		
		private String addItemFromName(String s) {
			
			String[] parts = s.split(";");
			
			if (parts.length >= 2) {
				try {
					// TODO Verify this replacement works - Item item = REGISTRY.getObject(new ResourceLocation(parts[0]));
					Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parts[0]));
					int probability = Integer.parseInt(parts[1]);
					int dimension = parts.length >= 3 ? Integer.parseInt(parts[2]) : Integer.MIN_VALUE;
					// TODO meta currently not being used below
					int meta = parts.length >= 4 ? Integer.parseInt(parts[3]) : 0;
					String nbt = parts.length >= 5 ? parts[4] : "";
					
					if (item == null)
						return "Invalid item: " + parts[0] + " in line: " + s;
					else
						// TODO get meta data back? addItem(new ItemStack(item, 1, meta), probability, dimension, nbt);
						addItem(new ItemStack(item), probability, dimension, nbt);
				}
				catch(NumberFormatException e) {
					return "Invalid numbers in line: " + s;
				}
			}
			else {
				return "Invalid format for line: \"" + s + "\" Please change to item;probability;meta";
			}
			
			return null;
		}

		public void addItem(ItemStack stack, int probability, int dimension, String nbt) {

			ITEM_POOL.addEntry(stack, probability, dimension == Integer.MIN_VALUE ? "ALL" : dimension, nbt);
		}
		
		public void addEnchantment(Enchantment enchantment, int probability, int dimension) {
			ENCHANTMENT_POOL.addEntry(enchantment, probability, dimension == Integer.MIN_VALUE ? "ALL" : dimension);
		}
		
		public ItemStack getRandom(Entity entity, int enchChance, float levelMultiplier) {
			
			if (ITEM_POOL.POOL.isEmpty()) 
				return null;
			
			ItemStack randomStack = ITEM_POOL.getRandom(entity);
			
			// Test to see if player has Enchantment stage
			if (enchantStageEnabled == false || enchantStageEnabled && playerHasEnchantStage) {
				
				// Test to see if there are no items to be enchanted
				if(randomStack != null) {
					
					if (!ENCHANTMENT_POOL.POOL.isEmpty() && enchChance > 0 && RND.nextInt(enchChance) == 0) {
						
						// Test for enchantment based on time and distance from world spawn
						if (getChance(entity, enchChance)) {
							
							Enchantment ench = ENCHANTMENT_POOL.getRandom(entity, randomStack);
	
							// If a valid enchantment, then set the level and apply it to the item
							if (ench != null) {
								double maxLevel = Math.max(ench.getMinLevel(), Math.min(ench.getMaxLevel(), Math.round(ench.getMaxLevel() * levelMultiplier)));
								int level = (int)Math.round(maxLevel * (0.5 + Math.random()/2));
								
								if (!randomStack.isEnchanted()) {
									randomStack.addEnchantment(ench, level);
								}
							}
						}
					}
				}
			}
			
			return randomStack;
		}
	}
	
	public static class EntryPool<T> {

		public final Map<T, Object[]> POOL = new HashMap<T, Object[]>();
		private List<T> entries = null;
		
		public void addEntry(T t, Object... data) {
			POOL.put(t, data);
		}
		
		public T getRandom(Entity entity) {
			return getRandom(entity, null);
		}
		
		public T getRandom(Entity entity, ItemStack item) {
			
			// Create a new valid entry pool for every entity spawn
			entries = new ArrayList<T>();
			
			// Loop through each entity in the POOL
			for (Entry<T, Object[]> entry : POOL.entrySet()) {
				Object[] data = entry.getValue();
				
				T key = entry.getKey();
				
				/* TODO JsonToNBT
				if (key instanceof ItemStack && data.length > 2 && ((String)data[2]).length() != 0) {
					try {
						CompoundNBT nbt = ((ItemStack)key).getShareTag();
						((ItemStack)key).share  setTagCompound(JsonToNBT.getTagFromJson((String) data[2]));
					} 
					catch (NBTException e) {
						RoughMobsRevamped.LOGGER.error("NBT Tag invalid: %s", e.toString());
						e.printStackTrace();
					}
				}
				*/
				
				// Exclude non-matching dimensions for entry's associated entity
				if (isDimension(entity, String.valueOf(data[1]))) {
					boolean validEntry = true;
					
					// Additionally check if getRandom is getting a random
					// enchantment for a valid item passed into the method
					// if so then build a list of enchantments only valid
					// for the item passed in.
					if (item != null && key instanceof Enchantment) {
						validEntry = ((Enchantment)key).canApply(item);
					}
				
					// Store entry n times where n = weight value
					if (validEntry) {
						for (int i = 0; i < (int)data[0]; i++) {
							entries.add(key);
						}
					}
				}
			}
			
			int entrySize = entries.size();
			
			// If there are valid entries, return one randomly, else return null
			if (entrySize != 0) {
				int rnd = RND.nextInt(entrySize);
				return entries.get(rnd);
			}
	
			return null;
		}
		
		private static boolean isDimension(Entity entity, String dimension) {
			return dimension.trim().toUpperCase().equals("ALL") || String.valueOf(entity.dimension).equals(dimension);
		}
	}
	
	/*
	 * This function is used to determine if a piece of equipment or an enchantment should be given to a spawned mob.
	 * It checks for two bonuses to the chance: proximity to midnight, distance from world spawn
	 */
	private static boolean getChance(Entity entity, int chance) {
		
		if (chance <= 0) {
			return false;
		}
		
		float distanceChanceIncrease = 0;
		float timeChanceIncrease = 0;
		
		// Increase chance the closer it is to midnight.
		if (RoughConfig.chanceTimeMultiplier) {
			long currentTime = entity.getEntityWorld().getGameTime();
			
			// Ensure the current time is in the range of 0 to 24000
			currentTime = currentTime % 24000;
			
			// Convert time in ticks to hours
			byte currentHour = (byte) Math.floor(currentTime / 1000);

			// Add additional 16% bonus every hour from 8 PM to midnight up to 100% bonus
			if (currentHour >= 13 && currentHour <= 18)
				timeChanceIncrease = (float)((currentHour - 12) * 0.16);
			// Remove 25% bonus every hour from 1 AM to 6 AM up to 100% bonus
			else if (currentHour > 18 && currentHour <= 22)
				timeChanceIncrease = (float)(Math.abs(currentHour - 23) * 0.25);
		}
	
		// Increase chance the farther from world spawn the mob is.
		if (RoughConfig.chanceDistMultiplier) {
			World world = entity.getEntityWorld();
			double distanceToSpawn = entity.getDistanceSq(world.getSpawnPoint().getX(), world.getSpawnPoint().getY(), world.getSpawnPoint().getZ());

			if (distanceToSpawn >= RoughConfig.minDistFromSpawn) {
				distanceToSpawn = distanceToSpawn - RoughConfig.minDistFromSpawn;
				distanceChanceIncrease = Math.min(1, (float)distanceToSpawn / RoughConfig.distThreshold);
			}
		}
		
		// Add time and distance bonuses to chance
		float adjustedChance = Math.min(1, (float)1/(float)chance + Math.min(1, (((float)1/(float)chance * distanceChanceIncrease) + ((float)1/(float)chance * timeChanceIncrease))));
		
		return Math.random() <= adjustedChance;
	}
}
