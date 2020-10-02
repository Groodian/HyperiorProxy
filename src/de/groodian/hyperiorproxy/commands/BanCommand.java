package de.groodian.hyperiorproxy.commands;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.util.UUIDFetcher;
import de.groodian.hyperiorproxy.data.Ban;
import de.groodian.hyperiorproxy.main.Main;
import de.groodian.hyperiorproxy.team.Team;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

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
                            String uuid = target.getUniqueId().toString().replaceAll("-", "");
                            Ban.ban(uuid, target.getName(), sender.getName(), Integer.parseInt(args[1]), args[2], reason);
                            sender.sendMessage(new TextComponent(Main.PREFIX + "§aDu hast §6" + target.getName() + "§a gebannt. Dauer: §6" + args[1] + args[2] + "§a Grund: §6" + reason));
                            Team.notify("§6" + sender.getName() + "§a hat §6" + target.getName() + "§a gebannt. Dauer: §6" + args[1] + args[2] + "§a Grund: §6" + reason);
                            target.disconnect(new TextComponent(Ban.getDisconnectReason(uuid)));
                        } else {
                            sender.sendMessage(new TextComponent(Main.PREFIX + "§7Dieser Spieler ist nicht Online, downloade UUID..."));
                            if (uuidFetcher.getUUID(args[0]) == null) {
                                sender.sendMessage(new TextComponent(Main.PREFIX + "§cDieser Spieler existiert nicht."));
                            } else {
                                Ban.ban(uuidFetcher.getUUID(args[0]), uuidFetcher.getName(args[0]), sender.getName(), Integer.parseInt(args[1]), args[2], reason);
                                sender.sendMessage(new TextComponent(Main.PREFIX + "§aDu hast §6" + uuidFetcher.getName(args[0]) + "§a gebannt. Dauer: §6" + args[1] + args[2] + "§a Grund: §6" + reason));
                                Team.notify("§6" + sender.getName() + "§a hat §6" + uuidFetcher.getName(args[0]) + "§a gebannt. Dauer: §6" + args[1] + args[2] + "§a Grund: §6" + reason);
                            }
                        }
                    } else
                        sender.sendMessage(new TextComponent(Main.PREFIX + "§cDie Einheit muss d für Tage, h für Stunden, m für Minuten oder s für Sekunden sein!"));
                } else
                    sender.sendMessage(new TextComponent(Main.PREFIX + "§cDie Dauer muss eine Zahl sein!"));
            } else
                sender.sendMessage(new TextComponent(Main.PREFIX + "§cBenutze §6/ban <Spieler> <Dauer> <d/h/m/s> <Grund>§c!"));
        } else
            sender.sendMessage(new TextComponent(Main.PREFIX + "Dieser Befehl muss von einem Spieler oder der Konsole ausgeführt werden."));
    }

}
