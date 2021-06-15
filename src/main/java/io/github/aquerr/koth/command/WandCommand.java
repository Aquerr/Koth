package io.github.aquerr.koth.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WandCommand extends AbstractCommand
{
    @Override
    public CommandResult execute(final CommandContext args) throws CommandException
    {
        ServerPlayer serverPlayer = requireServerPlayer(args);
        final Inventory inventory = serverPlayer.inventory();

        final List<Component> wandDescriptionLines = new ArrayList<>();
        final TextComponent firstLine = Component.text("Select first point with your").append(Component.text(" left click.", NamedTextColor.GOLD));
        final TextComponent secondLine = Component.text("Select second point with your").append(Component.text(" right click.", NamedTextColor.GOLD));
        wandDescriptionLines.add(firstLine);
        wandDescriptionLines.add(secondLine);

        final ItemStack kothWand = ItemStack.builder()
                .itemType(ItemTypes.IRON_SWORD)
                .quantity(1)
                .add(Keys.CUSTOM_NAME, Component.text("Koth Wand", NamedTextColor.GOLD))
                .add(Keys.LORE, wandDescriptionLines)
                .build();

        inventory.offer(kothWand);

        return CommandResult.success();
    }
}
