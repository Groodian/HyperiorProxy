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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UnbanCommand extends Command {

    private DateTimeFormatter dateFormatterTime;
    private UUIDFetcher uuidFetcher;

    public UnbanCommand() {
        super("unban");
        dateFormatterTime = DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm:ss");
        uuidFetcher = new UUIDFetcher();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer || sender instanceof ConsoleCommandSender) {
            if (sender instanceof ProxiedPlayer) {
                if (!HyperiorCore.getRanks().has(((ProxiedPlayer) sender).getUniqueId(), "unban")) {
                    return;
                }
            }
            if (args.length >= 2) {
                ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);
                String reason = "";
                for (int i = 1; i < args.length; i++) {
                    if (reason == "")
                        reason = args[i];
                    else
                        reason += " " + args[i];
                }
                if (target != null) {
                    if (Ban.hasBan(target.getUniqueId().toString().replaceAll("-", ""))) {
                        if (!Ban.getString("ban", target.getUniqueId().toString().replaceAll("-", "")).equals("")) {
                            if (Ban.getString("ban", target.getUniqueId().toString().replaceAll("-", "")).equals("PERMANENT")) {
                                Ban.unban(target.getUniqueId().toString().replaceAll("-", ""), target.getName(), sender.getName(), reason);
                                sender.sendMessage(Main.PREFIX + "§aDu hast §6" + target.getName() + " §aentbannt. Grund: §6" + reason);
                                Team.notify("§6" + sender.getName() + "§a hat §6" + target.getName() + " §aentbannt. Grund: §6" + reason);
                            } else {
                                if (LocalDateTime.now().isBefore(LocalDateTime.parse(Ban.getString("ban", target.getUniqueId().toString().replaceAll("-", "")), dateFormatterTime))) {
                                    Ban.unban(target.getUniqueId().toString().replaceAll("-", ""), target.getName(), sender.getName(), reason);
                                    sender.sendMessage(Main.PREFIX + "§aDu hast §6" + target.getName() + " §aentbannt. Grund: §6" + reason);
                                    Team.notify("§6" + sender.getName() + "§a hat §6" + target.getName() + " §aentbannt. Grund: §6" + reason);
                                } else
                                    sender.sendMessage(Main.PREFIX + "§cDieser Spieler hat keinen Ban.");
                            }
                        } else
                            sender.sendMessage(Main.PREFIX + "§cDieser Spieler hat keinen Ban.");
                    } else
                        sender.sendMessage(Main.PREFIX + "§cDieser Spieler hat keinen Ban.");
                } else {
                    sender.sendMessage(Main.PREFIX + "§7Dieser Spieler ist nicht Online, downloade UUID...");
                    String tempUUID = uuidFetcher.getUUID(args[0]);
                    String tempName = uuidFetcher.getName(args[0]);
                    if (tempUUID == null) {
                        sender.sendMessage(Main.PREFIX + "§cDieser Spieler existiert nicht.");
                    } else {
                        if (Ban.hasBan(tempUUID)) {
                            if (!Ban.getString("ban", tempUUID).equals("")) {
                                if (Ban.getString("ban", tempUUID).equals("PERMANENT")) {
                                    Ban.unban(tempUUID, tempName, sender.getName(), reason);
                                    sender.sendMessage(Main.PREFIX + "§aDu hast §6" + tempName + " §aentbannt. Grund: §6" + reason);
                                    Team.notify("§6" + sender.getName() + "§a hat §6" + tempName + " §aentbannt. Grund: §6" + reason);
                                } else {
                                    if (LocalDateTime.now().isBefore(LocalDateTime.parse(Ban.getString("ban", tempUUID), dateFormatterTime))) {
                                        Ban.unban(tempUUID, tempName, sender.getName(), reason);
                                        sender.sendMessage(Main.PREFIX + "§aDu hast §6" + tempName + " §aentbannt. Grund: §6" + reason);
                                        Team.notify("§6" + sender.getName() + "§a hat §6" + tempName + " §aentbannt. Grund: §6" + reason);
                                    } else
                                        sender.sendMessage(Main.PREFIX + "§cDieser Spieler hat keinen Ban.");
                                }
                            } else
                                sender.sendMessage(Main.PREFIX + "§cDieser Spieler hat keinen Ban.");
                        } else
                            sender.sendMessage(Main.PREFIX + "§cDieser Spieler hat keinen Ban.");
                    }
                }
            } else
                sender.sendMessage(Main.PREFIX + "§cBenutze §6/unban <Spieler> <Grund>§c!");
        } else
            sender.sendMessage(Main.PREFIX + "Dieser Befehl muss von einem Spieler oder der Konsole ausgeführt werden.");
    }

}
