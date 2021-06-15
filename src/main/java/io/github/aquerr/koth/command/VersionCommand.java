package io.github.aquerr.koth.command;

import io.github.aquerr.koth.PluginInfo;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

public class VersionCommand extends AbstractCommand
{
	@Override
	public CommandResult execute(final CommandContext context) throws CommandException
	{
		context.sendMessage(Identity.nil(), TextComponent.ofChildren(PluginInfo.PLUGIN_PREFIX.append(Component.text("Version: " + PluginInfo.VERSION, NamedTextColor.GREEN))));
		return CommandResult.success();
	}
}
