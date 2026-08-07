package twilightforest.mixin;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Blocks;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(Boat.Type.class)
public abstract class BoatTypeMixin {

	@Mutable
	@Shadow(aliases = {"$VALUES"})
	private static Boat.Type[] $VALUES;

	@Inject(
		method = "<clinit>",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/entity/vehicle/Boat$Type;CODEC:Lnet/minecraft/util/StringRepresentable$EnumCodec;",
			shift = At.Shift.BEFORE,
			opcode = Opcodes.PUTSTATIC)
	)
	private static void twilightforest$addCustomTypes(CallbackInfo ci) {
		int base = $VALUES.length;
		Boat.Type[] extended = Arrays.copyOf($VALUES, base + 8);

		// Blocks.OAK_PLANKS is replaced during the init stage using BoatTypeAccessor
		extended[base] = BoatTypeAccessor.twilightforest$constructor("TWILIGHTFOREST_TWILIGHT_OAK", base, Blocks.OAK_PLANKS, "twilight_oak");
		extended[base + 1] = BoatTypeAccessor.twilightforest$constructor("TWILIGHTFOREST_CANOPY", base + 1, Blocks.OAK_PLANKS, "canopy");
		extended[base + 2] = BoatTypeAccessor.twilightforest$constructor("TWILIGHTFOREST_MANGROVE", base + 2, Blocks.OAK_PLANKS, "twilight_mangrove");
		extended[base + 3] = BoatTypeAccessor.twilightforest$constructor("TWILIGHTFOREST_DARK", base + 3, Blocks.OAK_PLANKS, "dark");
		extended[base + 4] = BoatTypeAccessor.twilightforest$constructor("TWILIGHTFOREST_TIME", base + 4, Blocks.OAK_PLANKS, "time");
		extended[base + 5] = BoatTypeAccessor.twilightforest$constructor("TWILIGHTFOREST_TRANSFORMATION", base+5, Blocks.OAK_PLANKS, "transformation");
		extended[base + 6] = BoatTypeAccessor.twilightforest$constructor("TWILIGHTFOREST_MINING", base + 6, Blocks.OAK_PLANKS, "mining");
		extended[base + 7] = BoatTypeAccessor.twilightforest$constructor("TWILIGHTFOREST_SORTING", base + 7, Blocks.OAK_PLANKS, "sorting");

		$VALUES = extended;
	}
}