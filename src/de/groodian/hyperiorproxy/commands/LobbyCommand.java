package de.groodian.hyperiorproxy.commands;

import de.groodian.hyperiorproxy.main.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LobbyCommand extends Command {

    public LobbyCommand() {
        super("lobby", null, "l", "hub", "h", "spawn");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (!player.getServer().getInfo().getName().toLowerCase().contains("lobby")) {
                player.connect(ProxyServer.getInstance().getServerInfo("Lobby-1"));
            } else {
                sender.sendMessage(Main.PREFIX + "§cDu bist bereits in der Lobby.");
            }
        } else {
            sender.sendMessage(Main.PREFIX + "Dieser Befehl muss von einem Spieler ausgeführt werden.");
        }
    }

}
