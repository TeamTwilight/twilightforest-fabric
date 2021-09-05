package twilightforest.mixin.plugin;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import twilightforest.mixin.plugin.patches.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TwilightForestMixinPlugin implements IMixinConfigPlugin {
    private List<Patch> patches;
    @Override
    public void onLoad(String mixinPackage) {
        patches = new ArrayList<>();
        patches.add(new LevelPatch());
        patches.add(new ServerLevelPatch());
        patches.add(new ServerLevelEntitycallbackPatch());
        patches.add(new ServerEntityPatch());
        patches.add(new EntityRenderDispatcherPatch());
        patches.add(new LevelRendererPatch());
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        for(Patch patch : patches) {
            //System.out.println(targetClassName.equals(patch.getMixinClass()) + " : " + targetClassName);
            patch.applyClass(targetClass);
            if(patch.getMixinClass().equals(targetClassName)) {
                for(MethodNode node : targetClass.methods) {
                    //System.out.println(node.desc);
                    System.out.println(node.name + " : "+ node.desc);
                    if(node.name.equals(patch.getMethodName()) && node.desc.equals(patch.getMethodDesc())) {
                        patch.applyMethod(node);
                    }
                }
            }
        }
    }
}
