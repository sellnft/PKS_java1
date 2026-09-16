package repository;

import model.Announcement;
import model.AnnouncementStatus;
import util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;


public class AnnouncementRepository {

    public void addAnnouncement(Announcement newAnnouncement) {
        String query = "INSERT into announcements (category, title, description, status, created_at, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newAnnouncement.category());
            stmt.setString(2, newAnnouncement.title());
            stmt.setString(3, newAnnouncement.description());
            stmt.setString(4, newAnnouncement.status().name());
            stmt.setObject(5, newAnnouncement.createdAt());
            stmt.setInt(6, newAnnouncement.userId());

            int affected = stmt.executeUpdate();

            if(affected > 0) {
                System.out.println("Заявка успешно добавлена!");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления заявки: " + e);
        }
    }
}
