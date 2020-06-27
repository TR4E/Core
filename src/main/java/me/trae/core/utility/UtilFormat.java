package me.trae.core.utility;

import org.apache.commons.lang.WordUtils;

public final class UtilFormat {

    public static String cleanString(final String input) {
        return WordUtils.capitalizeFully(input.replaceAll("_", " ")).replaceAll("_", " ");
    }
}