# Executor: scout marker - now sitting at first air-block above ground
tellraw @a [{"text":"❖ 「斬落・着地」","color":"aqua","bold":true},{"text":" 异界之岸，已显形于刃下…","color":"aqua","italic":true}]

# Save target coords
execute store result storage catty:portal tx int 1 run data get entity @s Pos[0]
execute store result storage catty:portal ty int 1 run data get entity @s Pos[1]
execute store result storage catty:portal tz int 1 run data get entity @s Pos[2]

# Build target portal blocks at scout's position
function catty:portal/build_target_blocks

# Spawn the two paired permanent markers (with binding NBT) via macro
function catty:portal/finalize_pair with storage catty:portal
tellraw @a [{"text":"❖ 「双月結界・終成」","color":"gold","bold":true},{"text":" 对偶之门已永结契约，凡踏水者，皆得渡矣","color":"yellow","italic":true}]

# Remove scout
kill @s
