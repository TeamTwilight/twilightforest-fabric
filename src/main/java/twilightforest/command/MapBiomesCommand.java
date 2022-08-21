package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import twilightforest.init.BiomeKeys;
import twilightforest.item.MagicMapItem;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Thank you @SuperCoder79 (from Twitter) for sharing the original code! Code sourced from a LGPL project
 */
public class MapBiomesCommand {
    private static final DecimalFormat numberFormat = new DecimalFormat("#.00");

    private static final HashMap<ResourceLocation, Color> BIOME2COLOR = new HashMap<>();

    private static void init() {
        if (!BIOME2COLOR.isEmpty())
            return;

        BIOME2COLOR.put(BiomeKeys.STREAM.location(), new Color(0, 0, 255));
        BIOME2COLOR.put(BiomeKeys.LAKE.location(), new Color(0, 0, 255));
        BIOME2COLOR.put(BiomeKeys.CLEARING.location(), new Color(132, 245, 130));
        BIOME2COLOR.put(BiomeKeys.OAK_SAVANNAH.location(), new Color(239, 245, 130));
        BIOME2COLOR.put(BiomeKeys.FOREST.location(), new Color(0, 255, 0));
        BIOME2COLOR.put(BiomeKeys.DENSE_FOREST.location(), new Color(0, 170, 0));
        BIOME2COLOR.put(BiomeKeys.FIREFLY_FOREST.location(), new Color(88, 252, 102));
        BIOME2COLOR.put(BiomeKeys.ENCHANTED_FOREST.location(), new Color(0, 255, 255));
        BIOME2COLOR.put(BiomeKeys.SPOOKY_FOREST.location(), new Color(119, 0, 255));
        BIOME2COLOR.put(BiomeKeys.MUSHROOM_FOREST.location(), new Color(204, 0, 139));
        BIOME2COLOR.put(BiomeKeys.DENSE_MUSHROOM_FOREST.location(), new Color(184, 48, 184));

        BIOME2COLOR.put(BiomeKeys.SWAMP.location(), new Color(0, 204, 187));
        BIOME2COLOR.put(BiomeKeys.FIRE_SWAMP.location(), new Color(140, 0, 0));

        BIOME2COLOR.put(BiomeKeys.DARK_FOREST.location(), new Color(25, 61, 13));
        BIOME2COLOR.put(BiomeKeys.DARK_FOREST_CENTER.location(), new Color(157, 79, 0));

        BIOME2COLOR.put(BiomeKeys.SNOWY_FOREST.location(), new Color(255, 255, 255));
        BIOME2COLOR.put(BiomeKeys.GLACIER.location(), new Color(130, 191, 245));

        BIOME2COLOR.put(BiomeKeys.HIGHLANDS.location(), new Color(100, 65, 0));
        BIOME2COLOR.put(BiomeKeys.THORNLANDS.location(), new Color(128, 100, 90));
        BIOME2COLOR.put(BiomeKeys.FINAL_PLATEAU.location(), new Color(128, 128, 128));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("biomepng").requires(cs -> cs.hasPermission(2)).executes(MapBiomesCommand::execute);
    }

    private static int execute(CommandContext<CommandSourceStack> source) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
            return -1;

        init();

        //setup image
        Map<Biome, Integer> biomeCount = new HashMap<>();
        BufferedImage img = new BufferedImage(4096, 4096, BufferedImage.TYPE_INT_RGB);

        int progressUpdate = img.getHeight() / 8;

        for (int x = 0; x < img.getHeight(); x++) {
            for (int z = 0; z < img.getWidth(); z++) {
                ServerLevel level = source.getSource().getLevel();
                Biome b = level.getNoiseBiome(x - 2048, 0, z - 2048).value();
                ResourceLocation key = level.registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY).getKey(b);
                Color color = BIOME2COLOR.get(key);

                if (color == null) {
                    int colorInt = MagicMapItem.getBiomeColor(source.getSource().getLevel(), b);

                    if (colorInt == 0)
                        colorInt = b.getGrassColor(0, 0);

                    BIOME2COLOR.put(key, color = new Color(colorInt | 0xFF000000));
                }

                if (!biomeCount.containsKey(b)) {
                    biomeCount.put(b, 0);
                } else {
                    biomeCount.put(b, biomeCount.get(b) + 1);
                }

                //set the color
                img.setRGB(x, z, color.getRGB());
            }

            //send a progress update to let people know the server isn't dying
            if (x % progressUpdate == 0) {
                source.getSource().sendSuccess(Component.translatable(((double) x / img.getHeight()) * 100 + "% Done mapping"), true);
            }
        }

        source.getSource().sendSuccess(Component.literal("Approximate biome-block counts within an 2048x2048 region"), true);
        int totalCount = biomeCount.values().stream().mapToInt(i -> i).sum();
        biomeCount.forEach((biome, integer) -> source.getSource().sendSuccess(Component.literal(
                source.getSource().getLevel().registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY).getKey(biome).toString())
                .append(": " + (integer) + ChatFormatting.GRAY + " (" + numberFormat.format(((double) integer / totalCount) * 100) + "%)"), true));

        //save the biome map
        Path p = Paths.get("biomemap.png");
        try {
            ImageIO.write(img, "png", p.toAbsolutePath().toFile());
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

        source.getSource().sendSuccess(Component.literal("Image saved!"), true);

        return Command.SINGLE_SUCCESS;
    }
}