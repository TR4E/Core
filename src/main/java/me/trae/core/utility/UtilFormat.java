package me.trae.core.utility;

import org.apache.commons.lang.WordUtils;

public final class UtilFormat {

    public static String cleanString(final String input) {
        return WordUtils.capitalizeFully(input.replaceAll("_", " ")).replaceAll("_", " ");
    }

    public static String getFinalArg(final String[] args, final int start) {
        final StringBuilder bldr = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            if (i != start) {
                bldr.append(" ");
            }
            bldr.append(args[i]);
        }
        return bldr.toString();
    }


    public static boolean isNumeric(final String string) {
        return (string != null && string.matches("[-+]?\\d*\\.?\\d+"));
    }
}