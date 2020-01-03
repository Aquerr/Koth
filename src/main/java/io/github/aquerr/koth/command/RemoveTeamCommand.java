package io.github.aquerr.koth.command;

import io.github.aquerr.koth.Koth;
import io.github.aquerr.koth.PluginInfo;
import io.github.aquerr.koth.entity.Arena;
import io.github.aquerr.koth.entity.ArenaTeam;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class RemoveTeamCommand extends AbstractCommand
{
    public RemoveTeamCommand(final Koth plugin)
    {
        super(plugin);
    }

    @Override
    public CommandResult execute(final CommandSource source, final CommandContext args) throws CommandException
    {
        final String name = args.requireOne(Text.of("name"));

        if (!(source instanceof Player))
            throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Only in-game players can use this command!"));

        final Player player = (Player) source;

        Arena arena = super.getPlugin().getPlayersCreatingArena().get(player.getUniqueId());
        if (arena == null)
            arena = super.getPlugin().getPlayersEditingArena().get(player.getUniqueId());

        if(arena == null)
            throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "You must be in arena editing mode to do this."));

        if (!arena.hasTeam(name))
            throw new CommandException(Text.of(PluginInfo.PLUGIN_ERROR, TextColors.RED, "Team with such name does not exists in this arena!"));

        final ArenaTeam arenaTeam = arena.getTeams().stream().filter(x->x.getName().equals(name)).findFirst().get();
        arena.removeTeam(arenaTeam);
        super.getPlugin().getArenaManager().updateArena(arena);
        source.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.GREEN, "Successfully removed team from the arena!"));
        return CommandResult.success();
    }
}
