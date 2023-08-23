package de.groodian.hyperiorproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import de.groodian.hyperiorcore.command.HCommandVelocity;
import de.groodian.hyperiorproxy.main.Main;
import java.time.Duration;
import java.util.List;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ShopCommand extends HCommandVelocity<CommandSource> {

    private static final long DELAY = 900;
    private static final List<String> messages = List.of("§7[§bHyperior.de§7] §6Informationen zum §eVIP-Rang§6:",
            "§7Der §eVIP-Rang §7ist §6Lifetime §7nur während der §cBeta-Phase §7erhältlich.",
            "§7Du kannst §avolle Server betreten §7und damit bevorzugt spielen.",
            "§7Dein Name hat auf dem Server eine §eGelbe Farbe§7.",
            "§7Du hast auf dem Server einen §eexklusiven 'VIP' Präfix§7 vor deinem Namen.",
            "§7Du kannst dir bei der §eTäglichen Belohnung §7die §edreifache Anzahl an §6Coins §7abhohlen.",
            "§7Du erhältst am Ende einer Runde §edoppelte §6Coins§7.",
            "§7Du kannst eine Runde mit §a/start direkt Starten§7.",
            "§7Du kannst dir §aalle Gegenstände kaufen§7, so brauchst du nicht das Glück diese in einer Kiste zu bekommen.",
            "§aMit dem Kauf unterstützt du den Server, und trägst bei dass neue Updates kommen und der Server weiterhin Online bleibt.",
            "§7Für weitere Informationen kannst du dich an das Server-Team wenden.");

    private final Main plugin;

    public ShopCommand(Main plugin) {
        super(CommandSource.class, "shop", "The shop", Main.PREFIX_COMPONENT, null, List.of(), List.of());
        this.plugin = plugin;
    }

    @Override
    protected void onCall(CommandSource source, String[] args) {
        int count = 0;
        for (String message : messages) {
            plugin.getServer().getScheduler().buildTask(plugin, () -> {
                source.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
            }).delay(Duration.ofMillis(DELAY * count)).schedule();

            count++;
        }
    }

}
