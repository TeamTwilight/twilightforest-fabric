package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import twilightforest.block.entity.CicadaBlockEntity;
import twilightforest.init.TFBlockEntities;
import twilightforest.loot.TFLootTables;

import java.lang.reflect.Method;

public class CicadaBlock extends CritterBlock {

	public static final MapCodec<CicadaBlock> CODEC = simpleCodec(CicadaBlock::new);

	public CicadaBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CicadaBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.CICADA, CicadaBlockEntity::tick);
	}

	@Override
	public ResourceKey<LootTable> getSquishLootTable() {
		return TFLootTables.CICADA_SQUISH_DROPS;
	}

	@Override
	public void destroy(LevelAccessor accessor, BlockPos pos, BlockState state) {
		super.destroy(accessor, pos, state);
		if (accessor.isClientSide()) {
			stopCicadaSoundIfClient();
		}
	}

	private static void stopCicadaSoundIfClient() {
		try {
			Class<?> hooks = Class.forName("com.codex.twilight.client.render.ClientCritterSoundHooks");
			Method stop = hooks.getMethod("stopCicada");
			stop.invoke(null);
		} catch (ReflectiveOperationException ignored) {
		}
	}
}
