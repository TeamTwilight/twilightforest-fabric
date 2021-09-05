package twilightforest.mixin.plugin.patches;

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
        return "net.minecraft.server.level.ServerEntity";
    }

    @Override
    public String getMethodName() {
        return "sendDirtyEntityData";
    }

    @Override
    public String getMethodDesc() {
        return "()V";
    }
}
