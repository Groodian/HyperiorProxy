package de.groodian.hyperiorproxy.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import de.groodian.hyperiorproxy.main.Main;
import java.util.UUID;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class PingListener {

    private static final String SERVER_NAME = "§6§lH§f§lYPERIOR.DE §6§lS§f§lERVERNETZWERK";

    private final Main plugin;

    public PingListener(Main plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void handlePing(ProxyPingEvent e) {
        ServerPing.Builder builder = e.getPing().asBuilder();

        builder.samplePlayers(new ServerPing.SamplePlayer(SERVER_NAME, UUID.randomUUID()),
                new ServerPing.SamplePlayer("§aMinecraftParty und vieles mehr!", UUID.randomUUID()));

        builder.description(
                LegacyComponentSerializer.legacySection().deserialize(SERVER_NAME + " §a1.19.4\n§r").append(plugin.getMotdSecondLine()));

        builder.version(new ServerPing.Version(e.getConnection().getProtocolVersion().getProtocol() + 1, ""));

        if (plugin.isMaintenance()) {
            builder.version(new ServerPing.Version(e.getConnection().getProtocolVersion().getProtocol() + 1, "§4§lWARTUNGEN"));
        } else {
            builder.version(
                    new ServerPing.Version(e.getConnection().getProtocolVersion().getProtocol(), SERVER_NAME));
        }

        builder.maximumPlayers(plugin.getServer().getPlayerCount() + 1);

        e.setPing(builder.build());
    }

}
