package de.groodian.hyperiorproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import de.groodian.hyperiorcore.command.HCommandVelocity;
import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.user.User;
import de.groodian.hyperiorproxy.main.Main;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class MaintenanceCommand extends HCommandVelocity<CommandSource> {

    private final Main plugin;

    public MaintenanceCommand(Main plugin) {
        super(CommandSource.class, "maintenance", "Change the maintenance mode", Main.PREFIX_COMPONENT, "maintenance", List.of(),
                List.of());
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

        plugin.setMaintenance(!plugin.isMaintenance());

        if (plugin.isMaintenance()) {
            for (Player player : plugin.getServer().getAllPlayers()) {
                User user = HyperiorCore.getVelocity().getUserManager().get(player.getUniqueId());
                if (user != null && !user.has("maintenance")) {
                    player.disconnect(Component.text("Der Server wird nun gewartet!", NamedTextColor.RED));
                }
            }
        }

        sendMsg(source, Component.text("Successfully changed the maintenance mode to: " + plugin.isMaintenance(), NamedTextColor.GREEN));
        plugin.getTeam().notify("§6" + sourceName + "§a changed the maintenance mode to: " + plugin.isMaintenance());
    }

}
