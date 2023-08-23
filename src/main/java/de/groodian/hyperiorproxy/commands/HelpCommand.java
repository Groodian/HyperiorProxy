package de.groodian.hyperiorproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import de.groodian.hyperiorcore.command.HCommandVelocity;
import de.groodian.hyperiorproxy.main.Main;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class HelpCommand extends HCommandVelocity<CommandSource> {

    public HelpCommand() {
        super(CommandSource.class, "help", "Help for the server", Main.PREFIX_COMPONENT, null, List.of(), List.of());
    }

    @Override
    protected void onCall(CommandSource source, String[] args) {
        Component component = LegacyComponentSerializer.legacySection()
                .deserialize("""
                             §7[§bHyperior.de§7] §6Informationen zum Hyperior-Netzwerk:
                             §e/lobby §7Kehre zu einer Lobby zurück
                             §e/party §7Spiele mit Freunden in einer Party
                             §e/friend §7Verwalte deine Freunde
                             §e/report §7Melde einen Spieler
                             §e/shop §7Informationen über Ränge und mehr
                             §7Für weitere Informationen kannst du dich an das Server-Team wenden.
                             """);

        source.sendMessage(component);
    }

}
