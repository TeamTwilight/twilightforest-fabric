package twilightforest.enchantment;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import io.github.fabricators_of_create.porting_lib.block.EntityDestroyBlock;
import twilightforest.entity.projectile.ChainBlock;
import twilightforest.init.TFDataAttachments;

import java.util.Optional;

/**
 * Fabric port — codec is 1:1 with upstream so {@code destruction.json} parses
 * cleanly. Runtime uses the upstream smash counter and {@link ChainBlock}
 * breakability rules; block destruction is routed through the player's game
 * mode so Fabric's normal block-break hooks still see the break.
 */
public record SmashBlocksEffect(LevelBasedValue maxSmash, LevelBasedValue radius,
                                Optional<HolderSet<Block>> immuneBlocks,
                                Optional<HolderSet<Block>> vulnerableBlocks,
                                Optional<Holder<SoundEvent>> smashSound) implements EnchantmentEntityEffect {

    public static final MapCodec<SmashBlocksEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LevelBasedValue.CODEC.fieldOf("max_smash").forGetter(SmashBlocksEffect::maxSmash),
            LevelBasedValue.CODEC.fieldOf("radius").forGetter(SmashBlocksEffect::radius),
            RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("immune_blocks").forGetter(SmashBlocksEffect::immuneBlocks),
            RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("vulnerable_blocks").forGetter(SmashBlocksEffect::vulnerableBlocks),
            SoundEvent.CODEC.optionalFieldOf("smash_sound").forGetter(SmashBlocksEffect::smashSound))
        .apply(instance, SmashBlocksEffect::new));

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
        if (!(item.owner() instanceof ServerPlayer player)) return;

        var attachment = TFDataAttachments.get(entity, TFDataAttachments.SMASH_BLOCKS);
        int smashed = attachment.getBlocksSmashed();
        int maxSmash = Math.round(this.maxSmash.calculate(enchantmentLevel));
        int radius = Math.round(this.radius.calculate(enchantmentLevel));
        if (smashed >= maxSmash || maxSmash <= 0 || radius <= 0) return;

        BlockPos start = BlockPos.containing(position);
        boolean restrictedPlaceMode = player.gameMode.getGameModeForPlayer().isBlockPlacingRestricted();

        for (BlockPos pos : BlockPos.betweenClosed(start.offset(-radius, 0, -radius), start.offset(radius, 0, radius))) {
            if (smashed >= maxSmash) break;
            BlockState state = level.getBlockState(pos);
            if (state.isAir()) continue;
            if (this.immuneBlocks.isPresent() && this.immuneBlocks.get().contains(state.getBlockHolder())) continue;
            if (!ChainBlock.canBreakBlockAt(level, pos, state, item.itemStack(), restrictedPlaceMode) || !canEntityDestroy(state, level, pos, player)) continue;
            if (player.gameMode.destroyBlock(pos)) {
                if (this.smashSound.isPresent()) {
                    level.playSound(null, pos, this.smashSound.get().value(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                smashed++;
            }
        }
        attachment.setBlocksSmashed(smashed);
    }

    private static boolean canEntityDestroy(BlockState state, ServerLevel level, BlockPos pos, Entity entity) {
        return !(state.getBlock() instanceof EntityDestroyBlock guarded) || guarded.canEntityDestroy(state, level, pos, entity);
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
