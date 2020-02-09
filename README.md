# Rough-Mobs-Revamped
This is a revamp/bug fixed version of Rough Mobs 2 for Minecraft 1.12.2, originally by Lellson. Thanks to Lellson for open sourcing the mod.

* Compiled against Forge version: 1.12.2-14.23.5.2768 (snapshop_20171003)

## Features

* All the features of Rough Mobs 2 version 2.0.15
* GameStages support - 5 stages (enable each individually, or use roughmobsall to enable them all) (configurable)
* Only spawn if player's Minecraft Exp Level is high enough (configurable)
* Only spawn if underground (toggleable)
* Only spawn if below max spawn height (configurable)
* Only spawn if at least X distance from world spawn (configurable)

#### _Planned_

* Serene Seasons support - only spawn with equipment in specified season (toggleable)
* Player level affects equipment spawn chance (toggleable)
* Boss monster spawn chance increases based upon number of regular Rough Mobs have been killed.
* Add levels to rough mobs (higher levels = more hp/more dps)
* Add shield blocking
* In game commands to determine current rough mob spawn conditions (op only)
* Version checking chat output with config option to disable
* Zombie Pigmen always aggressive (configurable)

#### _In Development_

* 1.14.4 version

## Links

* Mod on CurseForge: https://www.curseforge.com/minecraft/mc-mods/rough-mobs-revamped
* Original mod: https://www.curseforge.com/minecraft/mc-mods/rough-mobs-2

## FAQ

**Can you update to Minecraft version 1.14, 1.15, etc.?**  
I'm working on it. 

**Can you add a feature?**  
Again I might. Feel free to make a request.  

**Can you fix this bug?**  
Please report all issues on my GitHub Issue Tracker.  

**What version of Forge is this compiled against?**  
Forge version 14.23.5.2768  

**How do I use Game Stages?**  
Required: GameStages mod. First, you must enable the GameStages_FeaturesEnabled option in the config, if you don't, rough mobs will spawn regardless of game stages. After that, you can enable each stage you want to use in the config, or simply enable the GameStages_AllStages option to enable all of them at the same time. Now, you must give the player the stages at runtime(via quests, achievements, etc.) in order for rough mobs to spawn near them. The names of each stage to be used in game are found in the config file's gamestages comment section. Adding a game stage can be tested with: /gamestage add @p roughmobsequip  

**Do I keep the old Rough Mobs 2 mod?**  
No. Delete that mod, and use this one instead. It has all the same functionality, and will conflict with the old version.  

**Can I use the old Rough Mobs 2 config file?**  
This mod creates its own config file (roughmobsrevamped.cfg). You can simply copy the contents of the old config into the new one. But new version of Rough Mobs Revamped have new config options that need to generate in a new file.  

**How do I add modded items to the config?**  
By using: modid:itemname;weight - Here's an example config for More Swords Legacy swords:  

S:zombiebossEquipmentMainhand <  
    msmlegacy:draconic_blade;10  
    msmlegacy:vampiric_blade;10  
    msmlegacy:relic_molten;10  
    msmlegacy:wither_bane;10  
>
