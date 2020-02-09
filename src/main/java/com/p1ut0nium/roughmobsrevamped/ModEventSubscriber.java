package com.p1ut0nium.roughmobsrevamped;

import com.p1ut0nium.roughmobsrevamped.misc.Constants;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Subscribe to events from the MOD EventBus that should be handled on both PHYSICAL sides in this class
 *
 * @author p1ut0nium_94
 */
@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class ModEventSubscriber {

	private static final Logger LOGGER = LogManager.getLogger(Constants.MODID + " Mod Event Subscriber");

	/**
	 * This method will be called by Forge when a config changes.
	 */
	
	/* TODO
	@SubscribeEvent
	public static void onModConfigEvent(final ModConfig.ModConfigEvent event) {
		final ModConfig config = event.getConfig();
		// Rebake the configs when they change
		if (config.getSpec() == ConfigHolder.CLIENT_SPEC) {
			ConfigHelper.bakeClient(config);
			LOGGER.debug("Baked client config");
		} else if (config.getSpec() == ConfigHolder.SERVER_SPEC) {
			ConfigHelper.bakeServer(config);
			LOGGER.debug("Baked server config");
		}
	}
	*/
}
