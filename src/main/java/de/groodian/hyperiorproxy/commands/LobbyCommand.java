package de.groodian.hyperiorproxy.commands;
/*
import de.groodian.hyperiorproxy.main.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
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
                player.connect(ProxyServer.getInstance().getServerInfo("LOBBY"));
            } else {
                sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§cDu bist bereits in einer Lobby."));
            }
        } else {
            sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "Dieser Befehl muss von einem Spieler ausgeführt werden."));
        }
    }

}
*/