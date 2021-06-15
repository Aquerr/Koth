package io.github.aquerr.koth;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

public class PluginInfo {
    public static final String ID = "koth";
    public static final String NAME = "King Of The Hill";
    public static final String VERSION = "0.5.0";
    public static final String DESCRIPTION = "Create King of the Hill arenas!";
    public static final TextComponent PLUGIN_PREFIX = Component.text("[KOTH] ", NamedTextColor.GOLD);
    public static final TextComponent PLUGIN_ERROR = Component.text("[KOTH] ", NamedTextColor.RED);

    public static final String[] AUTHORS = {"Aquerr/Nerdi"};
}
