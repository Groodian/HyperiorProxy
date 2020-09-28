package de.groodian.hyperiorcloud.listener;

import de.groodian.hyperiorcloud.commands.ReportCommand;
import de.groodian.hyperiorcloud.data.Data;
import de.groodian.hyperiorcloud.main.Main;
import de.groodian.hyperiorcloud.team.Team;
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
	public void handleDisconnect(PlayerDisconnectEvent e) {
		ProxyServer.getInstance().getScheduler().runAsync(plugin, () -> disconnect(e));
	}

	// Wird erst aufgerufen wenn der User das LoginEvent erfolgreich abgeschlossen
	// hat und muss wieder ausgeloggt werden
	private void disconnect(PlayerDisconnectEvent e) {
		String uuid = e.getPlayer().getUniqueId().toString();
		String name = e.getPlayer().getName();

		ReportCommand.removeReported(uuid);
		Team.userLogout(uuid, name);
		Data.logout(uuid, name);
	}

}
