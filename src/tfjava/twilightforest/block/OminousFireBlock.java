package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFItems;

/**
 * 1:1 port of upstream {@code twilightforest.block.OminousFireBlock} — Soul-Fire variant
 * that survives only on full faces, hurts non-undead entities with the OMINOUS_FIRE damage
 * type, and pick-blocks as an Exanimate Essence (the item that places it).
 *
 * <p>Codex Fabric port note: dropped unused upstream imports ({@code EventHooks},
 * {@code TFDataMaps}, {@code EntityTransformation}, {@code UUID}, {@code CompoundTag},
 * {@code ServerLevelAccessor}, {@code ServerLevel}, {@code Items}, {@code TwilightForestMod})
 * — they referenced data-map/transformation features that aren't called from this class
 * body. Translated upstream {@code TFDamageTypes.getDamageSource} → codex
 * {@code TFDamageTypes.source} and {@code TFItems.X.value()} → {@code TFItems.X.get()}
 * to match codex's {@code TFRegistryObject} API surface.</p>
 */
public class OminousFireBlock extends BaseFireBlock {
	public static final MapCodec<OminousFireBlock> CODEC = simpleCodec(OminousFireBlock::new);

	@Override
	public MapCodec<OminousFireBlock> codec() {
		return CODEC;
	}

	public OminousFireBlock(BlockBehaviour.Properties properties) {
		super(properties, 1.0F);
	}

	@Override
	protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		return this.canSurvive(state, level, currentPos) ? this.defaultBlockState() : Blocks.AIR.defaultBlockState();
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP) && level.getFluidState(pos).isEmpty();
	}

	@Override
	protected boolean canBurn(BlockState state) {
		return true;
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
		return new ItemStack(TFItems.EXANIMATE_ESSENCE.get());
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (!entity.getType().is(EntityTypeTags.UNDEAD)) {
			entity.hurt(TFDamageTypes.source(level, TFDamageTypes.OMINOUS_FIRE), 1.0F);
		}
	}
}
