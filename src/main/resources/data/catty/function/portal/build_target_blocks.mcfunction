# Executor: at the scout/portal position in TARGET dim
# Carve clearance to avoid burying portal in solid terrain (5x4x5 air)
fill ~-2 ~ ~-2 ~2 ~3 ~2 minecraft:air

# Stone safety platform 3 blocks below (only if originally air, not destroying terrain)
fill ~-2 ~-3 ~-2 ~2 ~-3 ~2 minecraft:stone replace minecraft:air
fill ~-2 ~-3 ~-2 ~2 ~-3 ~2 minecraft:stone replace minecraft:cave_air
fill ~-2 ~-3 ~-2 ~2 ~-3 ~2 minecraft:stone replace minecraft:void_air

# Portal core
setblock ~ ~ ~ twilightforest:twilight_portal
setblock ~ ~-1 ~ minecraft:glowstone
setblock ~ ~-2 ~ minecraft:sea_lantern

# Crying obsidian frame (4 cardinal)
setblock ~1 ~ ~ minecraft:crying_obsidian
setblock ~-1 ~ ~ minecraft:crying_obsidian
setblock ~ ~ ~1 minecraft:crying_obsidian
setblock ~ ~ ~-1 minecraft:crying_obsidian

# Activation flair at target side
particle minecraft:portal ~ ~ ~ 0.4 0.8 0.4 0.8 150 force
particle minecraft:end_rod ~ ~ ~ 0.3 0.4 0.3 0.05 50 force
particle minecraft:dragon_breath ~ ~ ~ 0.5 0.5 0.5 0.05 60 force
particle minecraft:flash ~ ~1 ~ 0 0 0 0 1 force
playsound minecraft:block.portal.trigger ambient @a[distance=..30] ~ ~ ~ 2 0.6
playsound minecraft:block.beacon.activate ambient @a[distance=..30] ~ ~ ~ 2 0.7
