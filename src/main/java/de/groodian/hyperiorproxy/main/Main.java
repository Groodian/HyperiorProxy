package de.groodian.hyperiorproxy.main;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import de.groodian.hyperiorcore.command.HCommandManagerVelocity;
import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorproxy.commands.BanCommand;
import de.groodian.hyperiorproxy.commands.BroadcastCommand;
import de.groodian.hyperiorproxy.commands.DiscordCommand;
import de.groodian.hyperiorproxy.commands.HelpCommand;
import de.groodian.hyperiorproxy.commands.KickCommand;
import de.groodian.hyperiorproxy.commands.LobbyCommand;
import de.groodian.hyperiorproxy.commands.LookupCommand;
import de.groodian.hyperiorproxy.commands.MaintenanceCommand;
import de.groodian.hyperiorproxy.commands.MotdCommand;
import de.groodian.hyperiorproxy.commands.PBanCommand;
import de.groodian.hyperiorproxy.commands.PingCommand;
import de.groodian.hyperiorproxy.commands.ShopCommand;
import de.groodian.hyperiorproxy.commands.SlotsCommand;
import de.groodian.hyperiorproxy.commands.UnbanCommand;
import de.groodian.hyperiorproxy.listener.ConnectListener;
import de.groodian.hyperiorproxy.listener.DisconnectListener;
import de.groodian.hyperiorproxy.listener.JoinListener;
import de.groodian.hyperiorproxy.listener.PingListener;
import de.groodian.hyperiorproxy.network.ProxyClient;
import de.groodian.hyperiorproxy.team.Team;
import de.groodian.hyperiorproxy.user.Ban;
import de.groodian.hyperiorproxy.user.Data;
import de.groodian.network.DataPackage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
    public static final Component PREFIX_COMPONENT = Component.text("[", NamedTextColor.GRAY)
            .append(Component.text("HyperiorProxy", NamedTextColor.AQUA))
            .append(Component.text("] ", NamedTextColor.GRAY));
    public static final String DISCONNECT_HEADER = "§6§lH§f§lYPERIOR.DE §6§lS§f§lERVERNETZWERK\n\n";

    private final ProxyServer server;
    private final Logger logger;

    private boolean maintenance;
    private Component motdSecondLine;
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
        setMotdSecondLine(Component.text("Dieses Netzwerk ist aktuell in der Beta.", NamedTextColor.RED).decorate(TextDecoration.ITALIC));
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

        HCommandManagerVelocity hCommandManagerVelocity = HyperiorCore.getVelocity().getHCommandManagerVelocity();
        hCommandManagerVelocity.registerCommand(new BanCommand(this));
        hCommandManagerVelocity.registerCommand(new KickCommand(this));
        hCommandManagerVelocity.registerCommand(new PBanCommand(this));
        hCommandManagerVelocity.registerCommand(new UnbanCommand(this));
        hCommandManagerVelocity.registerCommand(new LookupCommand(this));
        hCommandManagerVelocity.registerCommand(new BroadcastCommand(this));
        hCommandManagerVelocity.registerCommand(new DiscordCommand());
        hCommandManagerVelocity.registerCommand(new HelpCommand());
        hCommandManagerVelocity.registerCommand(new ShopCommand(this));
        hCommandManagerVelocity.registerCommand(new MaintenanceCommand(this));
        hCommandManagerVelocity.registerCommand(new MotdCommand(this));
        hCommandManagerVelocity.registerCommand(new SlotsCommand(this));
        hCommandManagerVelocity.registerCommand(new LobbyCommand(this), "l");
        hCommandManagerVelocity.registerCommand(new PingCommand());

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

    public Component getMotdSecondLine() {
        return motdSecondLine;
    }

    public void setMotdSecondLine(Component motdSecondLine) {
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
