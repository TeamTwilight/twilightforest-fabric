# Catty Twilight Realm - mob buff applied once per entity
attribute @s minecraft:generic.max_health modifier add catty:hp_boost 2.0 add_multiplied_base
attribute @s minecraft:generic.attack_damage modifier add catty:dmg_boost 1.0 add_multiplied_base
attribute @s minecraft:generic.movement_speed modifier add catty:spd_boost 0.2 add_multiplied_base
attribute @s minecraft:generic.knockback_resistance modifier add catty:kb_boost 0.5 add_value
attribute @s minecraft:generic.armor modifier add catty:armor_boost 8.0 add_value
attribute @s minecraft:generic.armor_toughness modifier add catty:tough_boost 4.0 add_value
attribute @s minecraft:generic.follow_range modifier add catty:range_boost 0.5 add_multiplied_base
effect give @s minecraft:fire_resistance infinite 0 true
data merge entity @s {Health:9999.0f}
tag @s add catty_buffed
