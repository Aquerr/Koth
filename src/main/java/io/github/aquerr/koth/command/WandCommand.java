package io.github.aquerr.koth.command;

import io.github.aquerr.koth.Koth;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;

public class WandCommand extends AbstractCommand
{
    private final Koth plugin;

    public WandCommand(final Koth plugin)
    {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(final CommandSource source, final CommandContext args) throws CommandException
    {

        return CommandResult.success();
    }
}
