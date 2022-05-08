package twilightforest.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import io.github.fabricators_of_create.porting_lib.event.client.RegisterShadersCallback;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

import java.io.IOException;
import java.util.function.Consumer;

public class TFShaders {

	public static ShaderInstance RED_THREAD;

	public static void init() {
		RegisterShadersCallback.EVENT.register((resourceManager, event) -> {
			try {
				event.registerShader(new ShaderInstance(resourceManager, new ResourceLocation(TwilightForestMod.ID, "red_thread/red_thread").toString(), DefaultVertexFormat.
						BLOCK), shader -> RED_THREAD = shader);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}
