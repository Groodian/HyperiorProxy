package de.groodian.hyperiorproxy.commands;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.util.UUIDFetcher;
import de.groodian.hyperiorproxy.data.Ban;
import de.groodian.hyperiorproxy.main.Main;
import de.groodian.hyperiorproxy.team.Team;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

import java.time.Duration;
import java.time.LocalDateTime;

public class BanCommand extends Command {

    private UUIDFetcher uuidFetcher;

    public BanCommand() {
        super("ban");
        uuidFetcher = new UUIDFetcher();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer || sender instanceof ConsoleCommandSender) {
            if (sender instanceof ProxiedPlayer) {
                if (!HyperiorCore.getRanks().has(((ProxiedPlayer) sender).getUniqueId(), "ban")) {
                    return;
                }
            }
            if (args.length >= 4) {
                ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);
                if (args[1].chars().allMatch(Character::isDigit)) {
                    if (args[2].equals("d") || args[2].equals("h") || args[2].equals("m") || args[2].equals("s")) {
                        String reason = "";
                        for (int i = 3; i < args.length; i++) {
                            if (reason == "")
                                reason = args[i];
                            else
                                reason += " " + args[i];
                        }
                        if (target != null) {
                            Ban.ban(target.getUniqueId().toString().replaceAll("-", ""), target.getName(), sender.getName(), Integer.parseInt(args[1]), args[2], reason);
                            sender.sendMessage(Main.PREFIX + "§aDu hast §6" + target.getName() + "§a gebannt. Dauer: §6" + args[1] + args[2] + "§a Grund: §6" + reason);
                            Team.notify("§6" + sender.getName() + "§a hat §6" + target.getName() + "§a gebannt. Dauer: §6" + args[1] + args[2] + "§a Grund: §6" + reason);
                            Duration duration = null;
                            if (args[2].equals("d")) {
                                duration = Duration.between(LocalDateTime.now(), LocalDateTime.now().plusDays(Integer.parseInt(args[1])));
                            }
                            if (args[2].equals("h")) {
                                duration = Duration.between(LocalDateTime.now(), LocalDateTime.now().plusHours(Integer.parseInt(args[1])));
                            }
                            if (args[2].equals("m")) {
                                duration = Duration.between(LocalDateTime.now(), LocalDateTime.now().plusMinutes(Integer.parseInt(args[1])));
                            }
                            if (args[2].equals("s")) {
                                duration = Duration.between(LocalDateTime.now(), LocalDateTime.now().plusSeconds(Integer.parseInt(args[1])));
                            }
                            String timeLeft;
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
                            } else {
                                timeLeft = "error";
                            }
                            target.disconnect("                  §6§lＨ§fＹＰＥＲＩＯＲ．ＤＥ §6§lＳ§fＥＲＶＥＲＮＥＴＺＷＥＲＫ \n\n§cDu wurdest von diesem Netzwerk gebannt.\n§cGrund: §e" + reason + "\n§cDauer: §e" + timeLeft
                                    + "\n\n§aDu kannst einen Entbannungsantrag an §eunban@hyperior.de §asenden.");
                        } else {
                            sender.sendMessage(Main.PREFIX + "§7Dieser Spieler ist nicht Online, downloade UUID...");
                            if (uuidFetcher.getUUID(args[0]) == null) {
                                sender.sendMessage(Main.PREFIX + "§cDieser Spieler existiert nicht.");
                            } else {
                                Ban.ban(uuidFetcher.getUUID(args[0]), uuidFetcher.getName(args[0]), sender.getName(), Integer.parseInt(args[1]), args[2], reason);
                                sender.sendMessage(Main.PREFIX + "§aDu hast §6" + uuidFetcher.getName(args[0]) + "§a gebannt. Dauer: §6" + args[1] + args[2] + "§a Grund: §6" + reason);
                                Team.notify("§6" + sender.getName() + "§a hat §6" + uuidFetcher.getName(args[0]) + "§a gebannt. Dauer: §6" + args[1] + args[2] + "§a Grund: §6" + reason);
                            }
                        }
                    } else
                        sender.sendMessage(Main.PREFIX + "§cDie Einheit muss d für Tage, h für Stunden, m für Minuten oder s für Sekunden sein!");
                } else
                    sender.sendMessage(Main.PREFIX + "§cDie Dauer muss eine Zahl sein!");
            } else
                sender.sendMessage(Main.PREFIX + "§cBenutze §6/ban <Spieler> <Dauer> <d/h/m/s> <Grund>§c!");
        } else
            sender.sendMessage(Main.PREFIX + "Dieser Befehl muss von einem Spieler oder der Konsole ausgeführt werden.");
    }

}
