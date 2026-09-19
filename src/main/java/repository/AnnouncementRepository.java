    package repository;

    import model.Announcement;
    import model.AnnouncementStatus;
    import model.CategoryType;
    import util.DatabaseManager;

    import java.sql.*;
    import java.time.OffsetDateTime;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Optional;


    public class AnnouncementRepository {

        private Announcement mapRow(ResultSet resultSet) throws SQLException {
            int emplId = resultSet.getInt("employee_id");
            Integer employeeId = resultSet.wasNull() ? null : emplId;

            return new Announcement(
                    resultSet.getInt("id"),
                    CategoryType.valueOf(resultSet.getString("category")),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    AnnouncementStatus.valueOf(resultSet.getString("status")),
                    resultSet.getObject("created_at", OffsetDateTime.class),
                    resultSet.getObject("updated_at", OffsetDateTime.class),
                    resultSet.getString("comment"),
                    employeeId,
                    resultSet.getInt("user_id")
            );
        }

        public void addAnnouncement(Announcement newAnnouncement) {
            String query = "INSERT into announcements (category, title, description, status, created_at, user_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try(Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, newAnnouncement.category().name());
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
                        allAnnouncements.add(mapRow(resultSet));
                    }
                }

            } catch (SQLException e) {
                throw new RuntimeException("Ошибка получения заявок: " + e);
            }
            return allAnnouncements;
        }

        public Optional<Announcement> getByID(int id) {
            Announcement announcementFound = null;
            String query = "SELECT id, category, title, description, status, created_at, updated_at, comment," +
                    "employee_id, user_id FROM announcements WHERE id = ?";

            try(Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, id);

                try(ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(mapRow(resultSet));
                    }
                }

            } catch(SQLException e) {
                throw new RuntimeException("Ошибка получения заявки по ID", e);
            }

            return Optional.empty();
        }

        public List<Announcement> getAnnouncementsByStatus(AnnouncementStatus status) {
            String neededStatus = status.toString();
            List <Announcement> announcementsFound = new ArrayList<Announcement>();

            String query = "SELECT id, category, title, description, status, created_at, " +
                    "updated_at, comment, employee_id, user_id FROM announcements WHERE status = ?";

            try(Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, neededStatus);

                try(ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        announcementsFound.add(mapRow(resultSet));
                    }
                }

            } catch (SQLException e) {
                throw new RuntimeException("Ошибка получения заявок по статусу", e);
            }

            return announcementsFound;
        }

        public boolean setNewAnnouncementStatus(int id, AnnouncementStatus status) {
            String statusToSet = status.toString();

            String query = "UPDATE announcements SET status = ? WHERE id = ?";

            try(Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, statusToSet);
                stmt.setInt(2, id);

                int affected = stmt.executeUpdate();

                if (affected > 0) {
                    return true;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Не удалось обновить статус у заявки", e);
            }
            return false;
        }

        public boolean setEmployeeForAnnouncement(int id, int employeeId) {
            String query = "UPDATE announcements set employee_id = ? WHERE id = ?";

            try(Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, employeeId);
                stmt.setInt(2, id);

                int affected = stmt.executeUpdate();

                if (affected > 0) {
                    return true;
                }

            } catch (SQLException e) {
                throw new RuntimeException("Ошибка установки ответственного заявке", e);
            }
            return false;
        }

        public boolean setUpdateAtAnnouncement(int id, Timestamp time) {
            String query = "UPDATE announcements SET updated_at = ? WHERE id = ?";

            try(Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setTimestamp(1, time);
                stmt.setInt(2, id);

                int affected = stmt.executeUpdate();

                if (affected > 0) {
                    return true;
                }

            } catch (SQLException e) {
                throw new RuntimeException("Ошибка обновления времени в заявке", e);
            }
            return false;
        }

        public boolean setDoneStatusToAnnouncement(int id) {
            String query = "UPDATE announcements SET status = ? WHERE id = ?";

            try(Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, AnnouncementStatus.DONE.name());
                stmt.setInt(2, id);

                int affected = stmt.executeUpdate();

                if (affected > 0) {
                    return true;
                }

            } catch (SQLException e) {
                throw new RuntimeException("Ошибка обновления статуса у заявки", e);
            }
            return false;
        }

        public boolean setCommentToAnnouncement(int id, String comment) {
            String query = "UPDATE announcements SET comment = ? WHERE id = ?";

            try(Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, comment);
                stmt.setInt(2, id);

                int affected = stmt.executeUpdate();

                if (affected > 0) {
                    return true;
                }

            } catch (SQLException e) {
                throw new RuntimeException("Ошибка установки комментария к заявке", e);
            }
            return false;
        }

    }
