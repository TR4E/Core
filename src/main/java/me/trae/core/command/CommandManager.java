package me.trae.core.command;

import me.trae.core.Main;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class CommandManager {

    private final Main instance;
    private final Set<Command> commands = new HashSet<>();

    public CommandManager(final Main instance) {
        this.instance = instance;
    }

    public void addCommand(final Command command) {
        commands.add(command);
    }

    public final Command getCommand(final String cmd) {
        return commands.stream().filter(c -> c.getCommandName().equalsIgnoreCase(cmd) || Arrays.stream(c.getAliases()).anyMatch(a -> a.equalsIgnoreCase(cmd))).findFirst().orElse(null);
    }

    public final Set<Command> getCommands() {
        return commands;
    }
}