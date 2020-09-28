package de.groodian.hyperiorcloud.commands;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.groodian.hyperiorcloud.data.Ban;
import de.groodian.hyperiorcloud.data.Data;
import de.groodian.hyperiorcloud.main.Main;
import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.util.UUIDFetcher;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class LookupCommand extends Command {

	private DateTimeFormatter dateFormatterTime;
	private UUIDFetcher uuidFetcher;

	public LookupCommand() {
		super("lookup");
		dateFormatterTime = DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm:ss");
		uuidFetcher = new UUIDFetcher();
	}

	@SuppressWarnings("deprecation")
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
					sender.sendMessage(output(player.getName(), player.getUniqueId().toString().replaceAll("-", "")));
				} else {
					sender.sendMessage(Main.PREFIX + "§7Dieser Spieler ist nicht Online, downloade UUID...");
					if (uuidFetcher.getUUID(args[0]) == null) {
						sender.sendMessage(Main.PREFIX + "§cDieser Spieler existiert nicht.");
					} else {
						sender.sendMessage(output(uuidFetcher.getName(args[0]), uuidFetcher.getUUID(args[0]).replaceAll("-", "")));
					}
				}
			} else if (args.length == 2) {
				ProxiedPlayer player = BungeeCord.getInstance().getPlayer(args[0]);
				if (player != null) {
					sender.sendMessage(Main.PREFIX + "§aReports §6" + player.getName() + "§a:\n" + Ban.getString("reporthistory", player.getUUID().toString().replaceAll("-", "")));
				} else {
					sender.sendMessage(Main.PREFIX + "§7Dieser Spieler ist nicht Online, downloade UUID...");
					if (uuidFetcher.getUUID(args[0]) == null) {
						sender.sendMessage(Main.PREFIX + "§cDieser Spieler existiert nicht.");
					} else {
						sender.sendMessage(Main.PREFIX + "§aReports §6" + uuidFetcher.getName(args[0]) + "§a:\n" + Ban.getString("reporthistory", uuidFetcher.getUUID(args[0]).replaceAll("-", "")));
					}
				}
			} else
				sender.sendMessage(Main.PREFIX + "§cBenutze §6/lookup <Spieler> [<reports>]§c!");

		} else
			sender.sendMessage(Main.PREFIX + "Dieser Befehl muss von einem Spieler oder der Konsole ausgeführt werden.");
	}

	public String output(String name, String uuid) {
		String ban = "";
		if (Ban.hasBan(uuid)) {
			if (!Ban.getString("ban", uuid).equals("")) {
				if (Ban.getString("ban", uuid).equals("PERMANENT")) {
					ban = "PERMANENT\n§aGrund: §6" + Ban.getString("reason", uuid);
				} else {
					Duration duration = Duration.between(LocalDateTime.now(), LocalDateTime.parse(Ban.getString("ban", uuid), dateFormatterTime));
					if (duration.toDays() > 1) {
						ban = duration.toDays() + " Tage" + " §7(" + Ban.getString("ban", uuid) + ")\n§aGrund: §6" + Ban.getString("reason", uuid);
					} else if (duration.toDays() == 1) {
						ban = duration.toDays() + " Tag" + " §7(" + Ban.getString("ban", uuid) + ")\n§aGrund: §6" + Ban.getString("reason", uuid);
					}

					else if (duration.toHours() > 1) {
						ban = duration.toHours() + " Stunden" + " §7(" + Ban.getString("ban", uuid) + ")\n§aGrund: §6" + Ban.getString("reason", uuid);
					} else if (duration.toHours() == 1) {
						ban = duration.toHours() + " Stunde" + " §7(" + Ban.getString("ban", uuid) + ")\n§aGrund: §6" + Ban.getString("reason", uuid);
					}

					else if (duration.toMinutes() > 1) {
						ban = duration.toMinutes() + " Minuten" + " §7(" + Ban.getString("ban", uuid) + ")\n§aGrund: §6" + Ban.getString("reason", uuid);
					} else if (duration.toMinutes() == 1) {
						ban = duration.toMinutes() + " Minute" + " §7(" + Ban.getString("ban", uuid) + ")\n§aGrund: §6" + Ban.getString("reason", uuid);
					}

					else if (duration.getSeconds() > 1 || duration.getSeconds() == 0) {
						ban = duration.getSeconds() + " Sekunden" + " §7(" + Ban.getString("ban", uuid) + ")\n§aGrund: §6" + Ban.getString("reason", uuid);
					} else if (duration.getSeconds() == 1) {
						ban = duration.getSeconds() + " Sekunde" + " §7(" + Ban.getString("ban", uuid) + ")\n§aGrund: §6" + Ban.getString("reason", uuid);
					}

					else {
						ban = "§cNein";
					}
				}
			} else {
				ban = "§cNein";
			}
		} else {
			ban = "§cNein";
		}
		String output = Main.PREFIX + "§aLookup §6" + name + "§a:\n§aRang: " + ((HyperiorCore.getRanks().getRank(uuid) == null) ? "Spieler" : HyperiorCore.getRanks().getRank(uuid)) + "\n§aBan: §6" + ban
				+ "\n§aHistory: §6" + Ban.getString("history", uuid) + "\n§aName History: §6" + uuidFetcher.getNameHistoryFromUUID(uuid) + "\n§aLogins: §6" + Data.getLong("logins", uuid) + "\n§aErster Login: §6"
				+ Data.getString("firstlogin", uuid) + "\n§aLetzter Login: §6" + Data.getString("lastlogin", uuid) + "\n§aLetzter Logout: §6" + Data.getString("lastlogout", uuid) + "\n§aLetzte IP: §6"
				+ Data.getString("lastip", uuid) + "\n§aLogin Tage: §6" + Data.getLong("logindays", uuid) + "\n§aVerbindungsdauer: §6" + Data.getLong("connectiontime", uuid) + " Minuten" + "\n§aReports: §6"
				+ Ban.getLong("reports", uuid) + " §7(/lookup " + name + " reports)";
		return output;
	}

}
