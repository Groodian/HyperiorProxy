package de.groodian.hyperiorproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import de.groodian.hyperiorcore.command.HArgument;
import de.groodian.hyperiorcore.command.HCommandVelocity;
import de.groodian.hyperiorcore.command.HTabCompleteType;
import de.groodian.hyperiorproxy.main.Main;
import java.util.List;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class MotdCommand extends HCommandVelocity<CommandSource> {

    private final Main plugin;

    public MotdCommand(Main plugin) {
        super(CommandSource.class, "motd", "Change the motd", Main.PREFIX_COMPONENT, "motd", List.of(),
                List.of(new HArgument("motd", true, HTabCompleteType.NONE, false)));
        this.plugin = plugin;
    }

    @Override
    protected void onCall(CommandSource source, String[] args) {
        String sourceName;
        if (source instanceof Player sourcePlayer) {
            sourceName = sourcePlayer.getUsername();
        } else {
            sourceName = "Console";
        }

        plugin.setMotdSecondLine(LegacyComponentSerializer.legacyAmpersand().deserialize(args[0]));
        sendMsg(source, LegacyComponentSerializer.legacyAmpersand().deserialize("&aSuccessfully changed the motd to:\n" + args[0]));
        plugin.getTeam()
                .notify(LegacyComponentSerializer.legacyAmpersand().deserialize("&6" + sourceName + "&a changed the motd to:\n" + args[0]));
    }

}
