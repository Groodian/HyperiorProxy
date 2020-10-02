package de.groodian.hyperiorproxy.commands;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorproxy.main.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class MotdCommand extends Command {

    private Main plugin;

    public MotdCommand(Main plugin) {
        super("motd");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer || sender instanceof ConsoleCommandSender) {
            if (sender instanceof ProxiedPlayer) {
                if (!HyperiorCore.getRanks().has(((ProxiedPlayer) sender).getUniqueId(), "motd")) {
                    return;
                }
            }
            if (args.length >= 1) {
                String message = "";
                for (int i = 0; i < args.length; i++) {
                    message += args[i] + " ";
                }
                plugin.setMotdSecondLine(ChatColor.translateAlternateColorCodes('&', message));
                sender.sendMessage(new TextComponent(Main.PREFIX + "§aMotd erfolgreich geändert."));
            } else
                sender.sendMessage(new TextComponent(Main.PREFIX + "§cBenutze §6/motd <motd>§c!"));
        } else
            sender.sendMessage(new TextComponent(Main.PREFIX + "Dieser Befehl muss von einem Spieler oder der Konsole ausgeführt werden."));
    }

}
