# Executor: diamond item entity at water position - ritual succeeded
# Skip if a portal already exists right here (avoid duplicates)
execute if entity @e[type=marker,tag=catty_portal,distance=..2] run return 0

# Lightning visual (no damage)
summon minecraft:lightning_bolt ~ ~ ~ {Damage:0f}

# === Step 1: Save source coords + dims to storage ===
execute store result storage catty:portal sx int 1 run data get entity @s Pos[0]
execute store result storage catty:portal sy int 1 run data get entity @s Pos[1]
execute store result storage catty:portal sz int 1 run data get entity @s Pos[2]
execute if dimension minecraft:overworld run data modify storage catty:portal src_dim set value "minecraft:overworld"
execute if dimension catty:twilight_realm run data modify storage catty:portal src_dim set value "catty:twilight_realm"
execute if dimension minecraft:overworld run data modify storage catty:portal dest_dim set value "catty:twilight_realm"
execute if dimension catty:twilight_realm run data modify storage catty:portal dest_dim set value "minecraft:overworld"

# === Step 2: Build SOURCE portal blocks at our position ===
setblock ~ ~ ~ twilightforest:twilight_portal
setblock ~ ~-1 ~ minecraft:glowstone
setblock ~ ~-2 ~ minecraft:sea_lantern
setblock ~1 ~ ~ minecraft:crying_obsidian
setblock ~-1 ~ ~ minecraft:crying_obsidian
setblock ~ ~ ~1 minecraft:crying_obsidian
setblock ~ ~ ~-1 minecraft:crying_obsidian

# === Step 3: Spawn scout marker in TARGET dim and drive it in one step ===
# Use chained `execute summon ... run function` (1.20.5+) so the new marker is
# the executor immediately - no @e race or cross-dimension-search bug.
tellraw @a [{"text":"❖ 「鞘鳴式」","color":"yellow","bold":true},{"text":" 暮影之刃，欲破鞘而出…","color":"gold","italic":true}]
execute if dimension minecraft:overworld in catty:twilight_realm positioned ~ 319 ~ summon minecraft:marker run function catty:portal/init_scout
execute if dimension catty:twilight_realm in minecraft:overworld positioned ~ 319 ~ summon minecraft:marker run function catty:portal/init_scout

# Consume diamond
kill @s

# Source-side activation effects
particle minecraft:portal ~ ~ ~ 0.5 0.8 0.5 0.8 200 force
particle minecraft:end_rod ~ ~ ~ 0.4 0.4 0.4 0.05 60 force
particle minecraft:dragon_breath ~ ~ ~ 0.5 0.5 0.5 0.05 80 force
particle minecraft:flash ~ ~1 ~ 0 0 0 0 1 force
playsound minecraft:block.beacon.activate ambient @a[distance=..50] ~ ~ ~ 3 0.7
playsound minecraft:entity.lightning_bolt.thunder ambient @a[distance=..80] ~ ~ ~ 1 1.5
playsound minecraft:block.portal.travel ambient @a[distance=..30] ~ ~ ~ 1.5 0.6
