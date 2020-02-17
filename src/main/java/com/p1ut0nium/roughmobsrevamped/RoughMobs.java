package com.p1ut0nium.roughmobsrevamped;

import javax.annotation.Nonnull;
import org.apache.logging.log4j.Logger;

import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.misc.Constants;
import com.p1ut0nium.roughmobsrevamped.proxy.ServerProxy;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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

@Mod(modid = Constants.MODID, name = Constants.MODNAME, version = Constants.MODVERSION, updateJSON = Constants.MODUPDATE, acceptableRemoteVersions="*")
public class RoughMobs {
	
	// Testing 
	@SidedProxy(clientSide = "com.p1ut0nium.roughmobsrevamped.proxy.ClientProxy", serverSide = "com.p1ut0nium.roughmobsrevamped.proxy.ServerProxy")
	public static ServerProxy proxy;
	
	@Instance(Constants.MODID)
	public static RoughMobs instance;
	
	@Nonnull
	public static RoughMobs instance() {
		return instance;
	}
	
	public static Logger logger;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		// Check for newer version
		// ForgeVersion.getResult(Loader.instance().activeModContainer());
		
		logger = event.getModLog();
		
		new RoughConfig(event);
		proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		//proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
	
	// Deprecated?
	public static void logError(String format, Object... data) {
		FMLLog.bigWarning("[" + Constants.MODNAME + "]: " + format, data);
	}
}
