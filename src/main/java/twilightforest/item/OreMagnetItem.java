package twilightforest.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFSounds;
import twilightforest.util.iterators.VoxelBresenhamIterator;

import java.util.*;
import java.util.stream.Collectors;

public class OreMagnetItem extends CodexItem {

    public static final HashMap<Block, Block> MAGNET_ORE_TO_BLOCK_REPLACEMENTS = new HashMap<>();
    public static final HashMap<Block, Block> TREE_ORE_TO_BLOCK_REPLACEMENTS = new HashMap<>();

    private static final int FIRING_TIME = 10;
    private static final float WIGGLE = 10.0F;

    public OreMagnetItem(Properties properties, Item fallback) {
        super(properties, fallback, -1);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getDamageValue() >= stack.getMaxDamage() && !player.getAbilities().instabuild) {
            return InteractionResultHolder.fail(stack);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int useRemaining) {
        int useTime = this.getUseDuration(stack, living) - useRemaining;
        if (level.isClientSide() || useTime < FIRING_TIME) return;
        if (!(living instanceof Player player)) return;
        if (stack.getDamageValue() >= stack.getMaxDamage() && !player.getAbilities().instabuild) return;

        int pulls = this.doMagnet(level, living, 0.0F, 0.0F);
        if (pulls == 0) pulls = this.doMagnet(level, living, WIGGLE, 0.0F);
        if (pulls == 0) pulls = this.doMagnet(level, living, WIGGLE, WIGGLE);
        if (pulls == 0) pulls = this.doMagnet(level, living, 0.0F, WIGGLE);
        if (pulls == 0) pulls = this.doMagnet(level, living, -WIGGLE, WIGGLE);
        if (pulls == 0) pulls = this.doMagnet(level, living, -WIGGLE, 0.0F);
        if (pulls == 0) pulls = this.doMagnet(level, living, -WIGGLE, -WIGGLE);
        if (pulls == 0) pulls = this.doMagnet(level, living, 0.0F, -WIGGLE);
        if (pulls == 0) pulls = this.doMagnet(level, living, WIGGLE, -WIGGLE);

        if (pulls > 0) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    TFSounds.MAGNET_GRAB, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (!player.getAbilities().instabuild) {
                stack.hurtAndBreak(pulls, living, LivingEntity.getSlotForHand(living.getUsedItemHand()));
            }
        }
        player.getCooldowns().addCooldown(this, 20);
    }

    private int doMagnet(Level level, LivingEntity living, float yawOffset, float pitchOffset) {
        double range = 32.0D;
        Vec3 srcVec = new Vec3(living.getX(), living.getY() + living.getEyeHeight(), living.getZ());
        Vec3 lookVec = getOffsetLook(living, yawOffset, pitchOffset);
        Vec3 destVec = srcVec.add(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range);
        return doMagnet(level, BlockPos.containing(srcVec), BlockPos.containing(destVec), false);
    }

    private Vec3 getOffsetLook(LivingEntity living, float yawOffset, float pitchOffset) {
        float xRot = living.getXRot() + pitchOffset;
        float yRot = living.getYRot() + yawOffset;
        float x = Mth.cos(-yRot * Mth.DEG_TO_RAD - (float) Math.PI);
        float z = Mth.sin(-yRot * Mth.DEG_TO_RAD - (float) Math.PI);
        float y = -Mth.cos(-xRot * Mth.DEG_TO_RAD);
        float ySin = Mth.sin(-xRot * Mth.DEG_TO_RAD);
        return new Vec3(z * y, ySin, x * y);
    }

    // -------------------------------------------------------------------------
    // Static ore-magnet effect used by MineLogCoreBlock
    // -------------------------------------------------------------------------

    /**
     * Scans the line from {@code usePos} to {@code destPos} using a Bresenham
     * voxel iterator, finds the first ore block and the first replaceable block,
     * then teleports the whole ore vein to the replaceable block position.
     *
     * @return number of blocks moved (0 if nothing found or nothing to replace)
     */
    public static int doMagnet(Level level, BlockPos usePos, BlockPos destPos) {
        return doMagnet(level, usePos, destPos, false);
    }

    public static int doMagnet(ServerLevel level, BlockPos usePos, BlockPos destPos, boolean sourceIsMineCore) {
        return doMagnet((Level) level, usePos, destPos, sourceIsMineCore);
    }

    public static int doMagnet(Level level, BlockPos usePos, BlockPos destPos, boolean sourceIsMineCore) {
        initOre2BlockMap();

        int blocksMoved = 0;
        BlockState attractedOreBlock = Blocks.AIR.defaultBlockState();
        BlockState replacementBlock = Blocks.AIR.defaultBlockState();
        BlockPos foundPos = null;
        BlockPos basePos = null;

        for (BlockPos coord : new VoxelBresenhamIterator(usePos, destPos)) {
            BlockState searchState = level.getBlockState(coord);
            if (basePos == null) {
                if (isReplaceable(searchState)) {
                    basePos = coord;
                }
            } else if (foundPos == null && !searchState.isAir() && isOre(searchState.getBlock(), sourceIsMineCore)
                    && level.getBlockEntity(coord) == null) {
                attractedOreBlock = searchState;
                replacementBlock = replacements(sourceIsMineCore)
                        .getOrDefault(attractedOreBlock.getBlock(), Blocks.STONE)
                        .defaultBlockState();
                foundPos = coord;
            }
        }

        if (basePos != null && foundPos != null && !attractedOreBlock.isAir()) {
            Set<BlockPos> veinBlocks = new HashSet<>();
            findVein(level, foundPos, attractedOreBlock, veinBlocks);

            int offX = basePos.getX() - foundPos.getX();
            int offY = basePos.getY() - foundPos.getY();
            int offZ = basePos.getZ() - foundPos.getZ();

            for (BlockPos coord : veinBlocks) {
                BlockPos replacePos = coord.offset(offX, offY, offZ);
                BlockState replaceState = level.getBlockState(replacePos);
                if (isReplaceable(replaceState) || replaceState.canBeReplaced() || replaceState.isAir()) {
                    level.setBlock(coord, replacementBlock, 2);
                    level.setBlock(replacePos, attractedOreBlock, 2);
                    if (sourceIsMineCore && level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(twilightforest.init.TFParticleType.LOG_CORE_PARTICLE,
                                replacePos.getX() + 0.5D, replacePos.getY() + 0.5D, replacePos.getZ() + 0.5D,
                                2, 0.25D, 0.25D, 0.25D, 0.0D);
                    }
                    blocksMoved++;
                }
            }
        }

        return blocksMoved;
    }

    private static boolean isReplaceable(BlockState state) {
        return state.is(BlockTagGenerator.ORE_MAGNET_SAFE_REPLACE_BLOCK);
    }

    private static boolean isOre(Block ore, boolean sourceIsMineCore) {
        return replacements(sourceIsMineCore).containsKey(ore);
    }

    private static Map<Block, Block> replacements(boolean sourceIsMineCore) {
        return sourceIsMineCore ? TREE_ORE_TO_BLOCK_REPLACEMENTS : MAGNET_ORE_TO_BLOCK_REPLACEMENTS;
    }

    private static void findVein(Level level, BlockPos here, BlockState oreState, Set<BlockPos> veinBlocks) {
        if (veinBlocks.contains(here) || veinBlocks.size() >= 24) return;
        if (level.getBlockState(here) == oreState) {
            veinBlocks.add(here);
            for (Direction dir : Direction.values()) {
                findVein(level, here.relative(dir), oreState, veinBlocks);
            }
        }
    }

    private static boolean oreCacheNeedsBuild = true;

    public static void markOreCacheDirty() {
        oreCacheNeedsBuild = true;
    }

    public static void refreshOreCacheFromTags() {
        MAGNET_ORE_TO_BLOCK_REPLACEMENTS.clear();
        TREE_ORE_TO_BLOCK_REPLACEMENTS.clear();

        List<TagKey<Block>> tags = BuiltInRegistries.BLOCK.getTagNames()
                .filter(t -> t.location().getNamespace().equals("c"))
                .collect(Collectors.toList());
        for (TagKey<Block> tag : tags) {
            String path = tag.location().getPath();
            if (!path.startsWith("ores_in_ground/")) continue;
            String ground = path.substring("ores_in_ground/".length());
            TagKey<Block> groundTag = TagKey.create(Registries.BLOCK,
                    ResourceLocation.fromNamespaceAndPath("c", "ore_bearing_ground/" + ground));
            if (tags.stream().noneMatch(t -> t.location().equals(groundTag.location()))) continue;

            BuiltInRegistries.BLOCK.getTag(groundTag).ifPresent(groundHolders ->
                    BuiltInRegistries.BLOCK.getTag(tag).ifPresent(oreHolders ->
                            groundHolders.forEach(groundHolder ->
                                    oreHolders.forEach(oreHolder -> {
                                        Block ore = oreHolder.value();
                                        Block groundBlock = groundHolder.value();
                                        if (!ore.defaultBlockState().is(BlockTagGenerator.ORE_MAGNET_IGNORE)) {
                                            MAGNET_ORE_TO_BLOCK_REPLACEMENTS.put(ore, groundBlock);
                                        }
                                        if (!ore.defaultBlockState().is(BlockTagGenerator.MINING_CORE_EXCLUDED)) {
                                            TREE_ORE_TO_BLOCK_REPLACEMENTS.put(ore, groundBlock);
                                        }
                                    }))));
        }

        if (!Blocks.ANCIENT_DEBRIS.defaultBlockState().is(BlockTagGenerator.ORE_MAGNET_IGNORE) && !MAGNET_ORE_TO_BLOCK_REPLACEMENTS.containsKey(Blocks.ANCIENT_DEBRIS)) {
            MAGNET_ORE_TO_BLOCK_REPLACEMENTS.put(Blocks.ANCIENT_DEBRIS, Blocks.NETHERRACK);
        }
        if (!Blocks.ANCIENT_DEBRIS.defaultBlockState().is(BlockTagGenerator.MINING_CORE_EXCLUDED) && !TREE_ORE_TO_BLOCK_REPLACEMENTS.containsKey(Blocks.ANCIENT_DEBRIS)) {
            TREE_ORE_TO_BLOCK_REPLACEMENTS.put(Blocks.ANCIENT_DEBRIS, Blocks.NETHERRACK);
        }
        oreCacheNeedsBuild = false;
    }

    private static void initOre2BlockMap() {
        if (!oreCacheNeedsBuild) return;
        refreshOreCacheFromTags();
    }
}
