package twilightforest.mixin;

import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.world.level.levelgen.structure.BoundingBox;

@Mixin(BoundingBox.class)
public class BoundingBoxMixin {
    @Redirect(method = "<init>(IIIIII)V", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;)V"))
    public void shush(Logger logger, String message) {}
}
