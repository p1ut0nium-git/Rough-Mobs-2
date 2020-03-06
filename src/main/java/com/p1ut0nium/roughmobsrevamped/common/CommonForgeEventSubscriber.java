package com.p1ut0nium.roughmobsrevamped.common;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.p1ut0nium.roughmobsrevamped.reference.Constants;

import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * Subscribe to events from the FORGE EventBus that should be handled on both PHYSICAL sides in this class
 *
 * @author p1ut0nium_94
 */
@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.FORGE)
public final class CommonForgeEventSubscriber {

	private static final Logger LOGGER = LogManager.getLogger(Constants.MODID + " Server Forge Event Subscriber");
}
