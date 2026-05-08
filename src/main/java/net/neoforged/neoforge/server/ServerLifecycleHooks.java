package net.neoforged.neoforge.server;

import net.minecraft.server.MinecraftServer;

public final class ServerLifecycleHooks {
    private static MinecraftServer currentServer;

    private ServerLifecycleHooks() {
    }

    public static MinecraftServer getCurrentServer() {
        return currentServer;
    }

    public static void setCurrentServer(MinecraftServer server) {
        currentServer = server;
    }
}
