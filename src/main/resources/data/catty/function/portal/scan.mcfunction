# Reset throttle
scoreboard players set #portal_tick catty.const 0

# A) Diamond in water (overworld) - check ritual completion
execute as @e[type=item,nbt={Item:{id:"minecraft:diamond"}}] at @s if dimension minecraft:overworld if block ~ ~ ~ water run function catty:portal/check_diamond

# B) Diamond in water (twilight realm) - reverse portal ritual
execute as @e[type=item,nbt={Item:{id:"minecraft:diamond"}}] at @s if dimension catty:twilight_realm if block ~ ~ ~ water run function catty:portal/check_diamond

# C) Player standing in activated portal (any dim) - dispatch via marker NBT
execute as @a[tag=!catty_porting] at @s if block ~ ~ ~ twilightforest:twilight_portal run function catty:portal/player_at_portal
execute as @a[tag=!catty_porting] at @s if block ~ ~ ~ water if block ~ ~-1 ~ glowstone if block ~ ~-2 ~ sea_lantern run function catty:portal/player_at_portal
