package twilightforest.world.components.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import twilightforest.enums.StructureWoodVariant;
import twilightforest.util.ArrayUtil;
import twilightforest.util.FeaturePlacers;
import twilightforest.world.registration.TFStructureProcessors;

import javax.annotation.Nullable;
import java.util.Random;

public final class CobblePlankSwizzler extends RandomizedTemplateProcessor {
    private final StructureWoodVariant OAK_SWIZZLE;
    private final StructureWoodVariant SPRUCE_SWIZZLE;
    private final StructureWoodVariant BIRCH_SWIZZLE;

    public static final Codec<CobblePlankSwizzler> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.floatRange(0, 1).fieldOf("integrity").orElse(1.0F).forGetter((obj) -> obj.integrity),
            Codec.intRange(0, StructureWoodVariant.values().length).fieldOf("oak_to_type").orElse(0).forGetter((obj) -> obj.OAK_SWIZZLE.ordinal()),
            Codec.intRange(0, StructureWoodVariant.values().length).fieldOf("spruce_to_type").orElse(1).forGetter((obj) -> obj.SPRUCE_SWIZZLE.ordinal()),
            Codec.intRange(0, StructureWoodVariant.values().length).fieldOf("birch_to_type").orElse(2).forGetter((obj) -> obj.BIRCH_SWIZZLE.ordinal())
    ).apply(instance, CobblePlankSwizzler::new));

    private CobblePlankSwizzler(float integrity, int oakSwizzle, int spruceSwizzle, int birchSwizzle) {
        super(integrity);
        this.OAK_SWIZZLE = ArrayUtil.wrapped(StructureWoodVariant.values(), oakSwizzle);
        this.SPRUCE_SWIZZLE = ArrayUtil.wrapped(StructureWoodVariant.values(), spruceSwizzle);
        this.BIRCH_SWIZZLE = ArrayUtil.wrapped(StructureWoodVariant.values(), birchSwizzle);
    }

    public CobblePlankSwizzler(float integrity, Random random) {
        super(integrity);
        this.OAK_SWIZZLE = StructureWoodVariant.getRandomWeighted(random);
        this.SPRUCE_SWIZZLE = StructureWoodVariant.getRandomWeighted(random);
        this.BIRCH_SWIZZLE = StructureWoodVariant.getRandomWeighted(random);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TFStructureProcessors.COBBLE_PLANK_SWIZZLER;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldIn, BlockPos pos, BlockPos piecepos, StructureTemplate.StructureBlockInfo p_215194_3_, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings) {
        Random random = settings.getRandom(pos);

        if (!this.shouldPlaceBlock(random)) return null;

        BlockState state = blockInfo.state;
        Block block = state.getBlock();

        if (block == Blocks.COBBLESTONE)
            return random.nextBoolean() ? blockInfo : new StructureTemplate.StructureBlockInfo(blockInfo.pos, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), null);

        if (block == Blocks.COBBLESTONE_STAIRS)
            return random.nextBoolean() ? blockInfo : new StructureTemplate.StructureBlockInfo(blockInfo.pos, FeaturePlacers.transferAllStateKeys(state, Blocks.MOSSY_COBBLESTONE_STAIRS), null);

        if (block == Blocks.COBBLESTONE_SLAB)
            return random.nextBoolean() ? blockInfo : new StructureTemplate.StructureBlockInfo(blockInfo.pos, FeaturePlacers.transferAllStateKeys(state, Blocks.MOSSY_COBBLESTONE_SLAB), null);

        if (block == Blocks.COBBLESTONE_WALL)
            return random.nextBoolean() ? blockInfo : new StructureTemplate.StructureBlockInfo(blockInfo.pos, FeaturePlacers.transferAllStateKeys(state, Blocks.MOSSY_COBBLESTONE_WALL), null);

        if (block == Blocks.STONE_BRICKS) {
            return random.nextBoolean() ? blockInfo : new StructureTemplate.StructureBlockInfo(blockInfo.pos, random.nextInt(2) == 0 ? Blocks.CRACKED_STONE_BRICKS.defaultBlockState() : Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), null);
        }

        StructureWoodVariant type = StructureWoodVariant.getVariantFromBlock(block);
        if (type != null) return switch (type) {
            case OAK -> new StructureTemplate.StructureBlockInfo(blockInfo.pos, StructureWoodVariant.modifyBlockWithType(state, this.OAK_SWIZZLE), null);
            case SPRUCE -> new StructureTemplate.StructureBlockInfo(blockInfo.pos, StructureWoodVariant.modifyBlockWithType(state, this.SPRUCE_SWIZZLE), null);
            case BIRCH -> new StructureTemplate.StructureBlockInfo(blockInfo.pos, StructureWoodVariant.modifyBlockWithType(state, this.BIRCH_SWIZZLE), null);
            default -> blockInfo;
        };

        return blockInfo;
    }
}
