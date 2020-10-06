package de.groodian.hyperiorproxy.data;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.util.MySQL;
import de.groodian.hyperiorproxy.main.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Ban {

    private static final String DAYS = "d", HOURS = "h", MINUTES = "m", SECONDS = "s";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm:ss");
    private static final MySQL dataMySQL = HyperiorCore.getMySQLManager().getDataMySQL();

    public static boolean hasActiveBan(String uuid) {
        uuid = uuid.replaceAll("-", "");
        if (isInDatabase(uuid)) {
            String ban = getString("ban", uuid);
            if (ban != null) {

                if (ban.equals("PERMANENT")) {
                    return true;
                }

                Duration duration = Duration.between(LocalDateTime.now(), LocalDateTime.parse(ban, dateTimeFormatter));

                if (duration.isZero()) {
                    return false;
                }

                if (duration.isNegative()) {
                    return false;
                }

                return true;

            }
        }

        return false;
    }

    public static String getDisconnectReason(String uuid) {
        uuid = uuid.replaceAll("-", "");
        String banTimeLeft = getBanTimeLeft(uuid);
        if (banTimeLeft != null) {

            return Main.DISCONNECT_HEADER +
                    "§cDu wurdest von diesem Netzwerk gebannt." +
                    "\n§cGrund: §e" + getString("reason", uuid) +
                    "\n§cDauer: §e" + banTimeLeft +
                    "\n" +
                    "\n§aDu kannst einen Entbannungsantrag an §eunban@hyperior.de §asenden.";

        }

        return null;
    }

    public static String getBanTimeLeft(String uuid) {
        uuid = uuid.replaceAll("-", "");
        if (hasActiveBan(uuid)) {
            String ban = getString("ban", uuid);

            if (ban.equals("PERMANENT")) {

                return "PERMANENT";

            } else {
                String timeLeft = null;
                Duration duration = Duration.between(LocalDateTime.now(), LocalDateTime.parse(ban, dateTimeFormatter));

                if (duration.toDays() > 1) {
                    timeLeft = duration.toDays() + " Tage";
                } else if (duration.toDays() == 1) {
                    timeLeft = duration.toDays() + " Tag";
                } else if (duration.toHours() > 1) {
                    timeLeft = duration.toHours() + " Stunden";
                } else if (duration.toHours() == 1) {
                    timeLeft = duration.toHours() + " Stunde";
                } else if (duration.toMinutes() > 1) {
                    timeLeft = duration.toMinutes() + " Minuten";
                } else if (duration.toMinutes() == 1) {
                    timeLeft = duration.toMinutes() + " Minute";
                } else if (duration.getSeconds() > 1 || duration.getSeconds() == 0) {
                    timeLeft = duration.getSeconds() + " Sekunden";
                } else if (duration.getSeconds() == 1) {
                    timeLeft = duration.getSeconds() + " Sekunde";
                }

                if (timeLeft != null) {

                    return timeLeft;

                }

            }
        }

        return null;
    }

    public static String getReason(String uuid) {
        return getString("reason", uuid);
    }

    public static String getHistory(String uuid) {
        return getString("history", uuid);
    }

    public static String getReportHistory(String uuid) {
        return getString("reporthistory", uuid);
    }

    public static Long getReports(String uuid) {
        return getLong("reports", uuid);
    }

    public static void ban(String targetUUID, String targetName, String executorName, int duration, String type, String reason) {
        targetUUID = targetUUID.replaceAll("-", "");
        if (isInDatabase(targetUUID)) {
            try {
                String history = getString("history", targetUUID);
                PreparedStatement ps = dataMySQL.getConnection().prepareStatement("UPDATE ban SET playername = ?, ban = ?, reason = ?, history = ? WHERE UUID = ?");
                ps.setString(1, targetName);
                if (type.equals(DAYS)) {
                    ps.setString(2, dateTimeFormatter.format(LocalDateTime.now().plusDays(duration)));
                    ps.setString(4, ((history == null) ? "" : history + "\n") + "(" + dateTimeFormatter.format(LocalDateTime.now()) + ") TEMPBAN von " + executorName + " für " + duration + "d. Grund: " + reason);
                }
                if (type.equals(HOURS)) {
                    ps.setString(2, dateTimeFormatter.format(LocalDateTime.now().plusHours(duration)));
                    ps.setString(4, ((history == null) ? "" : history + "\n") + "(" + dateTimeFormatter.format(LocalDateTime.now()) + ") TEMPBAN von " + executorName + " für " + duration + "h. Grund: " + reason);
                }
                if (type.equals(MINUTES)) {
                    ps.setString(2, dateTimeFormatter.format(LocalDateTime.now().plusMinutes(duration)));
                    ps.setString(4, ((history == null) ? "" : history + "\n") + "(" + dateTimeFormatter.format(LocalDateTime.now()) + ") TEMPBAN von " + executorName + " für " + duration + "m. Grund: " + reason);
                }
                if (type.equals(SECONDS)) {
                    ps.setString(2, dateTimeFormatter.format(LocalDateTime.now().plusSeconds(duration)));
                    ps.setString(4, ((history == null) ? "" : history + "\n") + "(" +dateTimeFormatter.format(LocalDateTime.now()) + ") TEMPBAN von " + executorName + " für " + duration + "s. Grund: " + reason);
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
                    ps.setString(3, dateTimeFormatter.format(LocalDateTime.now().plusDays(duration)));
                    ps.setString(5, "(" + dateTimeFormatter.format(LocalDateTime.now()) + ") TEMPBAN von " + executorName + " für " + duration + "d. Grund: " + reason);
                }
                if (type.equals(HOURS)) {
                    ps.setString(3, dateTimeFormatter.format(LocalDateTime.now().plusHours(duration)));
                    ps.setString(5, "(" + dateTimeFormatter.format(LocalDateTime.now()) + ") TEMPBAN von " + executorName + " für " + duration + "h. Grund: " + reason);
                }
                if (type.equals(MINUTES)) {
                    ps.setString(3, dateTimeFormatter.format(LocalDateTime.now().plusMinutes(duration)));
                    ps.setString(5, "(" + dateTimeFormatter.format(LocalDateTime.now()) + ") TEMPBAN von " + executorName + " für " + duration + "m. Grund: " + reason);
                }
                if (type.equals(SECONDS)) {
                    ps.setString(3, dateTimeFormatter.format(LocalDateTime.now().plusSeconds(duration)));
                    ps.setString(5, "(" + dateTimeFormatter.format(LocalDateTime.now()) + ") TEMPBAN von " + executorName + " für " + duration + "s. Grund: " + reason);
                }
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void pban(String targetUUID, String targetName, String executorName, String reason) {
        targetUUID = targetUUID.replaceAll("-", "");
        if (isInDatabase(targetUUID)) {
            try {
                String history = getString("history", targetUUID);
                PreparedStatement ps = dataMySQL.getConnection().prepareStatement("UPDATE ban SET playername = ?, ban = ?, reason = ?, history = ? WHERE UUID = ?");
                ps.setString(1, targetName);
                ps.setString(2, "PERMANENT");
                ps.setString(4, ((history == null) ? "" : history + "\n") + "(" + dateTimeFormatter.format(LocalDateTime.now()) + ") PBAN von " + executorName + ". Grund: " + reason);
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
                ps.setString(5, "(" + dateTimeFormatter.format(LocalDateTime.now()) + ") PBAN von " + executorName + ". Grund: " + reason);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void unban(String targetUUID, String targetName, String executorName, String reason) {
        targetUUID = targetUUID.replaceAll("-", "");
        if (isInDatabase(targetUUID)) {
            try {
                String history = getString("history", targetUUID);
                PreparedStatement ps = dataMySQL.getConnection().prepareStatement("UPDATE ban SET playername = ?, ban = ?, reason = ?, history = ? WHERE UUID = ?");
                ps.setString(1, targetName);
                ps.setString(2, null);
                ps.setString(4, ((history == null) ? "" : history + "\n") + "(" + dateTimeFormatter.format(LocalDateTime.now()) + ") UNBAN von " + executorName + ". Grund: " + reason);
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
        if (isInDatabase(targetUUID)) {
            try {
                String history = getString("history", targetUUID);
                PreparedStatement ps = dataMySQL.getConnection().prepareStatement("UPDATE ban SET playername = ?, reason = ?, history = ? WHERE UUID = ?");
                ps.setString(1, targetName);
                ps.setString(3, ((history == null) ? "" : history + "\n") + "(" + dateTimeFormatter.format(LocalDateTime.now()) + ") KICK von " + executorName + ". Grund: " + reason);
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
                ps.setString(4, "(" + dateTimeFormatter.format(LocalDateTime.now()) + ") KICK von " + executorName + ". Grund: " + reason);
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
        if (isInDatabase(targetUUID)) {
            try {
                String reportHistory = getString("reporthistory", targetUUID);
                PreparedStatement ps = dataMySQL.getConnection().prepareStatement("UPDATE ban SET playername = ?, reports = ?, reporthistory = ? WHERE UUID = ?");
                ps.setString(1, targetName);
                ps.setString(3, ((reportHistory == null) ? "" : reportHistory + "\n") + "(" + dateTimeFormatter.format(LocalDateTime.now()) + ") REPORT von " + executorName + ". Grund: " + reason);
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
                ps.setString(4, "(" + dateTimeFormatter.format(LocalDateTime.now()) + ") REPORT von " + executorName + ". Grund: " + reason);
                ps.setLong(3, 1);
                ps.setString(1, targetUUID);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isInDatabase(String uuid) {
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

    private static String getString(String data, String uuid) {
        uuid = uuid.replaceAll("-", "");
        if (isInDatabase(uuid)) {
            try {
                PreparedStatement ps = dataMySQL.getConnection().prepareStatement("SELECT " + data + " FROM ban WHERE UUID = ?");
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

    private static long getLong(String data, String uuid) {
        uuid = uuid.replaceAll("-", "");
        if (isInDatabase(uuid)) {
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
