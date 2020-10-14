package me.trae.core.command;

import me.trae.core.Main;

import java.util.*;

public final class CommandManager {

    private final Main instance;
    private final Map<String, Command> commands = new HashMap<>();

    public CommandManager(final Main instance) {
        this.instance = instance;
    }

    public void addCommand(final Command command) {
        this.commands.put(command.getCommandName(), command);
    }

    public final Command getCommand(final String cmd) {
        for (final Command command : getCommands()) {
            if (command.getCommandName().equalsIgnoreCase(cmd) || Arrays.asList(command.getAliases()).contains(cmd)) {
                return command;
            }
        }
        return null;
    }

    public final Set<Command> getCommands() {
        return new HashSet<>(commands.values());
    }
}