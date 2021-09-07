package twilightforest.mixin.plugin.patches;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class ServerEntityPatch implements Patch {
    @Override
    public void applyMethod(MethodNode node) {
        System.out.println("PATCHED CLASS");
    }

    @Override
    public void applyClass(ClassNode classNode) {}

    @Override
    public String getMixinClass() {
        return FabricLoader.getInstance().getMappingResolver().mapClassName("named", "net.minecraft.server.level.ServerEntity");
    }

    @Override
    public String getMethodName() {
        return FabricLoader.getInstance().getMappingResolver().mapMethodName("named", getMixinClass(), "sendDirtyEntityData", getMethodDesc());
    }

    @Override
    public String getMethodDesc() {
        return "()V";
    }
}
