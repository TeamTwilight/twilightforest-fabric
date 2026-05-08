package twilightforest.loot.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import twilightforest.init.TFLoot;

/**
 * Fabric-side replacement for {@code neoforge:can_item_perform_ability}.
 *
 * <p>NeoForge's {@code ItemAbility} is a tool-trait registry (e.g. {@code shears_dig},
 * {@code axe_dig}, {@code shovel_dig}) used to ask «can this tool perform that
 * ability». Fabric does not have a built-in equivalent. We map a small set of
 * common abilities to vanilla {@link TagKey item tags} so loot-table JSONs
 * referring to {@code shears_dig} / {@code axe_dig} / {@code hoe_dig} / etc.
 * still produce a sensible drop (matches TOOLS tags by default; falls back
 * to «true» so the table doesn't reject).
 */
public record NeoForgeCanItemPerformAbilityCondition(String ability) implements LootItemCondition {

    public static final MapCodec<NeoForgeCanItemPerformAbilityCondition> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(Codec.STRING.fieldOf("ability").forGetter(NeoForgeCanItemPerformAbilityCondition::ability))
                .apply(instance, NeoForgeCanItemPerformAbilityCondition::new));

    @Override
    public LootItemConditionType getType() {
        return TFLoot.NEOFORGE_CAN_ITEM_PERFORM_ABILITY.get();
    }

    @Override
    public boolean test(LootContext context) {
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        if (tool == null || tool.isEmpty()) return false;
        TagKey<Item> tag = mapAbilityToTag(this.ability);
        if (tag == null) return false;
        return tool.is(tag);
    }

    private static TagKey<Item> mapAbilityToTag(String ability) {
        return switch (ability) {
            case "shears_dig", "shears_carve", "shears_disarm", "shears_remove_armor",
                 "shears_trim" -> TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "shears"));
            case "axe_dig", "axe_strip", "axe_scrape", "axe_wax_off" -> ItemTags.AXES;
            case "shovel_dig", "shovel_flatten" -> ItemTags.SHOVELS;
            case "hoe_dig", "hoe_till" -> ItemTags.HOES;
            case "pickaxe_dig" -> ItemTags.PICKAXES;
            case "sword_dig", "sword_sweep" -> ItemTags.SWORDS;
            default -> null;
        };
    }
}
