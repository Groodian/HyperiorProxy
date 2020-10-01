package de.groodian.hyperiorproxy.listener;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Map;

public class JoinListener implements Listener {

    @EventHandler
    public void handleJoin(ServerConnectEvent e) {
        System.out.println("target: " + e.getTarget().getName());
        if (e.getTarget().getName().equalsIgnoreCase("LOBBY")) {
            for (Map.Entry<String, ServerInfo> entry : ProxyServer.getInstance().getServers().entrySet()) {
                System.out.println("available server: " + entry.getKey());
                if (entry.getKey().toUpperCase().contains("LOBBY") && !entry.getKey().equalsIgnoreCase("LOBBY")) {
                    System.out.println("set target: " + entry.getKey());
                    e.setTarget(entry.getValue());
                    return;
                }
            }
        }
    }

}
