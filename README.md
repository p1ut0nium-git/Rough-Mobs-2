# Rough-Mobs-Revamped
[![Generic badge](https://img.shields.io/badge/version-1.12.2-orange.svg)](https://shields.io/) 
[![GitHub issues](https://img.shields.io/github/issues/p1ut0nium-git/Rough-Mobs-Revamped)](https://github.com/p1ut0nium-git/Rough-Mobs-Revamped/issues/) 
[![GPLv3 license](https://img.shields.io/badge/License-GPLv3-blue.svg)](http://perso.crans.org/besson/LICENSE.html)  

This is a revamp of Rough Mobs 2 for Minecraft 1.12.2, originally by Lellson. Thanks to Lellson for open sourcing the mod.

* Compiled against Forge version: 1.12.2-14.23.5.2768 (snapshot_20171003)

## Features

* All the features of Rough Mobs 2 +
* GameStages support - 5 stages (enable each individually, or use roughmobsall to enable them all) (configurable)
* Serene Seasons support - only spawn rough mobs in specified season (toggleable) ("Winter is coming!")
* Only spawn if player's Minecraft Exp Level is high enough (configurable)
* Only spawn if underground (toggleable)
* Only spawn if below max spawn height (configurable)
* Only spawn if at least X distance from world spawn (configurable)
* Zombie Pigmen always aggressive (configurable)
* Display boss mob spawn warning in chat (toggleable)

***Planned***

* Boss monster spawn chance increases based upon number of regular Rough Mobs have been killed.
* Add levels to rough mobs (higher levels = more hp/more dps)
* Add shield blocking (possibly)
* In-game commands
* Version checking chat output with config option to disable
* In-game config
* Spawn replacement (convert mob type into another mob type on spawn)
* Configure mob to always attack list of other mobs (zombies attack pigs, etc.)
* Zombies use bows
* Giants?
* Additional mob abilities & AI

***In Development***

* 1.14.4 version
<br/>
<br/>
<br/>


## Links

* Mod on CurseForge: https://www.curseforge.com/minecraft/mc-mods/rough-mobs-revamped
* Original mod: https://www.curseforge.com/minecraft/mc-mods/rough-mobs-2
<br/>
<br/>
<br/>


## FAQ

**Can you update to Minecraft version 1.14, 1.15, etc.?**  
I'm working on it.

**Can you add a feature?**  
I might. Feel free to make a request.  

**Can you fix this bug?**  
Please report all issues on my GitHub Issue Tracker.  

**What version of Forge is this compiled against?**   
The 1.12.2 version is compiled against Forge version 14.23.5.2768  

**How do I use Game Stages?**  
Required: GameStages mod. You can enable each stage you want to use in the config, or simply enable the GameStages_AllStages option to enable all of them at the same time. Now, you must give the player the enabled stages at runtime(via quests, achievements, etc.) in order for rough mobs to spawn near them. The names of each stage to be used in game are found in the config file's gamestages comment section. Adding a game stage can be tested with: /gamestage add @p roughmobsequip  

**Do I keep the old Rough Mobs 2 mod?**  
No. Delete that mod, and use this one instead. It has all the same functionality, and will conflict with the old version.  

**Can I use the old Rough Mobs 2 config file?**  
This mod creates its own config file (roughmobsrevamped.cfg). You can simply copy the contents of the old config into the new one. But new version of Rough Mobs Revamped have new config options that need to generate in a new file.  

![#f03c15](https://placehold.it/50x25/f03c15/000000?text=NOTE:) _When new config options are added in new versions, you must backup/copy your old config, then delete it in the config folder. Let Rough Mobs generate a new config file, then copy your old config options into the new file, being careful not to delete the new config options._

**How do I add modded items to the config?**  
By using: modid:itemname;weight;<optional: dimension> - Here's an example config for More Swords Legacy swords:  

```
# These equipped in any dimension (dim specifier left blank)  
S:zombieEquipmentMainhand <  
    msmlegacy:draconic_blade;3  
    msmlegacy:vampiric_blade;2   
    msmlegacy:relic_molten;1   
    msmlegacy:wither_bane;2   
>

# These only equipped in the Nether (dim -1)  
S:zombieEquipmentOffhand <  
spartanshields:shield_basic_stone;3;-1  
spartanshields:shield_basic_gold;1;-1  
spartanshields:shield_basic_iron;2;-1  
spartanshields:shield_basic_constantan;1;-1  
>
 ```
