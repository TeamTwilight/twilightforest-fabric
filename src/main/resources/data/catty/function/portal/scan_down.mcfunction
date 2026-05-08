# Executor: scout marker - recursively drops until it lands on solid ground
scoreboard players add @s catty.depth 1

# Bottom-out conditions
# F1.12: -31 instead of -63 — twilight_realm dimension_type sets min_y=-32,
# so the deepest reachable Y is -32 (tp clamps below that). Scout would get
# stuck at Y=-32 because void_air below min_y is still `#minecraft:replaceable`
# and L13's tp would no-op repeatedly. Using -31 catches the scout one block
# above min_y so we never enter the stuck state.
execute store result score #catty.y catty.depth run data get entity @s Pos[1]
execute if score #catty.y catty.depth matches ..-31 at @s run return run function catty:portal/scout_landed
execute if score @s catty.depth matches 400.. at @s run return run function catty:portal/scout_landed

# If block below is replaceable (air/water/plant), drop down 1 and recurse
# CRITICAL: use `at @s` for the recursive call so the position context follows
# the scout downward. Without `at @s`, position stays at initial Y=319 forever
# and `~ ~-1 ~` always checks the same block - scout never actually drops.
execute if block ~ ~-1 ~ #minecraft:replaceable run tp @s ~ ~-1 ~
execute if block ~ ~-1 ~ #minecraft:replaceable at @s run function catty:portal/scan_down

# Else hit solid ground - finalize (also `at @s` so target portal builds at scout's position)
execute unless block ~ ~-1 ~ #minecraft:replaceable at @s run function catty:portal/scout_landed
