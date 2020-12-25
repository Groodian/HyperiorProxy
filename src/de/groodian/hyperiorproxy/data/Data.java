package de.groodian.hyperiorproxy.data;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.util.MySQL;
import de.groodian.hyperiorcore.util.MySQLConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Data {

    private static DateTimeFormatter dateFormatterTime = DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm");
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static Map<String, Long> logins = new HashMap<>();
    private static MySQL dataMySQL = HyperiorCore.getMySQLManager().getDataMySQL();

    private static boolean hasData(String uuid) {
        uuid = uuid.replaceAll("-", "");
        try {
            MySQLConnection connection = dataMySQL.getMySQLConnection();
            PreparedStatement ps = connection.getConnection().prepareStatement("SELECT playername FROM data WHERE UUID = ?");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            boolean hasData = rs.next();
            ps.close();
            connection.finish();
            return hasData;
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
                String lastLogin = getString("lastlogin", uuid);
                long logins = getLong("logins", uuid);
                long loginDays = getLong("logindays", uuid);

                MySQLConnection connection = dataMySQL.getMySQLConnection();
                PreparedStatement ps = connection.getConnection().prepareStatement("UPDATE data SET playername = ?, logins = ?, lastlogin = ?, lastip = ?, logindays = ? WHERE UUID = ?");
                ps.setString(1, name);
                ps.setLong(2, logins + 1);
                ps.setString(3, dateFormatterTime.format(LocalDateTime.now()));
                ps.setString(4, address);
                if (!lastLogin.contains((dateFormatter.format(LocalDate.now()))))
                    ps.setLong(5, loginDays + 1);
                else
                    ps.setLong(5, loginDays);
                ps.setString(6, uuid);
                ps.executeUpdate();
                ps.close();
                connection.finish();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                logins.put(uuid, System.currentTimeMillis());

                MySQLConnection connection = dataMySQL.getMySQLConnection();
                PreparedStatement ps = connection.getConnection().prepareStatement("INSERT INTO data (UUID, playername, logins, firstlogin, lastlogin, lastip, logindays) VALUES(?,?,?,?,?,?,?)");
                ps.setString(1, uuid);
                ps.setString(2, name);
                ps.setLong(3, 1);
                ps.setString(4, dateFormatterTime.format(LocalDateTime.now()));
                ps.setString(5, dateFormatterTime.format(LocalDateTime.now()));
                ps.setString(6, address);
                ps.setLong(7, 1);
                ps.executeUpdate();
                ps.close();
                connection.finish();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void logout(String uuid, String address) {
        uuid = uuid.replaceAll("-", "");
        if (hasData(uuid)) {
            try {
                String lastLogin = getString("lastlogin", uuid);
                long loginDays = getLong("logindays", uuid);
                long connectionTime = getLong("connectiontime", uuid);

                long loginTime = logins.get(uuid);
                logins.remove(uuid);

                MySQLConnection connection = dataMySQL.getMySQLConnection();
                PreparedStatement ps = connection.getConnection().prepareStatement("UPDATE data SET lastip = ?, lastlogout = ?, logindays = ?, connectiontime = ? WHERE UUID = ?");
                ps.setString(1, address);
                ps.setString(2, dateFormatterTime.format(LocalDateTime.now()));
                if (!lastLogin.contains((dateFormatter.format(LocalDate.now()))))
                    ps.setLong(3, loginDays + 1);
                else
                    ps.setLong(3, loginDays);
                ps.setLong(4, connectionTime + ((System.currentTimeMillis() - loginTime) / 1000 / 60));
                ps.setString(5, uuid);
                ps.executeUpdate();
                ps.close();
                connection.finish();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getString(String data, String uuid) {
        uuid = uuid.replaceAll("-", "");
        if (hasData(uuid)) {
            try {
                MySQLConnection connection = dataMySQL.getMySQLConnection();
                PreparedStatement ps = connection.getConnection().prepareStatement("SELECT " + data + " FROM data WHERE UUID = ?");
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                String returnString = null;
                if (rs.next()) {
                    returnString = rs.getString(data);
                }
                ps.close();
                connection.finish();
                return returnString;
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
                MySQLConnection connection = dataMySQL.getMySQLConnection();
                PreparedStatement ps = connection.getConnection().prepareStatement("SELECT " + data + " FROM data WHERE UUID = ?");
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                long returnLong = 0;
                if (rs.next()) {
                    returnLong = rs.getLong(data);
                }
                ps.close();
                connection.finish();
                return returnLong;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

}
