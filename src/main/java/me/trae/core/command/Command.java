package me.trae.core.command;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import org.bukkit.entity.Player;

public abstract class Command {

    private final Main instance;
    private final String commandName;
    private final String[] aliases;
    private final Rank requiredRank;

    public Command(final Main instance, final String commandName, final String[] aliases, final Rank requiredRank) {
        this.instance = instance;
        this.commandName = commandName;
        this.aliases = aliases;
        this.requiredRank = requiredRank;
    }

    public final Main getInstance() {
        return instance;
    }

    public final String getCommandName() {
        return commandName;
    }

    public final String[] getAliases() {
        return aliases;
    }

    public final Rank getRequiredRank() {
        return requiredRank;
    }

    public abstract void execute(final Player player, final String[] args);

    public abstract void help(final Player player);
}