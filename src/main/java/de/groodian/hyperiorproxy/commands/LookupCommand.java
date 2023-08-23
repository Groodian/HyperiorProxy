package de.groodian.hyperiorproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import de.groodian.hyperiorcore.command.HArgument;
import de.groodian.hyperiorcore.command.HCommandVelocity;
import de.groodian.hyperiorcore.command.HTabCompleteType;
import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.user.User;
import de.groodian.hyperiorcore.util.Time;
import de.groodian.hyperiorcore.util.UUIDFetcher;
import de.groodian.hyperiorproxy.main.Main;
import de.groodian.hyperiorproxy.user.UserHistory;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class LookupCommand extends HCommandVelocity<CommandSource> {

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private final Main plugin;

    public LookupCommand(Main plugin) {
        super(CommandSource.class, "lookup", "Lookup a player", Main.PREFIX_COMPONENT, "lookup", List.of(),
                List.of(new HArgument("player", HTabCompleteType.PLAYER)));
        this.plugin = plugin;
    }

    @Override
    protected void onCall(CommandSource source, String[] args) {
        sendMsg(source, "Please wait, this can take a moment.", NamedTextColor.GRAY);

        plugin.getServer().getScheduler().buildTask(plugin, () -> {
            UUIDFetcher.Result result = new UUIDFetcher().getNameAndUUIDFromName(args[0]);

            if (result == null) {
                sendMsg(source, Component.text("This player does not exist.", NamedTextColor.RED));
                return;
            }

            User user = HyperiorCore.getUserManager().loadUser(result.getUUID());

            if (user == null) {
                sendMsg(source, Component.text("This player has never joined this server.", NamedTextColor.RED));
                return;
            }

            UserHistory userHistoryCurrent = plugin.getBan().loadUserHistory(user.getBan());
            List<UserHistory> completeUserHistory = plugin.getBan().loadCompleteUserHistory(user.getUuid());

            String currentServer;
            Optional<Player> optionalPlayer = plugin.getServer().getPlayer(args[0]);
            if (optionalPlayer.isEmpty()) {
                currentServer = "§cNo";
            } else {
                Player player = optionalPlayer.get();
                Optional<ServerConnection> serverConnection = player.getCurrentServer();
                if (serverConnection.isEmpty()) {
                    currentServer = "§cNo server";
                } else {
                    currentServer = serverConnection.get().getServerInfo().getName();
                }
            }

            String ban = "No";
            if (plugin.getBan().getDisconnectReason(userHistoryCurrent) != null) {
                ban = "Yes";
            }

            StringBuilder history = new StringBuilder();
            for (UserHistory userHistory : completeUserHistory) {
                history.append("\n").append(formatUserHistory(user, userHistory));
            }

            Component component = LegacyComponentSerializer.legacySection()
                    .deserialize("§aLookup §6" + user.getName() + "§a:" +
                                 "\n§aOnline: §6" + currentServer +
                                 "\n§aRank: " + LegacyComponentSerializer.legacySection()
                                         .serialize(Component.text(user.getRank().name(), user.getRank().color())) +
                                 "\n§aCurrently Banned: §c" + ban +
                                 "\n§aHistory: §6" + history +
                                 "\n§aLogins: §6" + user.getLogins() +
                                 "\n§aFirst Login: §6" + formatDate(user.getFirstLogin()) +
                                 "\n§aLast Login: §6" + formatDate(user.getLastLogin()) +
                                 "\n§aLast Logout: §6" + formatDate(user.getLastLogout()) +
                                 "\n§aLogin Days: §6" + user.getLoginDays() +
                                 "\n§aConnection Time: §6" + user.getConnectionTime() / 60 + " Minuten");

            sendMsg(source, component);
        }).schedule();
    }

    private String formatUserHistory(User target, UserHistory userHistory) {
        StringBuilder stringBuilder = new StringBuilder();

        String createdByName;
        if (Objects.equals(userHistory.getCreatedBy(), new UUID(0, 0))) {
            createdByName = "Console";
        } else {
            User user = HyperiorCore.getUserManager().loadUser(userHistory.getCreatedBy());
            createdByName = user.getName();
        }

        stringBuilder.append("§6").append(createdByName).append("§a ");

        switch (userHistory.getType()) {
            case BAN -> {
                Date date = new Date();
                date.setTime(0);
                userHistory.getDuration().add(date);
                Duration duration = Duration.ofMillis(date.getTime());

                stringBuilder.append("banned §6").append(target.getName()).append("§a. Duration: §6").append(Time.durationString(duration));
            }
            case KICK -> {
                stringBuilder.append("kicked §6").append(target.getName());
            }
            case PERMANENT_BAN -> {
                stringBuilder.append("permanently banned §6").append(target.getName());
            }
            case UNBAN -> {
                stringBuilder.append("unbanned §6").append(target.getName());
            }
            default -> {
                return "§4Error";
            }
        }

        stringBuilder.append("§a. Reason: §6")
                .append(userHistory.getReason())
                .append(" §7(")
                .append(formatDate(userHistory.getCreatedAt()))
                .append(")");

        return stringBuilder.toString();
    }

    private String formatDate(OffsetDateTime date) {
        if (date == null) {
            return "NULL";
        } else {
            return dateFormatter.format(Date.from(date.toInstant()));
        }
    }

}
