package de.groodian.hyperiorproxy.commands;

import de.groodian.hyperiorproxy.main.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.concurrent.TimeUnit;

public class ShopCommand extends Command {

    private Main plugin;
    private long delay = 900;

    public ShopCommand(Main plugin) {
        super("shop");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(new TextComponent("§7[§bHyperior.de§7] §6Informationen zum §eVIP-Rang§6:"));
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
            sender.sendMessage(new TextComponent("§8§ §7Der §eVIP-Rang §7ist §6Lifetime §7nur während der §cBeta-Phase §7erhältlich."));
            ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
                sender.sendMessage(new TextComponent("§8§ §7Du kannst §avolle Server beitreten §7und damit bevorzugt spielen."));
                ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
                    sender.sendMessage(new TextComponent("§8§ §7Dein Name hat auf dem Server eine §eGelbe Farbe§7."));
                    ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
                        sender.sendMessage(new TextComponent("§8§ §7Du hast auf dem Server einen §eexklusiven 'VIP' Präfix§7 vor deinem Namen."));
                        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
                            sender.sendMessage(new TextComponent("§8§ §7Du kannst dir bei der §Täglichen Belohnung§ die §edreifache Anzahl an §6Coins §7abhohlen."));
                            ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
                                sender.sendMessage(new TextComponent("§8§ §7Du erh§lst am Ende einer Runde §edoppelte §6Coins§7."));
                                ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
                                    sender.sendMessage(new TextComponent("§8§ §7Du kannst eine Runde mit /start §adirekt Starten§7."));
                                    ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
                                        sender.sendMessage(new TextComponent("§8§ §7Du kannst dir §aalle Gegenstände kaufen§7 so brauchst du nicht das Glück diese ein einer Kiste zu bekommen."));
                                        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
                                            sender.sendMessage(new TextComponent("§8§ §aMit dem Kauf unterstützt du den Server, und trägst bei das neue Updates kommen und der Server weiterhin Online bleibt."));
                                            ProxyServer.getInstance().getScheduler().schedule(plugin, () -> sender.sendMessage(new TextComponent("§8§ §7Für weitere Informationen kannst du dich an das Server-Team wenden.")), delay, TimeUnit.MILLISECONDS);
                                        }, delay, TimeUnit.MILLISECONDS);
                                    }, delay, TimeUnit.MILLISECONDS);
                                }, delay, TimeUnit.MILLISECONDS);
                            }, delay, TimeUnit.MILLISECONDS);
                        }, delay, TimeUnit.MILLISECONDS);
                    }, delay, TimeUnit.MILLISECONDS);
                }, delay, TimeUnit.MILLISECONDS);
            }, delay, TimeUnit.MILLISECONDS);
        }, delay, TimeUnit.MILLISECONDS);
    }
}
