package de.groodian.hyperiorcloud.commands;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.groodian.hyperiorcloud.main.Main;
import de.groodian.hyperiorcore.main.HyperiorCore;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class ServerStarterCommand extends Command {

	private Main plugin;

	public ServerStarterCommand(Main plugin) {
		super("serverstarter", null, "starter");
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof ProxiedPlayer || sender instanceof ConsoleCommandSender) {
			if (sender instanceof ProxiedPlayer) {
				if (!HyperiorCore.getRanks().has(((ProxiedPlayer) sender).getUniqueId(), "serverstarter")) {
					return;
				}
			}
			if (args.length == 3) {
				if (args[0].equals("mp")) {
					if (args[1].chars().allMatch(Character::isDigit) && args[2].chars().allMatch(Character::isDigit)) {

						for (int i = 0; i < Integer.parseInt(args[1]); i++) {

							ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();

							exec.schedule(new Runnable() {
								@Override
								public void run() {

									plugin.getServer().getMinecraftPartyServerStarter().startServer();

								}

							}, Long.parseLong(args[2]) * i, TimeUnit.SECONDS);

						}

					} else
						sender.sendMessage(Main.PREFIX + "§cAnzahl und Delay müssen Zahlen sein!");
				} else
					sender.sendMessage(Main.PREFIX + "§cUnbekannter Server!");
			} else
				sender.sendMessage(Main.PREFIX + "§cBenutze §6/serverstarter <mp> <Anzahl> <Delay>§c!");
		} else
			sender.sendMessage(Main.PREFIX + "Dieser Befehl muss von einem Spieler oder der Konsole ausgeführt werden.");
	}
}
