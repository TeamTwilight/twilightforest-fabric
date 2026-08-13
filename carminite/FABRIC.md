TODO
====

## Main Init
- ResourceConditions.register(UncraftingTableCondition.TYPE);
- Register client-side MapData cache clearing event by calling MapDatamanger.init()

## Add these to Fabric convention tags
- Gold Ingot to ConventionalItemTags.GOLD_INGOTS
- Ores to ConventionalBlocktags.ORES and ConventionalItemTags.ORES
- Redstone Dust to ConventionalItemtags.REDSTONE_DUSTS

## ICondition for Uncrafting Table
- Replace the UncraftingTableCondition usages with ResourceCondition from Fabric

## IGlobalLootModifier & LootModifier
- Port to post-generation mixin

# TwilightForestRenderInfo
- Port implementation and usages to Fabric alternatives

# PartEntity
- Port system to Fabric

# Transfer
- Create NeoForge transfer shim for Fabric

# Uberous Soil
- Add Uberous Soil to the proper Block tags in order to have it support crops