package me.trae.core.gamer;

import me.trae.core.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class GamerUtilities {

    private final Main instance;
    private final Map<UUID, Gamer> gamers = new HashMap<>();

    public GamerUtilities(final Main instance) {
        this.instance = instance;
    }

    public void addGamer(final Gamer g) {
        gamers.put(g.getUUID(), g);
    }

    public void removeGamer(final Gamer g) {
        gamers.remove(g.getUUID());
    }

    public final Gamer getGamer(final UUID uuid) {
        return gamers.get(uuid);
    }

    public final Set<Gamer> getGamers() {
        return gamers.keySet().stream().map(gamers::get).collect(Collectors.toSet());
    }
}