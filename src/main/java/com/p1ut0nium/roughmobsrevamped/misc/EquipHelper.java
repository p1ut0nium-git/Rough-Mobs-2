package com.p1ut0nium.roughmobsrevamped.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.lang.Math;

import com.p1ut0nium.roughmobsrevamped.RoughMobsRevamped;
import com.p1ut0nium.roughmobsrevamped.compat.GameStagesCompat;
import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import java.util.Random;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EquipHelper {
	
	private static final String KEY_APPLIED = Constants.unique("equipApplied");
	private static final Random RND = new Random();

	private static boolean playerHasEnchantStage;
	private static boolean enchantStageEnabled;
	private static boolean chanceTimeMultiplier;
	private static boolean chanceDistanceMultiplier;
	private static int distThreshold;
	private static boolean disableBabyZombieEquipment;
	
	public static boolean disableBabyZombieEquipment() {
		return disableBabyZombieEquipment;
	}
	
	public static class EquipmentApplier {
		
		private final String name;
		private final int chancePerWeaponDefault;
		private final int chancePerPieceDefault;
		private final int enchChanceDefault;
		private final float enchMultiplierDefault;
		private final float dropChanceDefault;
		
		private EquipmentPool poolMainhand;
		private EquipmentPool poolOffhand;
		
		private EquipmentPool poolHelmet;
		private EquipmentPool poolChestplate;
		private EquipmentPool poolLeggings;
		private EquipmentPool poolBoots;
		
		private int chancePerWeapon;
		private int chancePerPiece;
		private int enchChance;
		private float enchMultiplier;
		private float dropChance;
		
		private String[] equipMainhand;
		private String[] equipOffhand;
		
		private String[] equipHelmet;
		private String[] equipChestplate;
		private String[] equipLeggings;
		private String[] equipBoots;
		
		private String[] equipWeaponEnchants;
		private String[] equipArmorEnchants;
		
		public EquipmentApplier(String name, int chancePerWeaponDefault, int chancePerPieceDefault, int enchChanceDefault, float enchMultiplierDefault, float dropChanceDefault) {
			this.name = name;
			this.chancePerWeaponDefault = chancePerWeaponDefault;
			this.chancePerPieceDefault = chancePerPieceDefault;
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
		
		public void equipEntity(LivingEntity entity) {
			equipEntity(entity, false);
		}
		
		public void equipEntity(LivingEntity entity, boolean isBoss) {
			
			if (entity == null || entity.getEntityData().getBoolean(KEY_APPLIED))
				return;
			
			// Get nearest player to the spawned mob
			PlayerEntity playerClosest = entity.world.getClosestPlayer(entity, -1.0D);
			
			// Get all Game Stage related info
			enchantStageEnabled = GameStagesCompat.useEnchantStage();			
			
			// Test to see if player has enchantment stage unlocked
			if (enchantStageEnabled) {
				playerHasEnchantStage = GameStageHelper.hasAnyOf(playerClosest, Constants.ROUGHMOBSALL, Constants.ROUGHMOBSENCHANT);
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
				int chance = i <= 1 ? chancePerWeapon : chancePerPiece;
				
				// Test for each weapon and each piece of armor, or if entity should have complete armor set
				if (getChance(entity, chance) || (completeArmorSet && i > 1)) {
					ItemStack stack = pool.getRandom(entity, enchChance, enchMultiplier);
					
					if (stack != null) {
						entity.setItemStackToSlot(slot, stack);
						entity.setDropChance(slot, dropChance);
					}
				}
			}
			
			entity.getEntityData().setBoolean(KEY_APPLIED, true);
		}

		public String initConfig(String[] defaultMainhand, String[] defaultOffhand, String[] defaultHelmets, String[] defaultChestplates, String[] defaultLeggings, String[] defaultBoots, String[] defaultWeaponEnchants, String[] defaultArmorEnchants, boolean skipChanceOptions) {

			String formatName = name.toLowerCase().replace(" ", "") + "Equipment";
			RoughConfig.getConfig().addCustomCategoryComment(formatName, "Add enchanted armor and weapons to a newly spawned " + name + ". Takes 2-3 values seperated by a semicolon:\n"
																				 + "Format: item or enchantment;chance;dimension\n"
																				 + "item or enchantment:\tthe item/enchantment id\n"
																				 + "chance:\t\t\t\tthe higher this number the more this item/enchantment gets selected\n"
																				 + "dimension:\t\t\tdimension (ID) in which the item/enchantment can be selected (optional! Leave this blank for any dimension)");
			
			if (skipChanceOptions) 
			{
				chancePerWeapon = chancePerWeaponDefault;
				chancePerPiece = chancePerPieceDefault;
				enchChance = enchChanceDefault;
			}
			else
			{
				chancePerWeapon = RoughConfig.getInteger(formatName, "WeaponChance", chancePerWeaponDefault, 0, Short.MAX_VALUE, "Chance (1 in X per hand) to give a " + name + " new weapons on spawn.\nNOTE: Bosses always spawn with weapons.\nSet to 0 to disable new weapons", true);
				chancePerPiece = RoughConfig.getInteger(formatName, "ArmorChance", chancePerPieceDefault, 0, Short.MAX_VALUE, "Chance (1 in X per piece) to give a " + name + " new armor on spawn.\nSet to 0 to disable new armor.", true);
				enchChance = RoughConfig.getInteger(formatName, "EnchantChance", enchChanceDefault, 0, Short.MAX_VALUE, "Chance (1 in X per item) to enchant newly given items\nSet to 0 to disable item enchanting", true);
			}
			
			enchMultiplier = RoughConfig.getFloat(formatName, "EnchantMultiplier", enchMultiplierDefault, 0F, 1F, "Multiplier for the applied enchantment level with the max. level. The level can still be a bit lower\ne.g. 0.5 would make sharpness to be at most level 3 (5 x 0.5 = 2.5 and [2.5] = 3) and fire aspect would always be level 1 (2 x 0.5 = 1)", true);
			dropChance = RoughConfig.getFloat(formatName, "DropChance", dropChanceDefault, 0F, 1F, "Chance (per slot) that the " + name + " drops the equipped item (1 = 100%, 0 = 0%)", true);
			
			equipMainhand = RoughConfig.getStringArray(formatName, "Mainhand", defaultMainhand, "Items which can be wielded by a " + name + " in their mainhand");
			equipOffhand = RoughConfig.getStringArray(formatName, "Offhand", defaultOffhand, "Items which can be wielded by a " + name + " in their offhand");
			equipHelmet = RoughConfig.getStringArray(formatName, "Helmet", defaultHelmets, "Helmets which can be worn by a " + name + " in their helmet slot");
			equipChestplate = RoughConfig.getStringArray(formatName, "Chestplate", defaultChestplates, "Chestplates which can be worn by a " + name + " in their chestplate slot");
			equipLeggings = RoughConfig.getStringArray(formatName, "Leggings", defaultLeggings, "Leggings which can be worn by a " + name + " in their leggings slot");
			equipBoots = RoughConfig.getStringArray(formatName, "Boots", defaultBoots, "Boots which can be worn by a " + name + " in their boots slot");
			
			equipWeaponEnchants = RoughConfig.getStringArray(formatName, "WeaponEnchants", defaultWeaponEnchants, "Enchantments which can be applied to mainhand and offhand items");
			equipArmorEnchants = RoughConfig.getStringArray(formatName, "ArmorEnchants", defaultArmorEnchants, "Enchantments which can be applied to armor items");
			
			RoughConfig.getConfig().addCustomCategoryComment("Equipment_GlobalOptions", "Options to control equipment spawning across all mobs that can wear equipment.");
			
			chanceTimeMultiplier = RoughConfig.getBoolean("Equipment_GlobalOptions", "_TimeMultiplier", true, "Should rough mobs get more gear as it gets closer to midnight?");
			chanceDistanceMultiplier = RoughConfig.getBoolean("Equipment_GlobalOptions", "_DistanceMultiplier", true, "Should rough mobs get more gear based upon distance from world spawn?");
			distThreshold = RoughConfig.getInteger("Equipment_GlobalOptions", "_DistanceThreshold", 1000, 0, Integer.MAX_VALUE, "The distance threshold used to calculate the DistanceMultiplier.\nShorter distances here means mobs will have more gear closer to the World Spawn.");
			disableBabyZombieEquipment = RoughConfig.getBoolean("Equipment_GlobalOptions", "_DisableBabyZombieEquipment", true, "Set to true to disable baby zombies getting equipment.");
			
			return formatName;
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
		
		public static EquipmentPool createEquipmentPool(String name, String[] arrayItems, String[] arrayEnchants) {
			
			EquipmentPool pool = new EquipmentPool();
			
			List<String> errorItems = pool.addItemsFromNames(arrayItems);
			if (!errorItems.isEmpty()) 
				RoughMobsRevamped.LOGGER.error(Constants.MODNAME + ": error on creating the " + name + " item pool! " + String.join(", ", errorItems));
			
			List<String> errorEnchants = pool.addEnchantmentsFromNames(arrayEnchants);
			if (!errorEnchants.isEmpty()) 
				RoughMobsRevamped.LOGGER.error(Constants.MODNAME + ": error on creating the " + name + " enchantment pool! " + String.join(", ", errorEnchants));
			
			return pool;
		}
		
		public List<String> addEnchantmentsFromNames(String[] array) {
			
			List<String> errors = new ArrayList<String>();
			for (String s : array) {
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
					Enchantment ench = Enchantment.getEnchantmentByLocation(parts[0]);
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

		public List<String> addItemsFromNames(String[] array) {
			
			List<String> errors = new ArrayList<String>();
			for (String s : array) {
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
					Item item = Item.REGISTRY.getObject(new ResourceLocation(parts[0]));
					int probability = Integer.parseInt(parts[1]);
					int dimension = parts.length >= 3 ? Integer.parseInt(parts[2]) : Integer.MIN_VALUE;
					int meta = parts.length >= 4 ? Integer.parseInt(parts[3]) : 0;
					String nbt = parts.length >= 5 ? parts[4] : "";
					
					if (item == null)
						return "Invalid item: " + parts[0] + " in line: " + s;
					else
						addItem(new ItemStack(item, 1, meta), probability, dimension, nbt);
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
				if (key instanceof ItemStack && data.length > 2 && ((String)data[2]).length() != 0) {
					try {
						((ItemStack)key).setTagCompound(JsonToNBT.getTagFromJson((String) data[2]));
					} 
					catch (NBTException e) {
						RoughMobsRevamped.LOGGER.error("NBT Tag invalid: %s", e.toString());
						e.printStackTrace();
					}
				}
				
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
		if (chanceTimeMultiplier) {
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
		if (chanceDistanceMultiplier) {
			World world = entity.getEntityWorld();
			double distanceToSpawn = entity.getDistanceSq(world.getSpawnPoint().getX(), world.getSpawnPoint().getY(), world.getSpawnPoint().getZ());

			if (distanceToSpawn >= SpawnHelper.getMinDistFromSpawn()) {
				distanceToSpawn = distanceToSpawn - SpawnHelper.getMinDistFromSpawn();
				distanceChanceIncrease = Math.min(1, (float)distanceToSpawn / distThreshold);
			}
		}
		
		// Add time and distance bonuses to chance
		float adjustedChance = Math.min(1, (float)1/(float)chance + Math.min(1, (((float)1/(float)chance * distanceChanceIncrease) + ((float)1/(float)chance * timeChanceIncrease))));
		
		return Math.random() <= adjustedChance;
	}
}
