package de.groodian.hyperiorcloud.commands;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PingCommand extends Command {

	private static final String PREFIX = "§7[§ePing§7] ";

	public PingCommand() {
		super("ping");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof ProxiedPlayer) {
			ProxiedPlayer player = (ProxiedPlayer) sender;
			if (args.length == 0) {
				player.sendMessage(PREFIX + "§aDein Ping beträgt §e" + player.getPing() + "ms§a.");
			} else {
				ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);
				if (target != null) {
					player.sendMessage(PREFIX + "§e" + target.getName() + "'s §aPing beträgt §e" + target.getPing() + "ms§a.");
				} else {
					player.sendMessage(PREFIX + "§cDieser Spieler ist nicht online.");
				}
			}
		} else {
			sender.sendMessage(PREFIX + "Dieser Befehl muss von einem Spieler ausgeführt werden.");
		}
	}

}
