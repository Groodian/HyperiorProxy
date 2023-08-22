package de.groodian.hyperiorproxy.team;

import com.velocitypowered.api.proxy.Player;
import de.groodian.hyperiorcore.user.User;
import de.groodian.hyperiorproxy.main.Main;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class Team {

    private static final String TEAM_PREFIX = "§7[§bTeam§7] §r";

    private final Main plugin;
    private final List<UUID> players;

    public Team(Main plugin) {
        this.plugin = plugin;
        this.players = new ArrayList<>();
    }

    public void userLogin(User user) {
        if (user.has("team")) {
            players.add(user.getUuid());
        }
    }

    public void userLogout(Player player) {
        players.remove(player.getUniqueId());
    }

    public void notify(String msg) {
        for (UUID playerUUID : players) {
            Optional<Player> player = plugin.getServer().getPlayer(playerUUID);
            player.ifPresent(value -> value.sendMessage(LegacyComponentSerializer.legacySection().deserialize(TEAM_PREFIX + msg)));
        }
    }

}
