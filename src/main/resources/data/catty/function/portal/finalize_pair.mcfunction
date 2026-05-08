# MACRO function - executor: scout marker at TARGET pos, has src/dest coords in storage
# Spawn TARGET marker at scout's current pos (target portal pos) with binding to source
$summon minecraft:marker ~ ~ ~ {Tags:["catty_portal"], data:{dest_dim:"$(src_dim)", dest_x:$(sx), dest_y:$(sy), dest_z:$(sz)}}
# Spawn SOURCE marker by hopping to source dim+pos with binding to target
$execute in $(src_dim) positioned $(sx) $(sy) $(sz) run summon minecraft:marker ~ ~ ~ {Tags:["catty_portal"], data:{dest_dim:"$(dest_dim)", dest_x:$(tx), dest_y:$(ty), dest_z:$(tz)}}
