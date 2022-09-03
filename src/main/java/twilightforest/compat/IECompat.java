package twilightforest.compat;

import io.github.fabricators_of_create.porting_lib.event.client.RegisterShadersCallback.ShaderRegistry;
import me.alphamode.star.events.client.ModelBakeEvent;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.phys.Vec3;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.entity.projectile.CicadaShot;

import java.util.Locale;

/*
import blusunrize.immersiveengineering.api.crafting.builders.ThermoelectricSourceBuilder;
import blusunrize.immersiveengineering.api.shader.ShaderRegistry;
import blusunrize.immersiveengineering.api.tool.RailgunHandler;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.client.TFClientEvents;
import twilightforest.client.shader.ShaderManager;
import twilightforest.compat.ie.IEShaderRegister;
import twilightforest.compat.ie.ShaderBagItemModel;
import twilightforest.compat.ie.TFShaderGrabbagItem;
import twilightforest.compat.ie.TFShaderItem;
import twilightforest.entity.projectile.CicadaShot;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * FIXME a few things still dont work in IE compat. Here is a list:
 * - Shader Grabbags are missing the starburst shader that appears in the back of it
 * - 5 Shaders are hopelessly broken at the moment, theyre commented out in {@link IEShaderRegister#initShaders}
 *   - I wasnt able to properly call {@link ShaderManager#initShaders}, so that may be why
 * - There was a lonely command in the client proxy that I moved to its own command class. Once ShaderManager is back online, verify the command works and add it to the list of commands.
 * - A class in the disabled bin called GradientMappedTexture is helplessly broken. If anyone can fix this, re-enable the event in {@link TFClientEvents.ModBusEvents#texStitch} and correct the bandaid fixes in {@link IEShaderRegister}
 *
public class IECompat extends TFCompat {

    public IECompat() {
        super("Immersive Engineering");
    }

    @Override
    protected void initItems(RegistryEvent.Register<Item> evt) {
        IForgeRegistry<Item> r = evt.getRegistry();
        r.register(new TFShaderItem().setRegistryName(TwilightForestMod.prefix("shader")));
        for (Rarity rarity : ShaderRegistry.rarityWeightMap.keySet()) {
            r.register(new TFShaderGrabbagItem(rarity));
        }
    }

    @Override
    protected boolean preInit() {
        ShaderRegistry.rarityWeightMap.put(TwilightForestMod.getRarity(), 1);
        return true;
    }

    @Override
    protected void init(FMLCommonSetupEvent event) {
        // Yeah, it's a thing! https://twitter.com/AtomicBlom/status/1004931868012056583
        RailgunHandler.registerProjectile(() -> Ingredient.of(TFBlocks.CICADA.get().asItem()),
                (new RailgunHandler.StandardRailgunProjectile(2.0D, 0.25D) {

                    @Override
                    public Entity getProjectile(@Nullable Player shooter, ItemStack ammo, Entity projectile) {
                        Vec3 look = shooter.getLookAngle();
                        //we zoomin
                        shooter.level.playSound(null, shooter.blockPosition(), TFSounds.CICADA_FLYING, SoundSource.NEUTRAL, 1.0F, 1.0F);
                        //FallingBlockEntity doesnt like cicadas, so custom entity it is
                        return new CicadaShot(shooter.level, shooter, look.x * 20.0D, look.y * 20.0D, look.z * 20.0D);
                    }

                    @Override
                    public boolean isValidForTurret() {
                        return false;
                    }
                }));

        ThermoelectricSourceBuilder.builder(TFBlocks.FIERY_BLOCK.get()).kelvin(2500);

        IEShaderRegister.initShaders();
    }

    @Override
    protected void postInit() {

    }

    @Override
    protected void handleIMCs() {
    }

    public static void registerShaderColors(ItemColors colors) {
        colors.register(TFShaderItem::getShaderColors, ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader")));
        for(Rarity r: ShaderRegistry.rarityWeightMap.keySet()) {
            colors.register((stack, tintIndex) -> {
                int c = r.color.getColor();

                float d = tintIndex + 1;

                return (int) ((c >> 16 & 0xFF) / d) << 16
                        | (int) ((c >> 8 & 0xFF) / d) << 8
                        | (int) ((c & 0xFF) / d);
            }, ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader_bag_" + r.name().toLowerCase(Locale.ROOT).replace(':', '_'))));
        }
    }

    public static void registerShaderModels(ModelBakeEvent event) {
        for (Rarity rarity : ShaderRegistry.rarityWeightMap.keySet()) {
            ResourceLocation itemRL = TwilightForestMod.prefix("shader_bag_" + rarity.name().toLowerCase(Locale.ROOT).replace(':', '_'));
            ModelResourceLocation mrl = new ModelResourceLocation(itemRL, "inventory");
            event.getModelRegistry().put(mrl, new ShaderBagItemModel(event.getModelRegistry().get(mrl), new ItemStack(ForgeRegistries.ITEMS.getValue(itemRL))));
        }
    }
}
*/
