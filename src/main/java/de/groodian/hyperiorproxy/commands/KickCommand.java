package de.groodian.hyperiorproxy.commands;
/*
import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorproxy.user.Ban;
import de.groodian.hyperiorproxy.main.Main;
import de.groodian.hyperiorproxy.team.Team;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class KickCommand extends Command {

    private Main plugin;

    public KickCommand(Main plugin) {
        super("kick");
        this.plugin = plugin;
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (sender instanceof ProxiedPlayer || sender instanceof ConsoleCommandSender) {
            if (sender instanceof ProxiedPlayer) {
                if (!HyperiorCore.getRanks().has(((ProxiedPlayer) sender).getUniqueId(), "kick")) {
                    return;
                }
            }
            if (args.length >= 2) {
                final ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    if (stringBuilder.length() == 0)
                        stringBuilder.append(args[i]);
                    else
                        stringBuilder.append(" ").append(args[i]);
                }
                if (target != null) {
                    final String reason = stringBuilder.toString();
                    sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§7Bitte warte, dies kann einen moment dauern."));

                    ProxyServer.getInstance().getScheduler().runAsync(plugin, () -> {

                        target.disconnect(TextComponent.fromLegacyText(Main.DISCONNECT_HEADER +
                                "§cDu wurdest von diesem Netzwerk gekickt." +
                                "\n§cGrund: §e" + reason));

                        Ban.kick(target.getUniqueId().toString().replaceAll("-", ""), target.getName(), sender.getName(), reason);
                        sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§aDu hast §6" + target.getName() + " §agekickt. Grund: §6" + reason));
                        Team.notify("§6" + sender.getName() + "§a hat §6" + target.getName() + " §agekickt. Grund: §6" + reason);

                    });

                } else {
                    sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§cDieser Spieler ist nicht Online!"));
                }
            } else
                sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§cBenutze §6/kick <Spieler> <Grund>§c!"));
        } else
            sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "Dieser Befehl muss von einem Spieler oder der Konsole ausgeführt werden."));
    }

}
*/