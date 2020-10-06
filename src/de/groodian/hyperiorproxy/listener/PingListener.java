package de.groodian.hyperiorproxy.listener;

import de.groodian.hyperiorproxy.main.Main;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.PlayerInfo;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class PingListener implements Listener {

    private Main plugin;

    public PingListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void handlePing(ProxyPingEvent e) {
        ServerPing ping = e.getResponse();
        ServerPing.Players players = ping.getPlayers();
        PlayerInfo info0 = new PlayerInfo("§6§lＨ§fＹＰＥＲＩＯＲ．ＤＥ §6§lＳ§fＥＲＶＥＲＮＥＴＺＷＥＲＫ", UUID.randomUUID());
        PlayerInfo info1 = new PlayerInfo("§aMinecraftParty und vieles mehr!", UUID.randomUUID());
        PlayerInfo[] allInfo = {info0, info1};
        players.setSample(allInfo);

        // fix wrong format
        if (e.getConnection().getVersion() > 47) {
            ping.setDescriptionComponent(new TextComponent(("§6§lＨ§fＹＰＥＲＩＯＲ．ＤＥ §6§lＳ§fＥＲＶＥＲＮＥＴＺＷＥＲＫ §8» §a1.8§7 - §a1.15.2\n§r" + plugin.getMotdSecondLine())));
        } else {
            ping.setDescriptionComponent(new TextComponent(("§6§lＨ§fＹＰＥＲＩＯＲ．ＤＥ §6§lＳ§fＥＲＶＥＲＮＥＴＺＷＥＲＫ §a1.8§7-§a1.15\n§r" + plugin.getMotdSecondLine())));
        }
        ping.setPlayers(players);
        if (plugin.isMaintenance()) {
            ping.setVersion(new ServerPing.Protocol("§4§lWARTUNGEN", e.getConnection().getVersion() + 1));
        } else {
            ping.setVersion(new ServerPing.Protocol("HYPERIOR.DE SERVERNETZWERK", e.getConnection().getVersion()));
        }
        int slots = BungeeCord.getInstance().getOnlineCount() + 1;
        if (slots > plugin.getSlots()) {
            slots = plugin.getSlots();
        }
        ping.getPlayers().setMax(slots);
        e.setResponse(ping);
    }

}
