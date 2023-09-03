package de.groodian.hyperiorproxy.commands;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.groodian.hyperiorcore.command.HCommandVelocity;
import de.groodian.hyperiorproxy.main.Main;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class LobbyCommand extends HCommandVelocity<Player> {

    private final Main plugin;

    public LobbyCommand(Main plugin) {
        super(Player.class, "lobby", "Return to the lobby", Main.PREFIX_COMPONENT, null, List.of(), List.of());
        this.plugin = plugin;
    }

    @Override
    protected void onCall(Player player, String[] args) {
        Optional<ServerConnection> currentServer = player.getCurrentServer();
        if (currentServer.isEmpty()) {
            return;
        }

        if (!currentServer.get().getServerInfo().getName().toUpperCase().contains("LOBBY")) {
            Optional<RegisteredServer> registeredServer = plugin.getServer().getServer("LOBBY");
            if (registeredServer.isEmpty()) {
                return;
            }
            player.createConnectionRequest(registeredServer.get()).fireAndForget();
        } else {
            sendMsg(player, Component.text("Du bist bereits in einer Lobby.", NamedTextColor.RED));
        }
    }

}
