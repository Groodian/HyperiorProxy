package de.groodian.hyperiorproxy.commands;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorproxy.main.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class BroadcastCommand extends Command {

    public BroadcastCommand() {
        super("broadcast", null, "b");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer || sender instanceof ConsoleCommandSender) {
            if (sender instanceof ProxiedPlayer) {
                if (!HyperiorCore.getRanks().has(((ProxiedPlayer) sender).getUniqueId(), "broadcast")) {
                    return;
                }
            }
            if (args.length >= 1) {
                String message = "";
                for (int i = 0; i < args.length; i++) {
                    message += args[i] + " ";
                }
                ProxyServer.getInstance().broadcast(ChatColor.translateAlternateColorCodes('&', message));
            } else
                sender.sendMessage(Main.PREFIX + "§cBenutze §6/broadcast <Nachricht>§c!");
        } else
            sender.sendMessage(Main.PREFIX + "Dieser Befehl muss von einem Spieler oder der Konsole ausgeführt werden.");
    }
}
