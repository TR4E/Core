package me.trae.core.utility;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public final class UtilProxy {

    public static boolean isProxy(final String ipAddress) {
        try {
            final URL url = new URL("http://202.5.31.117:4567/check/" + ipAddress);
            final Scanner s = new Scanner(url.openStream());
            final String result = s.nextLine();
            return result.contains("true");


//            final String result = s.nextLine().replaceAll("\\{\"result\":\"", "").replaceAll("\"}", "").trim();
//            return Boolean.parseBoolean(result);
        } catch (final IOException e) {
            System.out.println("[Error]: Proxy Checker Failed");
        }
        return false;
    }
}