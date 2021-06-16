package io.github.aquerr.koth.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KothWand {

    public static ItemStack getKothWand()
    {
        final ItemStack kothWand = ItemStack.builder()
                .itemType(ItemTypes.IRON_SWORD)
                .quantity(1)
                .add(Keys.CUSTOM_NAME, Component.text("Koth Wand", NamedTextColor.GOLD))
                .build();

        updateWandSelectionPoints(kothWand,new SelectionPoints(null,null));
        return kothWand;
    }

    public static void updateWandSelectionPoints(final ItemStack wand, final SelectionPoints points)
    {
        final List<Component> wandDescriptionLines = new ArrayList<>();
        final TextComponent firstLine = Optional.ofNullable(points.getFirstPoint())
                .map(point -> Component.text("First point: ").append(Component.text(point.toString(), NamedTextColor.GOLD)))
                .orElse(Component.text("Select first point with your").append(Component.text(" left click.", NamedTextColor.GOLD)));
        final TextComponent secondLine = Optional.ofNullable(points.getSecondPoint())
                .map(point -> Component.text("Second point: ").append(Component.text(point.toString(), NamedTextColor.GOLD)))
                .orElse(Component.text("Select second point with your").append(Component.text(" right click.", NamedTextColor.GOLD)));

        wandDescriptionLines.add(firstLine);
        wandDescriptionLines.add(secondLine);

        wand.offer(Keys.LORE, wandDescriptionLines);
    }
}
