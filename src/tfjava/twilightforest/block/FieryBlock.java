package twilightforest.block;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFItems;

/**
 * 1:1 port of upstream {@code twilightforest.block.FieryBlock} (NeoForge → Fabric:
 * <ul>
 *   <li>{@code ModList.get().isLoaded("ctm")} → {@code FabricLoader.getInstance().isModLoaded("ctm")}</li>
 *   <li>{@code TFDamageTypes.getDamageSource} → codex-side {@code TFDamageTypes.source}</li>
 *   <li>{@code @Override Block#isFireSource} is a NeoForge-only callback (vanilla Fabric 1.21.1
 *       has no equivalent hook on Block — fire-source registration goes through the
 *       vanilla static {@code Block#isFireSource} which is package-private). The body is kept
 *       1:1 without {@code @Override} so a future Fabric mixin or registry hookup can
 *       re-attach it; behaviour intent is preserved in source.</li>
 * </ul>
 *
 * <p>Behavior: full block brightness ({@code getShadeBrightness=1.0}), CTM-aware face
 * culling between adjacent FieryBlocks, no occlusion shape, fire-damage on step (skipped if
 * entity is fire-immune or wearing TF fiery boots).</p>
 */
public class FieryBlock extends Block {
	public FieryBlock(Properties properties) {
		super(properties);
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter getter, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean skipRendering(BlockState state, BlockState otherState, Direction direction) {
		return FabricLoader.getInstance().isModLoaded("ctm") && otherState.getBlock() instanceof FieryBlock;
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter getter, BlockPos pos) {
		return Shapes.empty();
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		if ((!entity.fireImmune())
			&& entity instanceof LivingEntity living
			&& !living.getItemBySlot(EquipmentSlot.FEET).is(TFItems.FIERY_BOOTS.get())) {
			entity.hurt(TFDamageTypes.source(level, TFDamageTypes.FIERY), 1.0F);
		}

		super.stepOn(level, pos, state, entity);
	}

	// Codex Fabric port note: NF-only Block#isFireSource hook; kept body 1:1 for future mixin/registry hookup.
	public boolean isFireSource(BlockState state, LevelReader level, BlockPos pos, Direction direction) {
		return true;
	}
}
