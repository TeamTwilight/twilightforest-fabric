# Apply post-teleport flair to the targeted player(s)
tag @a[tag=catty_porting_target] add catty_porting
schedule function catty:portal/clear_porting 100t replace
execute as @a[tag=catty_porting_target] run effect give @s minecraft:slow_falling 12 0 true
execute as @a[tag=catty_porting_target] run effect give @s minecraft:resistance 8 4 true
execute as @a[tag=catty_porting_target] run effect give @s minecraft:blindness 2 0 true
execute as @a[tag=catty_porting_target] at @s run playsound minecraft:entity.endermen.teleport master @s ~ ~ ~ 1 0.7
execute as @a[tag=catty_porting_target] at @s run particle minecraft:portal ~ ~1 ~ 0.5 1 0.5 0.5 80
execute as @a[tag=catty_porting_target] in catty:twilight_realm run title @s reset
execute as @a[tag=catty_porting_target] in catty:twilight_realm run title @s subtitle [{"text":"小心被强化的怪物... ฅฅ","color":"red"}]
execute as @a[tag=catty_porting_target] in catty:twilight_realm run title @s title [{"text":"⚔ 暮色刀剑维度 ⚔","color":"dark_purple","bold":true}]
execute as @a[tag=catty_porting_target] in minecraft:overworld run title @s reset
execute as @a[tag=catty_porting_target] in minecraft:overworld run title @s title [{"text":"返回主世界","color":"yellow","bold":true}]
