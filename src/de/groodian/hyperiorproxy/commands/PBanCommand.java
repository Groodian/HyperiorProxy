package de.groodian.hyperiorproxy.commands;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.util.UUIDFetcher;
import de.groodian.hyperiorproxy.data.Ban;
import de.groodian.hyperiorproxy.main.Main;
import de.groodian.hyperiorproxy.team.Team;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class PBanCommand extends Command {

    private UUIDFetcher uuidFetcher;

    public PBanCommand() {
        super("pban");
        uuidFetcher = new UUIDFetcher();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer || sender instanceof ConsoleCommandSender) {
            if (sender instanceof ProxiedPlayer) {
                if (!HyperiorCore.getRanks().has(((ProxiedPlayer) sender).getUniqueId(), "pban")) {
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
                    Ban.pban(target.getUniqueId().toString().replaceAll("-", ""), target.getName(), sender.getName(), reason);
                    sender.sendMessage(Main.PREFIX + "§aDu hast §6" + target.getName() + "§a gebannt. Dauer: §6PERMANENT §a Grund: §6" + reason);
                    Team.notify("§6" + sender.getName() + "§a hat §6" + target.getName() + "§a gebannt. Dauer: §6PERMANENT §a Grund: §6" + reason);
                    target.disconnect("                  §6§lＨ§fＹＰＥＲＩＯＲ．ＤＥ §6§lＳ§fＥＲＶＥＲＮＥＴＺＷＥＲＫ \n\n§cDu wurdest von diesem Netzwerk gebannt.\n§cGrund: §e" + reason
                            + "\n§cDauer: §ePERMANENT\n\n§aDu kannst einen Entbannungsantrag an §eunban@hyperior.de §asenden.");
                } else {
                    sender.sendMessage(Main.PREFIX + "§7Dieser Spieler ist nicht Online, downloade UUID...");
                    if (uuidFetcher.getUUID(args[0]) == null) {
                        sender.sendMessage(Main.PREFIX + "§cDieser Spieler existiert nicht.");
                    } else {
                        String tempName = uuidFetcher.getName(args[0]);
                        Ban.pban(uuidFetcher.getUUID(args[0]), tempName, sender.getName(), reason);
                        sender.sendMessage(Main.PREFIX + "§aDu hast §6" + tempName + "§a gebannt. Dauer: §6PERMANENT §a Grund: §6" + reason);
                        Team.notify("§6" + sender.getName() + "§a hat §6" + tempName + "§a gebannt. Dauer: §6PERMANENT §a Grund: §6" + reason);
                    }
                }
            } else
                sender.sendMessage(Main.PREFIX + "§cBenutze §6/pban <Spieler> <Grund>§c!");
        } else
            sender.sendMessage(Main.PREFIX + "Dieser Befehl muss von einem Spieler oder der Konsole ausgeführt werden.");
    }

}
