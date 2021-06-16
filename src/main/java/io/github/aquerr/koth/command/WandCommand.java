package io.github.aquerr.koth.command;

import io.github.aquerr.koth.util.KothWand;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;

public class WandCommand extends AbstractCommand
{
    @Override
    public CommandResult execute(final CommandContext args) throws CommandException
    {
        ServerPlayer serverPlayer = requireServerPlayer(args);
        final Inventory inventory = serverPlayer.inventory();

        ItemStack kothWand = KothWand.getKothWand();
        inventory.offer(kothWand);

        return CommandResult.success();
    }
}
