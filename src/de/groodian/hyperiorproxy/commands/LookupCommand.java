package de.groodian.hyperiorproxy.commands;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.ranks.Rank;
import de.groodian.hyperiorcore.util.UUIDFetcher;
import de.groodian.hyperiorproxy.data.Ban;
import de.groodian.hyperiorproxy.data.Data;
import de.groodian.hyperiorproxy.main.Main;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class LookupCommand extends Command {

    private UUIDFetcher uuidFetcher;

    public LookupCommand() {
        super("lookup");
        uuidFetcher = new UUIDFetcher();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer || sender instanceof ConsoleCommandSender) {
            if (sender instanceof ProxiedPlayer) {
                if (!HyperiorCore.getRanks().has(((ProxiedPlayer) sender).getUniqueId(), "lookup")) {
                    return;
                }
            }
            if (args.length == 1) {
                ProxiedPlayer player = BungeeCord.getInstance().getPlayer(args[0]);
                if (player != null) {
                    sender.sendMessage(new TextComponent(output(player.getName(), player.getUniqueId().toString().replaceAll("-", ""))));
                } else {
                    sender.sendMessage(new TextComponent(Main.PREFIX + "§7Dieser Spieler ist nicht Online, downloade UUID..."));
                    if (uuidFetcher.getUUID(args[0]) == null) {
                        sender.sendMessage(new TextComponent(Main.PREFIX + "§cDieser Spieler existiert nicht."));
                    } else {
                        sender.sendMessage(new TextComponent(output(uuidFetcher.getName(args[0]), uuidFetcher.getUUID(args[0]).replaceAll("-", ""))));
                    }
                }
            } else if (args.length == 2) {
                ProxiedPlayer player = BungeeCord.getInstance().getPlayer(args[0]);
                if (player != null) {
                    sender.sendMessage(new TextComponent(Main.PREFIX + "§aReports §6" + player.getName() + "§a:\n" + Ban.getReportHistory(player.getUniqueId().toString().replaceAll("-", ""))));
                } else {
                    sender.sendMessage(new TextComponent(Main.PREFIX + "§7Dieser Spieler ist nicht Online, downloade UUID..."));
                    if (uuidFetcher.getUUID(args[0]) == null) {
                        sender.sendMessage(new TextComponent(Main.PREFIX + "§cDieser Spieler existiert nicht."));
                    } else {
                        sender.sendMessage(new TextComponent(Main.PREFIX + "§aReports §6" + uuidFetcher.getName(args[0]) + "§a:\n" + Ban.getReportHistory(uuidFetcher.getUUID(args[0]).replaceAll("-", ""))));
                    }
                }
            } else
                sender.sendMessage(new TextComponent(Main.PREFIX + "§cBenutze §6/lookup <Spieler> [<reports>]§c!"));

        } else
            sender.sendMessage(new TextComponent(Main.PREFIX + "Dieser Befehl muss von einem Spieler oder der Konsole ausgeführt werden."));
    }

    private String output(String name, String uuid) {
        String ban = Ban.getBanTimeLeft(uuid);

        if (ban != null) {
            ban += " Grund: " + Ban.getReason(uuid);
        } else {
            ban = "§cNein";
        }

        Rank rank = HyperiorCore.getRanks().getRank(uuid);

        return Main.PREFIX + "§aLookup §6" + name + "§a:" +
                "\n§aRang: " + rank.getColor() + rank.getName() +
                "\n§aBan: §6" + ban +
                "\n§aHistory: §6" + Ban.getHistory(uuid) +
                "\n§aName History: §6" + uuidFetcher.getNameHistoryFromUUID(uuid) +
                "\n§aLogins: §6" + Data.getLong("logins", uuid) +
                "\n§aErster Login: §6" + Data.getString("firstlogin", uuid) +
                "\n§aLetzter Login: §6" + Data.getString("lastlogin", uuid) +
                "\n§aLetzter Logout: §6" + Data.getString("lastlogout", uuid) +
                "\n§aLetzte IP: §6" + Data.getString("lastip", uuid) +
                "\n§aLogin Tage: §6" + Data.getLong("logindays", uuid) +
                "\n§aVerbindungsdauer: §6" + Data.getLong("connectiontime", uuid) + " Minuten" +
                "\n§aReports: §6" + Ban.getReports(uuid) + " §7(/lookup " + name + " reports)";

    }

}
