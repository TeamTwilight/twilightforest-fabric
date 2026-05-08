# Catty Twilight Realm - return to overworld
execute in minecraft:overworld run tp @s 0 200 0
effect give @s minecraft:slow_falling 10 0 true
title @s reset
title @s title [{"text":"返回主世界","color":"yellow","bold":true}]
playsound minecraft:entity.player.levelup ambient @s ~ ~ ~ 1 1
tellraw @s [{"text":"[Catty 暮色刀剑] ","color":"dark_purple","bold":true},{"text":"欢迎回来主人 ฅฅ","color":"gray"}]
