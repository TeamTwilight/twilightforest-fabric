package twilightforest.enums;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.entity.spawner.BossSpawnerBlockEntity;
import twilightforest.init.TFBlockEntities;

import java.util.Locale;

public enum BossVariant implements StringRepresentable {

	NAGA(TrophyType.GOLD, TFBlockEntities.NAGA_SPAWNER),
	LICH(TrophyType.GOLD, TFBlockEntities.LICH_SPAWNER),
	HYDRA(TrophyType.GOLD, TFBlockEntities.HYDRA_SPAWNER),
	UR_GHAST(TrophyType.GOLD, TFBlockEntities.UR_GHAST_SPAWNER),
	KNIGHT_PHANTOM(TrophyType.IRON, TFBlockEntities.KNIGHT_PHANTOM_SPAWNER),
	SNOW_QUEEN(TrophyType.GOLD, TFBlockEntities.SNOW_QUEEN_SPAWNER),
	MINOSHROOM(TrophyType.IRON, TFBlockEntities.MINOSHROOM_SPAWNER),
	ALPHA_YETI(TrophyType.IRON, TFBlockEntities.ALPHA_YETI_SPAWNER),
	QUEST_RAM(TrophyType.IRONWOOD, null),
	FINAL_BOSS(TrophyType.GOLD, TFBlockEntities.FINAL_BOSS_SPAWNER);

	public static final EnumCodec<BossVariant> CODEC = StringRepresentable.fromEnum(BossVariant::values);
	private final TrophyType trophyType;
	@Nullable
	private final BlockEntityType<? extends BossSpawnerBlockEntity<?>> blockEntityType;

	BossVariant(TrophyType trophyType, @Nullable BlockEntityType<? extends BossSpawnerBlockEntity<?>> blockEntityType) {
		this.trophyType = trophyType;
		this.blockEntityType = blockEntityType;
	}

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public TrophyType getTrophyType() {
		return this.trophyType;
	}


	@Nullable
	public BlockEntityType<? extends BossSpawnerBlockEntity<?>> getType() {
		return blockEntityType;
	}

	public enum TrophyType {
		GOLD("trophy"),
		IRON("trophy_minor"),
		IRONWOOD("trophy_quest");

		private final String modelName;

		TrophyType(String modelName) {
			this.modelName = modelName;
		}

		public String getModelName() {
			return this.modelName;
		}
	}
}
