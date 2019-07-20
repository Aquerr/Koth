package io.github.aquerr.koth.command;

import io.github.aquerr.koth.Koth;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class WandCommand extends AbstractCommand
{
    private final Koth plugin;

    public WandCommand(final Koth plugin)
    {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(final CommandSource source, final CommandContext args) throws CommandException {
        if (!(source instanceof Player))
            throw new CommandException(Text.of("Only in-game players can use this command!"));

        final Player player = (Player) source;


        return CommandResult.success();
    }
}
