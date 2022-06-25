package twilightforest.world.components.structures.start;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class TFStructureStart<C extends FeatureConfiguration> extends StructureStart {
	private boolean conquered = false;

	public TFStructureStart(Structure structure, ChunkPos chunkPos, int references, PiecesContainer pieces) {
		super(structure, chunkPos, references, pieces);
	}

	@Override
	public CompoundTag createTag(StructurePieceSerializationContext level, ChunkPos chunkPos) {
		CompoundTag tag = super.createTag(level, chunkPos);
		if (this.isValid())
			tag.putBoolean("conquered", this.conquered);
		return tag;
	}

	public void load(CompoundTag nbt) {
		this.conquered = nbt.getBoolean("conquered");
	}

	public final void setConquered(boolean flag) {
		this.conquered = flag;
	}

	public final boolean isConquered() {
		return this.conquered;
	}
}