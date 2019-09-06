package io.github.aquerr.koth.command;

import io.github.aquerr.koth.Koth;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;

public class ListCommand extends AbstractCommand
{
    public ListCommand(final Koth plugin)
    {
        super(plugin);
    }

    @Override
    public CommandResult execute(final CommandSource source, final CommandContext args) throws CommandException
    {
        //TODO: List Command should list all arenas on the server.

        return CommandResult.success();
    }
}
