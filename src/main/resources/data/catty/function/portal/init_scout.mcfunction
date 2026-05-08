# Executor: freshly summoned marker in TARGET dimension at y=319
# Tag it, reset depth
data merge entity @s {Tags:["catty_portal_scout"]}
scoreboard players set @s catty.depth 0
tellraw @a [{"text":"❖ 「破空一閃」","color":"light_purple","bold":true},{"text":" 紫月斥候，撕裂虚空，自天穹直坠！","color":"light_purple","italic":true}]

# Force-load this chunk so block checks during scan_down actually work.
# Without this, summon places the scout in an unloaded chunk and `if block`
# returns "no result" for both branches - scan_down dies after 1 iteration.
forceload add ~ ~
tp @s ~ 200 ~
tellraw @a [{"text":"❖ 「縛地・界鎖」","color":"dark_aqua","bold":true},{"text":" 异界之地脉，已锁于刃下…","color":"dark_aqua","italic":true}]
# F1.12 fix: `at @s` so scan_down inherits scout's actual Y=200 position context.
# Without it, scan_down's `~ ~-1 ~` resolves relative to caller's Y=319, which
# is above twilight_realm max_y=256, causing scout to clamp-tp upward to Y=256
# then waste ~280 iterations dropping; eventually stuck at min_y=-32 because
# void_air below counts as `#minecraft:replaceable` so scout never lands.
execute at @s run function catty:portal/scan_down
execute store result score #catty.dbg_y catty.depth run data get entity @s Pos[1]
tellraw @a [{"text":"❖ 「測距・残月」","color":"dark_purple","italic":true},{"text":" 紫月既沉，斥候已栖于幽暗之底，地脉无声","color":"dark_purple","italic":true}]
# Keep the chunk forceloaded - we WANT the target portal chunk hot so TP destinations
# stay valid. Use `/forceload remove all` manually if you ever want to clean up.
