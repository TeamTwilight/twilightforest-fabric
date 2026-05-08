# MACRO function - executor: marker, args from marker's data: dest_dim/dest_x/dest_y/dest_z
$execute as @a[tag=catty_porting_target] in $(dest_dim) run tp @s $(dest_x) $(dest_y) $(dest_z)
function catty:portal/post_tp_effects
