package de.groodian.hyperiorproxy.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.groodian.hyperiorproxy.main.Main;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class JoinListener {

    private final Main plugin;

    public JoinListener(Main plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void handleJoin(ServerPreConnectEvent e) {
        if (e.getResult().getServer().isEmpty()) {
            return;
        }

        if (e.getResult().getServer().get().getServerInfo().getName().equalsIgnoreCase("LOBBY")) {
            for (RegisteredServer registeredServer : plugin.getServer().getAllServers()) {
                String serverName = registeredServer.getServerInfo().getName();
                if (serverName.contains("LOBBY") && !serverName.equalsIgnoreCase("LOBBY")) {
                    e.setResult(ServerPreConnectEvent.ServerResult.allowed(registeredServer));
                    return;
                }
            }

            e.getPlayer()
                    .disconnect(LegacyComponentSerializer.legacySection()
                            .deserialize(Main.DISCONNECT_HEADER + "§cEs konnte keine Lobby gefunden werden! Versuche es später nochmal."));
        }
    }

}
