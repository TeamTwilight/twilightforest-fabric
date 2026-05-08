# Catty Twilight Realm - enter the realm
execute in catty:twilight_realm run tp @s 100 200 100
effect give @s minecraft:slow_falling 10 0 true
effect give @s minecraft:resistance 5 4 true
title @s reset
title @s subtitle [{"text":"小心被强化的怪物... ฅฅ","color":"red"}]
title @s title [{"text":"⚔ 暮色刀剑维度 ⚔","color":"dark_purple","bold":true}]
playsound minecraft:entity.warden_emerge ambient @s ~ ~ ~ 1 0.7
tellraw @s [{"text":"[Catty 暮色刀剑] ","color":"dark_purple","bold":true},{"text":"主人小心点喵～怪物 HP×3 攻击×2","color":"gray"}]
