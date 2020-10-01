package de.groodian.hyperiorproxy.commands;

import de.groodian.hyperiorproxy.data.Ban;
import de.groodian.hyperiorproxy.main.Main;
import de.groodian.hyperiorproxy.team.Team;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportCommand extends Command {

    private static final String REPORT_PREFIX = "§bReport §7>> §r";

    private static Map<String, List<String>> reported = new HashMap<>();

    public ReportCommand() {
        super("report");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            if (args.length >= 2) {
                ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);
                ProxiedPlayer player = (ProxiedPlayer) sender;
                String reason = "";
                for (int i = 1; i < args.length; i++) {
                    if (reason == "")
                        reason = args[i];
                    else
                        reason += " " + args[i];
                }
                if (target != null) {
                    if (target != sender) {
                        if (!reported.containsKey(target.getUniqueId().toString())) {
                            List<String> tempList = new ArrayList<>();
                            tempList.add(player.getUniqueId().toString());
                            reported.put(target.getUniqueId().toString(), tempList);
                            Ban.report(target.getUniqueId().toString().replaceAll("-", ""), target.getName(), sender.getName(), reason);
                            sender.sendMessage(Main.PREFIX + "§aDu hast §6" + target.getName() + " §aerfolgreich reportet.");
                            Team.notify("§6" + sender.getName() + "§a hat §6" + target.getName() + " §areportet. Grund: §6" + reason + " §aServer: §6" + target.getServer().getInfo().getName());
                        } else {
                            if (!reported.get(target.getUniqueId().toString()).contains(player.getUniqueId().toString())) {
                                List<String> tempList = reported.get(target.getUniqueId().toString());
                                tempList.add(player.getUniqueId().toString());
                                reported.put(target.getUniqueId().toString(), tempList);
                                Ban.report(target.getUniqueId().toString().replaceAll("-", ""), target.getName(), sender.getName(), reason);
                                sender.sendMessage(Main.PREFIX + "§aDu hast §6" + target.getName() + " §aerfolgreich reportet.");
                                Team.notify("§6" + sender.getName() + "§a hat §6" + target.getName() + " §areportet. Grund: §6" + reason + " §aServer: §6" + target.getServer().getInfo().getName());
                            } else {
                                sender.sendMessage(Main.PREFIX + "§cDu hast diesen Spieler bereits reportet!");
                            }
                        }
                    } else {
                        sender.sendMessage(Main.PREFIX + "§cDu kannst dich nicht selber reporten!");
                    }
                } else {
                    sender.sendMessage(Main.PREFIX + "§cDieser Spieler ist nicht Online!");
                }
            } else
                sender.sendMessage(Main.PREFIX + "§cBenutze §6/report <Spieler> <Grund>§c!");
        } else
            sender.sendMessage(REPORT_PREFIX + "Dieser Befehl muss von einem Spieler ausgeführt werden.");
    }

    public static void removeReported(String uuid) {
        if (reported.containsKey(uuid)) {
            reported.remove(uuid);
        }
    }

}
