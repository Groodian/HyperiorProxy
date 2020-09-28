package de.groodian.hyperiorcloud.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.util.MySQL;

public class Data {

	private static DateTimeFormatter dateFormatterTime = DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm");
	private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private static Map<String, Long> logins = new HashMap<>();
	private static MySQL dataMySQL = HyperiorCore.getMySQLManager().getDataMySQL();

	private static boolean hasData(String uuid) {
		uuid = uuid.replaceAll("-", "");
		try {
			PreparedStatement ps = dataMySQL.getConnection().prepareStatement("SELECT playername FROM data WHERE UUID = ?");
			ps.setString(1, uuid);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void login(String uuid, String name, String address) {
		uuid = uuid.replaceAll("-", "");
		if (hasData(uuid)) {
			try {
				logins.put(uuid, System.currentTimeMillis());
				PreparedStatement ps = dataMySQL.getConnection().prepareStatement("UPDATE data SET playername = ?, logins = ?, lastlogin = ?, lastip = ?, logindays = ? WHERE UUID = ?");
				ps.setString(1, name);
				ps.setLong(2, getLong("logins", uuid) + 1);
				ps.setString(3, dateFormatterTime.format(LocalDateTime.now()));
				ps.setString(4, address);
				System.out.println(getString("lastlogin", uuid) + " " + dateFormatter.format(LocalDate.now()));
				if (!getString("lastlogin", uuid).contains((dateFormatter.format(LocalDate.now()))))
					ps.setLong(5, getLong("logindays", uuid) + 1);
				else
					ps.setLong(5, getLong("logindays", uuid));
				ps.setString(6, uuid);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				logins.put(uuid, System.currentTimeMillis());
				PreparedStatement ps = dataMySQL.getConnection().prepareStatement("INSERT INTO data (UUID, playername, logins, firstlogin, lastlogin, lastip, logindays) VALUES(?,?,?,?,?,?,?)");
				ps.setString(1, uuid);
				ps.setString(2, name);
				ps.setLong(3, 1);
				ps.setString(4, dateFormatterTime.format(LocalDateTime.now()));
				ps.setString(5, dateFormatterTime.format(LocalDateTime.now()));
				ps.setString(6, address);
				ps.setLong(7, 1);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void logout(String uuid, String address) {
		uuid = uuid.replaceAll("-", "");
		if (hasData(uuid)) {
			try {
				PreparedStatement ps = dataMySQL.getConnection().prepareStatement("UPDATE data SET lastip = ?, lastlogout = ?, logindays = ?, connectiontime = ? WHERE UUID = ?");
				ps.setString(1, address);
				ps.setString(2, dateFormatterTime.format(LocalDateTime.now()));
				System.out.println(getString("lastlogin", uuid) + " " + dateFormatter.format(LocalDate.now()));
				if (!getString("lastlogin", uuid).contains((dateFormatter.format(LocalDate.now()))))
					ps.setLong(3, getLong("logindays", uuid) + 1);
				else
					ps.setLong(3, getLong("logindays", uuid));
				ps.setLong(4, getLong("connectiontime", uuid) + ((System.currentTimeMillis() - logins.get(uuid)) / 1000 / 60));
				ps.setString(5, uuid);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getString(String data, String uuid) {
		uuid = uuid.replaceAll("-", "");
		if (hasData(uuid)) {
			try {
				PreparedStatement ps = dataMySQL.getConnection().prepareStatement("SELECT " + data + " FROM data WHERE UUID = ?");
				ps.setString(1, uuid);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					return rs.getString(data);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static long getLong(String data, String uuid) {
		uuid = uuid.replaceAll("-", "");
		if (hasData(uuid)) {
			try {
				PreparedStatement ps = dataMySQL.getConnection().prepareStatement("SELECT " + data + " FROM data WHERE UUID = ?");
				ps.setString(1, uuid);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					return rs.getLong(data);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

}
