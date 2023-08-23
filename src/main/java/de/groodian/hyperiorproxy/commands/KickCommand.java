package de.groodian.hyperiorproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import de.groodian.hyperiorcore.command.HArgument;
import de.groodian.hyperiorcore.command.HCommandVelocity;
import de.groodian.hyperiorcore.command.HTabCompleteType;
import de.groodian.hyperiorproxy.main.Main;
import de.groodian.hyperiorproxy.user.UserHistory;
import de.groodian.hyperiorproxy.user.UserHistoryType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class KickCommand extends HCommandVelocity<CommandSource> {

    private final Main plugin;

    public KickCommand(Main plugin) {
        super(CommandSource.class, "kick", "Kick a player", Main.PREFIX_COMPONENT, "kick", List.of(),
                List.of(new HArgument("player", HTabCompleteType.PLAYER), new HArgument("reason", true, HTabCompleteType.NONE)));
        this.plugin = plugin;
    }

    @Override
    protected void onCall(CommandSource source, String[] args) {
        Optional<Player> optionalPlayer = plugin.getServer().getPlayer(args[0]);
        if (optionalPlayer.isEmpty()) {
            sendMsg(source, Component.text("The player is not online.", NamedTextColor.RED));
            return;
        }

        Player player = optionalPlayer.get();

        sendMsg(source, "Please wait, this can take a moment.", NamedTextColor.GRAY);

        plugin.getServer().getScheduler().buildTask(plugin, () -> {
            UUID createdBy;
            String sourceName;
            if (source instanceof Player sourcePlayer) {
                createdBy = sourcePlayer.getUniqueId();
                sourceName = sourcePlayer.getUsername();
            } else {
                createdBy = new UUID(0, 0);
                sourceName = "Console";
            }

            UserHistory userHistory = new UserHistory(player.getUniqueId(), createdBy, UserHistoryType.KICK, args[1], null);

            if (plugin.getBan().ban(player.getUsername(), userHistory)) {
                sendMsg(source, LegacyComponentSerializer.legacySection()
                        .deserialize("§aYou have kicked §6" + player.getUsername() + "§a. Reason: §6" + args[1]));
                plugin.getTeam().notify("§6" + sourceName + "§a kicked §6" + player.getUsername() + "§a. Reason: §6" + args[1]);
                player.disconnect(LegacyComponentSerializer.legacySection()
                        .deserialize(Main.DISCONNECT_HEADER + "§cDu wurdest von diesem Netzwerk gekickt.\n§cGrund: §e" + args[1]));
            } else {
                sendMsg(source, Component.text("An error occurred!", NamedTextColor.DARK_RED));
            }
        }).schedule();
    }

}
