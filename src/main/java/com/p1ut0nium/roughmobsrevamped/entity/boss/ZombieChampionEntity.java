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
package com.p1ut0nium.roughmobsrevamped.entity.boss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.entity.monster.HostileBatEntity;
import com.p1ut0nium.roughmobsrevamped.init.ModEntityTypes;
import com.p1ut0nium.roughmobsrevamped.init.ModSounds;
import com.p1ut0nium.roughmobsrevamped.misc.BossHelper;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ZombieChampionEntity extends ZombieEntity implements IChampion {
	
	// Bat Swarm variables
	private static boolean BATSWARM_ENABLED = BossHelper.bossBatSwarmEnabled;
	private static int BATSWARM_ATTACK_RANGE = BossHelper.bossBatSwarmRange;
	private static int BATSWARM_MINIONS_MAX = BossHelper.bossBatSwarmCount;
	private static int BATSWARM_DELAY = BossHelper.bossBatSwarmDelay * 20;
	private List<HostileBatEntity> batMinions = new ArrayList<HostileBatEntity>();
	private static int batSwarmTick;

	// Fog variables
	private static boolean FOG_DOT_ENABLED = RoughConfig.bossFogDoTEnabled;
    private static boolean FOG_WARNING_ENABLED = RoughConfig.bossFogDoTWarning;
	private static int FOG_MAX_DISTANCE = BossHelper.bossFogMaxDistance;
    private static int FOG_DOT_DELAY = RoughConfig.bossFogDoTDelay* 20;
    private static int FOG_WARNING_TIME = RoughConfig.bossFogDoTWarningTime * 20;    
    private HashMap<String, Long> playersWarned = new HashMap<>();
    private List<PlayerEntity> playersInFog = new ArrayList<PlayerEntity>();
    private StringTextComponent fogWarningMsg;
	private static int fog_dot_tick;

	// TODO private double[] bossColorTheme = {0.0, 1.0, 0.0};
	
	public ZombieChampionEntity(EntityType<? extends ZombieEntity> zombie, World worldIn) {
		super(zombie, worldIn);
        this.experienceValue = 100;
        
        fog_dot_tick = 0;
        batSwarmTick = 0;
        
		fogWarningMsg = new StringTextComponent("The thick fog reaches out for you... You begin to choke as you move through it.\nPerhaps you should find the source of the poisonous mist, or flee to safety.");
		fogWarningMsg.getStyle().setColor(TextFormatting.DARK_GREEN);
	}
	
	public ZombieChampionEntity(World worldIn) {
	    this((EntityType<? extends ZombieEntity>) ModEntityTypes.ZOMBIE_CHAMPION.get(), worldIn);
	}

	@Override
	public void onAddedToWorld() {
    	super.onAddedToWorld();

        if (this.posY >= world.getSeaLevel() && this.world.canBlockSeeSky(this.getPosition()))
        	((ServerWorld)this.world).addLightningBolt(new LightningBoltEntity((ServerWorld)this.world, this.posX, this.posY, this.posZ, true));
    }

	@Override
    public void livingTick() {

        if (this.world.isRemote) {
            for (int i = 0; i < 2; ++i) {
                this.world.addParticle(ParticleTypes.LARGE_SMOKE, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.getWidth(), this.posY + this.rand.nextDouble() * (double)this.getHeight(), this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.getWidth(), 0.0D, 0.0D, 0.0D);
            }
        }

        /* TODO
        if (!this.world.isRemote) {

        	// TODO make sure player is not in creative
        	PlayerEntity closetPlayer = this.world.getClosestPlayer(this, FOG_MAX_DISTANCE);
        	
        	// Bat Swarm can only be fired every BATSWARM_DELAY ticks
        	batSwarmTick = Math.max(batSwarmTick - 1, 0);
        	
            if (batSwarmTick == 0) {
            	batSwarmTick = BATSWARM_DELAY;

		        if (BATSWARM_ENABLED && closetPlayer != null && this.canEntityBeSeen(closetPlayer) && closetPlayer.getDistance(this) <= BATSWARM_ATTACK_RANGE) {
		        	if (batMinions.isEmpty()) {
		        		for (int i = 0; i < BATSWARM_MINIONS_MAX; i++) {
		        			// TODO change null to EntityType.HOSTILE_BAT
		        			HostileBatEntity bat = new HostileBatEntity(null, this.world);
		        			bat.setPosition(this.posX + Math.random() - Math.random(), this.posY + Math.random(), this.posZ + Math.random() - Math.random());
		        			bat.onInitialSpawn(world, this.getEntityWorld().getDifficultyForLocation(this.getPosition()), SpawnReason.MOB_SUMMONED, null, null);
		        			
		        			this.world.addEntity(bat);
		        			
		        			bat.setIsBatHanging(false);
		        			bat.setBoss(this);
		        			
		        			batMinions.add(bat);
		        		}
		        		playSoundBatSwarm();
		        	}
		        }  
            }
            
	        // Remove any dead bats from the group of batMinions
	        if (!batMinions.isEmpty()) {
	        	for (HostileBatEntity bat : batMinions) {
	        		if (!bat.isAlive()) {
	        			batMinions.remove(bat);
	        			break;
	        		}
	        	}
	        }
	        
	        if (FOG_DOT_ENABLED) {
	        	List<PlayerEntity> playersInRange = this.world.getEntitiesWithinAABB(PlayerEntity.class, this.getBoundingBox().grow(FOG_MAX_DISTANCE));
	        	
	    		// Test to see if playersInRange actually contains players
	    		if (Utilities.containsInstance(playersInRange, PlayerEntity.class)) {

					// For each player in range
					for (PlayerEntity player : playersInRange) {
						
						// Add new player to list of players in the fog
						if (!playersInFog.contains(player)) {
							playersInFog.add(player);
							
							// If fog warning is enabled...
							if (FOG_WARNING_ENABLED) {
								// Warn players on first entering the fog and add them to the players warned list
								if (!playersWarned.containsKey(player.getScoreboardName())) {
									playersWarned.put(player.getScoreboardName(), world.getGameTime() + FOG_WARNING_TIME);
									player.sendMessage(fogWarningMsg);
								}
								// If warned player hasn't been warned in a while, warn them again
								else if (playersWarned.containsKey(player.getScoreboardName()) && world.getGameTime() >= playersWarned.get(player.getScoreboardName())) {
									playersWarned.replace(player.getScoreboardName(), world.getGameTime() + FOG_WARNING_TIME);
									player.sendMessage(fogWarningMsg);
								}
							}
						}
					}
					
					// For each player marked as "in the fog"
					for (PlayerEntity playerInFog : playersInFog) {
						
						// Remove them from the "in fog" list if they are no longer in range
						if (!Utilities.containsPlayer(playersInRange, playerInFog)) {
							playersInFog.remove(playerInFog);
						}
					}

					// Reset fog DoT delay
					if (fog_dot_tick == FOG_DOT_DELAY) {
						fog_dot_tick = 0;
					}
					
					// Damage player while inside fog.
					if (fog_dot_tick == 0) {
						for (PlayerEntity player : playersInRange) {
							player.attackEntityFrom(DamageSourceFog.POISONOUS_FOG, 0);
						}
					}

					fog_dot_tick += 1;
				}
	    		
	    		// If no players are in range, then clear the list that keeps track of all players in the fog
	    		else if (!Utilities.containsInstance(playersInRange, PlayerEntity.class)) {
	    			playersInFog.clear();
	    		}
	        }
        }
        */

        super.livingTick();
    }
	
	// @SideOnly(Side.CLIENT)
	private void playSoundBatSwarm() {
		if (!world.isRemote)
			this.playSound(ModSounds.ENTITY_BOSS_BATSWARM, 1.0F, 1.0F);
	}

	@Override
    public void onDeath(DamageSource cause) {
        // Max out batSwarmTick so no more bats can spawn
        batSwarmTick = BATSWARM_DELAY;

        /* TODO
        // Kill all bat minions when boss dies
        if (!batMinions.isEmpty() ) {
	        for (HostileBatEntity bat : batMinions) {
	        	bat.remove();
	        }
        }
        */
        
        //TODO Is there a better way of handling this?
        // FogEventHandler.bossDied = true;
        
        super.onDeath(cause);
    }
	
	protected boolean canDespawn() {
		return false;
	}

    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY_BOSS_IDLE;
    }
    
    protected SoundEvent getDeathSound() {
        return ModSounds.ENTITY_BOSS_DEATH;
    }
}
