package com.p1ut0nium.roughmobsrevamped;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.p1ut0nium.roughmobsrevamped.misc.Constants;

import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MODID)
public final class RoughMobsRevamped {
	
	public static final Logger LOGGER = LogManager.getLogger(Constants.MODID);
	
	public RoughMobsRevamped() {
		LOGGER.debug("Hello World from the 1.14.4 version of " + Constants.MODID);
	}

}
