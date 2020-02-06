package de.lellson.roughmobs2.misc;

import com.mojang.authlib.GameProfile;

import de.lellson.roughmobs2.config.RoughConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PlayerHelper {
	
	// private static int mobKillsNeeded;
	private static int playerMobKills;
	// private static int playerSpawnLevel;
	
	/*
	public PlayerHelper(World worldIn, GameProfile gameProfileIn) {
		super(worldIn, gameProfileIn);

		System.out.println("World Dimension: " + worldIn.provider.getDimension()); // returns dimension id
		System.out.println("World Dimension Type: " + worldIn.provider.getDimensionType()); // returns dimention type/name
		
		System.out.println("GameProfile.getName: " + gameProfileIn.getName()); // returns player name

		// Get a refernce to the player entity
		EntityPlayer thisPlayer = worldIn.getPlayerEntityByName(gameProfileIn.getName());
		
		// Outputs player level on xp bar
		System.out.println("Player level: " + thisPlayer.experienceLevel);
	}

	@Override
	public boolean isSpectator() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCreative() {
		// TODO Auto-generated method stub
		return false;
	}
	*/

	public void preInit() {
		if (!hasDefaultConfig())
			return;
	}
	
	public void initConfig() {		
	}
	
	public void postInit() {
		
	}
	
	public static int getPlayerMobKills() {
		// System.out.println("PlayerMobKills: " + playerMobKills);
		return playerMobKills;
	}
	
	public static void setPlayerMobKills() {
		System.out.println("playerMobKills++");
		playerMobKills++;
	}

	public boolean hasDefaultConfig() {
		return true;
	}
}
