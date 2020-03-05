package com.p1ut0nium.roughmobsrevamped.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.p1ut0nium.roughmobsrevamped.misc.BossHelper;
import com.p1ut0nium.roughmobsrevamped.util.DamageSourceFog;
import com.p1ut0nium.roughmobsrevamped.util.Helpers;
import com.p1ut0nium.roughmobsrevamped.util.handlers.FogEventHandler;
import com.p1ut0nium.roughmobsrevamped.util.handlers.SoundHandler;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class BossSkeleton extends EntitySkeleton implements IBoss {

	// Fog variables
    private static boolean FOG_DOT_ENABLED = BossHelper.bossFogDoTEnabled;
    private static boolean FOG_WARNING_ENABLED = BossHelper.bossFogDoTWarning;
	private static int FOG_MAX_DISTANCE = BossHelper.bossFogMaxDistance;
    private static int FOG_DOT_DELAY = BossHelper.bossFogDoTDelay * 20;
    private static int FOG_WARNING_TIME = BossHelper.bossFogDoTWarningTime * 20;
    private HashMap<String, Long> playersWarned = new HashMap<>();
    private List<EntityPlayer> playersInFog = new ArrayList<EntityPlayer>();
    private TextComponentString fogWarningMsg;
	private static int fog_dot_tick;

	//TODO private double[] bossColorTheme = {1.0, 0.0, 0.0};

	public BossSkeleton(World worldIn) {
		super(worldIn);
        this.experienceValue = 100;
        
        fog_dot_tick = 0;
        
		fogWarningMsg = new TextComponentString("The thick fog reaches out for you... You begin to choke as you move through it.\nPerhaps you should find the source of the poisonous mist, or flee to safety.");
		fogWarningMsg.getStyle().setColor(TextFormatting.DARK_GREEN);
	}
	
    public void onAddedToWorld() {
    	super.onAddedToWorld();
    	
        if (this.world.isRemote && this.posY >= world.getSeaLevel() && this.world.canSeeSky(this.getPosition())) {
			this.world.addWeatherEffect(new EntityLightningBolt(this.world, this.posX, this.posY, this.posZ, true));
			SoundEvent soundEvent = new SoundEvent(new ResourceLocation("entity.lightning.thunder"));
			this.world.playSound(this.posX, this.posY, this.posZ, soundEvent, SoundCategory.AMBIENT, 100.0F, 1.0F, true);
        }
    }
	
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            for (int i = 0; i < 2; ++i) {
                this.world.spawnParticle(EnumParticleTypes.FLAME, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0.0D, 0.0D, 0.0D);
            }
        }
        
        if (!this.world.isRemote) {
	        
	        if (FOG_DOT_ENABLED) {
	        	List<EntityPlayer> playersInRange = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(FOG_MAX_DISTANCE));
	        	
	    		// Test to see if playersInRange actually contains players
	    		if (Helpers.containsInstance(playersInRange, EntityPlayer.class)) {

					// For each player in range
					for (EntityPlayer player : playersInRange) {
						
						// Add new player to list of players in the fog
						if (!playersInFog.contains(player)) {
							playersInFog.add(player);
							
							// If fog warning is enabled...
							if (FOG_WARNING_ENABLED) {
								// Warn players on first entering the fog and add them to the players warned list
								if (!playersWarned.containsKey(player.getName())) {
									playersWarned.put(player.getName(), world.getWorldTime() + FOG_WARNING_TIME);
									player.sendMessage(fogWarningMsg);
								}
								// If warned player hasn't been warned in a while, warn them again
								else if (playersWarned.containsKey(player.getName()) && world.getWorldTime() >= playersWarned.get(player.getName())) {
									playersWarned.replace(player.getName(), world.getWorldTime() + FOG_WARNING_TIME);
									player.sendMessage(fogWarningMsg);
								}
							}
						}
					}
					
					// For each player marked as "in the fog"
					for (EntityPlayer playerInFog : playersInFog) {
						
						// Remove them from the "in fog" list if they are no longer in range
						if (!Helpers.containsPlayer(playersInRange, playerInFog)) {
							playersInFog.remove(playerInFog);
						}
					}

					// Reset fog DoT delay
					if (fog_dot_tick == FOG_DOT_DELAY) {
						fog_dot_tick = 0;
					}
					
					// Damage player while inside fog.
					if (fog_dot_tick == 0) {
						for (EntityPlayer player : playersInRange) {
							player.attackEntityFrom(DamageSourceFog.POISONOUS_FOG, 0);
						}
					}

					fog_dot_tick += 1;
				}
	    		
	    		// If no players are in range, then clear the list that keeps track of all players in the fog
	    		else if (!Helpers.containsInstance(playersInRange, EntityPlayer.class)) {
	    			playersInFog.clear();
	    		}
	        }
        }

        super.onLivingUpdate();
    }

    public void onDeath(DamageSource cause)
    {
        super.onDeath(cause);
        
        //TODO Add custom death effects
        
        FogEventHandler.bossDied = true;
    }
    
	protected boolean canDespawn() {
		return false;
	}
    
    //TODO Add custom ambient sound
    protected SoundEvent getAmbientSound() {
        return SoundHandler.ENTITY_BOSS_IDLE;
    }
    
	//TODO Add custom death sound
    protected SoundEvent getDeathSound() {
        return SoundHandler.ENTITY_BOSS_DEATH;
    }
    
    //TODO Add custom loot table
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_ZOMBIE;
    }
    
    /* TODO
	public double[] getBossColorTheme() {
		return bossColorTheme;
	}

	public void setBossColorTheme(String[] bossColorTheme) {
		this.bossColorTheme = Arrays.stream(bossColorTheme).mapToDouble(Double::parseDouble).toArray();
	}
	*/
}

