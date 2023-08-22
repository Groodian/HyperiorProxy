package de.groodian.hyperiorproxy.commands;
/*
import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorproxy.main.Main;
import de.groodian.hyperiorproxy.team.Team;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class SlotsCommand extends Command {

    private Main plugin;

    public SlotsCommand(Main plugin) {
        super("slots");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer || sender instanceof ConsoleCommandSender) {
            if (sender instanceof ProxiedPlayer) {
                if (!HyperiorCore.getRanks().has(((ProxiedPlayer) sender).getUniqueId(), "slots")) {
                    return;
                }
            }
            if (args.length == 1) {
                if (args[0].chars().allMatch(Character::isDigit)) {
                    int slots = Integer.parseInt(args[0]);
                    plugin.setSlots(slots);
                    sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§aSlots erfolgreich geändert."));
                    Team.notify("§6" + sender.getName() + "§a hat die Slots auf §6" + slots + "§a gesetzt!");
                } else
                    sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§cDie Slots müssen eine Zahl sein!"));
            } else
                sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§cBenutze §6/slots <Slots>§c!"));
        } else
            sender.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "Dieser Befehl muss von einem Spieler oder der Konsole ausgeführt werden."));
    }

}
*/