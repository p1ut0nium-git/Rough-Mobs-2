package com.p1ut0nium.roughmobsrevamped;

import java.util.*;

import com.google.common.collect.Sets;
import com.p1ut0nium.roughmobsrevamped.ai.combat.*;
import com.p1ut0nium.roughmobsrevamped.ai.misc.*;
import com.p1ut0nium.roughmobsrevamped.compat.CompatHandler;
import com.p1ut0nium.roughmobsrevamped.compat.GameStagesCompat;
import com.p1ut0nium.roughmobsrevamped.config.NewRoughConfig;
import com.p1ut0nium.roughmobsrevamped.config.RoughConfig;
import com.p1ut0nium.roughmobsrevamped.entities.IBoss;
import com.p1ut0nium.roughmobsrevamped.features.BlazeFeatures;
import com.p1ut0nium.roughmobsrevamped.features.CreeperFeatures;
import com.p1ut0nium.roughmobsrevamped.features.EndermanFeatures;
import com.p1ut0nium.roughmobsrevamped.features.EndermiteFeatures;
import com.p1ut0nium.roughmobsrevamped.features.EntityFeatures;
import com.p1ut0nium.roughmobsrevamped.features.GhastFeatures;
import com.p1ut0nium.roughmobsrevamped.features.GuardianFeatures;
import com.p1ut0nium.roughmobsrevamped.features.HostileHorseFeatures;
import com.p1ut0nium.roughmobsrevamped.features.MagmaCubeFeatures;
import com.p1ut0nium.roughmobsrevamped.features.SilverfishFeatures;
import com.p1ut0nium.roughmobsrevamped.features.SkeletonFeatures;
import com.p1ut0nium.roughmobsrevamped.features.SlimeFeatures;
import com.p1ut0nium.roughmobsrevamped.features.SpiderFeatures;
import com.p1ut0nium.roughmobsrevamped.features.WitchFeatures;
import com.p1ut0nium.roughmobsrevamped.features.WitherFeatures;
import com.p1ut0nium.roughmobsrevamped.features.ZombieFeatures;
import com.p1ut0nium.roughmobsrevamped.features.ZombiePigmanFeatures;
import com.p1ut0nium.roughmobsrevamped.misc.*;
import com.p1ut0nium.roughmobsrevamped.util.Constants;
import com.p1ut0nium.roughmobsrevamped.util.DamageSourceFog;
import com.p1ut0nium.roughmobsrevamped.util.handlers.FogEventHandler;
import com.p1ut0nium.roughmobsrevamped.util.handlers.SoundHandler;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class RoughApplier {

    public static final String FEATURES_APPLIED = Constants.unique("featuresApplied");

    public static final List<EntityFeatures> FEATURES = new ArrayList<EntityFeatures>();

    public static final String ROUGH_HORSE = Constants.unique("roughhorse");
    public static final String BOSS_MINION = Constants.unique("bossMinion");

    public static final Random RND = new Random();
    public static final int MAX = Short.MAX_VALUE;

    private static boolean gameStagesEnabled;
    private static boolean playerHasAbilsStage;
    private static boolean abilsStageEnabled;
    private static boolean equipStageEnabled;
    private static boolean playerHasEquipStage;

    private EquipHelper.EquipmentApplier zombieEquipApplier;
    private EquipHelper.EquipmentApplier skeletonEquipApplier;
    private BossHelper.BossApplier zombieBossApplier;
    private BossHelper.BossApplier skeletonBossApplier;

    private MountHelper.Rider rider;

    private Map<Potion, Integer> effects;

    public static List<String> flameTouch;
    public static List<String> pushAttackersAway;

    public static List<String> explodeOnDeath;
    public static float pushDamage;
    public static float deathExplosionStrength;
    public static boolean deathExplosionDestroyBlocks;

    public static List<String> dropEnderpearl;

    public static List<String> teleportAttacker;

    public static List<String> blind;
    public static int blindChance;
    public static int blindDuration;

    public static List<String> stealItem;
    public static int stealItemChance;

    public static String immunityItem;
    public static Item immunityItemItem;

    public static List<String> invisible;
    public static float invisibleRange;

    public static List<String> creeperCustomFuseTime;
    public static int creeperFuseTime;

    public static List<String> creeperBurnExplosion;

    public static List<String> creeperCustomExplosionRadius;
    public static int creeperExplosionRadius;

    public static List<String> burnInSunlight;

    public static List<String> witherMobs;

    public static List<String> projectileImmunity;

    public static List<String> dropTnt;

    public static List<String> customExplosionRadius;
    public static int explosionRadius;

    public static List<String> dropWater;

    public static boolean preventHorseDespawn;
    public static int randomRiderChance;
    public static boolean horseBurn;

    public static List<String> regenInLava;
    public static List<String> dropLava;

    public static List<String> split;
    public static int splitChance;

    public static List<String> customKnockbackMultiplier;
    public static float knockbackMultiplier;

    public static List<String> customFallDamageMultiplier;
    public static float fallDamageMultiplier;

    public static List<String> inflictSlowness;
    public static int slownessDuration;
    public static int slownessChance;
    public static boolean slownessCreateWeb;

    public static List<String> rideable;

    public static List<String> applyEffects;
    public static String[] applyEffectNames;
    public static float applyEffectsRange;

    public static int witchLingeringChance;

    public static List<String> customBatsOnDeath;
    public static int batsOnDeath;

    public static List<String> summonSkeleton;
    public static int summonSkeletonTimer;

    public static List<String> customLeap;
    public static int leapChance;
    public static float leapHeight;

    public static List<String> inflictHunger;
    public static int hungerDuration;
    public static int hungerChance;

    public static List<String> spawnWithHorse;
    public static int spawnWithHorseChance;
    public static int spawnWithHorseMinY;

    public static boolean zombieBabyBurn;
    public static boolean zombieHelmetBurn;

    public static String[] breakBlocksSet;
    public static List<String> customBreakBlocks;
    public static Set<Block> breakBlocks;

    public static boolean skeletonChangeWeapons;
    public static int skeletonBowCooldown;
    public static boolean skeletonHelmetBurn;

    public static boolean pigmanAggressiveTouch;
    public static boolean pigmanAlwaysAggressive;
    public static int pigmanAggressiveRange;
    public static float pigmanAggressiveBlockRange;
    public static int pigmanAggressiveBlockChance;

    // TODO: Set default config values
    public static void initConfig() {
        RoughConfig.getConfig().addCustomCategoryComment("Features", "Configuration options which the special mob features features");

        flameTouch = Arrays.asList(NewRoughConfig.getStringArray("flame_Touch", "_Entities", new String[]{"minecraft:blaze"}, "_Entities which have the %s feature"));
        pushAttackersAway = Arrays.asList(NewRoughConfig.getStringArray("push_Attackers_Away", "_Entities", new String[]{"minecraft:blaze"}, "Entities which have the %s feature"));
        pushDamage = NewRoughConfig.getFloat("push_Damage", "", 1.0F, 0F, MAX, "Amount of damage done to attacker when pushed away. Set to 0 to disable");

        explodeOnDeath = Arrays.asList(NewRoughConfig.getStringArray("explode_On_Death", "_Entities", new String[]{"minecraft:witch", "minecraft:zombie"}, "Entities which have the %s feature"));
        deathExplosionStrength = NewRoughConfig.getFloat("death_Explosion_Strength", "", 1.0F, 0F, MAX, "Explosion strength of the explosions, which mobs create on death");
        deathExplosionDestroyBlocks = NewRoughConfig.getBoolean("death_Explosion_Destroy_Blocks", "", false, "Set to true to enable destroying blocks when a mob explodes on death.");

        dropEnderpearl = Arrays.asList(NewRoughConfig.getStringArray("drop_Enderpearl", "_Entities", new String[]{"minecraft:creeper", "minecraft:pig"}, "Entities which drop an extra ender pearl on death"));
        teleportAttacker = Arrays.asList(NewRoughConfig.getStringArray("teleport_Attacker", "_Entities", new String[]{"minecraft:cow", "minecraft:cave_spider"}, "Entities which have the %s feature"));

        blind = Arrays.asList(NewRoughConfig.getStringArray("blind", "_Entities", new String[]{"minecraft:enderman"}, "Entities which have the %s feature"));
        blindChance = NewRoughConfig.getInteger("blind_Chance", "", 1, 0, MAX, "Chance (1 in X) that an %s applies the blindness effect to its target\nSet to 0 to disable this feature");
        blindDuration = NewRoughConfig.getInteger("blind_Duration", "", 200, 1, MAX, "Duration in ticks of the applied blindness effect (20 ticks = 1 second)");

        stealItem = Arrays.asList(NewRoughConfig.getStringArray("steal_Item", "_Entities", new String[]{"minecraft:zombie"}, "Entities which have the %s feature"));
        stealItemChance = NewRoughConfig.getInteger("steal_Item_Chance", "", 1, 0, MAX, "Chance (1 in X) that an %s steals the targets held or equipped item to drop it on the ground\nSet to 0 to disable this feature");
        immunityItem = NewRoughConfig.getString("immunity_Item", "", "minecraft:ender_eye", "If this item is somewhere in the players inventory, the player becomes immune to teleportation and item stealing\nLeave this empty to disable this feature");

        invisible = Arrays.asList(NewRoughConfig.getStringArray("invisible", "_Entities", new String[]{"minecraft:creeper"}, "_Entities which have the %s feature"));
        invisibleRange = RoughConfig.getFloat("invisible_Range", "", 6F, 0F, MAX, "Block range to the target in which %ss become invisible\nSet to 0 to prevent %ss from becoming invisible");

        creeperCustomFuseTime = Arrays.asList(NewRoughConfig.getStringArray("custom_Fuse_Time", "_Entities", new String[]{"minecraft:creeper"}, "_Entities which have the %s feature (only applicable to creepers)"));
        creeperFuseTime = RoughConfig.getInteger("creeper_Fuse_Time", "", 20, 0, MAX, "Creeper fuse time of entities listed in the `creeper_Custom_Fuse_Time` (In ticks, 20 ticks = 1 second)\nThe vanilla default is 30");

        creeperBurnExplosion = Arrays.asList(NewRoughConfig.getStringArray("creeper_Burn_Explosion", "_Entities", new String[]{"minecraft:creeper"}, "_Entities which explode when they catch on fire (only applicable to creepers)"));

        creeperCustomExplosionRadius = Arrays.asList(NewRoughConfig.getStringArray("creeper_Custom_Explosion_Radius", "_Entities", new String[]{"minecraft:creeper"}, "_Entities which have the %s feature (only applicable to creepers)"));
        creeperExplosionRadius = RoughConfig.getInteger("creeper_Explosion_Radius", "", 4, 0, MAX, "Creeper explosion radius of entities listed in the 'creeper_Custom_Explosion_Radius'\nThe vanilla default is 3");

        burnInSunlight = Arrays.asList(NewRoughConfig.getStringArray("burn_In_Sunlight", "_Entities", new String[]{"minecraft:creeper", "minecraft:chicken"}, "_Entities which have the %s feature"));

        witherMobs = Arrays.asList(NewRoughConfig.getStringArray("wither_Mobs", "_Entities", new String[]{"minecraft:endermite"}, "_Entities which have the %s feature"));

        projectileImmunity = Arrays.asList(NewRoughConfig.getStringArray("flame_Touch", "_Entities", new String[]{"minecraft:ghast"}, "_Entities which have the %s feature"));
        dropTnt = Arrays.asList(NewRoughConfig.getStringArray("flame_Touch", "_Entities", new String[]{"minecraft:ghast"}, "_Entities which have the %s feature"));

        customExplosionRadius = Arrays.asList(NewRoughConfig.getStringArray("custom_Explosion_Radius", "_Entities", new String[]{"minecraft:ghast"}, "_Entities which have the %s feature (only applicable to ghasts)"));
        explosionRadius = RoughConfig.getInteger("explosion_Radius", "", 3, 0, MAX, "Fireball explosion radius of entities listed in the `custom_Explosion_Radius`\nThe vanilla default is 1");

        dropWater = Arrays.asList(NewRoughConfig.getStringArray("drop_Water", "_Entities", new String[]{"minecraft:guardian"}, "_Entities which drop water on death"));

        randomRiderChance = RoughConfig.getInteger("random_Rider_Chance", "", 3, 0, MAX, "Chance (1 in X) that a random skeleton or zombie starts riding unmounted hostile horses around it");
        preventHorseDespawn = RoughConfig.getBoolean("prevent_Horse_Despawn", "", true, "Prevent undead horses summoned through this mod from despawning");
        horseBurn = RoughConfig.getBoolean("horse_Burn", "", true, "Set this to false to prevent undead horses from burning in sunlight (as long as they have no rider)");

        regenInLava = Arrays.asList(NewRoughConfig.getStringArray("regen_In_Lava", "_Entities", new String[]{"minecraft:magma_cube"}, "_Entities regenerate health in lava"));
        dropLava = Arrays.asList(NewRoughConfig.getStringArray("drop_Lava", "_Entities", new String[]{"minecraft:magma_cube"}, "_Entities which drop water on death"));

        split = Arrays.asList(NewRoughConfig.getStringArray("split", "_Entities", new String[]{"minecraft:parrot", "minecraft:chicken"}, "_Entities which split into 2 when hit"));
        splitChance = NewRoughConfig.getInteger("split_Chance", "", 2, 0, MAX, "Chance (1 in X) to split a mob in two when attacked\nSet to 0 to disable this feature");

        skeletonChangeWeapons = NewRoughConfig.getBoolean("skeleton_Change_Weapons", "", true, "Set this to false to prevent skeletons from switching their weapons");

        skeletonBowCooldown = NewRoughConfig.getInteger("Skeleton_Bow_Cooldown", "", 0, 0, MAX, "Bow cooldown of %ss in ticks (The vanilla default is 20)");

        skeletonHelmetBurn = NewRoughConfig.getBoolean("Skeleton_Helmet_Burn", "", false, "Set this to true to make all %ss burn in sunlight even if they wear a helmet");

        customKnockbackMultiplier = Arrays.asList(NewRoughConfig.getStringArray("custom_Knockback_Multiplier", "_Entities", new String[]{"minecraft:slime"}, "_Entities whith modified knockback"));
        knockbackMultiplier = NewRoughConfig.getFloat("knockback_Multiplier", "", 2.2F, 0F, MAX, "Amount of extra knockback entities deal\nCalculated with this value times the slime size\nSet to 0 to disable this feature");

        customFallDamageMultiplier = Arrays.asList(NewRoughConfig.getStringArray("custom_Fall_Damage_Multiplier", "_Entities", new String[]{"minecraft:spider"}, "_Entities which have the %s feature"));
        fallDamageMultiplier = NewRoughConfig.getFloat("fall_Damage_Multiplier", "", 0.0f, 0.0f, MAX, "The fall damage taken by entities is multiplied by this value (0.0 means no fall damage, 1.0 means normal full damage)");

        inflictSlowness = Arrays.asList(NewRoughConfig.getStringArray("inflict_Slowness", "_Entities", new String[]{"minecraft:spider"}, "_Entities which have the %s feature"));
        slownessDuration = NewRoughConfig.getInteger("slowness_Duration", "", 200, 1, MAX, "Duration in ticks of the applied slowness effect (20 ticks = 1 second)");
        slownessChance = NewRoughConfig.getInteger("slowness_Chance", "", 1, 0, MAX, "Chance (1 in X) for a %s to apply the slowness effect on attack\nSet to 0 to disable this feature");
        slownessCreateWeb = NewRoughConfig.getBoolean("slowness_Create_Web", "", true, "Set this to false to prevent %ss from creating webs on slowed targets");

        rideable = Arrays.asList(NewRoughConfig.getStringArray("rideable", "_Entities", new String[]{"minecraft:spider"}, "_Entities which can spawn with hostile mobs on top of them.\nDefaults to and is intended for spiders"));

        applyEffects = Arrays.asList(NewRoughConfig.getStringArray("apply_Effects", "_Entities", new String[]{"minecraft:witch"}, "_Entities which have the %s feature"));
        applyEffectNames = NewRoughConfig.getStringArray("apply_Effect_Names", "", Constants.DEFAULT_WITCH_BUFFS, "Potion effects which may be added to nearby mobs\nFormat: effect;amplifier");
        applyEffectsRange = NewRoughConfig.getFloat("apply_Effects_Range", "", 10, 0, MAX, "Range in each direction from the %ses position in which allied mobs get buffed\nSet to 0 to disable this feature");

        witchLingeringChance = NewRoughConfig.getInteger("witch_Lingering_Chance", "", 5, 0, MAX, "Chance (1 in X) for a %ses thrown potion to become a lingering potion\nSet to 0 to disable this feature");

        customBatsOnDeath = Arrays.asList(NewRoughConfig.getStringArray("custom_Bats_On_Death", "_Entities", new String[]{"minecraft:witch"}, "_Entities which spawn bats on death"));
        batsOnDeath = NewRoughConfig.getInteger("bats_On_Death", "BatsOnDeath", 5, 0, MAX, "Amount of bats which spawn on %ses death\nSet to 0 to disable this feature");

        summonSkeleton = Arrays.asList(NewRoughConfig.getStringArray("summon_Skeleton", "_Entities", new String[]{"minecraft:wither"}, "_Entities which spawn wither skeletons"));
        summonSkeletonTimer = RoughConfig.getInteger("summon_Skeleton_Timer", "", 200, 0, MAX, "Delay in ticks between each spawned Skeleton\nSet to 0 to disable this feature");

        customLeap = Arrays.asList(NewRoughConfig.getStringArray("custom_Leap", "_Entities", new String[]{"minecraft:zombie", "minecraft:endermite"}, "_Entities which can leap"));
        leapChance = NewRoughConfig.getInteger("leap_Chance", "", 5, 0, MAX, "Chance (1 in X) for an eintity to leap to the target\nSet to 0 to disable this feature");
        leapHeight = NewRoughConfig.getFloat("Leap_Height", "", 0.2F, 0, MAX, "Amount of blocks the entity jumps on leap attack");

        inflictHunger = Arrays.asList(NewRoughConfig.getStringArray("inflict_Hunger", "_Entities", new String[]{"minecraft:zombie"}, "_Entities which can inflict hunger when attacking"));
        hungerDuration = NewRoughConfig.getInteger("Hunger_Duration", "", 200, 1, MAX, "Duration in ticks of the applied hunger effect (20 ticks = 1 second)");
        hungerChance = NewRoughConfig.getInteger("Hunger_Chance", "", 1, 0, MAX, "Chance (1 in X) for entities to apply the hunger effect on attack\nSet to 0 to disable this feature");

        spawnWithHorse = Arrays.asList(NewRoughConfig.getStringArray("spawn_With_Horse", "_Entities", new String[]{"minecraft:zombie", "minecraft:skeleton"}, "_Entities which can spawn riding a zombie or skeleton horse"));
        spawnWithHorseChance = NewRoughConfig.getInteger("Horse_Chance", "", 10, 0, MAX, "Chance (1 in X) that a %s spawns riding a %s horse\nSet to 0 to disable this feature");
        spawnWithHorseMinY = NewRoughConfig.getInteger("Horse_Min_Y", "", 63, 0, MAX, "Minimal Y position above %s horses may spawn");

        zombieBabyBurn = NewRoughConfig.getBoolean("zombie_Baby_Burn", "", true, "Set this to false to prevent baby zombies from burning in sunlight");
        zombieHelmetBurn = NewRoughConfig.getBoolean("zombie_Helmet_Burn", "", false, "Set this to true to make all zombies burn in sunlight even if they wear a helmet");

        customBreakBlocks = Arrays.asList(NewRoughConfig.getStringArray("custom_Break_Blocks", "_Entities", new String[]{"minecraft:zombie", "minecraft:shulker", "minecraft:skeleton", "minecraft:chicken"}, "_Entities which can inflict hunger when attacking"));
        breakBlocksSet = NewRoughConfig.getStringArray("zombie_Break_Blocks_Set", "", Constants.DEFAULT_DESTROY_BLOCKS, "Blocks which can be destroyed by zombies if they have no attack target\nDelete all lines to disable this feature");

        pigmanAggressiveTouch = NewRoughConfig.getBoolean("Pigman_Aggressive_Touch", "", false, "Set to false to prevent zombie pigman from getting aggressive if the player touches its hitbox");
        pigmanAlwaysAggressive = NewRoughConfig.getBoolean("Pigman_Always_Aggressive", "", true, "Set to true for zombie pigmen to always be aggressive");
        pigmanAggressiveRange = NewRoughConfig.getInteger("Pigman_Aggressive_Range", "", 0, 0, MAX, "The range at which zombie pigmen will be aggressive to the player.");
        pigmanAggressiveBlockRange = NewRoughConfig.getFloat("Pigman_Aggressive_Block_Range", "", 20, 1, MAX, "Block radius in which zombie pigman get aggressive if the player breaks blocks");
        pigmanAggressiveBlockChance = NewRoughConfig.getInteger("Pigman_Aggressive_Block_Chance", "", 10, 0, MAX, "Chance (1 in X) that a zombie pigman gets aggressive if the player breaks nearby blocks");
    }

    public RoughApplier() {
        MinecraftForge.EVENT_BUS.register(this);

//        FEATURES.add(new ZombieFeatures());
//        FEATURES.add(new SkeletonFeatures());
//        FEATURES.add(new HostileHorseFeatures());
//        FEATURES.add(new CreeperFeatures());
//        FEATURES.add(new SlimeFeatures());
//        FEATURES.add(new EndermanFeatures());
//        FEATURES.add(new SpiderFeatures());
//        FEATURES.add(new WitchFeatures().addPotionHandler(FEATURES));
//        FEATURES.add(new SilverfishFeatures());
//        FEATURES.add(new ZombiePigmanFeatures());
//        FEATURES.add(new BlazeFeatures());
//        FEATURES.add(new GhastFeatures());
//        FEATURES.add(new MagmaCubeFeatures());
//        FEATURES.add(new WitherFeatures());
//        FEATURES.add(new EndermiteFeatures());
//        FEATURES.add(new GuardianFeatures());
    }

    public void preInit() {

//        for (EntityFeatures features : FEATURES)
//            features.preInit();
        rider = new MountHelper.Rider("new_rider", Constants.DEFAULT_SPIDER_RIDERS, 10);

        zombieEquipApplier = new EquipHelper.EquipmentApplier("zombie", 3, 4, 8, 0.5f, 0.085F);
        skeletonEquipApplier = new EquipHelper.EquipmentApplier("skeleton", 3, 4, 8, 0.5f, 0.085F);

        zombieBossApplier = new BossHelper.BossApplier("zombie", 200, 1F, 0.2F, new String[]{"Zombie King", "Flesh King", "Dr. Zomboss", "Azog", "Zon-Goku", "Amy", "Z0mb3y"}) {
            @Override
            public void addBossFeatures(Entity entity) {
                if (!SpawnHelper.disableBabyZombies()) {
                    for (int i = 0; i < 4; i++) {
                        EntityZombie zombieMinion = new EntityZombie(entity.getEntityWorld());
                        zombieMinion.setPosition(entity.posX + Math.random() - Math.random(), entity.posY + Math.random(), entity.posZ + Math.random() - Math.random());
                        zombieMinion.onInitialSpawn(entity.getEntityWorld().getDifficultyForLocation(entity.getPosition()), null);
                        zombieMinion.setChild(true);
                        zombieMinion.getEntityData().setBoolean(BOSS_MINION, true);

                        entity.world.spawnEntity(zombieMinion);
                    }
                }
            }
        };
        skeletonBossApplier = new BossHelper.BossApplier("skeleton", 200, 1F, 0.2F, new String[]{"Lich King", "Skeleton Lord", "Stallord", "Skeletron", "Skeletron Prime", "Krosis", "Wolnir", "Stalmaster"}) {
            @Override
            public void addBossFeatures(Entity entity) {
            }
        };

        rider.initConfigs();

        zombieEquipApplier.initConfig(
                Constants.DEFAULT_MAINHAND,
                Constants.DEFAULT_OFFHAND,
                Constants.DEFAULT_HELMETS,
                Constants.DEFAULT_CHESTPLATES,
                Constants.DEFAULT_LEGGINGS,
                Constants.DEFAULT_BOOTS,
                Constants.DEFAULT_WEAPON_ENCHANTS,
                Constants.DEFAULT_ARMOR_ENCHANTS,
                false
        );

        skeletonEquipApplier.initConfig(
                Constants.SKELETON_MAINHAND,
                Constants.DEFAULT_MAINHAND,
                Constants.DEFAULT_HELMETS,
                Constants.DEFAULT_CHESTPLATES,
                Constants.DEFAULT_LEGGINGS,
                Constants.DEFAULT_BOOTS,
                Constants.DEFAULT_WEAPON_ENCHANTS,
                Constants.DEFAULT_ARMOR_ENCHANTS,
                false
        );

        zombieBossApplier.initConfig();
        skeletonBossApplier.initConfig();

        RoughConfig.loadFeatures();
        NewRoughConfig.loadFeatures();
    }

    public void postInit() {

//        for (EntityFeatures features : FEATURES)
//            features.postInit();

        AttributeHelper.initAttributeOption();

        SpawnHelper.initSpawnOption();
        SpawnHelper.addEntries();

        BossHelper.initGlobalBossConfig();

        TargetHelper.init();

        RoughConfig.saveFeatures();
        NewRoughConfig.saveFeatures();

        immunityItemItem = Item.REGISTRY.getObject(new ResourceLocation(immunityItem));

        rider.postInit();
        effects = FeatureHelper.getPotionsFromNames(applyEffectNames);

        zombieEquipApplier.createPools();
        skeletonEquipApplier.createPools();

        zombieBossApplier.postInit();
        skeletonBossApplier.postInit();

        breakBlocks = FeatureHelper.getBlocksFromNames(breakBlocksSet, Sets.newIdentityHashSet());
    }


    //TODO Move to SpawnHandler class?
    @SubscribeEvent
    // Apply AI features here
    public void onEntitySpawn(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            FogEventHandler.playerRespawned = true;
        }

        // Ignore spawn if on the client side, or if entity is the player.
        if (event.getWorld().isRemote || event.getEntity() instanceof EntityPlayer || !(event.getEntity() instanceof EntityLiving))
            return;

        EntityLiving entityLiving = (EntityLiving) event.getEntity();
        Entity entity = event.getEntity();
        boolean isBoss = entityLiving instanceof IBoss;
        boolean canSpawn = SpawnHelper.checkSpawnConditions(event);

        // If enabled and the entity can spawn, add additional targets from config
        // Also add additional targets if the entity is ignoring spawn conditions
        if (TargetHelper.targetAttackerEnabled() && canSpawn || TargetHelper.targetAttackerEnabled() && TargetHelper.ignoreSpawnConditions)
            TargetHelper.setTargets(entityLiving);

        // If entity failed spawn conditions, and isn't a boss, then exit event and spawn vanilla mob with no features added
        if (!isBoss && !canSpawn)
            return;

        if (entityLiving == null || entity == null)
            return;

        getGameStages(entityLiving.world.getClosestPlayerToEntity(entityLiving, -1.0D));
        addAttributes(entityLiving);

        if (entity instanceof EntityPlayer) {
            return;
        }
//        addFeatures(event, entityLiving);

        EntityAITasks tasks = ((EntityLiving) entity).tasks;
        EntityAITasks targetTasks = ((EntityLiving) entity).targetTasks;

        ResourceLocation loc = EntityList.getKey(entity);
        if (loc == null) return;

        if (invisible.contains(loc.toString()) && invisibleRange > 0)
            tasks.addTask(2, new RoughAIInvisibleTarget((EntityLiving) entity, invisibleRange));

        // crashes the game for some reason
        if (burnInSunlight.contains(loc.toString()))
            tasks.addTask(0, new RoughAISunlightBurn((EntityLiving) entity, false));

        if (entity instanceof EntityGhast) {
            if (customExplosionRadius.contains(loc.toString()) && explosionRadius != 1)
                ReflectionHelper.setPrivateValue(EntityGhast.class, (EntityGhast) entity, explosionRadius, 1);
        }

        if (dropTnt.contains(loc.toString()))
            tasks.addTask(1, new RoughAIDropTNT((EntityLiving) entity));

        if (witherMobs.contains(loc.toString()))
            tasks.addTask(1, new RoughAIAddEffect((EntityLiving) entity, MobEffects.WITHER, 6));

        if (entity instanceof EntityCreeper) {
            if (creeperCustomFuseTime.contains(loc.toString()) && creeperFuseTime != 30)
                ReflectionHelper.setPrivateValue(EntityCreeper.class, (EntityCreeper) entity, creeperFuseTime, 5);

            if (creeperCustomExplosionRadius.contains(loc.toString()) && creeperExplosionRadius != 3)
                ReflectionHelper.setPrivateValue(EntityCreeper.class, (EntityCreeper) entity, creeperExplosionRadius, 6);

            if (creeperBurnExplosion.contains(loc.toString()))
                tasks.addTask(0, new RoughAIBurnExplosion((EntityCreeper) entity));
        }

        // Hostile horse features
        if (entity instanceof EntitySkeletonHorse || entity instanceof EntityZombieHorse) {
            if (randomRiderChance > 0)
                tasks.addTask(1, new RoughAISearchForRider((EntityLiving) entity, getRiders(entity), 32, randomRiderChance));

            if (!preventHorseDespawn && entity.getEntityData().getBoolean(ROUGH_HORSE))
                tasks.addTask(1, new RoughAIDespawn((EntityLivingBase) entity));

            if (horseBurn && !entity.isImmuneToFire())
                tasks.addTask(0, new RoughAISunlightBurn((EntityLiving) entity, false) {
                    @Override
                    public boolean shouldExecute() {
                        return super.shouldExecute() && !entity.isBeingRidden();
                    }
                });
        }

        if (regenInLava.contains(loc.toString()))
            tasks.addTask(1, new RoughAILavaRegen((EntityLiving) entity));

        // Attempt to populate rideable entities
        if (rideable.contains(loc.toString())) {
            rider.tryAddRider((EntityLivingBase) entity);
            rider.addAI((EntityLiving) entity);
        }

        // Attempt to give the entity a friend to keep them company
        if (spawnWithHorse.contains(loc.toString()) && spawnWithHorseChance > 0) {
            MountHelper.HorseType type;
            if (RND.nextInt(2) == 0) {
                type = MountHelper.HorseType.ZOMBIE;
            } else {
                type = MountHelper.HorseType.SKELETON;
            }
            MountHelper.tryMountHorse(entity, type, spawnWithHorseChance, spawnWithHorseMinY);
        }

        // Implement support classes
        if (applyEffects.contains(loc.toString()) && applyEffectsRange > 0 && !effects.isEmpty())
            tasks.addTask(1, new RoughAIMobBuff((EntityLivingBase) entity, effects, applyEffectsRange));

        // Change pot entities to lingering ones
        if (entity instanceof EntityPotion && (((EntityPotion) entity).getThrower() instanceof EntityWitch) && RND.nextInt(witchLingeringChance) == 0) {
            ItemStack potion = new ItemStack(Items.LINGERING_POTION);
            PotionUtils.addPotionToItemStack(potion, PotionUtils.getPotionFromItem(((EntityPotion) entity).getPotion()));
            ((EntityPotion) entity).setItem(potion);
        }

        if (summonSkeleton.contains(loc.toString()) && summonSkeletonTimer > 0)
            tasks.addTask(1, new RoughAISummonSkeleton((EntityLiving) entity, summonSkeletonTimer, true));

        if (customLeap.contains(loc.toString()) && leapChance > 0)
            tasks.addTask(1, new RoughAILeapAtTargetChanced((EntityLiving) entity, leapHeight, leapChance));

        // Zombie features
        if (entity instanceof EntityZombie) {
            if (!entity.isImmuneToFire()) {
                if (zombieBabyBurn && ((EntityZombie) entity).isChild())
                    tasks.addTask(0, new RoughAISunlightBurn((EntityLiving) entity, false));
//
                if (zombieHelmetBurn && !entity.isImmuneToFire())
                    tasks.addTask(0, new RoughAISunlightBurn((EntityLiving) entity, true));
            }
        }

        // Skeleton features
        if (entity instanceof EntitySkeleton) {
            if (skeletonChangeWeapons && entity instanceof EntityLiving)
                targetTasks.addTask(1, new RoughAIWeaponSwitch((EntityLiving) entity, 12D));

            if (skeletonHelmetBurn)
                tasks.addTask(0, new RoughAISunlightBurn((EntityLiving) entity, true));

            if (entity instanceof AbstractSkeleton) {
                EntityAIAttackRangedBow<AbstractSkeleton> ai = ReflectionHelper.getPrivateValue(AbstractSkeleton.class, (AbstractSkeleton) entity, 1);
                if (ai != null)
                    ai.setAttackCooldown(skeletonBowCooldown);
            }
        }

        // Pigman features
        if (entity instanceof EntityPigZombie) {
            if (pigmanAggressiveTouch)
                tasks.addTask(1, new RoughAIAggressiveTouch((EntityLiving) entity));

            if (pigmanAlwaysAggressive)
                tasks.addTask(3, new RoughAIAlwaysAggressive((EntityLiving) entity, pigmanAggressiveRange));
        }

        // Allow mobs to break blocks
        if (customBreakBlocks.contains(loc.toString()) && !breakBlocks.isEmpty())
            tasks.addTask(1, new RoughAIBreakBlocks((EntityLiving) entity, 8, breakBlocks));

        // Handle bosses
        if (entity instanceof EntityZombie && bossesEnabled(entity)) {
            Entity boss = zombieBossApplier.trySetBoss(entity);
            if (boss != null) {
                entity = boss;
                event.setCanceled(true);
            } else
                zombieEquipApplier.equipEntity((EntityLiving) entity);

        } else if (entity instanceof EntitySkeleton && bossesEnabled(entity)) {
            Entity boss = skeletonBossApplier.trySetBoss(entity);
            if (boss != null) {
                entity = boss;
                event.setCanceled(true);
            } else
                skeletonEquipApplier.equipEntity((EntityLiving) entity);
        }
    }

    @SubscribeEvent
    public void onAttack(LivingAttackEvent event) {

        Entity trueSource = event.getSource().getTrueSource();
        Entity immediateSource = event.getSource().getImmediateSource();
        Entity target = event.getEntity();

        ResourceLocation loc;

        // trueSource is not a player - it must be an attacking mob. Target is an attacked player
        if (trueSource instanceof EntityLiving) {
            if (trueSource == null || target == null || target instanceof FakePlayer || trueSource instanceof FakePlayer || immediateSource instanceof FakePlayer || target.world.isRemote)
                return;

            loc = EntityList.getKey(trueSource);

            // Offense - make sure the victim exists and the target is not a player in creative mode
            if (blind.contains(loc.toString()) && blindChance > 0 && RND.nextInt(blindChance) == 0) {
                ((EntityLivingBase) target).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, blindDuration));
            }
//
            if (stealItem.contains(loc.toString()) && stealItemChance > 0 && RND.nextInt(stealItemChance) == 0) {
                if (isImmuneToTeleport(target)) {
                    FeatureHelper.playSound(target, SoundEvents.ENTITY_ENDEREYE_DEATH);
                } else
                    tryDropHeldItem((EntityLivingBase) target, trueSource);
            }

            if (customKnockbackMultiplier.contains(loc.toString()) && knockbackMultiplier > 0) {
                FeatureHelper.knockback(trueSource, (EntityLivingBase) target, Math.max(getSlimeSize(target), 1) * knockbackMultiplier, 0.3F);
            }

            if (inflictSlowness.contains(loc.toString()) && target instanceof EntityLivingBase && slownessChance > 0) {
                EntityLivingBase living = (EntityLivingBase) target;
                int maxAmp = 4;

                FeatureHelper.addEffect(living, MobEffects.SLOWNESS, slownessDuration, 0, slownessChance, true, maxAmp);
                PotionEffect active = living.getActivePotionEffect(MobEffects.SLOWNESS);

                if (slownessCreateWeb && active != null && active.getAmplifier() >= maxAmp && RND.nextInt(slownessChance) == 0 && target.getEntityWorld().getBlockState(target.getPosition()).getBlock() == Blocks.AIR) {
                    target.getEntityWorld().setBlockState(target.getPosition(), Blocks.WEB.getDefaultState());
                }
            }

            if (inflictHunger.contains(loc.toString()) && hungerChance > 0)
                FeatureHelper.addEffect((EntityLivingBase) target, MobEffects.HUNGER, hungerDuration, 0, hungerChance, true, 4);

            // target is not a player - it must be an attacked mob
        } else if (!(target instanceof EntityPlayer)) {
            loc = EntityList.getKey(target);
            // Defense - make sure the target exists and the attacker is not a player in creative mode
            if (loc == null) return;
            if (target instanceof EntityLiving && trueSource instanceof EntityPlayer && !((EntityPlayer) trueSource).isCreative()) {
                if (flameTouch.contains(loc.toString()))
                    trueSource.setFire(8);

                if (pushAttackersAway.contains(loc.toString()) && trueSource == immediateSource) {
                    FeatureHelper.knockback(target, (EntityLivingBase) trueSource, 1F, 0.05F);
                    trueSource.attackEntityFrom(DamageSource.GENERIC, 0);

                    if (target instanceof EntityWither)
                        FeatureHelper.playSound(target, SoundEvents.ENTITY_WITHER_BREAK_BLOCK, 0.7f, 1.0f);
                    else
                        FeatureHelper.playSound(target, SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.7f, 1.0f);

                }

                if (teleportAttacker.contains(loc.toString()) && trueSource instanceof EntityLivingBase) {
                    if (isImmuneToTeleport(trueSource)) {
                        FeatureHelper.playSound(trueSource, SoundEvents.ENTITY_ENDEREYE_DEATH);
                    } else
                        teleportRandom((EntityLivingBase) trueSource, 24);
                }

                float health = ((EntityLiving) target).getHealth();

                // Server desync issues exist without this line
                if (target.world.isRemote) return;

                if (split.contains(loc.toString()) && splitChance > 0 && health > 0 && target.getEntityWorld().rand.nextInt(splitChance) == 0) {
                    try {
                        EntityLiving new_mob = (EntityLiving) target.getClass().getConstructor(World.class).newInstance(target.getEntityWorld());
                        new_mob.setPosition(target.posX, target.posY, target.posZ);
                        new_mob.onInitialSpawn(target.getEntityWorld().getDifficultyForLocation(target.getPosition()), null);
                        new_mob.setHealth(health);

                        target.world.spawnEntity(new_mob);
                    } catch (Exception e) {
                        RoughMobs.logger.error("Couldn't create an entity from default constructor: " + target.getName());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {

        Entity deadEntity = event.getEntity();
        Entity trueSource = event.getSource().getTrueSource();

        ResourceLocation loc = EntityList.getKey(deadEntity);
        if (loc == null) return;

        if (deadEntity == null || deadEntity.world.isRemote || !(deadEntity instanceof EntityLiving) || (trueSource instanceof FakePlayer))
            return;

        if (deathExplosionStrength > 0 && explodeOnDeath.contains(loc.toString())) {
            deadEntity.world.createExplosion(deadEntity, deadEntity.posX, deadEntity.posY, deadEntity.posZ, deathExplosionStrength, deathExplosionDestroyBlocks);
        }

        if (dropEnderpearl.contains(loc.toString())) {
            deadEntity.dropItem(Items.ENDER_PEARL, 1);
        }

        if (dropWater.contains(loc.toString())) {
            BlockPos pos1 = deadEntity.getPosition();
            BlockPos pos2 = deadEntity.getPosition().up();

            if (deadEntity.world.getBlockState(pos1).getBlock() == Blocks.AIR)
                deadEntity.world.setBlockState(pos1, Blocks.WATER.getDefaultState(), 11);

            if (deadEntity.world.getBlockState(pos2).getBlock() == Blocks.AIR)
                deadEntity.world.setBlockState(pos2, Blocks.FLOWING_WATER.getStateFromMeta(1));
        }

        if (dropLava.contains(loc.toString()) && (!(deadEntity instanceof EntitySlime) || ((EntitySlime) deadEntity).getSlimeSize() <= 1)) {
            BlockPos pos1 = deadEntity.getPosition();
            BlockPos pos2 = deadEntity.getPosition().up();

            if (deadEntity.world.getBlockState(pos1).getBlock() == Blocks.AIR)
                deadEntity.world.setBlockState(pos1, Blocks.FLOWING_LAVA.getStateFromMeta(11));

            if (deadEntity.world.getBlockState(pos2).getBlock() == Blocks.AIR)
                deadEntity.world.setBlockState(pos2, Blocks.FLOWING_LAVA.getStateFromMeta(1));
        }

        if (customBatsOnDeath.contains(loc.toString())) {
            for (int i = 0; i < batsOnDeath; i++) {
                EntityBat bat = new EntityBat(deadEntity.getEntityWorld());
                bat.setPosition(deadEntity.posX + Math.random() - Math.random(), deadEntity.posY + Math.random(), deadEntity.posZ + Math.random() - Math.random());
                bat.onInitialSpawn(deadEntity.getEntityWorld().getDifficultyForLocation(deadEntity.getPosition()), null);

                deadEntity.getEntityWorld().spawnEntity(bat);
            }
        }
    }

    @SubscribeEvent
    public void onFall(LivingFallEvent event) {

        Entity entity = event.getEntity();

        ResourceLocation loc = EntityList.getKey(entity);
        if (loc == null) return;

        if (entity == null || entity.world.isRemote || !(entity instanceof EntityLiving))
            return;

        if (customFallDamageMultiplier.contains(loc.toString())) {
            if (fallDamageMultiplier == 1)
                return;

            if (fallDamageMultiplier == 0)
                event.setCanceled(true);
            else {
                event.setDamageMultiplier(event.getDamageMultiplier() * fallDamageMultiplier);
            }
        }


//        for (EntityFeatures features : FEATURES) {
//            if (features.isEntity((EntityLiving) entity)) {
//                features.onFall((EntityLiving) entity, event);
//            }
//        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {

        EntityPlayer player = event.getPlayer();

        ResourceLocation loc = EntityList.getKey(player);
//        if (loc == null) return;

        if (player == null || player.world.isRemote || player.isCreative())
            return;

        AxisAlignedBB where = new AxisAlignedBB(event.getPos()).grow(pigmanAggressiveBlockRange);

//        List<Entity> entities = player.world.getEntitiesWithinAABB(EntityPigZombie.class, player.getEntityBoundingBox().expand(pigmanAggressiveBlockRange, pigmanAggressiveBlockRange, pigmanAggressiveBlockRange));
        List<Entity> entities = player.world.getEntitiesWithinAABB(EntityPigZombie.class, where);

        for (Entity entity : entities) {
            if (pigmanAggressiveBlockChance > 0 && entity instanceof EntityLiving && RND.nextInt(pigmanAggressiveBlockChance) == 0) {
                EntityLiving living = (EntityLiving) entity;
                living.setAttackTarget(player);
                living.setRevengeTarget(player);
                FeatureHelper.playSound(living, SoundEvents.ENTITY_ZOMBIE_PIG_ANGRY);
            }
        }

    }

    /*
     * When an entity targets another entity
     */
    @SubscribeEvent
    public void onTarget(LivingSetAttackTargetEvent event) {

        if (!TargetHelper.targetBlockerEnabled() || event.getTarget() == null || !(event.getTarget() instanceof EntityLiving) || !(event.getEntityLiving() instanceof EntityLiving))
            return;

        // Get the invalid attacker of the victim/target
        // The invalid attacker cannot attack the target
        Class<? extends Entity> invalidAttacker = TargetHelper.getBlockerEntityForTarget(event.getTarget());

        if (invalidAttacker != null && invalidAttacker.isInstance(event.getEntityLiving())) {
            EntityPlayer player = event.getEntityLiving().getEntityWorld().getNearestAttackablePlayer(event.getEntityLiving(), 32, 32);

            if (player != null && player.isOnSameTeam(event.getEntityLiving()))
                return;

            event.getEntityLiving().setRevengeTarget(player);
            ((EntityLiving) event.getEntityLiving()).setAttackTarget(player);
        }

        // else if invalidAttacker = null
        // we currently do nothing
        System.out.println("InvalidAttacker = null; do nothing");
    }

    /*
     * Test if Game Stages is loaded, if any stages are enabled, and if the player has the stages
     */
    private void getGameStages(EntityPlayer player) {

        gameStagesEnabled = CompatHandler.isGameStagesLoaded();
        abilsStageEnabled = GameStagesCompat.useAbilitiesStage();
        equipStageEnabled = GameStagesCompat.useEquipmentStage();

        // Test to see if player has these stages unlocked.
        if (gameStagesEnabled) {
            playerHasEquipStage = GameStageHelper.hasAnyOf(player, Constants.ROUGHMOBSALL, Constants.ROUGHMOBSEQUIP);
            playerHasAbilsStage = GameStageHelper.hasAnyOf(player, Constants.ROUGHMOBSALL, Constants.ROUGHMOBSABILS);
        } else {
            playerHasEquipStage = playerHasAbilsStage = false;
        }
    }

    private void addAttributes(EntityLiving entity) {
        // Test to see if abilities stage is disabled or if it is enabled and player has it
        if (abilsStageEnabled == false || abilsStageEnabled && playerHasAbilsStage) {

            if (entity instanceof EntityLiving)
                AttributeHelper.addAttributes((EntityLiving) entity);
        }
    }

    /*
     * Add equipment and enchantments, and AI
     * Also try to create bosses
     */
    private void addFeatures(EntityJoinWorldEvent event, EntityLiving entity) {

        boolean isBoss = entity instanceof IBoss;

        if (entity.getClass().equals(EntityPlayer.class)) {
            RoughMobs.logger.debug("Entity is player...skipping addFeatures");
            return;
        }

        // Loop through the features list and add equipment and AI to the entity
        for (EntityFeatures features : FEATURES) {
            if (features.isEntity(entity)) {
                // Don't attempt to add equipment to a boss. It has already been given equipment in the BossApplier class
                // Also test if baby zombies should have equipment
                if (!isBoss || ((EntityLiving) entity).isChild() && EquipHelper.disableBabyZombieEquipment() == false) {
                    // Test to see if equip stage is disabled or if it is enabled and player has it
                    if (equipStageEnabled == false || equipStageEnabled && playerHasEquipStage) {

                        if (!entity.getEntityData().getBoolean(FEATURES_APPLIED))
                            features.addFeatures(event, entity);
                    }
                }

                // Test to see if abilities stage is disabled or if it is enabled and player has it
                if (abilsStageEnabled == false || abilsStageEnabled && playerHasAbilsStage) {

                    if (entity instanceof EntityLiving)
                        features.addAI(event, entity, ((EntityLiving) entity).tasks, ((EntityLiving) entity).targetTasks);
                }
            }
        }
    }

    //TODO Move this to fog handler?
    @SubscribeEvent
    public void onEntityHurt(LivingAttackEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if (!player.isCreative()) {
                if (event.getSource().equals(DamageSourceFog.POISONOUS_FOG)) {

                    if (BossHelper.bossFogPlayerCough)
                        playHurtSound(player);

                    player.setHealth(player.getHealth() - BossHelper.bossFogDoTDamage);
                    event.setCanceled(true);
                }
            }
        }
    }

    private void playHurtSound(EntityPlayer player) {
        player.world.playSound(null, player.getPosition(), SoundHandler.ENTITY_PLAYER_COUGH, SoundCategory.PLAYERS, 1.0F, (float) Math.max(0.75, Math.random()));
    }

    /*
     * When an entity spawns, we do all the magic, such as adding equipment and AI, trying to turn it into a boss, etc.
     */

    public static boolean teleportRandom(EntityLivingBase entity, double multi) {

        if (entity == null || entity instanceof FakePlayer || entity.isDead)
            return false;

        double x = entity.posX + (RND.nextDouble() - 0.5D) * multi;
        double y = entity.posY + (RND.nextInt(64) - 32);
        double z = entity.posZ + (RND.nextDouble() - 0.5D) * multi;

        EnderTeleportEvent event = new EnderTeleportEvent(entity, x, y, z, 0);

        if (MinecraftForge.EVENT_BUS.post(event) || event == null || entity == null)
            return false;

        boolean flag = entity.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

        if (flag) {
            FeatureHelper.playSound(entity, SoundEvents.ENTITY_ENDERMEN_TELEPORT);
        }

        return flag;
    }

    private boolean isImmuneToTeleport(Entity attacker) {
        return immunityItemItem != null && attacker instanceof EntityPlayer && ((EntityPlayer) attacker).inventory.hasItemStack(new ItemStack(immunityItemItem));
    }

    private void tryDropHeldItem(EntityLivingBase target, Entity attacker) {

        List<EnumHand> filledHands = new ArrayList<EnumHand>();

        if (!target.getHeldItemMainhand().isEmpty())
            filledHands.add(EnumHand.MAIN_HAND);

        if (!target.getHeldItemOffhand().isEmpty())
            filledHands.add(EnumHand.OFF_HAND);

        if (filledHands.isEmpty())
            return;

        EnumHand hand = filledHands.get(RND.nextInt(filledHands.size()));
        ItemStack heldStack = target.getHeldItem(hand).copy();
        target.setHeldItem(hand, ItemStack.EMPTY);
        attacker.entityDropItem(heldStack, (float) (Math.random() + 0.5f));
    }

    private List<Class<? extends Entity>> getRiders(Entity entity) {

        List<Class<? extends Entity>> riders = new ArrayList<>();

        if (entity instanceof EntitySkeletonHorse)
            riders.add(AbstractSkeleton.class);
        else
            riders.add(EntityZombie.class);

        return riders;
    }

    private int getSlimeSize(Entity entity) {
        return entity instanceof EntitySlime ? ((EntitySlime) entity).getSlimeSize() : 2;
    }

    public boolean bossesEnabled(Entity entity) {

        boolean bossStageEnabled = GameStagesCompat.useBossStage();

        if (bossStageEnabled) {
            EntityPlayer playerClosest = entity.world.getClosestPlayerToEntity(entity, -1.0D);
            return (GameStageHelper.hasAnyOf(playerClosest, Constants.ROUGHMOBSALL, Constants.ROUGHMOBSBOSS));
        }

        // If boss game stage isn't enabled, then its ok to spawn bosses
        return true;
    }
}