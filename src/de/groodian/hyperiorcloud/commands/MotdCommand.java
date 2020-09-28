package de.groodian.hyperiorcloud.commands;

import de.groodian.hyperiorcloud.main.Main;
import de.groodian.hyperiorcore.main.HyperiorCore;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class MotdCommand extends Command {
	
	private Main plugin;

	public MotdCommand(Main plugin) {
		super("motd");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof ProxiedPlayer || sender instanceof ConsoleCommandSender) {
			if (sender instanceof ProxiedPlayer) {
				if (!HyperiorCore.getRanks().has(((ProxiedPlayer) sender).getUniqueId(), "motd")) {
					return;
				}
			}
			if (args.length >= 1) {
				String message = "";
				for (int i = 0; i < args.length; i++) {
					message += args[i] + " ";
				}
				plugin.setMotdSecondLine(ChatColor.translateAlternateColorCodes('&', message));
				sender.sendMessage(Main.PREFIX + "§aMotd erfolgreich geändert.");
			} else
				sender.sendMessage(Main.PREFIX + "§cBenutze §6/motd <motd>§c!");
		} else
			sender.sendMessage(Main.PREFIX + "Dieser Befehl muss von einem Spieler oder der Konsole ausgeführt werden.");
	}

}
