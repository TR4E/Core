package me.trae.core.utility;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class UtilMath {

    public static String format(final double input, final String decimalFormat) {
        final DecimalFormat df = new DecimalFormat(decimalFormat);
        return df.format(input);
    }

    public static Vector getTrajectory2d(final Vector from, final Vector to) {
        return to.subtract(from).setY(0).normalize();
    }

    public static double trim(final double untrimmed, final int decimal) {
        final StringBuilder format = new StringBuilder("#.#");
        for (int i = 1; i < decimal; i++) {
            format.append("#");
        }
        final DecimalFormat twoDec = new DecimalFormat(format.toString());
        return Double.parseDouble(twoDec.format(untrimmed));
    }

    public static int getInteger(final String value, final int min, final int max) {
        int i = min;
        try {
            i = Integer.parseInt(value);
        } catch (final NumberFormatException ex) {
            ex.printStackTrace();
        }
        if (i < min) {
            i = min;
        } else if (i > max) {
            i = max;
        }
        return i;
    }

    public static double offset(final Location a, final Location b) {
        return offset(a.toVector(), b.toVector());
    }

    public static double offset(final Vector a, final Vector b) {
        return a.subtract(b).length();
    }

    public static double offset(final Entity a, final Entity b) {
        return offset(a.getLocation().toVector(), b.getLocation().toVector());
    }

    public static double offset2d(final Location a, final Location b) {
        return offset2d(a.toVector(), b.toVector());
    }

    public static double offset2d(final Vector a, final Vector b) {
        a.setY(0);
        b.setY(0);
        return a.subtract(b).length();
    }

    public static int randomInt(final int value) {
        return ThreadLocalRandom.current().nextInt(value);
    }

    public static int randomInt(final int min, final int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static Set<Location> makeHollow(final Set<Location> blocks, final boolean sphere) {
        final Set<Location> edge = new HashSet<>();
        if (!sphere) {
            for (final Location l : blocks) {
                final World w = l.getWorld();
                final int X = l.getBlockX();
                final int Y = l.getBlockY();
                final int Z = l.getBlockZ();
                final Location front = new Location(w, X + 1, Y, Z);
                final Location back = new Location(w, X - 1, Y, Z);
                final Location left = new Location(w, X, Y, Z + 1);
                final Location right = new Location(w, X, Y, Z - 1);
                if (!(blocks.contains(front) && blocks.contains(back) && blocks.contains(left) && blocks.contains(right))) {
                    edge.add(l);
                }
            }
            return edge;
        } else {
            for (final Location l : blocks) {
                final World w = l.getWorld();
                final int X = l.getBlockX();
                final int Y = l.getBlockY();
                final int Z = l.getBlockZ();
                final Location front = new Location(w, X + 1, Y, Z);
                final Location back = new Location(w, X - 1, Y, Z);
                final Location left = new Location(w, X, Y, Z + 1);
                final Location right = new Location(w, X, Y, Z - 1);
                final Location top = new Location(w, X, Y + 1, Z);
                final Location bottom = new Location(w, X, Y - 1, Z);
                if (!(blocks.contains(front) && blocks.contains(back) && blocks.contains(left) && blocks.contains(right) && blocks.contains(top) && blocks.contains(bottom))) {
                    edge.add(l);
                }
            }
            return edge;
        }
    }

    public static List<Location> sphere(final Location loc, final Integer r, final Integer h, final boolean hollow, final boolean sphere, final int plus_y) {
        final List<Location> circleblocks = new ArrayList<>();
        final int cx = loc.getBlockX();
        final int cy = loc.getBlockY();
        final int cz = loc.getBlockZ();
        for (int x = cx - r; x <= cx + r; x++) {
            for (int z = cz - r; z <= cz + r; z++) {
                for (int y = sphere ? (cy - r) : cy; y < (sphere ? (cy + r) : (cy + h)); y++) {
                    final double dist = ((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0));
                    if (dist < (r * r) && (!hollow || dist >= ((r - 1) * (r - 1)))) {
                        final Location l = new Location(loc.getWorld(), x, (y + plus_y), z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }
}