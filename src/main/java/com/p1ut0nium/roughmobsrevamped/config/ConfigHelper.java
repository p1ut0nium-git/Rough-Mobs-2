package com.p1ut0nium.roughmobsrevamped.config;

import net.minecraftforge.fml.config.ModConfig;

/**
 * This bakes the config values to normal fields
 *
 * @author p1ut0nium
 * It can be merged into the main ExampleModConfig class, but is separate because of personal preference and to keep the code organised
 */
public final class ConfigHelper {

	// We store a reference to the ModConfigs here to be able to change the values in them from our code
	// (For example from a config GUI)
	private static ModConfig clientConfig;
	private static ModConfig serverConfig;

	public static void bakeClient(final ModConfig config) {
		clientConfig = config;

		RoughMobsRevampedConfig.clientBoolean = ConfigHolder.CLIENT.clientBoolean.get();
		RoughMobsRevampedConfig.clientStringList = ConfigHolder.CLIENT.clientStringList.get();
		RoughMobsRevampedConfig.clientDyeColorEnum = ConfigHolder.CLIENT.clientDyeColorEnum.get();

		RoughMobsRevampedConfig.modelTranslucency = ConfigHolder.CLIENT.modelTranslucency.get();
		RoughMobsRevampedConfig.modelScale = ConfigHolder.CLIENT.modelScale.get();

	}

	public static void bakeServer(final ModConfig config) {
		serverConfig = config;

		RoughMobsRevampedConfig.serverBoolean = ConfigHolder.SERVER.serverBoolean.get();
		RoughMobsRevampedConfig.serverStringList = ConfigHolder.SERVER.serverStringList.get();
		RoughMobsRevampedConfig.serverEnumDyeColor = ConfigHolder.SERVER.serverEnumDyeColor.get();

		RoughMobsRevampedConfig.electricFurnaceEnergySmeltCostPerTick = ConfigHolder.SERVER.electricFurnaceEnergySmeltCostPerTick.get();
		RoughMobsRevampedConfig.heatCollectorTransferAmountPerTick = ConfigHolder.SERVER.heatCollectorTransferAmountPerTick.get();
	}

	/**
	 * Helper method to set a value on a config and then save the config.
	 *
	 * @param modConfig The ModConfig to change and save
	 * @param path      The name/path of the config entry
	 * @param newValue  The new value of the config entry
	 */
	public static void setValueAndSave(final ModConfig modConfig, final String path, final Object newValue) {
		modConfig.getConfigData().set(path, newValue);
		modConfig.save();
	}

}
