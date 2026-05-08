package twilightforest.asm;

import me.shedaniel.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.block.Blocks;
import twilightforest.init.TFSounds;

/**
 * Manningham Mills {@code mm:early_risers} entrypoint that grafts five Twilight Forest
 * custom {@code BiomeSpecialEffects$GrassColorModifier} enum values into the JVM enum
 * BEFORE the class is initialized. Required so the codec accepts the
 * {@code "twilightforest:dark_forest"}-style strings that the TF biome JSONs reference.
 *
 * <p>Implementation notes:</p>
 * <ul>
 *   <li>{@code GrassColorModifier} in 1.21.1 is an abstract enum whose vanilla entries
 *       are anonymous subclasses. Manningham Mills {@code addEnumSubclass} mirrors that
 *       bytecode shape for the five Twilight Forest values.</li>
 *   <li>The struct classes under {@code twilightforest.asm.grass} implement
 *       {@code modifyColor(double, double, int)}; MM copies those methods onto the
 *       generated enum subclasses before Minecraft initializes the enum.</li>
 *   <li>Color formulas live in {@code BiomeGrassColors} and match upstream Twilight
 *       Forest grass color algorithms 1:1.</li>
 * </ul>
 */
public class CodexGrassColorEarlyRiser implements Runnable {

    @Override
    public void run() {
        ClassTinkerers.addTransformation(mapClass("class_4763$class_5486"), node -> {
            if (node.permittedSubclasses != null) {
                node.permittedSubclasses.clear();
            }
        });

        ClassTinkerers.enumBuilder(mapClass("class_8107"), "Ljava/lang/String;", descriptor(mapClass("class_3414")))
                .addEnum("TWILIGHTFOREST_PINCH", () -> new Object[]{"twilightforest:pinch", TFSounds.PINCH_BEETLE_ATTACK})
                .build();

        ClassTinkerers.enumBuilder(mapClass("class_1814"), "I", "Ljava/lang/String;", descriptor(mapClass("class_124")))
                .addEnum("TWILIGHTFOREST_TWILIGHT", () -> new Object[]{5, "twilightforest:twilight", ChatFormatting.DARK_GREEN})
                .build();

        ClassTinkerers.enumBuilder(mapClass("class_811"), int.class, String.class)
                .addEnum("TWILIGHTFOREST_JARRED", () -> new Object[]{9, "twilightforest:jarred"})
                .build();

        ClassTinkerers.enumBuilder(mapClass("class_1690$class_1692"), descriptor(mapClass("class_2248")), "Ljava/lang/String;")
                .addEnum("TWILIGHTFOREST_TWILIGHT_OAK", () -> new Object[]{Blocks.OAK_PLANKS, "twilightforest:twilight_oak"})
                .addEnum("TWILIGHTFOREST_CANOPY", () -> new Object[]{Blocks.DARK_OAK_PLANKS, "twilightforest:canopy"})
                .addEnum("TWILIGHTFOREST_MANGROVE", () -> new Object[]{Blocks.MANGROVE_PLANKS, "twilightforest:mangrove"})
                .addEnum("TWILIGHTFOREST_DARK", () -> new Object[]{Blocks.DARK_OAK_PLANKS, "twilightforest:dark"})
                .addEnum("TWILIGHTFOREST_TIME", () -> new Object[]{Blocks.SPRUCE_PLANKS, "twilightforest:time"})
                .addEnum("TWILIGHTFOREST_TRANSFORMATION", () -> new Object[]{Blocks.JUNGLE_PLANKS, "twilightforest:transformation"})
                .addEnum("TWILIGHTFOREST_MINING", () -> new Object[]{Blocks.BIRCH_PLANKS, "twilightforest:mining"})
                .addEnum("TWILIGHTFOREST_SORTING", () -> new Object[]{Blocks.CHERRY_PLANKS, "twilightforest:sorting"})
                .build();

        ClassTinkerers.enumBuilder(mapClass("class_4763$class_5486"), String.class)
                .addEnumSubclass("TWILIGHTFOREST_ENCHANTED_FOREST", "twilightforest.asm.grass.EnchantedForestGrassColorModifier", "twilightforest:enchanted_forest")
                .addEnumSubclass("TWILIGHTFOREST_SWAMP", "twilightforest.asm.grass.SwampGrassColorModifier", "twilightforest:swamp")
                .addEnumSubclass("TWILIGHTFOREST_DARK_FOREST", "twilightforest.asm.grass.DarkForestGrassColorModifier", "twilightforest:dark_forest")
                .addEnumSubclass("TWILIGHTFOREST_DARK_FOREST_CENTER", "twilightforest.asm.grass.DarkForestCenterGrassColorModifier", "twilightforest:dark_forest_center")
                .addEnumSubclass("TWILIGHTFOREST_SPOOKY_FOREST", "twilightforest.asm.grass.SpookyForestGrassColorModifier", "twilightforest:spooky_forest")
                .build();
    }

    private static String mapClass(String intermediaryName) {
        return FabricLoader.getInstance().getMappingResolver()
                .mapClassName("intermediary", "net.minecraft." + intermediaryName);
    }

    private static String descriptor(String mappedClassName) {
        return "L" + mappedClassName.replace('.', '/') + ";";
    }
}
