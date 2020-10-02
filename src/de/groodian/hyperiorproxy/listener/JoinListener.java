package de.groodian.hyperiorproxy.listener;

import de.groodian.hyperiorproxy.main.Main;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Map;

public class JoinListener implements Listener {

    @EventHandler
    public void handleJoin(ServerConnectEvent e) {
        if (e.getTarget().getName().equalsIgnoreCase("LOBBY")) {
            for (Map.Entry<String, ServerInfo> entry : ProxyServer.getInstance().getServers().entrySet()) {
                if (entry.getKey().toUpperCase().contains("LOBBY") && !entry.getKey().equalsIgnoreCase("LOBBY")) {
                    e.setTarget(entry.getValue());
                    return;
                }
            }
            e.getPlayer().disconnect(TextComponent.fromLegacyText(Main.DISCONNECT_HEADER + "§cEs konnte keine Lobby gefunden werden! Versuche es später nochmal."));
        }
    }

}
