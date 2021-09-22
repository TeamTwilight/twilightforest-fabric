package twilightforest.compat.clothConfig;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.util.Utils;
import me.shedaniel.clothconfig2.api.*;
import me.shedaniel.clothconfig2.gui.entries.MultiElementListEntry;
import me.shedaniel.clothconfig2.gui.entries.NestedListListEntry;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import twilightforest.TwilightForestMod;

import java.util.*;
import java.util.stream.Collectors;

@Config(name = TwilightForestMod.ID)
public class TFConfigMenuFabric implements ConfigData {
    private static final String config = TwilightForestMod.ID + ".config.";

    private final static TranslatableComponent toolTip(String string){
        return new TranslatableComponent(config + string + ".tooltip");
    }

    public static ConfigBuilder getConfigBuilderWithDemo() {
        class Pair<T, R> {
            T t;
            R r;

            public Pair(T t, R r) {
                this.t = t;
                this.r = r;
            }

            public T getLeft() {
                return t;
            }

            public R getRight() {
                return r;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Pair<?, ?> pair = (Pair<?, ?>) o;

                if (!Objects.equals(t, pair.t)) return false;
                return Objects.equals(r, pair.r);
            }

            @Override
            public int hashCode() {
                int result = t != null ? t.hashCode() : 0;
                result = 31 * result + (r != null ? r.hashCode() : 0);
                return result;
            }
        }

        ConfigBuilder builder = ConfigBuilder.create().setTitle(new TranslatableComponent("title.cloth-config.config"));
        builder.setDefaultBackgroundTexture(new ResourceLocation("minecraft:textures/block/oak_planks.png"));
        //builder.setGlobalized(true);
        //builder.alwaysShowTabs();
        builder.getEntryBuilder();
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        {
            ConfigCategory common = builder.getOrCreateCategory(new TextComponent("Common"));

            {
                SubCategoryBuilder Dimensions = entryBuilder.startSubCategory(new TranslatableComponent(config + "dimension"))
                        .setTooltip(toolTip("hollow_hill_stalactites"))
                        .setExpanded(true);

                //newPlayersSpawnInTF
                Dimensions.add(entryBuilder
                        .startBooleanToggle(new TranslatableComponent(config + "spawn_in_tf"), false)
                        .setTooltip(toolTip("spawn_in_tf"))
                        .setDefaultValue(false)
                        .build());

                //skylightForest
                Dimensions.add(entryBuilder
                        .startBooleanToggle(new TranslatableComponent(config + "skylight_forest"), false)
                        .setTooltip(toolTip("skylight_forest"))
                        .setDefaultValue(false)
                        .requireRestart()
                        .build());

                //skylightOaks
                Dimensions.add(entryBuilder
                        .startBooleanToggle(new TranslatableComponent(config + "skylight_oaks"), true)
                        .setTooltip(toolTip("skylight_oaks"))
                        .setDefaultValue(true)
                        .requireRestart()
                        .build());

                //portalDestinationID
                Dimensions.add(entryBuilder
                        .startIntField(new TranslatableComponent(config + "portal_destination_id"), 22)
                        .setTooltip(toolTip("portal_destination_id"))
                        .setDefaultValue(22)
                        .requireRestart()
                        .build());
                {
                    SubCategoryBuilder dimension_hollow_hill = entryBuilder.startSubCategory(new TranslatableComponent(config + "hollow_hill_stalactites"))
                            .setTooltip(toolTip("hollow_hill_stalactites"))
                            .setExpanded(false);

                    dimension_hollow_hill.add(entryBuilder
                            .startStrList(new TranslatableComponent(config + "large_hill"), new ArrayList<String>())
                            .setTooltip(toolTip("large_hill"))
                            .requireRestart()
                            .build());

                    dimension_hollow_hill.add(entryBuilder
                            .startStrList(new TranslatableComponent(config + "mediumHill"), new ArrayList<String>())
                            .setTooltip(toolTip("mediumHill"))
                            .requireRestart()
                            .build());

                    dimension_hollow_hill.add(entryBuilder
                            .startStrList(new TranslatableComponent(config + "smallHill"), new ArrayList<String>())
                            .setTooltip(toolTip("smallHill"))
                            .requireRestart()
                            .build());

                    dimension_hollow_hill.add(entryBuilder
                            .startBooleanToggle(new TranslatableComponent(config + "stalactite_config_only"), false)
                            .setTooltip(toolTip("stalactite_config_only"))
                            .requireRestart()
                            .build());

                    Dimensions.add(dimension_hollow_hill.build());
                }

                common.addEntry(Dimensions.build());
            }

            ConfigCategory client = builder.getOrCreateCategory(new TextComponent("client"));

            client.addEntry(entryBuilder.startBooleanToggle(new TranslatableComponent(config + "doCompat"), true)
                    .setTooltip(toolTip("doCompat"))
                    .setDefaultValue(true)
                    .requireRestart()
                    .build());

            client.addEntry(entryBuilder
                    .startStrField(new TranslatableComponent(config + "origin_dimension"), "minecraft:overworld")
                    .setTooltip(toolTip("origin_dimension"))
                    .build());

            client.addEntry(entryBuilder
                    .startBooleanToggle(new TranslatableComponent(config + "portals_in_other_dimensions"), false)
                    .setTooltip(toolTip("portals_in_other_dimensions"))
                    .setDefaultValue(false)
                    .build());

            common.addEntry(entryBuilder
                    .startBooleanToggle(new TranslatableComponent(config + "admin_portals"), false)
                    .setTooltip(toolTip("admin_portals"))
                    .setDefaultValue(false)
                    .build());

            common.addEntry(entryBuilder
                    .startBooleanToggle(new TranslatableComponent(config + "portals"), false)
                    .setTooltip(toolTip("portals"))
                    .setDefaultValue(false)
                    .build());

            common.addEntry(entryBuilder
                    .startBooleanToggle(new TranslatableComponent(config + "check_portal_destination"), false)
                    .setTooltip(toolTip("check_portal_destination"))
                    .setDefaultValue(false)
                    .build());

            common.addEntry(entryBuilder
                    .startBooleanToggle(new TranslatableComponent(config + "portal_lighting"), false)
                    .setTooltip(toolTip("portal_lighting"))
                    .setDefaultValue(false)
                    .build());

            common.addEntry(entryBuilder
                    .startBooleanToggle(new TranslatableComponent(config + "portal_return"), true)
                    .setTooltip(toolTip("portal_return"))
                    .setDefaultValue(true)
                    .build());

            common.addEntry(entryBuilder
                    .startBooleanToggle(new TranslatableComponent(config + "uncrafting"), false)
                    .setTooltip(toolTip("uncrafting"))
                    .setDefaultValue(false)
                    .requireRestart()
                    .build());

            common.addEntry(entryBuilder
                    .startBooleanToggle(new TranslatableComponent(config + "casket_uuid_locking"), false)
                    .setTooltip(toolTip("casket_uuid_locking"))
                    .setDefaultValue(false)
                    .requireRestart()
                    .build());

            common.addEntry(entryBuilder
                    .startBooleanToggle(new TranslatableComponent(config + "disable_skull_candles"), false)
                    .setTooltip(toolTip("disable_skull_candles"))
                    .setDefaultValue(false)
                    .build());

            {
                SubCategoryBuilder shield_interactions = entryBuilder.startSubCategory(new TranslatableComponent(config + "shield_parry"))
                        .setTooltip(toolTip("shield_parry"))
                        .setExpanded(false);

                shield_interactions.add(entryBuilder.startBooleanToggle(new TranslatableComponent(config + "parry_non_twilight"), false)
                        .setTooltip(toolTip("parry_non_twilight"))
                        .setDefaultValue(false)
                        .build());

                shield_interactions.add(entryBuilder.startIntSlider(new TranslatableComponent(config + "parry_window_arrow"), 40, 0, Integer.MAX_VALUE)
                        .setTooltip(toolTip("parry_window_arrow"))
                        .setDefaultValue(40)
                        .build());

                shield_interactions.add(entryBuilder.startIntSlider(new TranslatableComponent(config + "parry_window_fireball"), 40, 0, Integer.MAX_VALUE)
                        .setTooltip(toolTip("parry_window_fireball"))
                        .setDefaultValue(40)
                        .build());

                shield_interactions.add(entryBuilder.startIntSlider(new TranslatableComponent(config + "parry_window_throwable"), 40, 0, Integer.MAX_VALUE)
                        .setTooltip(toolTip("parry_window_throwable"))
                        .setDefaultValue(40)
                        .build());

                shield_interactions.add(entryBuilder.startIntSlider(new TranslatableComponent(config + "parry_window_beam"), 10, 0, Integer.MAX_VALUE)
                        .setTooltip(toolTip("parry_window_beam"))
                        .setDefaultValue(40)
                        .build());



                common.addEntry(shield_interactions.build());
            }

        }

        builder.setDefaultBackgroundTexture(new ResourceLocation(TwilightForestMod.ID, "textures/block/maze_stone_brick.png"));

        return builder;

        /*
        testing.addEntry(entryBuilder.startKeyCodeField(new TextComponent("Cool Key"), InputConstants.UNKNOWN).setDefaultValue(InputConstants.UNKNOWN).build());
        testing.addEntry(entryBuilder.startModifierKeyCodeField(new TextComponent("Cool Modifier Key"), ModifierKeyCode.of(InputConstants.Type.KEYSYM.getOrCreate(79), Modifier.of(false, true, false))).setDefaultValue(ModifierKeyCode.of(InputConstants.Type.KEYSYM.getOrCreate(79), Modifier.of(false, true, false))).build());
        testing.addEntry(entryBuilder.startDoubleList(new TextComponent("A list of Doubles"), Arrays.asList(1d, 2d, 3d)).setDefaultValue(Arrays.asList(1d, 2d, 3d)).build());
        testing.addEntry(entryBuilder.startLongList(new TextComponent("A list of Longs"), Arrays.asList(1L, 2L, 3L)).setDefaultValue(Arrays.asList(1L, 2L, 3L)).build());
        testing.addEntry(entryBuilder.startStrList(new TextComponent("A list of Strings"), Arrays.asList("abc", "xyz")).setTooltip(new TextComponent("Yes this is some beautiful tooltip\nOh and this is the second line!")).setDefaultValue(Arrays.asList("abc", "xyz")).build());

        SubCategoryBuilder colors = entryBuilder.startSubCategory(new TextComponent("Colors")).setExpanded(true);
        colors.add(entryBuilder.startColorField(new TextComponent("A color field"), 0x00ffff).setDefaultValue(0x00ffff).build());
        colors.add(entryBuilder.startColorField(new TextComponent("An alpha color field"), 0xff00ffff).setDefaultValue(0xff00ffff).setAlphaMode(true).build());
        colors.add(entryBuilder.startColorField(new TextComponent("An alpha color field"), 0xffffffff).setDefaultValue(0xffff0000).setAlphaMode(true).build());
        colors.add(entryBuilder.startDropdownMenu(new TextComponent("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.APPLE), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setDefaultValue(Items.APPLE).setSelections(Registry.ITEM.stream().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new))).setSaveConsumer(item -> System.out.println("save this " + item)).build());
        colors.add(entryBuilder.startDropdownMenu(new TextComponent("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.APPLE), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setDefaultValue(Items.APPLE).setSelections(Registry.ITEM.stream().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new))).setSaveConsumer(item -> System.out.println("save this " + item)).build());
        colors.add(entryBuilder.startDropdownMenu(new TextComponent("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.APPLE), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setDefaultValue(Items.APPLE).setSelections(Registry.ITEM.stream().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new))).setSaveConsumer(item -> System.out.println("save this " + item)).build());
        colors.add(entryBuilder.startDropdownMenu(new TextComponent("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.APPLE), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setDefaultValue(Items.APPLE).setSelections(Registry.ITEM.stream().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new))).setSaveConsumer(item -> System.out.println("save this " + item)).build());
        colors.add(entryBuilder.startDropdownMenu(new TextComponent("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.APPLE), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setDefaultValue(Items.APPLE).setSelections(Registry.ITEM.stream().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new))).setSaveConsumer(item -> System.out.println("save this " + item)).build());

        SubCategoryBuilder innerColors = entryBuilder.startSubCategory(new TextComponent("Inner Colors")).setExpanded(true);
        innerColors.add(entryBuilder.startDropdownMenu(new TextComponent("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.APPLE), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setDefaultValue(Items.APPLE).setSelections(Registry.ITEM.stream().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new))).setSaveConsumer(item -> System.out.println("save this " + item)).build());
        innerColors.add(entryBuilder.startDropdownMenu(new TextComponent("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.APPLE), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setDefaultValue(Items.APPLE).setSelections(Registry.ITEM.stream().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new))).setSaveConsumer(item -> System.out.println("save this " + item)).build());
        innerColors.add(entryBuilder.startDropdownMenu(new TextComponent("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.APPLE), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setDefaultValue(Items.APPLE).setSelections(Registry.ITEM.stream().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new))).setSaveConsumer(item -> System.out.println("save this " + item)).build());

        SubCategoryBuilder innerInnerColors = entryBuilder.startSubCategory(new TextComponent("Inner Inner Colors")).setExpanded(true);
        innerInnerColors.add(entryBuilder.startDropdownMenu(new TextComponent("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.APPLE), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setDefaultValue(Items.APPLE).setSelections(Registry.ITEM.stream().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new))).setSaveConsumer(item -> System.out.println("save this " + item)).build());
        innerInnerColors.add(entryBuilder.startDropdownMenu(new TextComponent("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.APPLE), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setDefaultValue(Items.APPLE).setSelections(Registry.ITEM.stream().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new))).setSaveConsumer(item -> System.out.println("save this " + item)).build());
        innerInnerColors.add(entryBuilder.startDropdownMenu(new TextComponent("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.APPLE), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setDefaultValue(Items.APPLE).setSelections(Registry.ITEM.stream().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new))).setSaveConsumer(item -> System.out.println("save this " + item)).build());
        innerColors.add(innerInnerColors.build());
        colors.add(innerColors.build());
        testing.addEntry(colors.build());
        testing.addEntry(entryBuilder.startDropdownMenu(new TextComponent("Suggestion Random Int"), DropdownMenuBuilder.TopCellElementBuilder.of(10,
                s -> {
                    try {
                        return Integer.parseInt(s);
                    } catch (NumberFormatException ignored) {

                    }
                    return null;
                })).setDefaultValue(10).setSelections(Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).build());
        testing.addEntry(entryBuilder.startDropdownMenu(new TextComponent("Selection Random Int"), DropdownMenuBuilder.TopCellElementBuilder.of(10,
                s -> {
                    try {
                        return Integer.parseInt(s);
                    } catch (NumberFormatException ignored) {

                    }
                    return null;
                })).setDefaultValue(5).setSuggestionMode(false).setSelections(Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).build());
        testing.addEntry(new NestedListListEntry<Pair<Integer, Integer>, MultiElementListEntry<Pair<Integer, Integer>>>(
                new TextComponent("Nice"),
                Lists.newArrayList(new Pair<>(10, 10), new Pair<>(20, 40)),
                false,
                Optional::empty,
                list -> {
                },
                () -> Lists.newArrayList(new Pair<>(10, 10), new Pair<>(20, 40)),
                entryBuilder.getResetButtonKey(),
                true,
                true,
                (elem, nestedListListEntry) -> {
                    if (elem == null) {
                        Pair<Integer, Integer> newDefaultElemValue = new Pair<>(10, 10);
                        return new MultiElementListEntry<>(new TextComponent("Pair"), newDefaultElemValue,
                                Lists.newArrayList(entryBuilder.startIntField(new TextComponent("Left"), newDefaultElemValue.getLeft()).setDefaultValue(10).build(),
                                        entryBuilder.startIntField(new TextComponent("Right"), newDefaultElemValue.getRight()).setDefaultValue(10).build()),
                                true);
                    } else {
                        return new MultiElementListEntry<>(new TextComponent("Pair"), elem,
                                Lists.newArrayList(entryBuilder.startIntField(new TextComponent("Left"), elem.getLeft()).setDefaultValue(10).build(),
                                        entryBuilder.startIntField(new TextComponent("Right"), elem.getRight()).setDefaultValue(10).build()),
                                true);
                    }
                }
        ));
        testing.addEntry(entryBuilder.startTextDescription(
                new TranslatableComponent("text.cloth-config.testing.1",
                        new TextComponent("ClothConfig").withStyle(s -> s.withBold(true).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(Util.make(new ItemStack(Items.PINK_WOOL), stack -> stack.setHoverName(new TextComponent("(\u30FB\u2200\u30FB)")).enchant(Enchantments.BLOCK_EFFICIENCY, 10)))))),
                        new TranslatableComponent("text.cloth-config.testing.2").withStyle(s -> s.withColor(ChatFormatting.BLUE).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent("https://shedaniel.gitbook.io/cloth-config/"))).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://shedaniel.gitbook.io/cloth-config/"))),
                        new TranslatableComponent("text.cloth-config.testing.3").withStyle(s -> s.withColor(ChatFormatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, Utils.getConfigFolder().getParent().resolve("options.txt").toString())))
                )
        ).build());
        builder.transparentBackground();
        return builder;

         */


    }
}
