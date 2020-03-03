/*
 * Rough Mobs Revamped
 * A revamp of the open source mod Rough Mobs 2 by Lellson
 * Author: p1ut0nium_94
 * Original Author: Lellson
 * This version is for Minecraft 1.12.2
 * 
 * On CurseForge: https://www.curseforge.com/minecraft/mc-mods/rough-mobs-revamped
 * On GitHub: https://github.com/p1ut0nium-git/Rough-Mobs-Revamped/tree/1.12.2
 */

package com.p1ut0nium.roughmobsrevamped;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.Logger;

import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.misc.Constants;
import com.p1ut0nium.roughmobsrevamped.proxy.IProxy;
import com.p1ut0nium.roughmobsrevamped.util.handlers.RegistryHandler;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Constants.MODID, name = Constants.MODNAME, version = Constants.MODVERSION, updateJSON = Constants.MODUPDATE, acceptableRemoteVersions="*")
public class RoughMobs {
	
	@SidedProxy(clientSide = "com.p1ut0nium.roughmobsrevamped.proxy.ClientProxy", serverSide = "com.p1ut0nium.roughmobsrevamped.proxy.ServerProxy")
	public static IProxy proxy;
	
	@Instance(Constants.MODID)
	public static RoughMobs instance;
	
	private RoughApplier applier;
	public static Logger logger;
	
	@Nonnull
	public static RoughMobs instance() {
		return instance;
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		logger = event.getModLog();
		
		new RoughConfig(event);
		
		RegistryHandler.preInitRegistries();
		
		applier = new RoughApplier();
		applier.preInit();
		
		proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		applier.postInit();
		proxy.postInit(event);
	}
	
	// Deprecated?
	public static void logError(String format, Object... data) {
		FMLLog.bigWarning("[" + Constants.MODNAME + "]: " + format, data);
	}
}
