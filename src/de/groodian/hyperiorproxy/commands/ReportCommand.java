package de.groodian.hyperiorproxy.commands;

import de.groodian.hyperiorproxy.data.Ban;
import de.groodian.hyperiorproxy.main.Main;
import de.groodian.hyperiorproxy.team.Team;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportCommand extends Command {

    private static final String PREFIX = "§7[§bReport§7] §r";

    private Main plugin;
    private Map<String, List<String>> reported;

    public ReportCommand(Main plugin) {
        super("report");
        this.plugin = plugin;
        reported = new HashMap<>();
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (sender instanceof ProxiedPlayer) {
            if (args.length >= 2) {
                final ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);
                final ProxiedPlayer player = (ProxiedPlayer) sender;
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    if (stringBuilder.length() == 0)
                        stringBuilder.append(args[i]);
                    else
                        stringBuilder.append(" ").append(args[i]);
                }
                if (target != null) {
                    if (target != sender) {
                        final String reason = stringBuilder.toString();
                        sender.sendMessage(TextComponent.fromLegacyText(PREFIX + "§7Bitte warte, dies kann einen moment dauern."));

                        ProxyServer.getInstance().getScheduler().runAsync(plugin, () -> {

                            if (!reported.containsKey(target.getUniqueId().toString())) {
                                List<String> tempList = new ArrayList<>();
                                tempList.add(player.getUniqueId().toString());
                                reported.put(target.getUniqueId().toString(), tempList);
                                Ban.report(target.getUniqueId().toString().replaceAll("-", ""), target.getName(), sender.getName(), reason);
                                sender.sendMessage(TextComponent.fromLegacyText(PREFIX + "§aDu hast §6" + target.getName() + " §aerfolgreich reportet."));
                                Team.notify("§6" + sender.getName() + "§a hat §6" + target.getName() + " §areportet. Grund: §6" + reason + " §aServer: §6" + target.getServer().getInfo().getName());
                            } else {
                                if (!reported.get(target.getUniqueId().toString()).contains(player.getUniqueId().toString())) {
                                    List<String> tempList = reported.get(target.getUniqueId().toString());
                                    tempList.add(player.getUniqueId().toString());
                                    reported.put(target.getUniqueId().toString(), tempList);
                                    Ban.report(target.getUniqueId().toString().replaceAll("-", ""), target.getName(), sender.getName(), reason);
                                    sender.sendMessage(TextComponent.fromLegacyText(PREFIX + "§aDu hast §6" + target.getName() + " §aerfolgreich reportet."));
                                    Team.notify("§6" + sender.getName() + "§a hat §6" + target.getName() + " §areportet. Grund: §6" + reason + " §aServer: §6" + target.getServer().getInfo().getName());
                                } else {
                                    sender.sendMessage(TextComponent.fromLegacyText(PREFIX + "§cDu hast diesen Spieler bereits reportet!"));
                                }
                            }

                        });

                    } else {
                        sender.sendMessage(TextComponent.fromLegacyText(PREFIX + "§cDu kannst dich nicht selber reporten!"));
                    }
                } else {
                    sender.sendMessage(TextComponent.fromLegacyText(PREFIX + "§cDieser Spieler ist nicht Online!"));
                }
            } else
                sender.sendMessage(TextComponent.fromLegacyText(PREFIX + "§cBenutze §6/report <Spieler> <Grund>§c!"));
        } else
            sender.sendMessage(TextComponent.fromLegacyText(PREFIX + "Dieser Befehl muss von einem Spieler ausgeführt werden."));
    }

    public void removeReported(String uuid) {
        reported.remove(uuid);
    }

}
