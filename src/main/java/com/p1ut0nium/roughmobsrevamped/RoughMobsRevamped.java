package com.p1ut0nium.roughmobsrevamped;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.p1ut0nium.roughmobsrevamped.misc.Constants;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(Constants.MODID)
public final class RoughMobsRevamped {
	
	public static final Logger LOGGER = LogManager.getLogger(Constants.MODID);
	
	public RoughMobsRevamped() {
		LOGGER.debug("Hello World from the 1.14.4 version of " + Constants.MODID);

		final ModLoadingContext modLoadingContext = ModLoadingContext.get();
		//TODO Register Configs
		//modLoadingContext.registerConfig(ModConfig.Type.CLIENT, ConfigHolder.CLIENT_SPEC);
		//modLoadingContext.registerConfig(ModConfig.Type.SERVER, ConfigHolder.SERVER_SPEC);
	}
}
