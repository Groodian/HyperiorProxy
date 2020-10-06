package de.groodian.hyperiorproxy.commands;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.ranks.Rank;
import de.groodian.hyperiorcore.util.UUIDFetcher;
import de.groodian.hyperiorproxy.data.Ban;
import de.groodian.hyperiorproxy.data.Data;
import de.groodian.hyperiorproxy.main.Main;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class LookupCommand extends Command {

    private Main plugin;
    private UUIDFetcher uuidFetcher;

    public LookupCommand(Main plugin) {
        super("lookup");
        this.plugin = plugin;
        uuidFetcher = new UUIDFetcher();
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (sender instanceof ProxiedPlayer || sender instanceof ConsoleCommandSender) {
            if (sender instanceof ProxiedPlayer) {
                if (!HyperiorCore.getRanks().has(((ProxiedPlayer) sender).getUniqueId(), "lookup")) {
                    return;
                }
            }
            if (args.length == 1) {
                final ProxiedPlayer player = BungeeCord.getInstance().getPlayer(args[0]);
                sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§7Bitte warte, dies kann einen moment dauern."));

                ProxyServer.getInstance().getScheduler().runAsync(plugin, () -> {

                    if (player != null) {
                        sender.sendMessage(TextComponent.fromLegacyText(output(player.getName(), player.getUniqueId().toString(), player.getServer().getInfo().getName())));
                    } else {
                        sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§7Dieser Spieler ist nicht Online, downloade UUID..."));
                        UUIDFetcher.Result result = uuidFetcher.getNameAndUUIDFromName(args[0]);
                        if (result == null) {
                            sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§cDieser Spieler existiert nicht."));
                        } else {
                            sender.sendMessage(TextComponent.fromLegacyText(output(result.getName(), result.getUUID(), null)));
                        }
                    }

                });

            } else
                sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§cBenutze §6/lookup <Spieler>§c!"));
        } else
            sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "Dieser Befehl muss von einem Spieler oder der Konsole ausgeführt werden."));
    }

    private String output(String name, String uuid, String currentServer) {
        Rank rank = HyperiorCore.getRanks().get(uuid);

        String ban = Ban.getBanTimeLeft(uuid);
        if (ban != null) {
            ban += "\n§aGrund: §c" + Ban.getReason(uuid);
        } else {
            ban = "Nein";
        }

        if (currentServer == null) {
            currentServer = "§cNein";
        }

        return Main.PREFIX + "§aLookup §6" + name + "§a:" +
                "\n§aOnline: §6" + currentServer +
                "\n§aRang: " + rank.getColor() + rank.getName() +
                "\n§aBan: §c" + ban +
                "\n§aHistory: §6" + Ban.getHistory(uuid) +
                "\n§aName History: §6" + uuidFetcher.getNameHistoryFromUUID(uuid) +
                "\n§aLogins: §6" + Data.getLong("logins", uuid) +
                "\n§aErster Login: §6" + Data.getString("firstlogin", uuid) +
                "\n§aLetzter Login: §6" + Data.getString("lastlogin", uuid) +
                "\n§aLetzter Logout: §6" + Data.getString("lastlogout", uuid) +
                "\n§aLetzte IP: §6" + Data.getString("lastip", uuid) +
                "\n§aLogin Tage: §6" + Data.getLong("logindays", uuid) +
                "\n§aVerbindungsdauer: §6" + Data.getLong("connectiontime", uuid) + " Minuten" +
                "\n§aReports: §6" + Ban.getReports(uuid) +
                "\n§aReport History: §6" + Ban.getReportHistory(uuid);

    }

}
