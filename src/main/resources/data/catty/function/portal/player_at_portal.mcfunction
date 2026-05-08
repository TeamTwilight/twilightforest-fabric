# Executor: player standing in an activated Twilight portal with our markers nearby
# Tag this player so the marker macro can find them
tag @s add catty_porting_target

# Find the nearest portal marker (this dim) and trigger its bound TP
execute as @e[type=marker,tag=catty_portal,distance=..3,sort=nearest,limit=1] at @s run function catty:portal/marker_tp

# Cleanup tag (whether tp succeeded or not)
tag @s remove catty_porting_target
