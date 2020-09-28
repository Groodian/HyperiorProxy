package de.groodian.hyperiorcloud.commands;

import de.groodian.hyperiorcloud.data.Ban;
import de.groodian.hyperiorcloud.main.Main;
import de.groodian.hyperiorcloud.team.Team;
import de.groodian.hyperiorcore.main.HyperiorCore;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class KickCommand extends Command {

	public KickCommand() {
		super("kick");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof ProxiedPlayer || sender instanceof ConsoleCommandSender) {
			if (sender instanceof ProxiedPlayer) {
				if (!HyperiorCore.getRanks().has(((ProxiedPlayer) sender).getUniqueId(), "kick")) {
					return;
				}
			}
			if (args.length >= 2) {
				ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);
				String reason = "";
				for (int i = 1; i < args.length; i++) {
					if (reason == "")
						reason = args[i];
					else
						reason += " " + args[i];
				}
				if (target != null) {
					target.disconnect("                  §6§lＨ§fＹＰＥＲＩＯＲ．ＤＥ §6§lＳ§fＥＲＶＥＲＮＥＴＺＷＥＲＫ \n\n§cDu wurdest von diesem Netzwerk gekickt.\n§cGrund: §e" + reason);
					Ban.kick(target.getUniqueId().toString().replaceAll("-", ""), target.getName(), sender.getName(), reason);
					sender.sendMessage(Main.PREFIX + "§aDu hast §6" + target.getName() + " §agekickt. Grund: §6" + reason);
					Team.notify("§6" + sender.getName() + "§a hat §6" + target.getName() + " §agekickt. Grund: §6" + reason);
				} else {
					sender.sendMessage(Main.PREFIX + "§cDieser Spieler ist nicht Online!");
				}
			} else
				sender.sendMessage(Main.PREFIX + "§cBenutze §6/kick <Spieler> <Grund>§c!");
		} else
			sender.sendMessage(Main.PREFIX + "Dieser Befehl muss von einem Spieler oder der Konsole ausgeführt werden.");
	}

}
