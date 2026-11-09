# Sunrise Gardening
Sunrise Gardening is an overhaul mod for growth and farming mechanics in Minecraft to closer match farming games like Stardew Valley. It changes many vanilla farming mechanics and adds supporting blocks and items for them such as Sprinklers.

This mod was made for modpacks and is highly configurable using both datapacks and actual configs. Don

It is the spiritual successor of [Dew Drop: Sturdy Farmland - Growth Edition](https://modrinth.com/mod/sturdy-farmland-growth-edition) as its gone from a fork to its own project that no longer resembles the original.
## Features

### Crop Growth Changes

**Daily Growth**
Every morning, crops will grow by 1 stage if they are watered. This is calculated using Serene Season's season system, and has additional support for other mods. This mod also disables random ticks of all crops with the tag `#sunrise_gardening:cancel_random_tick`.

**Growth Caching**
Chunk loading fields and fields of crops is a hassle, so Sunrise Gardening keeps track of when crops last grew and catches up crops based on the number of days that have passed since its grown.

**Growth Customization**
Crops no longer have to be tied to the amount of blockstates! The `crop_grow_times` datamap allows you to specify how long a given crop takes to grow.

**Single and Multi-harvest customization**
Crops can be designated as single harvest or multi-harvest, going back to a set age like Sweetberry Bushes. This can be customized using the `multi_harvest_crops` datamap and `sunrise_gardening:single_harvest_crop` tag.

This requires Quark installed for now.

**Other Features**
- **Crops Require Sun** - Crops now require sky access to grow.
- **Permanent Season Biomes** - Serene Seasons only supports always winter and always summer biomes. Sunrise Gardening adds onto this, adding a `permanant_season_biomes` datamap for permanent autumn and spring biomes!
- **Area of Effect tilling** - Hoes can be used to till a large area of dirt when crouching.
- **Crop Growth Jade info and Tooltips** - When Jade is installed, crops will show their growth information. Seeds will always have tooltips on a given crops grow time and harvest data.
### Fertilized Farmland

To pair with the new growth system, six new fertilizers are added:
*   Weak Fertilizer - Boosts initial crop growth by 10%
*   Strong Fertilizer - Boosts initial crop growth by 25%
*   Hyper Fertilizer - Boosts initial crop growth by 33%
*   Hydrating Fertilizer - Does not dry in the morning until halfway grown (rounded down)
*   Deluxe Hydrating Fertilizer - Does not dry
*   Bountiful Fertilizer - A blank canvas for modpack makers to add their own mechanics
*   Low Quality Fertilizer - Blank farmland with a tag for Quality Food
*   High Quality Fertilizer - Theoretical stronger version of the above
*   Pristine Quality Fertilizer - Strongest version of the above

### Garden Pots
Garden Pots can only be manually watered but will grow crop during any season (if Serene Seasons is installed). Does not break crop when not watered and can be upgraded with Hydrating Fertilizer. Includes hanging variants when placed

### Using Quality Fertilizers

If using the [Quality Food](https://www.curseforge.com/minecraft/mc-mods/quality-food) mod, you must update the configuration for them to have an affect. This can be done using the `farmland_config` like this:

```
farmland_config = [ "2;#minecraft:crops;#sunrisegardening:low_quality_fertilized_farmland;1.5", "2;#minecraft:crops;#sunrisegardening:high_quality_fertilized_farmland;2","2;#minecraft:crops;#sunrisegardening:pristine_quality_fertilized_farmland;3"]
```

### Sprinklers
This mod comes with 4 tiers of Sprinklers, Iron, Gold, Diamond, Netherite. Sprinklers can be enabled in the server config using `checkSprinklers = true` (on by default)

When enabled, farmland will check for a nearby sprinkler on the same or one level above. This is done using the `sprinkler_size` datamap.

### Greenhouse Glass
Sunrise Gardening adds a blockset for Greenhouse Glass that override Serene Seasons more permissive tag. Seasons are no longer as easy to ignore!
## Dependencies (Required Versions)

This mod has the following dependencies and has been tested with the following versions:

*   [Bot's Lib](https://www.curseforge.com/minecraft/mc-mods/bots-lib) version 4.0 
*   [Serene Seasons](https://www.curseforge.com/minecraft/mc-mods/serene-seasons) version 9.1.0.0

## Mod Support 
**Note, mod support is WIP, not all those on the list are implemented yet**
Additional support for "Weird" crops that have additional logic is provided. If you have a mod crop you would like supported, list them out in the comments and I'll do my best to accommodate.
### Implemented
* Serene Seasons
* Farmer's Delight (Tomato, Rice Crop)
* Vintage Delight (Gearo Berry Bush)
* Supplementaries (Flax Crop)
* Cultural Delights (Corn Crop)
* Windswept! (Wild Berry Bush)

### Work in progress
* Cobblemon Berry Bushes
* Haunted Harvest (Corn crop)
* Let's Do: Vinery
* Flowerary version (Tall flower crops) (Not on 1.21.1)
## Credits
- Greenhouse Glass texture from [More Minecarts](https://github.com/ryyanmapes/minecart-mod) (MIT)
- Art by Nitbe
- [Dew Drop: Sturdy Farmland](https://github.com/coolbot100s/Dew-Drop-Farmland) by [coolbot100s](https://github.com/coolbot100s)
## Support

Questions? Comments? Suggestions? Join our [Discord](https://discord.gg/GdcZkrZqkj)!