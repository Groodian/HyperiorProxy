package de.groodian.hyperiorproxy.user;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.util.DatabaseConnection;
import java.sql.PreparedStatement;
import java.util.UUID;

public class Data {

    public void login(UUID uuid) {
        DatabaseConnection databaseConnection = HyperiorCore.getDatabaseManager().getConnection();

        try {
            PreparedStatement ps = databaseConnection.getPreparedStatement(
                    "UPDATE hyperior_mc.users SET logins = logins + 1, last_login = now(), login_days = login_days + EXTRACT(DAY FROM (now() - COALESCE(last_logout, now() - INTERVAL '1 DAY'))) WHERE uuid = ?");
            ps.setObject(1, uuid);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        databaseConnection.finish();
    }

    public void logout(UUID uuid) {
        DatabaseConnection databaseConnection = HyperiorCore.getDatabaseManager().getConnection();

        try {
            PreparedStatement ps = databaseConnection.getPreparedStatement(
                    "UPDATE hyperior_mc.users SET last_logout = now(), login_days = login_days + EXTRACT(DAY FROM (now() - last_login)), connection_time = connection_time + EXTRACT(EPOCH FROM (now() - last_login)) WHERE uuid = ?");
            ps.setObject(1, uuid);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        databaseConnection.finish();
    }

}
