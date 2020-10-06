package de.groodian.hyperiorproxy.team;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorproxy.main.Main;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Team {

    private static final String TEAM_PREFIX = "§7[§bTeam§7] §r";

    private static Main plugin;
    private static ArrayList<String> onlineTeamMembers = new ArrayList<>();
    private static DateTimeFormatter dateFormatterTime = DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm:ss");
    private static FileWriter writer = loadFile();

    public static void init(Main plugin) {
        Team.plugin = plugin;
    }

    public static void userLogin(String uuid, String name) {
        if (HyperiorCore.getRanks().has(uuid, "team")) {
            onlineTeamMembers.add(name);
        }
    }

    public static void userLogout(String uuid, String name) {
        onlineTeamMembers.remove(name);
    }

    public static void notify(final String msg) {
        ProxyServer.getInstance().getScheduler().runAsync(plugin, () -> {
            try {
                writer.write("[" + dateFormatterTime.format(LocalDateTime.now()) + "] " + msg);
                writer.write(System.getProperty("line.separator"));
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        for (String name : onlineTeamMembers) {
            BungeeCord.getInstance().getPlayer(name).sendMessage(TextComponent.fromLegacyText(TEAM_PREFIX + msg));
        }

    }

    private static FileWriter loadFile() {
        try {
            return new FileWriter(new File("team.log"), true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
