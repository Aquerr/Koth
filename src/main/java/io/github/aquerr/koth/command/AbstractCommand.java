package io.github.aquerr.koth.command;

import io.github.aquerr.koth.Koth;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public abstract class AbstractCommand implements CommandExecutor
{

    private final Koth plugin;

    public AbstractCommand(final Koth plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public abstract CommandResult execute(CommandSource source, CommandContext args) throws CommandException;
}
