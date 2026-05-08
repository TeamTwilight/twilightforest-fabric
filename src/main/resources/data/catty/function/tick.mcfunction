# Catty - master tick
# 1) Mob buff scan in twilight realm
execute in catty:twilight_realm as @e[type=#catty:twilight_buff_targets,tag=!catty_buffed] at @s run function catty:buff_apply

# 2) Legacy Catty marker portal disabled.
# Java twilightforest:twilight_portal now owns teleportation to twilightforest:twilight_forest.

# 3) Legacy Catty portal scan disabled to avoid hijacking real Twilight portals.
