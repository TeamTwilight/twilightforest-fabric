package twilightforest.client.renderer.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;

import java.time.LocalDate;
import java.time.Month;
import java.util.EnumMap;
import java.util.Map;

public class TFChestRenderer<T extends ChestBlockEntity & LidBlockEntity> implements BlockEntityRenderer<T> {
	public static final Map<Block, EnumMap<ChestType, Material>> MATERIALS;

	private final ModelPart lid;
	private final ModelPart bottom;
	private final ModelPart lock;
	private final ModelPart doubleLeftLid;
	private final ModelPart doubleLeftBottom;
	private final ModelPart doubleLeftLock;
	private final ModelPart doubleRightLid;
	private final ModelPart doubleRightBottom;
	private final ModelPart doubleRightLock;
	private final boolean xmasTextures;

	static {
		ImmutableMap.Builder<Block, EnumMap<ChestType, Material>> builder = ImmutableMap.builder();

		builder.put(TFBlocks.TWILIGHT_OAK_CHEST.get(), chestMaterial("twilight", false));
		builder.put(TFBlocks.CANOPY_CHEST.get(), chestMaterial("canopy", false));
		builder.put(TFBlocks.MANGROVE_CHEST.get(), chestMaterial("mangrove", false));
		builder.put(TFBlocks.DARK_CHEST.get(), chestMaterial("darkwood", false));
		builder.put(TFBlocks.TIME_CHEST.get(), chestMaterial("time", false));
		builder.put(TFBlocks.TRANSFORMATION_CHEST.get(), chestMaterial("transformation", false));
		builder.put(TFBlocks.MINING_CHEST.get(), chestMaterial("mining", false));
		builder.put(TFBlocks.SORTING_CHEST.get(), chestMaterial("sorting", false));

		builder.put(TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.get(), chestMaterial("twilight", true));
		builder.put(TFBlocks.CANOPY_TRAPPED_CHEST.get(), chestMaterial("canopy", true));
		builder.put(TFBlocks.MANGROVE_TRAPPED_CHEST.get(), chestMaterial("mangrove", true));
		builder.put(TFBlocks.DARK_TRAPPED_CHEST.get(), chestMaterial("darkwood", true));
		builder.put(TFBlocks.TIME_TRAPPED_CHEST.get(), chestMaterial("time", true));
		builder.put(TFBlocks.TRANSFORMATION_TRAPPED_CHEST.get(), chestMaterial("transformation", true));
		builder.put(TFBlocks.MINING_TRAPPED_CHEST.get(), chestMaterial("mining", true));
		builder.put(TFBlocks.SORTING_TRAPPED_CHEST.get(), chestMaterial("sorting", true));

		MATERIALS = builder.build();
	}

	public TFChestRenderer(BlockEntityRendererProvider.Context context) {
		ModelPart single = context.bakeLayer(ModelLayers.CHEST);
		this.lid = single.getChild("lid");
		this.bottom = single.getChild("bottom");
		this.lock = single.getChild("lock");

		ModelPart left = context.bakeLayer(ModelLayers.DOUBLE_CHEST_LEFT);
		this.doubleLeftLid = left.getChild("lid");
		this.doubleLeftBottom = left.getChild("bottom");
		this.doubleLeftLock = left.getChild("lock");

		ModelPart right = context.bakeLayer(ModelLayers.DOUBLE_CHEST_RIGHT);
		this.doubleRightLid = right.getChild("lid");
		this.doubleRightBottom = right.getChild("bottom");
		this.doubleRightLock = right.getChild("lock");

		LocalDate today = LocalDate.now();
		this.xmasTextures = today.getMonth() == Month.DECEMBER && today.getDayOfMonth() >= 24 && today.getDayOfMonth() <= 26;
	}

	@Override
	public void render(T entity, float partialTicks, PoseStack stack, MultiBufferSource source, int light, int overlay) {
		Level level = entity.getLevel();
		boolean hasLevel = level != null;
		BlockState state = hasLevel ? entity.getBlockState() : Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
		ChestType chestType = state.hasProperty(ChestBlock.TYPE) ? state.getValue(ChestBlock.TYPE) : ChestType.SINGLE;
		Block block = state.getBlock();
		if (!(block instanceof AbstractChestBlock<?> chestBlock)) {
			return;
		}

		boolean doubleChest = chestType != ChestType.SINGLE;
		stack.pushPose();
		float facing = state.getValue(ChestBlock.FACING).toYRot();
		stack.translate(0.5F, 0.5F, 0.5F);
		stack.mulPose(Axis.YP.rotationDegrees(-facing));
		stack.translate(-0.5F, -0.5F, -0.5F);

		float openness = entity.getOpenNess(partialTicks);
		int packedLight = light;
		if (hasLevel) {
			DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> result = chestBlock.combine(state, level, entity.getBlockPos(), true);
			openness = ((Float2FloatFunction) result.apply(ChestBlock.opennessCombiner(entity))).get(partialTicks);
			packedLight = ((Int2IntFunction) result.apply(new BrightnessCombiner<>())).applyAsInt(light);
		}
		openness = 1.0F - openness;
		openness = 1.0F - openness * openness * openness;

		Material material = this.getMaterial(entity, chestType);
		VertexConsumer consumer = material.buffer(source, RenderType::entityCutout);
		if (doubleChest) {
			if (chestType == ChestType.LEFT) {
				this.render(stack, consumer, this.doubleLeftLid, this.doubleLeftLock, this.doubleLeftBottom, openness, packedLight, overlay);
			} else {
				this.render(stack, consumer, this.doubleRightLid, this.doubleRightLock, this.doubleRightBottom, openness, packedLight, overlay);
			}
		} else {
			this.render(stack, consumer, this.lid, this.lock, this.bottom, openness, packedLight, overlay);
		}

		stack.popPose();
	}

	protected Material getMaterial(T entity, ChestType chestType) {
		EnumMap<ChestType, Material> materials = MATERIALS.get(entity.getBlockState().getBlock());
		if (materials != null) {
			Material material = materials.get(chestType);
			if (material != null) {
				return material;
			}
		}
		return Sheets.chooseMaterial(entity, chestType, this.xmasTextures);
	}

	private void render(PoseStack stack, VertexConsumer consumer, ModelPart lid, ModelPart lock, ModelPart bottom, float openness, int light, int overlay) {
		lid.xRot = -(openness * Mth.HALF_PI);
		lock.xRot = lid.xRot;
		lid.render(stack, consumer, light, overlay);
		lock.render(stack, consumer, light, overlay);
		bottom.render(stack, consumer, light, overlay);
	}

	private static EnumMap<ChestType, Material> chestMaterial(String type, boolean trapped) {
		EnumMap<ChestType, Material> map = new EnumMap<>(ChestType.class);

		map.put(ChestType.SINGLE, new Material(Sheets.CHEST_SHEET, TwilightForestMod.prefix("entity/chest/" + type + "/" + (trapped ? "trapped" : "single"))));
		map.put(ChestType.LEFT, new Material(Sheets.CHEST_SHEET, TwilightForestMod.prefix("entity/chest/" + type + "/" + (trapped ? "trapped_left" : "left"))));
		map.put(ChestType.RIGHT, new Material(Sheets.CHEST_SHEET, TwilightForestMod.prefix("entity/chest/" + type + "/" + (trapped ? "trapped_right" : "right"))));

		return map;
	}
}
