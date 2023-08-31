package de.groodian.hyperiorproxy.listener;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import de.groodian.hyperiorproxy.main.Main;

public class DisconnectListener {

    private final Main plugin;

    public DisconnectListener(Main plugin) {
        this.plugin = plugin;
    }

    @Subscribe(order = PostOrder.EARLY)
    public void onLogin(DisconnectEvent e, Continuation continuation) {
        plugin.getServer().getScheduler().buildTask(plugin, () -> disconnect(e, continuation)).schedule();
    }

    private void disconnect(DisconnectEvent e, Continuation continuation) {
        Player player = e.getPlayer();

        plugin.getTeam().userLogout(player);
        plugin.getData().logout(player);

        continuation.resume();
    }

}
