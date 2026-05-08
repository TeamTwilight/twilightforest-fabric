# Executor: diamond item entity at water position
# TF-style ritual - 4 cardinal flowers. Accept either:
#   (A) Flowers at SAME Y as water (flat layout)
#   (B) Flowers at Y+1 (natural "water pool + flower rim" layout)
# This avoids the vanilla problem where water flows into and destroys flowers.

# Variant A: flowers at same Y as water
execute if block ~1 ~ ~ #minecraft:flowers if block ~-1 ~ ~ #minecraft:flowers if block ~ ~ ~1 #minecraft:flowers if block ~ ~ ~-1 #minecraft:flowers run return run function catty:portal/activate

# Variant B: flowers at Y+1 (rim flowers, water sunk in 1x1 hole)
execute if block ~1 ~1 ~ #minecraft:flowers if block ~-1 ~1 ~ #minecraft:flowers if block ~ ~1 ~1 #minecraft:flowers if block ~ ~1 ~-1 #minecraft:flowers run return run function catty:portal/activate
