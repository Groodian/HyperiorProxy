package de.groodian.hyperiorproxy.commands;

import com.velocitypowered.api.proxy.Player;
import de.groodian.hyperiorcore.command.HCommandVelocity;
import de.groodian.hyperiorproxy.main.Main;
import java.util.List;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class PingCommand extends HCommandVelocity<Player> {

    public PingCommand() {
        super(Player.class, "ping", "Get your ping", Main.PREFIX_COMPONENT, null, List.of(), List.of());
    }

    @Override
    protected void onCall(Player player, String[] args) {
        sendMsg(player, LegacyComponentSerializer.legacySection().deserialize("§aDein Ping beträgt §e" + player.getPing() + "ms§a."));
    }

}
