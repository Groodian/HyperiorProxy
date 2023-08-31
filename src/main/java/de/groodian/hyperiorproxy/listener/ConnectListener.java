package de.groodian.hyperiorproxy.listener;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.user.User;
import de.groodian.hyperiorproxy.main.Main;
import de.groodian.hyperiorproxy.user.UserHistory;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ConnectListener {

    private final Main plugin;

    public ConnectListener(Main plugin) {
        this.plugin = plugin;
    }

    @Subscribe(order = PostOrder.EARLY)
    public void onLogin(LoginEvent e, Continuation continuation) {
        plugin.getServer().getScheduler().buildTask(plugin, () -> connect(e, continuation)).schedule();
    }

    private void connect(LoginEvent e, Continuation continuation) {
        Player player = e.getPlayer();

        User user = HyperiorCore.getVelocity().getUserManager().get(player.getUniqueId());

        plugin.getTeam().userLogin(user);
        plugin.getData().login(player);

        // https://wiki.vg/Protocol_version_numbers
        if (player.getProtocolVersion().getProtocol() != 762) {
            player.disconnect(LegacyComponentSerializer.legacySection()
                    .deserialize(Main.DISCONNECT_HEADER + "§7Bitte verwende die Version §a1.19.4"));
            plugin.getTeam()
                    .notify("§6" + player.getUsername() +
                            "§a hat versucht den Server beizutreten aber verwendet eine falsche Version. §7(" +
                            player.getProtocolVersion().toString() + ")");
            continuation.resume();
            return;
        }

        UserHistory userHistory = plugin.getBan().loadUserHistory(user.getBan());
        String banDisconnectReason = plugin.getBan().getDisconnectReason(userHistory);
        if (banDisconnectReason != null) {
            player.disconnect(LegacyComponentSerializer.legacySection().deserialize(banDisconnectReason));
            plugin.getTeam().notify("§6" + player.getUsername() + "§a hat versucht den Server beizutreten ist aber gebannt.");
            continuation.resume();
            return;
        }

        if (plugin.isMaintenance()) {
            if (!user.has("maintenance")) {
                player.disconnect(LegacyComponentSerializer.legacySection()
                        .deserialize(Main.DISCONNECT_HEADER + "§cDer Server wird derzeit gewartet! Versuche es später nochmal."));
                plugin.getTeam()
                        .notify("§6" + player.getUsername() +
                                "§a hat versucht den Server beizutreten aber der Server ist im Wartungs-Modus.");
                continuation.resume();
                return;
            }
        }

        int slots = plugin.getServer().getPlayerCount() + 1;
        if (slots > plugin.getSlots()) {
            if (!user.has("joinfullserver")) {
                player.disconnect(LegacyComponentSerializer.legacySection()
                        .deserialize(Main.DISCONNECT_HEADER +
                                     "§cDer Server ist derzeit voll!\nDu benötigst einen Rang um trotzdem beitreten zu können."));
                plugin.getTeam().notify("§6" + player.getUsername() + "§a hat versucht den Server beizutreten aber der Server ist voll.");
                continuation.resume();
                return;
            }
        }

        continuation.resume();
    }

}
