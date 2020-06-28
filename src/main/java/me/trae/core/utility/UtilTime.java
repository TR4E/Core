package me.trae.core.utility;

import java.text.DecimalFormat;

public final class UtilTime {

    public static double convert(final double time, TimeUnit unit, final int decPoint) {
        if (unit == TimeUnit.BEST) {
            if (time < 60000L) unit = TimeUnit.SECONDS;
            else if (time < 3600000L) unit = TimeUnit.MINUTES;
            else if (time < 86400000L) unit = TimeUnit.HOURS;
            else if (time < 31536000000L) unit = TimeUnit.DAYS;
            else unit = TimeUnit.YEARS;
        }
        if (unit == TimeUnit.SECONDS) return UtilMath.trim(time / 1000.0D, decPoint);
        if (unit == TimeUnit.MINUTES) return UtilMath.trim(time / 60000.0D, decPoint);
        if (unit == TimeUnit.HOURS) return UtilMath.trim(time / 3600000.0D, decPoint);
        if (unit == TimeUnit.DAYS) return UtilMath.trim(time / 86400000.0D, decPoint);
        if (unit == TimeUnit.YEARS) return UtilMath.trim(time / 31536000000.0D, decPoint);
        return UtilMath.trim(time, decPoint);
    }

    public static String getTimeUnit2(final double d) {
        if (d < 60000L) {
            return "Seconds";
        } else if (d >= 60000L && d < 3600000L) {
            return "Minutes";
        } else if (d >= 3600000L && d < 86400000L) {
            return "Hours";
        } else if (d >= 86400000L && d < 31536000000L) {
            return "Days";
        }
        return "Years";
    }

    public static double trim(final double untrimmed, final double d) {
        final StringBuilder format = new StringBuilder("#.#");
        for (int i = 1; i < d; i++) {
            format.append("#");
        }
        final DecimalFormat twoDec = new DecimalFormat(format.toString());
        return Double.parseDouble(twoDec.format(untrimmed));
    }

    public static String getTimeUnit(final String unit) {
        switch (unit) {
            case "s":
                return "Seconds";
            case "m":
                return "Minutes";
            case "h":
                return "Hours";
            case "d":
                return "Days";
            case "y":
                return "Years";
        }
        return "";
    }

    public static boolean elapsed(final long from, final long to) {
        return (System.currentTimeMillis() - from > to);
    }

    public static String getTime(final double d, final TimeUnit unit, final int decPoint) {
        return (convert(d, unit, decPoint) + " " + getTimeUnit2(d));
    }

    public enum TimeUnit {
        BEST,
        YEARS,
        DAYS,
        HOURS,
        MINUTES,
        SECONDS,
    }
}