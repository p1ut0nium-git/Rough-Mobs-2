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
package com.p1ut0nium.roughmobsrevamped.config;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.p1ut0nium.roughmobsrevamped.RoughMobsRevamped;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod.EventBusSubscriber
public class RoughConfig {
	
	public static class ServerConfig {
		
		public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
		public static final ForgeConfigSpec SPEC;
		
		public static final ForgeConfigSpec.BooleanValue testBoolean;
	
		static {
			
			BUILDER.comment("Server related options");
			BUILDER.push("Testing");
			testBoolean = BUILDER.comment("This is a test boolean.").define("test_boolean", true);
			BUILDER.pop();
			
			SPEC = BUILDER.build();
		}
	}
	
	public static void setup() {
		
		final ModLoadingContext modLoadingContext = ModLoadingContext.get();
		
		Path configPath = FMLPaths.CONFIGDIR.get();
		Path roughConfigPath = Paths.get(configPath.toAbsolutePath().toString(), "roughmobsrevamped");
		
		try {
			Files.createDirectory(roughConfigPath);
		} catch (FileAlreadyExistsException e) {
			// Do nothing
		} catch (IOException e) {
			RoughMobsRevamped.LOGGER.error("Failed to create roughmobsrevamped config directory.", e);
		}
		
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, ServerConfig.SPEC, "roughmobsrevamped/server.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, FogConfig.SPEC, "roughmobsrevamped/bossfog.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, ModCompatConfig.SPEC, "roughmobsrevamped/modcompat.toml");
	}
	
	/* TODO Delete this
	public static void loadConfig(ForgeConfigSpec config, String path) {
		RoughMobsRevamped.LOGGER.info("Loading config: " + path);;
		final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
		
		RoughMobsRevamped.LOGGER.info("Built config: " + path);
		file.load();
		
		RoughMobsRevamped.LOGGER.info("Loaded config: " + path);
		config.setConfig(file);
	}
	*/
}
