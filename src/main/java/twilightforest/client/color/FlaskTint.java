package twilightforest.client.color;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import twilightforest.components.item.PotionFlaskComponent;
import twilightforest.init.TFDataComponents;

public record FlaskTint(int defaultColor) implements ItemTintSource {
	public static final MapCodec<FlaskTint> MAP_CODEC = RecordCodecBuilder.mapCodec(
		i -> i.group(ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(FlaskTint::defaultColor)).apply(i, FlaskTint::new)
	);

	public FlaskTint() {
		this(-13083194);
	}

	@Override
	public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
		var contents = stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS, PotionFlaskComponent.EMPTY);
		if (contents.potion().potion().isEmpty()) return ARGB.opaque(defaultColor());
		return ARGB.opaque(contents.potion().getColor());
	}

	@Override
	public MapCodec<? extends ItemTintSource> type() {
		return MAP_CODEC;
	}
}
