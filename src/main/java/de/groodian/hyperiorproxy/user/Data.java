package de.groodian.hyperiorproxy.user;

import com.velocitypowered.api.proxy.Player;
import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.util.DatabaseConnection;
import java.sql.PreparedStatement;

public class Data {

    public void login(Player player) {
        DatabaseConnection databaseConnection = HyperiorCore.getVelocity().getDatabaseManager().getConnection();

        try {
            PreparedStatement ps = databaseConnection.getPreparedStatement(
                    "UPDATE hyperior_mc.users SET name = ?, logins = logins + 1, last_login = now(), login_days = login_days + DATE_PART('DAY', (now() - COALESCE(last_logout, now() - INTERVAL '1 DAY'))) WHERE uuid = ?");
            ps.setString(1, player.getUsername());
            ps.setObject(2, player.getUniqueId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        databaseConnection.finish();
    }

    public void logout(Player player) {
        DatabaseConnection databaseConnection = HyperiorCore.getVelocity().getDatabaseManager().getConnection();

        try {
            PreparedStatement ps = databaseConnection.getPreparedStatement(
                    "UPDATE hyperior_mc.users SET last_logout = now(), login_days = login_days + DATE_PART('DAY', (now() - last_login)), connection_time = connection_time + EXTRACT(EPOCH FROM (now() - last_login)) WHERE uuid = ?");
            ps.setObject(1, player.getUniqueId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        databaseConnection.finish();
    }

}
