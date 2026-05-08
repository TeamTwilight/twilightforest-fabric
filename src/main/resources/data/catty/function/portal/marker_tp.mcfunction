# Executor: portal marker carrying dest_dim/dest_x/y/z in its data
# Hand off to macro that reads the marker's NBT and tps the tagged player
function catty:portal/do_tp_macro with entity @s data
