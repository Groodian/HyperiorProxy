package de.groodian.hyperiorproxy.listener;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorproxy.data.Data;
import de.groodian.hyperiorproxy.main.Main;
import de.groodian.hyperiorproxy.team.Team;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class DisconnectListener implements Listener {

    private Main plugin;

    public DisconnectListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void handleDisconnect(final PlayerDisconnectEvent e) {
        ProxyServer.getInstance().getScheduler().runAsync(plugin, () -> disconnect(e));
    }

    // Wird erst aufgerufen wenn der User das LoginEvent erfolgreich abgeschlossen
    // hat und muss wieder ausgeloggt werden
    private void disconnect(PlayerDisconnectEvent e) {
        String uuid = e.getPlayer().getUniqueId().toString();
        String name = e.getPlayer().getName();
        String address = e.getPlayer().getPendingConnection().getSocketAddress().toString();

        HyperiorCore.getRanks().logout(e.getPlayer().getUniqueId());
        plugin.getReport().removeReported(uuid);
        Team.userLogout(uuid, name);
        Data.logout(uuid, address);
    }

}
