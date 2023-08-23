package de.groodian.hyperiorproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import de.groodian.hyperiorcore.command.HArgument;
import de.groodian.hyperiorcore.command.HCommandVelocity;
import de.groodian.hyperiorcore.command.HTabCompleteType;
import de.groodian.hyperiorproxy.main.Main;
import java.util.List;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class BroadcastCommand extends HCommandVelocity<CommandSource> {

    private final Main plugin;

    public BroadcastCommand(Main plugin) {
        super(CommandSource.class, "broadcast", "Broadcast a message", Main.PREFIX_COMPONENT, "broadcast", List.of(),
                List.of(new HArgument("reason", true, HTabCompleteType.NONE)));
        this.plugin = plugin;
    }

    @Override
    protected void onCall(CommandSource source, String[] args) {
        for (Player player : plugin.getServer().getAllPlayers()) {
            player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(args[0]));
        }
    }

}
