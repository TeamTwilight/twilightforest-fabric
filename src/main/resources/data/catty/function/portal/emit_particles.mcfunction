# Per-marker per-tick particle emission for portal visuals
# Self-destruct if portal got physically broken
execute unless block ~ ~ ~ twilightforest:twilight_portal unless block ~ ~ ~ water run kill @s
execute unless block ~ ~-1 ~ glowstone run kill @s
execute unless block ~ ~-2 ~ sea_lantern run kill @s

# Purple portal particles
particle minecraft:portal ~ ~0.5 ~ 0.4 0.5 0.4 0.4 10 force
particle minecraft:reverse_portal ~ ~1 ~ 0.4 0.6 0.4 0.06 5 force
particle minecraft:dragon_breath ~ ~0.2 ~ 0.3 0.1 0.3 0.01 3 force
particle minecraft:dust_color_transition{from_color:[0.5,0.0,0.9],to_color:[0.9,0.4,1.0],scale:1.2} ~ ~0.5 ~ 0.5 0.4 0.5 1 14 force
particle minecraft:enchant ~ ~2 ~ 0.4 0.4 0.4 0.6 3 force
particle minecraft:soul_fire_flame ~ ~0.1 ~ 0.4 0 0.4 0.01 2 force
