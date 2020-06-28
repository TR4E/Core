package me.trae.core.gamer;

import me.trae.core.Main;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class GamerUtilities {

    private final Main instance;
    private final Set<Gamer> gamers = new HashSet<>();

    public GamerUtilities(final Main instance) {
        this.instance = instance;
    }

    public void addGamer(final Gamer g) {
        gamers.add(g);
    }

    public void removeGamer(final Gamer g) {
        gamers.remove(g);
    }

    public final Gamer getGamer(final UUID uuid) {
        return gamers.stream().filter(g -> g.getUUID().equals(uuid)).findFirst().orElse(null);
    }

    public final Set<Gamer> getGamers() {
        return gamers;
    }
}