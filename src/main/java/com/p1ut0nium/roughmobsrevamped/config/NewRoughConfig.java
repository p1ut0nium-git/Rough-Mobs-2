package com.p1ut0nium.roughmobsrevamped.config;

import com.p1ut0nium.roughmobsrevamped.RoughApplier;
import com.p1ut0nium.roughmobsrevamped.features.EntityFeatures;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class NewRoughConfig {

	private static Configuration config;

	public NewRoughConfig(FMLPreInitializationEvent event) {
		config = new Configuration(new File(event.getModConfigurationDirectory() + "/roughFeatures.cfg"));
	}

	public static void loadFeatures() {

		config.load();

		RoughApplier.initConfig();
	}
	
	public static void saveFeatures() {
		
		if (config.hasChanged())
			config.save();
	}
	
	public static Configuration getConfig() {
		return config;
	}
	
	public static boolean getBoolean(String name, String id, boolean defaultValue, String description, boolean important) {
		return config.getBoolean((important ? "_" : "") + name + id, name, defaultValue, description.replace("%s", name));
	}
	
	public static boolean getBoolean(String name, String id, boolean defaultValue, String description) {
		return getBoolean(name, id, defaultValue, description, false);
	}
	
	public static int getInteger(String name, String id, int defaultValue, int min, int max, String description, boolean important) {
		return config.getInt((important ? "_" : "") + name + id, name, defaultValue, min, max, description.replace("%s", name));
	}
	
	public static int getInteger(String name, String id, int defaultValue, int min, int max, String description) {
		return getInteger(name, id, defaultValue, min, max, description, false);
	}
	
	public static float getFloat(String name, String id, float defaultValue, float min, float max, String description, boolean important) {
		return config.getFloat((important ? "_" : "") + name + id, name, defaultValue, min, max, description.replace("%s", name));
	}
	
	public static float getFloat(String name, String id, float defaultValue, float min, float max, String description) {
		return getFloat(name, id, defaultValue, min, max, description, false);
	}
	
	public static String[] getStringArray(String name, String id, String[] defaultValue, String description, boolean important) {
		return config.getStringList((important ? "_" : "") + name + id, name, defaultValue, description.replace("%s", name));
	}
	
	public static String[] getStringArray(String name, String id, String[] defaultValue, String description) {
		return getStringArray(name, id, defaultValue, description, false);
	}
	
	public static String getString(String name, String id, String defaultValue, String description, boolean important) {
		return config.getString((important ? "_" : "") + name + id, name, defaultValue, description.replace("%s", name));
	}
	
	public static String getString(String name, String id, String defaultValue, String description) {
		return getString(name, id, defaultValue, description, false);
	}
}
