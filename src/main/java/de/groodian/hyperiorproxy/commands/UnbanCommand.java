package de.groodian.hyperiorproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import de.groodian.hyperiorcore.command.HArgument;
import de.groodian.hyperiorcore.command.HCommandVelocity;
import de.groodian.hyperiorcore.command.HTabCompleteType;
import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.user.User;
import de.groodian.hyperiorcore.util.UUIDFetcher;
import de.groodian.hyperiorproxy.main.Main;
import de.groodian.hyperiorproxy.user.UserHistory;
import de.groodian.hyperiorproxy.user.UserHistoryType;
import java.util.List;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class UnbanCommand extends HCommandVelocity<CommandSource> {

    private final Main plugin;

    public UnbanCommand(Main plugin) {
        super(CommandSource.class, "unban", "Unban a player", Main.PREFIX_COMPONENT, "unban", List.of(),
                List.of(new HArgument("player", HTabCompleteType.PLAYER), new HArgument("reason", true, HTabCompleteType.NONE)));
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
            UserHistory userHistoryCurrent = plugin.getBan().loadUserHistory(user.getBan());
            String banDisconnectReason = plugin.getBan().getDisconnectReason(userHistoryCurrent);
            if (banDisconnectReason == null) {
                sendMsg(source, Component.text("This player does not have an active ban.", NamedTextColor.RED));
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

            UserHistory userHistory = new UserHistory(result.getUUID(), createdBy, UserHistoryType.UNBAN, args[1], null);

            if (plugin.getBan().ban(result.getName(), userHistory)) {
                sendMsg(source, LegacyComponentSerializer.legacySection()
                        .deserialize("§aYou have unbanned §6" + result.getName() + "§a. Reason: §6" + args[1]));
                plugin.getTeam().notify("§6" + sourceName + "§a unbanned §6" + result.getName() + "§a. Reason: §6" + args[1]);
            } else {
                sendMsg(source, Component.text("An error occurred!", NamedTextColor.DARK_RED));
            }
        }).schedule();
    }

}
