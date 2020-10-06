package de.groodian.hyperiorproxy.commands;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorproxy.main.Main;
import de.groodian.hyperiorproxy.team.Team;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class MaintenanceCommand extends Command {

    private Main plugin;

    public MaintenanceCommand(Main plugin) {
        super("wartung");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer || sender instanceof ConsoleCommandSender) {
            if (sender instanceof ProxiedPlayer) {
                if (!HyperiorCore.getRanks().has(((ProxiedPlayer) sender).getUniqueId(), "maintenance")) {
                    return;
                }
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("an")) {
                    plugin.setMaintenance(true);
                    sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§aDer Wartungs-Modus ist nun an."));
                    Team.notify("§6" + sender.getName() + "§a aktiviert den Wartungs-Modus!");
                    for (ProxiedPlayer online : plugin.getProxy().getPlayers()) {
                        if (!HyperiorCore.getRanks().has(online.getUniqueId(), "maintenance")) {
                            online.disconnect(TextComponent.fromLegacyText("§cDer Server wird nun gewartet!"));
                        }
                    }
                } else if (args[0].equalsIgnoreCase("aus")) {
                    plugin.setMaintenance(false);
                    sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§aDer Wartungs-Modus ist nun aus."));
                    Team.notify("§6" + sender.getName() + "§a deaktiviert den Wartungs-Modus!");
                } else
                    sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§cBenutze §6/wartung <an/aus>§c!"));
            } else
                sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§cBenutze §6/wartung <an/aus>§c!"));
        } else
            sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "Dieser Befehl muss von einem Spieler oder der Konsole ausgeführt werden."));
    }

}
