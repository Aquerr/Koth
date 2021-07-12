package io.github.aquerr.koth.command;

import io.github.aquerr.koth.manager.ArenaClassManager;
import io.github.aquerr.koth.model.ArenaClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;

import java.util.LinkedList;
import java.util.List;

public class CreateArenaClassCommand extends AbstractCommand
{
    private final ArenaClassManager arenaClassManager;

    public CreateArenaClassCommand(final ArenaClassManager arenaClassManager)
    {
        this.arenaClassManager = arenaClassManager;
    }

    @Override
    public CommandResult execute(CommandContext context) throws CommandException {
        final String className = context.requireOne(Parameter.string().key("name").build());
        ServerPlayer serverPlayer = requireServerPlayer(context);

        final List<ItemStack> items = getItemFromPlayer(serverPlayer);
        final boolean didSucceed = this.arenaClassManager.saveOrUpdate(new ArenaClass(className, items));
        if(!didSucceed)
            throw new CommandException(Component.text("Something went wrong while creating the arena class..."));
        serverPlayer.sendMessage(TextComponent.ofChildren(Component.text("Successfully created the arena class!", NamedTextColor.GOLD)));
        return CommandResult.success();
    }
    private List<ItemStack> getItemFromPlayer(final ServerPlayer serverPlayer)
    {
        final List<ItemStack> items = new LinkedList<>();
        final List<Slot> slots = serverPlayer.inventory().slots();

        for (final Slot slot : slots)
        {
            if(!slot.peek().equalTo(ItemStack.empty()))
                items.add(slot.peek());
        }
        return items;
    }
}
