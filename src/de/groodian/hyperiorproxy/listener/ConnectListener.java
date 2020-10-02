package de.groodian.hyperiorproxy.listener;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorproxy.data.Ban;
import de.groodian.hyperiorproxy.data.Data;
import de.groodian.hyperiorproxy.main.Main;
import de.groodian.hyperiorproxy.team.Team;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ConnectListener implements Listener {

    private Main plugin;

    public ConnectListener(Main plugin) {
        this.plugin = plugin;
    }

    // Durch EventPriority.HIGHEST ist dass das letzte Event welches aufgerufen wird
    // und wenn !e.isCancelled() false ist dann wurden durch alle vorherigen Events
    // das Event noch nicht abgebrochen erst dann wird die Methode connect()
    // aufgerufen.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleConnect(LoginEvent e) {
        if (!e.isCancelled()) {
            // da das Event async ist muss gewartet werden bis es abgeschlossen ist sonst
            // werden die folgenden Events also das nächste währe PostLoginEvent ausgeführt
            // obwohl das LoginEvent noch nicht abschlossen ist (viele Fehlermeldungen xD)
            // mit e.registerIntent(plugin) wird das warten eingeleitet und mit
            // e.completeIntent(plugin) das warten abgeschlossen.
            e.registerIntent(plugin);
            ProxyServer.getInstance().getScheduler().runAsync(plugin, () -> connect(e));
        }
    }

    private void connect(LoginEvent e) {
        String uuid = e.getConnection().getUniqueId().toString();
        String name = e.getConnection().getName();
        String address = e.getConnection().getSocketAddress().toString();

        // https://wiki.vg/Protocol_version_numbers
        if (e.getConnection().getVersion() < 47 || e.getConnection().getVersion() > 578) {
            e.setCancelReason(TextComponent.fromLegacyText(Main.DISCONNECT_HEADER + "§7Bitte verwende die Version §a1.8 §7- §a1.15.2"));
            Team.notify("§6" + e.getConnection().getName() + "§a hat versucht den Server beizutreten aber verwendet eine falsche Version. §7(" + e.getConnection().getVersion() + ")");
            e.setCancelled(true);
            e.completeIntent(plugin);
            return;
        }

        String banDisconnectReason = Ban.getDisconnectReason(uuid);
        if (banDisconnectReason != null) {
            e.setCancelReason(TextComponent.fromLegacyText(banDisconnectReason));
            Team.notify("§6" + name + "§a hat versucht den Server beizutreten ist aber gebannt.");
            e.setCancelled(true);
            e.completeIntent(plugin);
            return;
        }

        if (plugin.isMaintenance()) {
            if (!HyperiorCore.getRanks().has(uuid, "maintenance")) {
                e.setCancelReason(TextComponent.fromLegacyText(Main.DISCONNECT_HEADER + "§cDer Server wird derzeit gewartet! Versuche es später nochmal."));
                Team.notify("§6" + name + "§a hat versucht den Server beizutreten aber der Server ist im Wartungs-Modus.");
                e.setCancelled(true);
                e.completeIntent(plugin);
                return;
            }
        }

        int slots = BungeeCord.getInstance().getOnlineCount() + 1;
        if (slots > plugin.getSlots()) {
            if (!HyperiorCore.getRanks().has(uuid, "joinfullserver")) {

                e.setCancelReason(TextComponent.fromLegacyText(Main.DISCONNECT_HEADER +
                        "§cDer Server ist derzeit voll!" +
                        "\nDu benötigst einen Rang um trotzdem beitreten zu können."));

                Team.notify("§6" + name + "§a hat versucht den Server beizutreten aber der Server ist voll.");
                e.setCancelled(true);
                e.completeIntent(plugin);
                return;
            }
        }

        // An dieser Stelle hat der User alle hindernisse überwunden und kann eingeloggt
        // werden

        Team.userLogin(uuid, name);
        Data.login(uuid, name, address);

        e.completeIntent(plugin);
    }

}
