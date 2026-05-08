package twilightforest.init;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.item.enchantment.effects.AllOf;
import net.minecraft.world.item.enchantment.effects.DamageItem;
import net.minecraft.world.item.enchantment.effects.Ignite;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.EnchantmentLevelProvider;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.enchantment.ApplyFrostedEffect;
import twilightforest.enchantment.RechargeScepterEffect;
import twilightforest.enchantment.SmashBlocksEffect;

import java.util.Optional;

public final class TFEnchantments {
    public static final ResourceKey<Enchantment> DESTRUCTION = key("destruction");
    public static final ResourceKey<Enchantment> RENEWAL = key("renewal");
    public static final ResourceKey<Enchantment> FIRE_REACT = key("fire_react");
    public static final ResourceKey<Enchantment> CHILL_AURA = key("chill_aura");

    private TFEnchantments() {
    }

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);

        register(context, FIRE_REACT, new Enchantment.Builder(Enchantment.definition(
            items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
            items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
            1,
            3,
            Enchantment.dynamicCost(5, 9),
            Enchantment.dynamicCost(20, 9),
            8,
            EquipmentSlotGroup.ARMOR)
        ).exclusiveWith(HolderSet.direct(enchantments.getOrThrow(Enchantments.THORNS), enchantments.getOrThrow(CHILL_AURA)))
            .withEffect(EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.VICTIM,
                EnchantmentTarget.ATTACKER,
                AllOf.entityEffects(
                    new Ignite(LevelBasedValue.perLevel(2.0F, 3.0F)),
                    new DamageItem(LevelBasedValue.constant(2.0F))),
                LootItemRandomChanceCondition.randomChance(EnchantmentLevelProvider.forEnchantmentLevel(LevelBasedValue.perLevel(0.15F)))));

        register(context, CHILL_AURA, new Enchantment.Builder(Enchantment.definition(
            items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
            items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
            1,
            3,
            Enchantment.dynamicCost(5, 9),
            Enchantment.dynamicCost(20, 9),
            8,
            EquipmentSlotGroup.ARMOR)
        ).exclusiveWith(HolderSet.direct(enchantments.getOrThrow(Enchantments.THORNS), enchantments.getOrThrow(FIRE_REACT)))
            .withEffect(EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.VICTIM,
                EnchantmentTarget.ATTACKER,
                AllOf.entityEffects(
                    new ApplyFrostedEffect(LevelBasedValue.constant(200), LevelBasedValue.perLevel(0.0F, 1.0F)),
                    new DamageItem(LevelBasedValue.constant(2.0F))),
                LootItemRandomChanceCondition.randomChance(EnchantmentLevelProvider.forEnchantmentLevel(LevelBasedValue.perLevel(0.15F)))));

        register(context, DESTRUCTION, new Enchantment.Builder(Enchantment.definition(
            items.getOrThrow(ItemTagGenerator.BLOCK_AND_CHAIN_ENCHANTABLE),
            1,
            3,
            Enchantment.dynamicCost(5, 9),
            Enchantment.dynamicCost(20, 9),
            8,
            EquipmentSlotGroup.HAND))
            .withEffect(EnchantmentEffectComponents.DAMAGE, new AddValue(LevelBasedValue.perLevel(-1.5F)))
            .withEffect(EnchantmentEffectComponents.HIT_BLOCK, new SmashBlocksEffect(
                LevelBasedValue.constant(12.0F),
                LevelBasedValue.constant(1.0F),
                Optional.of(blocks.get(BlockTagGenerator.BLOCK_AND_CHAIN_NEVER_BREAKS).orElseThrow()),
                Optional.of(blocks.getOrThrow(BlockTags.MINEABLE_WITH_PICKAXE)),
                Optional.empty())));

        register(context, RENEWAL, new Enchantment.Builder(Enchantment.definition(
            items.getOrThrow(ItemTagGenerator.SCEPTERS),
            1,
            1,
            Enchantment.dynamicCost(5, 9),
            Enchantment.dynamicCost(20, 9),
            8,
            EquipmentSlotGroup.HAND))
            .withEffect(EnchantmentEffectComponents.TICK, new RechargeScepterEffect()));
    }

    private static ResourceKey<Enchantment> key(String path) {
        return ResourceKey.create(Registries.ENCHANTMENT, TwilightForestMod.prefix(path));
    }

    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.location()));
    }
}
