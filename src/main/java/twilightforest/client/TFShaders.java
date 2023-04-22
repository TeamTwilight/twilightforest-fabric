package twilightforest.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

import java.io.IOException;

public class TFShaders {

	public static ShaderInstance RED_THREAD;

	public static void init() {
		CoreShaderRegistrationCallback.EVENT.register(context -> {
			try {
				context.register(new ResourceLocation(TwilightForestMod.ID, "red_thread/red_thread"), DefaultVertexFormat.BLOCK, i -> RED_THREAD = i);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}
