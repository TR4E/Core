package me.trae.core.utility;

import net.minecraft.server.v1_8_R3.MinecraftServer;

public final class UtilServer {

    public static String getTPS() {
        return UtilMath.format(MinecraftServer.getServer().recentTps[0], "#0.00");
    }
}