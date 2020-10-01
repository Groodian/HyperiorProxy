package de.groodian.hyperiorproxy.data;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.util.MySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Ban {

    private static final String DAYS = "d", HOURS = "h", MINUTES = "m", SECONDS = "s";
    private static DateTimeFormatter dateFormatterTime = DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm:ss");
    private static MySQL dataMySQL = HyperiorCore.getMySQLManager().getDataMySQL();

    public static boolean hasBan(String uuid) {
        uuid = uuid.replaceAll("-", "");
        try {
            PreparedStatement ps = dataMySQL.getConnection().prepareStatement("SELECT playername FROM ban WHERE UUID = ?");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void ban(String targetUUID, String targetName, String executorName, int duration, String type, String reason) {
        targetUUID = targetUUID.replaceAll("-", "");
        if (hasBan(targetUUID)) {
            try {
                PreparedStatement ps = dataMySQL.getConnection().prepareStatement("UPDATE ban SET playername = ?, ban = ?, reason = ?, history = ? WHERE UUID = ?");
                ps.setString(1, targetName);
                if (type.equals(DAYS)) {
                    ps.setString(2, dateFormatterTime.format(LocalDateTime.now().plusDays(duration)));
                    ps.setString(4, getString("history", targetUUID) + "\n(" + dateFormatterTime.format(LocalDateTime.now()) + ") TEMPBAN von " + executorName + " f§r " + duration + "d. Grund: " + reason);
                }
                if (type.equals(HOURS)) {
                    ps.setString(2, dateFormatterTime.format(LocalDateTime.now().plusHours(duration)));
                    ps.setString(4, getString("history", targetUUID) + "\n(" + dateFormatterTime.format(LocalDateTime.now()) + ") TEMPBAN von " + executorName + " f§r " + duration + "h. Grund: " + reason);
                }
                if (type.equals(MINUTES)) {
                    ps.setString(2, dateFormatterTime.format(LocalDateTime.now().plusMinutes(duration)));
                    ps.setString(4, getString("history", targetUUID) + "\n(" + dateFormatterTime.format(LocalDateTime.now()) + ") TEMPBAN von " + executorName + " f§r " + duration + "m. Grund: " + reason);
                }
                if (type.equals(SECONDS)) {
                    ps.setString(2, dateFormatterTime.format(LocalDateTime.now().plusSeconds(duration)));
                    ps.setString(4, getString("history", targetUUID) + "\n(" + dateFormatterTime.format(LocalDateTime.now()) + ") TEMPBAN von " + executorName + " f§r " + duration + "s. Grund: " + reason);
                }
                ps.setString(3, reason);
                ps.setString(5, targetUUID);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                PreparedStatement ps = dataMySQL.getConnection().prepareStatement("INSERT INTO ban (UUID, playername, ban, reason, history) VALUES(?,?,?,?,?)");
                ps.setString(1, targetUUID);
                ps.setString(2, targetName);
                ps.setString(4, reason);
                if (type.equals(DAYS)) {
                    ps.setString(3, dateFormatterTime.format(LocalDateTime.now().plusDays(duration)));
                    ps.setString(5, "(" + dateFormatterTime.format(LocalDateTime.now()) + ") TEMPBAN von " + executorName + " f§r " + duration + "d. Grund: " + reason);
                }
                if (type.equals(HOURS)) {
                    ps.setString(3, dateFormatterTime.format(LocalDateTime.now().plusHours(duration)));
                    ps.setString(5, "(" + dateFormatterTime.format(LocalDateTime.now()) + ") TEMPBAN von " + executorName + " f§r " + duration + "h. Grund: " + reason);
                }
                if (type.equals(MINUTES)) {
                    ps.setString(3, dateFormatterTime.format(LocalDateTime.now().plusMinutes(duration)));
                    ps.setString(5, "(" + dateFormatterTime.format(LocalDateTime.now()) + ") TEMPBAN von " + executorName + " f§r " + duration + "m. Grund: " + reason);
                }
                if (type.equals(SECONDS)) {
                    ps.setString(3, dateFormatterTime.format(LocalDateTime.now().plusSeconds(duration)));
                    ps.setString(5, "(" + dateFormatterTime.format(LocalDateTime.now()) + ") TEMPBAN von " + executorName + " f§r " + duration + "s. Grund: " + reason);
                }
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void pban(String targetUUID, String targetName, String executorName, String reason) {
        targetUUID = targetUUID.replaceAll("-", "");
        if (hasBan(targetUUID)) {
            try {
                PreparedStatement ps = dataMySQL.getConnection().prepareStatement("UPDATE ban SET playername = ?, ban = ?, reason = ?, history = ? WHERE UUID = ?");
                ps.setString(1, targetName);
                ps.setString(2, "PERMANENT");
                ps.setString(4, getString("history", targetUUID) + "\n(" + dateFormatterTime.format(LocalDateTime.now()) + ") PBAN von " + executorName + ". Grund: " + reason);
                ps.setString(3, reason);
                ps.setString(5, targetUUID);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                PreparedStatement ps = dataMySQL.getConnection().prepareStatement("INSERT INTO ban (UUID, playername, ban, reason, history) VALUES(?,?,?,?,?)");
                ps.setString(1, targetUUID);
                ps.setString(2, targetName);
                ps.setString(4, reason);
                ps.setString(3, "PERMANENT");
                ps.setString(5, "(" + dateFormatterTime.format(LocalDateTime.now()) + ") PBAN von " + executorName + ". Grund: " + reason);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void unban(String targetUUID, String targetName, String executorName, String reason) {
        targetUUID = targetUUID.replaceAll("-", "");
        if (hasBan(targetUUID)) {
            try {
                PreparedStatement ps = dataMySQL.getConnection().prepareStatement("UPDATE ban SET playername = ?, ban = ?, reason = ?, history = ? WHERE UUID = ?");
                ps.setString(1, targetName);
                ps.setString(2, null);
                ps.setString(4, getString("history", targetUUID) + "\n(" + dateFormatterTime.format(LocalDateTime.now()) + ") UNBAN von " + executorName + ". Grund: " + reason);
                ps.setString(3, reason);
                ps.setString(5, targetUUID);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void kick(String targetUUID, String targetName, String executorName, String reason) {
        targetUUID = targetUUID.replaceAll("-", "");
        if (hasBan(targetUUID)) {
            try {
                PreparedStatement ps = dataMySQL.getConnection().prepareStatement("UPDATE ban SET playername = ?, reason = ?, history = ? WHERE UUID = ?");
                ps.setString(1, targetName);
                ps.setString(3, getString("history", targetUUID) + "\n(" + dateFormatterTime.format(LocalDateTime.now()) + ") KICK von " + executorName + ". Grund: " + reason);
                ps.setString(2, reason);
                ps.setString(4, targetUUID);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                PreparedStatement ps = dataMySQL.getConnection().prepareStatement("INSERT INTO ban (UUID, playername, reason, history) VALUES(?,?,?,?)");
                ps.setString(2, targetName);
                ps.setString(4, "(" + dateFormatterTime.format(LocalDateTime.now()) + ") KICK von " + executorName + ". Grund: " + reason);
                ps.setString(3, reason);
                ps.setString(1, targetUUID);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void report(String targetUUID, String targetName, String executorName, String reason) {
        targetUUID = targetUUID.replaceAll("-", "");
        if (hasBan(targetUUID)) {
            try {
                PreparedStatement ps = dataMySQL.getConnection().prepareStatement("UPDATE ban SET playername = ?, reports = ?, reporthistory = ? WHERE UUID = ?");
                ps.setString(1, targetName);
                ps.setString(3, getString("reporthistory", targetUUID) + "\n(" + dateFormatterTime.format(LocalDateTime.now()) + ") REPORT von " + executorName + ". Grund: " + reason);
                ps.setLong(2, getLong("reports", targetUUID) + 1);
                ps.setString(4, targetUUID);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                PreparedStatement ps = dataMySQL.getConnection().prepareStatement("INSERT INTO ban (UUID, playername, reports, reporthistory) VALUES(?,?,?,?)");
                ps.setString(2, targetName);
                ps.setString(4, "(" + dateFormatterTime.format(LocalDateTime.now()) + ") REPORT von " + executorName + ". Grund: " + reason);
                ps.setLong(3, 1);
                ps.setString(1, targetUUID);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getString(String data, String uuid) {
        uuid = uuid.replaceAll("-", "");
        if (hasBan(uuid)) {
            try {
                PreparedStatement ps = dataMySQL.getConnection().prepareStatement("SELECT " + data + " FROM ban WHERE UUID = ?");
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String tempData = rs.getString(data);
                    if (tempData == null)
                        return "";
                    else
                        return tempData;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static long getLong(String data, String uuid) {
        uuid = uuid.replaceAll("-", "");
        if (hasBan(uuid)) {
            try {
                PreparedStatement ps = dataMySQL.getConnection().prepareStatement("SELECT " + data + " FROM ban WHERE UUID = ?");
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
