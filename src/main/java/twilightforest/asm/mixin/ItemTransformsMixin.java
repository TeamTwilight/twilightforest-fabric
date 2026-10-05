package twilightforest.asm.mixin;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.resources.model.cuboid.ItemTransform;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asm.hooks.JarTransformsDuck;

import java.lang.reflect.Type;

@Mixin(ItemTransforms.class)
public class ItemTransformsMixin implements JarTransformsDuck {

	@Unique
	private ItemTransform twilightforest$jarredTransform = ItemTransform.NO_TRANSFORM;

	@ModifyReturnValue(
		method = "getTransform(Lnet/minecraft/world/item/ItemDisplayContext;)Lnet/minecraft/client/resources/model/cuboid/ItemTransform;",
		at = @At("RETURN")
	)
	private ItemTransform twilightforest$getJarredTransform(
		ItemTransform original,
		@Local(argsOnly = true, name = "type") ItemDisplayContext type
	) {
		return type == ItemDisplayContext.TWILIGHTFOREST_JARRED ? this.twilightforest$jarredTransform : original;
	}

	@Override
	public void twilightforest$setJarredTransform(ItemTransform transform) {
		this.twilightforest$jarredTransform = transform;
	}

	@Mixin(targets = "net/minecraft/client/resources/model/cuboid/ItemTransforms$Deserializer")
	public static class DeserializerMixin {

		@Inject(
			method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Ljava/lang/Object;",
			at = @At("RETURN")
		)
		private void twilightforest$deserializeJarredTransform(
			JsonElement json,
			Type typeOfT,
			JsonDeserializationContext context,
			CallbackInfoReturnable<ItemTransforms> cir
		) {
			JsonObject object = json.getAsJsonObject();
			if (object.has("twilightforest:jarred")) {
				ItemTransform transform = context.deserialize(object.get("twilightforest:jarred"), ItemTransform.class);
				((JarTransformsDuck) (Object) cir.getReturnValue()).twilightforest$setJarredTransform(transform);
			}
		}
	}
}