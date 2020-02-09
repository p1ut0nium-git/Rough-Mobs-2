package com.p1ut0nium.roughmobsrevamped.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.p1ut0nium.roughmobsrevamped.RoughMobsRevamped;
import java.util.Map.Entry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class EquipHelper {

	private static final Random RND = new Random();
	
	public static class EquipmentApplier {
		
		private final String name;
		private final int chancePerWeaponDefault;
		private final int chancePerPieceDefault;
		private final float dropChanceDefault;
		
		private EquipmentPool poolMainhand;
		private EquipmentPool poolOffhand;
		
		private EquipmentPool poolHelmet;
		private EquipmentPool poolChestplate;
		private EquipmentPool poolLeggings;
		private EquipmentPool poolBoots;
		
		private int chancePerWeapon;
		private int chancePerPiece;
		private float dropChance;
		
		private String[] equipMainhand;
		private String[] equipOffhand;
		
		private String[] equipHelmet;
		private String[] equipChestplate;
		private String[] equipLeggings;
		private String[] equipBoots;
		
		public EquipmentApplier(String name, int chancePerWeaponDefault, int chancePerPieceDefault, float dropChanceDefault) {
			this.name = name;
			this.chancePerWeaponDefault = chancePerWeaponDefault;
			this.chancePerPieceDefault = chancePerPieceDefault;
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
			
			//final DataParameter<Boolean> HASEQUIPMENT = null;
			//boolean hasEquipment = entity.getDataManager().get(HASEQUIPMENT);
			
			if (entity == null) {
				return;
			}
			
			//entity.getHeldItemMainhand()
			//entity.getArmorInventoryList()
			
			// Get nearest player to the spawned mob
			// PlayerEntity playerClosest = entity.world.getClosestPlayer(entity, -1.0D);
			
			EquipmentPool[] pools = new EquipmentPool[] {
					poolMainhand, poolOffhand, poolBoots, poolLeggings, poolChestplate, poolHelmet
			};
			
			for (int i = 0; i < pools.length; i++) 
			{
				EquipmentPool pool = pools[i];
				EquipmentSlotType slot = EquipmentSlotType.values()[i];
				
				int rnd = i <= 1 ? chancePerWeapon : chancePerPiece;
				if (rnd > 0 && RND.nextInt(rnd) == 0) 
				{
					ItemStack stack = pool.getRandom(entity);
					if (stack != null) 
					{
						entity.setItemStackToSlot(slot, stack);
						//TODO entity.setDropChance(slot, dropChance);
					}
				}
			}
			
			//TODO entity.getDataManager().set(HASEQUIPMENT, true);
		}

		public String initConfig(String[] defaultMainhand, String[] defaultOffhand, String[] defaultHelmets, String[] defaultChestplates, String[] defaultLeggings, String[] defaultBoots) {

			String formatName = name.toLowerCase().replace(" ", "") + "Equipment";

			chancePerWeapon = chancePerWeaponDefault;
			chancePerPiece = chancePerPieceDefault;
			
			dropChance = 100;
			
			equipMainhand = defaultMainhand;
			equipOffhand = defaultOffhand;
			equipHelmet = defaultHelmets;
			equipChestplate = defaultChestplates;
			equipLeggings = defaultLeggings;
			equipBoots = defaultBoots;
			
			return formatName;
		}
		
		public void createPools() {
			setPoolMainhand(EquipmentPool.createEquipmentPool("mainhand", equipMainhand));
			setPoolOffhand(EquipmentPool.createEquipmentPool("offhand", equipOffhand));
			setPoolHelmet(EquipmentPool.createEquipmentPool("helmet", equipHelmet));
			setPoolChestplate(EquipmentPool.createEquipmentPool("chestplate", equipChestplate));
			setPoolLeggings(EquipmentPool.createEquipmentPool("leggings", equipLeggings));
			setPoolBoots(EquipmentPool.createEquipmentPool("boots", equipBoots));
		}
	}
	
	public static class EquipmentPool {

		public final EntryPool<ItemStack> ITEM_POOL = new EntryPool<ItemStack>();

		public static EquipmentPool createEquipmentPool(String name, String[] arrayItems) {

			EquipmentPool pool = new EquipmentPool();

			List<String> errorItems = pool.addItemsFromNames(arrayItems);
			if (!errorItems.isEmpty())
				RoughMobsRevamped.LOGGER.debug("There was an Equiopment Pool issue");
			return pool;
		}

		public List<String> addItemsFromNames(String[] array) {
			
			List<String> errors = new ArrayList<String>();

			for (String s : array)
			{
				//#8
				String error = addItemFromName(s);
				if (error != null)
					errors.add(error);
			}

			return errors;
		}

		private String addItemFromName(String s) {
			
			String[] parts = s.split(";");
			
			if (parts.length >= 2) 
			{
				try 
				{
					// Get item object based upon name
					Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parts[0]));

					int probability = Integer.parseInt(parts[1]);
					int dimension = parts.length >= 3 ? Integer.parseInt(parts[2]) : Integer.MIN_VALUE;
					
					if (item == null)
						return "Invalid item: " + parts[0] + " in line: " + s;
					else
						addItem(new ItemStack(item, 1), probability, dimension);
				}
				catch(NumberFormatException e) 
				{
					return "Invalid numbers in line: " + s;
				}
			}
			else
			{
				return "Invalid format for line: \"" + s + "\" Please change to item;probability;meta";
			}
			
			return null;
		}

		public void addItem(ItemStack stack, int probability, int dimension) {
			ITEM_POOL.addEntry(stack, probability, dimension == Integer.MIN_VALUE ? "ALL" : dimension);
		}
		
		public ItemStack getRandom(Entity entity) {
			
			if (ITEM_POOL.POOL.isEmpty()) 
				return null;
			
			ItemStack randomStack = ITEM_POOL.getRandom(entity);
			
			return randomStack;
		}
	}

	public static class EntryPool<T> {
		public final Map<T, Object[]> POOL = new HashMap<T, Object[]>();
		private List<T> entries = null;
		private List<String> dimensions = new ArrayList<String>();
		private boolean needsReload;

		public void addEntry(T t, Object... data) {
			POOL.put(t, data);
			needsReload = true;
		}
		
		public T getRandom(Entity entity) {
			
			if (entries == null || needsReload) 
			{
				entries = new ArrayList<T>();
				for (Entry<T, Object[]> entry : POOL.entrySet()) 
				{
					Object[] data = entry.getValue();
					
					T key = entry.getKey();
					if (key instanceof ItemStack && data.length > 2 && ((String)data[2]).length() != 0) {
						try {
							((ItemStack)key).setTag(JsonToNBT.getTagFromJson((String) data[2]));
						} catch (CommandSyntaxException e) {
							RoughMobsRevamped.LOGGER.debug("NBT Tag invalid: %s", e.toString());
						}
					}
					
					for (int i = 0; i < (int)data[0]; i++) {
						entries.add(key);
						dimensions.add(String.valueOf(data[1]));
					}
				}
				
				needsReload = false;
			}
			
			int rnd = RND.nextInt(entries.size());
			T entry = entries.get(rnd);
			String dimension = dimensions.get(rnd);
			int i = 100;
			
			while (!isDimension(entity, dimension) && i > 0) {
				rnd = RND.nextInt(entries.size());
				entry = entries.get(rnd);
				dimension = dimensions.get(rnd);
				i--;
			}
			
			return entry;
		}
		
		private static boolean isDimension(Entity entity, String dimension) {
			return dimension.trim().toUpperCase().equals("ALL") || String.valueOf(entity.dimension).equals(dimension);
		}
	}
}
