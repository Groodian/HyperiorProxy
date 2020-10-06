package de.groodian.hyperiorproxy.commands;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.util.UUIDFetcher;
import de.groodian.hyperiorproxy.data.Ban;
import de.groodian.hyperiorproxy.main.Main;
import de.groodian.hyperiorproxy.team.Team;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class BanCommand extends Command {

    private Main plugin;
    private UUIDFetcher uuidFetcher;

    public BanCommand(Main plugin) {
        super("ban");
        this.plugin = plugin;
        uuidFetcher = new UUIDFetcher();
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (sender instanceof ProxiedPlayer || sender instanceof ConsoleCommandSender) {
            if (sender instanceof ProxiedPlayer) {
                if (!HyperiorCore.getRanks().has(((ProxiedPlayer) sender).getUniqueId(), "ban")) {
                    return;
                }
            }
            if (args.length >= 4) {
                final ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);
                if (args[1].chars().allMatch(Character::isDigit)) {
                    if (args[2].equals("d") || args[2].equals("h") || args[2].equals("m") || args[2].equals("s")) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 3; i < args.length; i++) {
                            if (stringBuilder.length() == 0)
                                stringBuilder.append(args[i]);
                            else
                                stringBuilder.append(" ").append(args[i]);
                        }
                        final String reason = stringBuilder.toString();
                        sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§7Bitte warte, dies kann einen moment dauern."));

                        ProxyServer.getInstance().getScheduler().runAsync(plugin, () -> {

                            if (target != null) {
                                String uuid = target.getUniqueId().toString();
                                Ban.ban(uuid, target.getName(), sender.getName(), Integer.parseInt(args[1]), args[2], reason);
                                sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§aDu hast §6" + target.getName() + "§a gebannt. Dauer: §6" + args[1] + args[2] + "§a Grund: §6" + reason));
                                Team.notify("§6" + sender.getName() + "§a hat §6" + target.getName() + "§a gebannt. Dauer: §6" + args[1] + args[2] + "§a Grund: §6" + reason);
                                target.disconnect(TextComponent.fromLegacyText(Ban.getDisconnectReason(uuid)));
                            } else {
                                sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§7Dieser Spieler ist nicht Online, downloade UUID..."));
                                UUIDFetcher.Result result = uuidFetcher.getNameAndUUIDFromName(args[0]);
                                if (result == null) {
                                    sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§cDieser Spieler existiert nicht."));
                                } else {
                                    Ban.ban(result.getUUID(), result.getName(), sender.getName(), Integer.parseInt(args[1]), args[2], reason);
                                    sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§aDu hast §6" + result.getName() + "§a gebannt. Dauer: §6" + args[1] + args[2] + "§a Grund: §6" + reason));
                                    Team.notify("§6" + sender.getName() + "§a hat §6" + result.getName() + "§a gebannt. Dauer: §6" + args[1] + args[2] + "§a Grund: §6" + reason);
                                }
                            }

                        });

                    } else
                        sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§cDie Einheit muss d für Tage, h für Stunden, m für Minuten oder s für Sekunden sein!"));
                } else
                    sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§cDie Dauer muss eine Zahl sein!"));
            } else
                sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§cBenutze §6/ban <Spieler> <Dauer> <d/h/m/s> <Grund>§c!"));
        } else
            sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "Dieser Befehl muss von einem Spieler oder der Konsole ausgeführt werden."));
    }

}
