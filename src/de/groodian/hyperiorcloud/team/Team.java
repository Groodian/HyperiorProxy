package de.groodian.hyperiorcloud.team;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import de.groodian.hyperiorcore.main.HyperiorCore;
import net.md_5.bungee.BungeeCord;

public class Team {

	private static final String TEAM_PREFIX = "§bTeam §7>> §r";

	private static ArrayList<String> onlineTeamMembers = new ArrayList<>();
	private static DateTimeFormatter dateFormatterTime = DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm:ss");
	private static FileWriter writer = loadFile();

	private static FileWriter loadFile() {
		try {
			return new FileWriter(new File("team.log"), true);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void userLogin(String uuid, String name) {
		if (HyperiorCore.getRanks().has(uuid, "team")) {
			onlineTeamMembers.add(name);
		}
	}

	public static void userLogout(String uuid, String name) {
		if (HyperiorCore.getRanks().has(uuid, "team")) {
			onlineTeamMembers.remove(name);
		}
	}

	@SuppressWarnings("deprecation")
	public static void notify(String msg) {
		try {
			writer.write("[" + dateFormatterTime.format(LocalDateTime.now()) + "] " + msg);
			writer.write(System.getProperty("line.separator"));
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String name : onlineTeamMembers) {
			BungeeCord.getInstance().getPlayer(name).sendMessage(TEAM_PREFIX + msg);
		}

	}

}
