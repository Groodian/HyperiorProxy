package de.groodian.hyperiorproxy.main;

import de.groodian.hyperiorproxy.commands.BanCommand;
import de.groodian.hyperiorproxy.commands.BroadcastCommand;
import de.groodian.hyperiorproxy.commands.HelpCommand;
import de.groodian.hyperiorproxy.commands.KickCommand;
import de.groodian.hyperiorproxy.commands.LobbyCommand;
import de.groodian.hyperiorproxy.commands.LookupCommand;
import de.groodian.hyperiorproxy.commands.MaintenanceCommand;
import de.groodian.hyperiorproxy.commands.MotdCommand;
import de.groodian.hyperiorproxy.commands.PBanCommand;
import de.groodian.hyperiorproxy.commands.PingCommand;
import de.groodian.hyperiorproxy.commands.ReportCommand;
import de.groodian.hyperiorproxy.commands.ShopCommand;
import de.groodian.hyperiorproxy.commands.SlotsCommand;
import de.groodian.hyperiorproxy.commands.UnbanCommand;
import de.groodian.hyperiorproxy.listener.ConnectListener;
import de.groodian.hyperiorproxy.listener.DisconnectListener;
import de.groodian.hyperiorproxy.listener.JoinListener;
import de.groodian.hyperiorproxy.listener.PingListener;
import de.groodian.hyperiorproxy.network.ProxyClient;
import de.groodian.network.DataPackage;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class Main extends Plugin {

    public static final String PREFIX = "§bHyperiorCloud §7>> §r";
    public static final String DISCONNECT_HEADER = "§6§lH§fYPERIOR.DE §6§lS§fERVERNETZWERK\n\n";

    private boolean maintenance;
    private String motdSecondLine;
    private int slots;

    private ProxyClient client;

    @Override
    public void onEnable() {
        BungeeCord.getInstance().getConsole().sendMessage(new TextComponent(PREFIX + "§aDas Plugin wird geladen...."));

        setMaintenance(true);
        setMotdSecondLine("§c§oDieses Netzwerk ist aktuell in der Beta.");
        setSlots(50);
        init(BungeeCord.getInstance().getPluginManager());

        client = new ProxyClient("localhost", 4444, new DataPackage("LOGIN", "Bungeecord", 1));

        BungeeCord.getInstance().getConsole().sendMessage(new TextComponent(PREFIX + "§aGeladen!"));
    }

    @Override
    public void onDisable() {
        BungeeCord.getInstance().getConsole().sendMessage(new TextComponent(PREFIX + "§cDas Plugin wird gestoppt...."));

        client.stop();

        BungeeCord.getInstance().getConsole().sendMessage(new TextComponent(PREFIX + "§cGestoppt!"));
    }

    private void init(PluginManager pluginManager) {
        pluginManager.registerCommand(this, new BroadcastCommand());
        pluginManager.registerCommand(this, new MaintenanceCommand(this));
        pluginManager.registerCommand(this, new LobbyCommand());
        pluginManager.registerCommand(this, new BanCommand());
        pluginManager.registerCommand(this, new LookupCommand());
        pluginManager.registerCommand(this, new UnbanCommand());
        pluginManager.registerCommand(this, new KickCommand());
        pluginManager.registerCommand(this, new PBanCommand());
        pluginManager.registerCommand(this, new ReportCommand());
        pluginManager.registerCommand(this, new PingCommand());
        pluginManager.registerCommand(this, new HelpCommand());
        pluginManager.registerCommand(this, new ShopCommand(this));
        pluginManager.registerCommand(this, new MotdCommand(this));
        pluginManager.registerCommand(this, new SlotsCommand(this));

        pluginManager.registerListener(this, new ConnectListener(this));
        pluginManager.registerListener(this, new DisconnectListener(this));
        pluginManager.registerListener(this, new PingListener(this));
        pluginManager.registerListener(this, new JoinListener());
    }

    public ProxyClient getClient() {
        return client;
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

}
