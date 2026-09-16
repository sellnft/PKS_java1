package repository;

import model.Announcement;
import model.AnnouncementStatus;
import util.DatabaseManager;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;


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

    public List<Announcement> findAll() {
        List<Announcement> allAnnouncements = new ArrayList<Announcement>();
        String query = "SELECT id, category, title, description, status," +
                "created_at, updated_at, comment, employee_id, user_id FROM announcements";

        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            try(ResultSet resultSet = stmt.executeQuery()) {
                while(resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String category = resultSet.getString("category");
                    String title = resultSet.getString("title");
                    String description = resultSet.getString("description");
                    AnnouncementStatus status = AnnouncementStatus.valueOf(resultSet.getString("status"));
                    OffsetDateTime createdAt = resultSet.getObject("created_at", OffsetDateTime.class);
                    OffsetDateTime updatedAt = resultSet.getObject("updated_at", OffsetDateTime.class);
                    String comment = resultSet.getString("comment");
                    Integer employeeId = resultSet.getInt("employee_id");
                    Integer userId = resultSet.getInt("user_id");

                    Announcement announcementFound = new Announcement(id, category, title, description, status, createdAt, updatedAt, comment, employeeId, userId);
                    allAnnouncements.add(announcementFound);

                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения заявок: " + e);
        }
        return allAnnouncements;
    }
}
