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
import twilightforest.world.registration.TFStructureProcessors;

import javax.annotation.Nullable;
import java.util.Random;

public class DruidHutTemplateProcessor extends RandomizedTemplateProcessor {
    private final StructureWoodVariant OAK_SWIZZLE;
    private final StructureWoodVariant SPRUCE_SWIZZLE;
    private final StructureWoodVariant BIRCH_SWIZZLE;

    public static final Codec<DruidHutTemplateProcessor> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.FLOAT.fieldOf("integrity").orElse(1.0F).forGetter((obj) -> obj.integrity),
            Codec.INT.fieldOf("oak_to_type").orElse(0).forGetter((obj) -> obj.OAK_SWIZZLE.ordinal()),
            Codec.INT.fieldOf("spruce_to_type").orElse(0).forGetter((obj) -> obj.SPRUCE_SWIZZLE.ordinal()),
            Codec.INT.fieldOf("birch_to_type").orElse(0).forGetter((obj) -> obj.BIRCH_SWIZZLE.ordinal())
    ).apply(instance, DruidHutTemplateProcessor::new));


    public DruidHutTemplateProcessor(float integrity, int oakSwizzle, int spruceSwizzle, int birchSwizzle) {
        super(integrity);
        int limit = StructureWoodVariant.values().length;
        this.OAK_SWIZZLE = StructureWoodVariant.values()[Math.floorMod(oakSwizzle, limit)];
        this.SPRUCE_SWIZZLE = StructureWoodVariant.values()[Math.floorMod(spruceSwizzle, limit)];
        this.BIRCH_SWIZZLE = StructureWoodVariant.values()[Math.floorMod(birchSwizzle, limit)];
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TFStructureProcessors.HUT;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldIn, BlockPos pos, BlockPos piecepos, StructureTemplate.StructureBlockInfo p_215194_3_, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings) {
        Random random = settings.getRandom(pos);

        if (!shouldPlaceBlock(random)) return null;

        BlockState state = blockInfo.state;
        Block block = state.getBlock();

        if (block == Blocks.COBBLESTONE)
            return random.nextBoolean() ? blockInfo : new StructureTemplate.StructureBlockInfo(blockInfo.pos, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), null);

        if (block == Blocks.COBBLESTONE_WALL)
            return random.nextBoolean() ? blockInfo : new StructureTemplate.StructureBlockInfo(blockInfo.pos, Blocks.MOSSY_COBBLESTONE_WALL.defaultBlockState(), null);

        if (block == Blocks.STONE_BRICKS) { // TODO: By default it's not chiseled stone as that's a different block
            return random.nextBoolean() ? blockInfo : new StructureTemplate.StructureBlockInfo(blockInfo.pos, random.nextInt(2) == 0 ? Blocks.CRACKED_STONE_BRICKS.defaultBlockState() : Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), null);
        }

        StructureWoodVariant type = StructureWoodVariant.getVariantFromBlock(block);
        if (type != null) {
            switch (type) {
                case OAK:
                    return new StructureTemplate.StructureBlockInfo(blockInfo.pos, StructureWoodVariant.modifyBlockWithType(state, OAK_SWIZZLE), null);
                case SPRUCE:
                    return new StructureTemplate.StructureBlockInfo(blockInfo.pos, StructureWoodVariant.modifyBlockWithType(state, SPRUCE_SWIZZLE), null);
                case BIRCH:
                    return new StructureTemplate.StructureBlockInfo(blockInfo.pos, StructureWoodVariant.modifyBlockWithType(state, BIRCH_SWIZZLE), null);
            }
        }

        return blockInfo;
    }
}
