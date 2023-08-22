package de.groodian.hyperiorproxy.main;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import de.groodian.hyperiorproxy.listener.ConnectListener;
import de.groodian.hyperiorproxy.listener.DisconnectListener;
import de.groodian.hyperiorproxy.listener.JoinListener;
import de.groodian.hyperiorproxy.listener.PingListener;
import de.groodian.hyperiorproxy.network.ProxyClient;
import de.groodian.hyperiorproxy.team.Team;
import de.groodian.hyperiorproxy.user.Ban;
import de.groodian.hyperiorproxy.user.Data;
import de.groodian.network.DataPackage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.slf4j.Logger;

@Plugin(
        id = "hyperiorproxy",
        name = "HyperiorProxy",
        version = "5.0.0-SNAPSHOT",
        description = "Proxy for Hyperior",
        authors = {"Groodian"}
)
public class Main {

    public static final String PREFIX = "§7[§bHyperiorProxy§7] §r";
    public static final String DISCONNECT_HEADER = "§6§lH§f§lYPERIOR.DE §6§lS§f§lERVERNETZWERK\n\n";

    private final ProxyServer server;
    private final Logger logger;

    private boolean maintenance;
    private String motdSecondLine;
    private int slots;
    private int groupNumber;

    private ProxyClient proxyClient;
    private Team team;
    private Ban ban;
    private Data data;

    @Inject
    public Main(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info(PREFIX + "§aDas Plugin wird geladen...");

        setMaintenance(true);
        setMotdSecondLine("§c§oDieses Netzwerk ist aktuell in der Beta.");
        setSlots(50);
        groupNumber = Integer.parseInt(PlainTextComponentSerializer.plainText().serialize(server.getConfiguration().getMotd()));

        proxyClient = new ProxyClient(this, "localhost", 4444, new DataPackage("LOGIN", "Proxy", groupNumber));
        team = new Team(this);
        ban = new Ban();
        data = new Data();

        EventManager eventManager = server.getEventManager();
        eventManager.register(this, new ConnectListener(this));
        eventManager.register(this, new DisconnectListener(this));
        eventManager.register(this, new JoinListener(this));
        eventManager.register(this, new PingListener(this));

        logger.info(PREFIX + "§aGeladen!");
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        logger.info(PREFIX + "§cDas Plugin wird gestoppt...");

        proxyClient.stop();

        logger.info(PREFIX + "§cGestoppt!");
    }

    public ProxyServer getServer() {
        return server;
    }

    public Logger getLogger() {
        return logger;
    }

    public boolean isMaintenance() {
        return maintenance;
    }

    public void setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
    }

    public String getMotdSecondLine() {
        return motdSecondLine;
    }

    public void setMotdSecondLine(String motdSecondLine) {
        this.motdSecondLine = motdSecondLine;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public Team getTeam() {
        return team;
    }

    public Ban getBan() {
        return ban;
    }

    public Data getData() {
        return data;
    }

}
