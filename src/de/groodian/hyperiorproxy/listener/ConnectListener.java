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

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConnectListener implements Listener {

    private Main plugin;
    private DateTimeFormatter dateFormatterTime;

    public ConnectListener(Main plugin) {
        this.plugin = plugin;
        this.dateFormatterTime = DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm:ss");
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
            e.setCancelReason(
                    new TextComponent("§8§m-----------------------------------\n\n§6§lＨ§fＹＰＥＲＩＯＲ．ＤＥ §6§lＳ§fＥＲＶＥＲＮＥＴＺＷＥＲＫ\n§7Bitte verwende die Version §a1.8 §7- §a1.15.2\n\n§8§m-----------------------------------"));
            Team.notify("§6" + e.getConnection().getName() + "§a hat versucht den Server beizutreten aber verwendet eine falsche Version. §7(" + e.getConnection().getVersion() + ")");
            e.setCancelled(true);
            e.completeIntent(plugin);
            return;
        }

        if (Ban.hasBan(uuid)) {
            if (!Ban.getString("ban", uuid).equals("")) {
                if (Ban.getString("ban", uuid).equals("PERMANENT")) {
                    e.setCancelReason(new TextComponent("                     §6§lＨ§fＹＰＥＲＩＯＲ．ＤＥ §6§lＳ§fＥＲＶＥＲＮＥＴＺＷＥＲＫ \n\n§cDu wurdest von diesem Netzwerk gebannt.\n§cGrund: §e" + Ban.getString("reason", uuid)
                            + "\n§cDauer: §ePERMANENT\n\n§aDu kannst einen Entbannungsantrag an §eunban@hyperior.de §asenden."));
                    Team.notify("§6" + name + "§a hat versucht den Server beizutreten ist aber §6PERMANENT §agebannt.");
                    e.setCancelled(true);
                    e.completeIntent(plugin);
                    return;
                } else {
                    String timeLeft = "";
                    Duration duration = Duration.between(LocalDateTime.now(), LocalDateTime.parse(Ban.getString("ban", uuid), dateFormatterTime));

                    if (duration.toDays() > 1) {
                        timeLeft = duration.toDays() + " Tage";
                    } else if (duration.toDays() == 1) {
                        timeLeft = duration.toDays() + " Tag";
                    } else if (duration.toHours() > 1) {
                        timeLeft = duration.toHours() + " Stunden";
                    } else if (duration.toHours() == 1) {
                        timeLeft = duration.toHours() + " Stunde";
                    } else if (duration.toMinutes() > 1) {
                        timeLeft = duration.toMinutes() + " Minuten";
                    } else if (duration.toMinutes() == 1) {
                        timeLeft = duration.toMinutes() + " Minute";
                    } else if (duration.getSeconds() > 1 || duration.getSeconds() == 0) {
                        timeLeft = duration.getSeconds() + " Sekunden";
                    } else if (duration.getSeconds() == 1) {
                        timeLeft = duration.getSeconds() + " Sekunde";
                    }

                    if (!timeLeft.equals("")) {
                        e.setCancelReason(new TextComponent("                     §6§lＨ§fＹＰＥＲＩＯＲ．ＤＥ §6§lＳ§fＥＲＶＥＲＮＥＴＺＷＥＲＫ \n\n§cDu wurdest von diesem Netzwerk gebannt.\n§cGrund: §e" + Ban.getString("reason", uuid)
                                + "\n§cDauer: §e" + timeLeft + "\n\n§aDu kannst einen Entbannungsantrag an §eunban@hyperior.de §asenden."));
                        Team.notify("§6" + name + "§a hat versucht den Server beizutreten ist aber noch §6" + timeLeft + " §agebannt.");
                        e.setCancelled(true);
                        e.completeIntent(plugin);
                        return;
                    }

                }
            }
        }

        if (plugin.isMaintenance()) {
            if (!HyperiorCore.getRanks().has(uuid, "maintenance")) {
                e.setCancelReason(new TextComponent("§cDer Server wird derzeit gewartet! Versuche es später nochmal."));
                Team.notify("§6" + name + "§a hat versucht den Server beizutreten aber der Server ist im Wartungs-Modus.");
                e.setCancelled(true);
                e.completeIntent(plugin);
                return;
            }
        }

        int slots = BungeeCord.getInstance().getOnlineCount() + 1;
        if (slots > plugin.getSlots()) {
            if (!HyperiorCore.getRanks().has(uuid, "joinfullserver")) {
                e.setCancelReason(new TextComponent("§cDer Server ist derzeit voll!\nDu benötigst einen Rang um trotzdem beitreten zu können."));
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
