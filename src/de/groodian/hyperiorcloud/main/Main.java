package de.groodian.hyperiorcloud.main;

import de.groodian.hyperiorcloud.commands.BanCommand;
import de.groodian.hyperiorcloud.commands.BroadcastCommand;
import de.groodian.hyperiorcloud.commands.HelpCommand;
import de.groodian.hyperiorcloud.commands.KickCommand;
import de.groodian.hyperiorcloud.commands.LobbyCommand;
import de.groodian.hyperiorcloud.commands.LookupCommand;
import de.groodian.hyperiorcloud.commands.MaintenanceCommand;
import de.groodian.hyperiorcloud.commands.MotdCommand;
import de.groodian.hyperiorcloud.commands.PBanCommand;
import de.groodian.hyperiorcloud.commands.PingCommand;
import de.groodian.hyperiorcloud.commands.ReportCommand;
import de.groodian.hyperiorcloud.commands.ServerStarterCommand;
import de.groodian.hyperiorcloud.commands.ShopCommand;
import de.groodian.hyperiorcloud.commands.SlotsCommand;
import de.groodian.hyperiorcloud.commands.UnbanCommand;
import de.groodian.hyperiorcloud.listener.ConnectListener;
import de.groodian.hyperiorcloud.listener.DisconnectListener;
import de.groodian.hyperiorcloud.listener.PingListener;
import de.groodian.network.Server;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class Main extends Plugin {

	public static final String PREFIX = "§bHyperiorCloud §7>> §r";

	private Server server;
	private boolean maintenance;
	private String motdSecondLine;
	private int slots;

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		BungeeCord.getInstance().getConsole().sendMessage(PREFIX + "§aDas Plugin wird geladen....");

		setMaintenance(true);
		setMotdSecondLine("§c§oDieses Netzwerk ist aktuell in der Beta.");
		setSlots(50);
		init(BungeeCord.getInstance().getPluginManager());

		server = new Server(4444);
		server.start();

		BungeeCord.getInstance().getConsole().sendMessage(PREFIX + "§aGeladen!");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onDisable() {
		BungeeCord.getInstance().getConsole().sendMessage(PREFIX + "§cDas Plugin wird gestoppt....");

		BungeeCord.getInstance().getConsole().sendMessage(PREFIX + "§cGestoppt!");
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
		pluginManager.registerCommand(this, new ServerStarterCommand(this));
		pluginManager.registerCommand(this, new PingCommand());
		pluginManager.registerCommand(this, new HelpCommand());
		pluginManager.registerCommand(this, new ShopCommand(this));
		pluginManager.registerCommand(this, new MotdCommand(this));
		pluginManager.registerCommand(this, new SlotsCommand(this));

		pluginManager.registerListener(this, new ConnectListener(this));
		pluginManager.registerListener(this, new DisconnectListener(this));
		pluginManager.registerListener(this, new PingListener(this));
	}

	public Server getServer() {
		return server;
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
