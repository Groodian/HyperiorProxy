package de.groodian.hyperiorcloud.commands;

import de.groodian.hyperiorcloud.main.Main;
import de.groodian.hyperiorcore.main.HyperiorCore;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class SlotsCommand extends Command {

	private Main plugin;

	public SlotsCommand(Main plugin) {
		super("slots");
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof ProxiedPlayer || sender instanceof ConsoleCommandSender) {
			if (sender instanceof ProxiedPlayer) {
				if (!HyperiorCore.getRanks().has(((ProxiedPlayer) sender).getUniqueId(), "slots")) {
					return;
				}
			}
			if (args.length == 1) {
				plugin.setSlots(Integer.parseInt(args[0]));
				sender.sendMessage(Main.PREFIX + "�aSlots erfolgreich ge�ndert.");
			} else
				sender.sendMessage(Main.PREFIX + "�cBenutze �6/slots <Slots>�c!");
		} else
			sender.sendMessage(Main.PREFIX + "Dieser Befehl muss von einem Spieler oder der Konsole ausgef�hrt werden.");
	}

}
