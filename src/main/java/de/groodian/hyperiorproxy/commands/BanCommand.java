package de.groodian.hyperiorproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import de.groodian.hyperiorcore.command.HArgument;
import de.groodian.hyperiorcore.command.HCommandVelocity;
import de.groodian.hyperiorcore.command.HTabCompleteType;
import de.groodian.hyperiorcore.util.UUIDFetcher;
import de.groodian.hyperiorproxy.main.Main;
import de.groodian.hyperiorproxy.user.UserHistory;
import de.groodian.hyperiorproxy.user.UserHistoryType;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.postgresql.util.PGInterval;

public class BanCommand extends HCommandVelocity<CommandSource> {

    private final Main plugin;

    private static final Map<String, String> durationType = Map.of("d", "day", "h", "hour", "m", "minute", "s", "second");

    public BanCommand(Main plugin) {
        super(CommandSource.class, "ban", "Ban a player", Main.PREFIX_COMPONENT, "ban", List.of(),
                List.of(new HArgument("player", HTabCompleteType.PLAYER), new HArgument("duration"),
                        new HArgument("duration type", HTabCompleteType.CUSTOM), new HArgument("reason", true, HTabCompleteType.NONE)));
        this.plugin = plugin;
    }


    @Override
    protected void onCall(CommandSource source, String[] args) {
        int duration;
        try {
            duration = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sendMsg(source, Component.text("The duration has to be a number.", NamedTextColor.RED));
            return;
        }

        if (duration < 0) {
            sendMsg(source, Component.text("The duration has to be a positive.", NamedTextColor.RED));
            return;
        }

        if (!durationType.containsKey(args[2])) {
            Component msg = Component.text("The duration type has to be:", NamedTextColor.RED);
            for (Map.Entry<String, String> entry : durationType.entrySet()) {
                msg = msg.append(Component.text("\n" + entry.getKey() + " for " + entry.getValue() + "(s)", NamedTextColor.GOLD));
            }
            sendMsg(source, msg);
            return;
        }

        PGInterval pgInterval = null;
        try {
            pgInterval = new PGInterval(duration + " " + durationType.get(args[2]));
        } catch (SQLException e) {
            sendMsg(source, Component.text("Error.", NamedTextColor.RED));
            return;
        }

        sendMsg(source, "Please wait, this can take a moment.", NamedTextColor.GRAY);

        PGInterval finalPgInterval = pgInterval;
        plugin.getServer().getScheduler().buildTask(plugin, () -> {
            UUIDFetcher.Result result = new UUIDFetcher().getNameAndUUIDFromName(args[0]);

            if (result == null) {
                sendMsg(source, Component.text("This player does not exist.", NamedTextColor.RED));
                return;
            }

            UUID createdBy;
            String sourceName;
            if (source instanceof Player player) {
                createdBy = player.getUniqueId();
                sourceName = player.getUsername();
            } else {
                createdBy = new UUID(0, 0);
                sourceName = "Console";
            }

            UserHistory userHistory = new UserHistory(result.getUUID(), createdBy, UserHistoryType.BAN, args[3], finalPgInterval);

            if (plugin.getBan().ban(result.getName(), userHistory)) {
                sendMsg(source, LegacyComponentSerializer.legacySection()
                        .deserialize("§aYou have banned §6" + result.getName() + "§a. Duration: §6" + duration + durationType.get(args[2]) +
                                     "(s)§a Reason: §6" + args[3]));
                plugin.getTeam()
                        .notify("§6" + sourceName + "§a banned §6" + result.getName() + "§a. Duration: §6" + duration +
                                durationType.get(args[2]) + "(s)§a Reason: §6" + args[3]);
                plugin.getServer().getPlayer(userHistory.getTarget()).ifPresent((target) -> {
                    target.disconnect(
                            LegacyComponentSerializer.legacySection().deserialize(plugin.getBan().getDisconnectReason(userHistory)));
                });
            } else {
                sendMsg(source, Component.text("An error occurred!", NamedTextColor.DARK_RED));
            }
        }).schedule();
    }

}
