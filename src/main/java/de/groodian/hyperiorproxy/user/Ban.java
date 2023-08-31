package de.groodian.hyperiorproxy.user;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.util.DatabaseConnection;
import de.groodian.hyperiorcore.util.Time;
import de.groodian.hyperiorproxy.main.Main;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.postgresql.util.PGInterval;

public class Ban {

    public UserHistory loadUserHistory(UUID userHistoryId) {
        if (userHistoryId == null) {
            return null;
        }

        UserHistory userHistory = null;

        DatabaseConnection databaseConnection = HyperiorCore.getVelocity().getDatabaseManager().getConnection();

        try {
            PreparedStatement ps = databaseConnection.getPreparedStatement(
                    "SELECT id, target, created_by, type, reason, created_at, duration FROM hyperior_mc.users_ban_history WHERE id = ?");
            ps.setObject(1, userHistoryId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                userHistory = userHistoryFromResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        databaseConnection.finish();

        return userHistory;
    }

    public String getDisconnectReason(UserHistory userHistory) {
        String banTimeLeft = getBanTimeLeft(userHistory);
        if (banTimeLeft != null) {

            return Main.DISCONNECT_HEADER +
                   "§cDu wurdest von diesem Netzwerk gebannt." +
                   "\n§cGrund: §e" + userHistory.getReason() +
                   "\n§cDauer: §e" + banTimeLeft +
                   "\n" +
                   "\n§aDu kannst einen Entbannungsantrag an §eunban@hyperior.de §asenden.";

        }

        return null;
    }

    public String getBanTimeLeft(UserHistory userHistory) {
        if (userHistory == null) {
            return null;
        }

        switch (userHistory.getType()) {
            case BAN -> {
                Date date = new Date();
                date.setTime(0);
                userHistory.getDuration().add(date);
                Duration duration = Duration.ofMillis(date.getTime());

                OffsetDateTime userHistoryCreatedAt = OffsetDateTime.of(userHistory.getCreatedAt().toLocalDateTime(),
                        userHistory.getCreatedAt().getOffset());

                String timeLeftString = Time.timeLeftString(OffsetDateTime.now(), userHistoryCreatedAt.plus(duration));
                if (!timeLeftString.equals("")) {
                    return timeLeftString;
                }
            }

            case PERMANENT_BAN -> {
                return "PERMANENT";
            }

            case KICK, UNBAN -> {
                return null;
            }
        }

        return null;
    }

    public boolean ban(String name, UserHistory userHistory) {
        HyperiorCore.getVelocity().getUserManager().getOrCreateUser(userHistory.getTarget(), name);

        DatabaseConnection databaseConnection = HyperiorCore.getVelocity().getDatabaseManager().getConnection();

        try {
            databaseConnection.getConnection().setAutoCommit(false);

            PreparedStatement ps1 = databaseConnection.getPreparedStatement(
                    "INSERT INTO hyperior_mc.users_ban_history (id, target, created_by, type, reason, created_at, duration) VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps1.setObject(1, userHistory.getId());
            ps1.setObject(2, userHistory.getTarget());
            ps1.setObject(3, userHistory.getCreatedBy());
            ps1.setInt(4, userHistory.getType().getTypeId());
            ps1.setString(5, userHistory.getReason());
            ps1.setObject(6, userHistory.getCreatedAt());
            ps1.setObject(7, userHistory.getDuration());
            ps1.executeUpdate();

            PreparedStatement ps2 = databaseConnection.getPreparedStatement("UPDATE hyperior_mc.users SET ban = ? WHERE uuid = ?");
            ps2.setObject(1, userHistory.getId());
            ps2.setObject(2, userHistory.getTarget());
            ps2.executeUpdate();

            databaseConnection.getConnection().commit();

            databaseConnection.getConnection().setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
            databaseConnection.finish();
            return false;
        }

        databaseConnection.finish();

        return true;
    }

    public List<UserHistory> loadCompleteUserHistory(UUID uuid) {
        List<UserHistory> completeUserHistory = new ArrayList<>();

        DatabaseConnection databaseConnection = HyperiorCore.getVelocity().getDatabaseManager().getConnection();

        try {
            PreparedStatement ps = databaseConnection.getPreparedStatement(
                    "SELECT id, target, created_by, type, reason, created_at, duration FROM hyperior_mc.users_ban_history WHERE target = ? ORDER BY created_at ASC");
            ps.setObject(1, uuid);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                completeUserHistory.add(userHistoryFromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        databaseConnection.finish();

        return completeUserHistory;
    }

    private UserHistory userHistoryFromResultSet(ResultSet rs) throws Exception {
        UserHistoryType userHistoryType = null;
        int userHistoryTypeId = rs.getInt("type");

        for (UserHistoryType current : UserHistoryType.values()) {
            if (current.getTypeId() == userHistoryTypeId) {
                userHistoryType = current;
                break;
            }
        }

        if (userHistoryType == null) {
            throw new Exception("Unknown user history type id: " + userHistoryTypeId);
        }

        return new UserHistory(rs.getObject("id", UUID.class),
                rs.getObject("target", UUID.class),
                rs.getObject("created_by", UUID.class),
                userHistoryType,
                rs.getString("reason"),
                rs.getObject("created_at", OffsetDateTime.class),
                rs.getObject("duration", PGInterval.class)
        );
    }

}
