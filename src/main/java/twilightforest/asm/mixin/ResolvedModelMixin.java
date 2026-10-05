package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.cuboid.ItemTransform;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.JarTransformsDuck;

import static net.minecraft.client.resources.model.ResolvedModel.findTopTransform;

@Mixin(ResolvedModel.class)
public interface ResolvedModelMixin {

	@ModifyReturnValue(
		method = "findTopTransforms(Lnet/minecraft/client/resources/model/ResolvedModel;)Lnet/minecraft/client/resources/model/cuboid/ItemTransforms;",
		at = @At("RETURN")
	)
	private static ItemTransforms twilightforest$addJarredTransform(
		ItemTransforms original,
		@Local(argsOnly = true, name = "top") ResolvedModel top
	) {
		ItemTransform transform = findTopTransform(top, ItemDisplayContext.TWILIGHTFOREST_JARRED);
		((JarTransformsDuck) (Object) original).twilightforest$setJarredTransform(transform);
		return original;
	}
}
