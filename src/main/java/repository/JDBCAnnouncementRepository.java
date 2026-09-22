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
    import java.util.logging.Level;
    import java.util.logging.Logger;


    public class JDBCAnnouncementRepository implements AnnouncementRepository {

        private final Logger logger = Logger.getLogger(JDBCAnnouncementRepository.class.getName());

        private Announcement mapRow(ResultSet resultSet) throws SQLException {
            int emplId = resultSet.getInt("employee_id");
            Integer employeeId = resultSet.wasNull() ? null : emplId;

            return new Announcement(
                    resultSet.getInt("id"),
                    resultSet.getString("category"),
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

        @Override
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

                if(affected == 0) {
                    logger.warning("INSERT не добавил ни одной строки");
                }
                logger.info("Создана новая заявка от пользователя " + newAnnouncement.userId());

            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Ошибка добавления заявки", e);
                throw new RuntimeException("Ошибка добавления заявки: " + e);
            }
        }

        @Override
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
                logger.log(Level.SEVERE, "Ошибка получения заявок", e);
                throw new RuntimeException("Ошибка получения заявок", e);
            }
            logger.info("Список заявок успешно предоставлен");
            return allAnnouncements;
        }

        @Override
        public Optional<Announcement> getByID(int id) {
            Announcement announcementFound = null;
            String query = "SELECT id, category, title, description, status, created_at, updated_at, comment," +
                    "employee_id, user_id FROM announcements WHERE id = ?";

            try(Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, id);

                try(ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        logger.info("Заявка " + id + " успешно найдена");
                        return Optional.of(mapRow(resultSet));
                    }
                }

            } catch(SQLException e) {
                logger.log(Level.SEVERE, "Ошибка получения заявки по ID", e);
                throw new RuntimeException("Ошибка получения заявки по ID", e);
            }

            logger.warning("Запрошена заявка с несущствеющим ID: " + id);
            return Optional.empty();
        }

        @Override
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
                logger.log(Level.SEVERE, "Ошибка получения заявок по статусу", e);
                throw new RuntimeException("Ошибка получения заявок по статусу", e);
            }

            logger.info("Заявки со статусом " + neededStatus + " успешно найдены");
            return announcementsFound;
        }

        @Override
        public boolean setNewAnnouncementStatus(int id, AnnouncementStatus status) {
            String statusToSet = status.toString();

            String query = "UPDATE announcements SET status = ? WHERE id = ?";

            try(Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, statusToSet);
                stmt.setInt(2, id);

                int affected = stmt.executeUpdate();

                if (affected == 0) {
                    logger.warning("UPDATE не обновил ни одной строки");
                    return false;
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Не удалось обновить статус у заявки", e);
                throw new RuntimeException("Не удалось обновить статус у заявки", e);
            }
            logger.info("Статус заявки " + id + " успешно сменен на статус " + statusToSet);
            return true;
        }

        @Override
        public boolean setEmployeeForAnnouncement(int id, int employeeId) {
            String query = "UPDATE announcements set employee_id = ? WHERE id = ?";

            try(Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, employeeId);
                stmt.setInt(2, id);

                int affected = stmt.executeUpdate();

                if (affected == 0) {
                    logger.warning("UPDATE не обновил ни одной строки");
                    return false;
                }

            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Ошибка установки ответственного заявке", e);
                throw new RuntimeException("Ошибка установки ответственного заявке", e);
            }
            logger.info("Заявке " + id + " успешно установлен ответственный " + employeeId);
            return true;
        }

        @Override
        public boolean setUpdateAtAnnouncement(int id, Timestamp time) {
            String query = "UPDATE announcements SET updated_at = ? WHERE id = ?";

            try(Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setTimestamp(1, time);
                stmt.setInt(2, id);

                int affected = stmt.executeUpdate();

                if (affected == 0) {
                    logger.warning("UPDATE не обновил ни одной строки");
                    return false;
                }

            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Ошибка обновления времени в заявке", e);
                throw new RuntimeException("Ошибка обновления времени в заявке", e);
            }
            logger.info("Время в заявке " + id + " успешно обновлено");
            return true;
        }

        @Override
        public boolean setCommentToAnnouncement(int id, String comment) {
            String query = "UPDATE announcements SET comment = ? WHERE id = ?";

            try(Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, comment);
                stmt.setInt(2, id);

                int affected = stmt.executeUpdate();

                if (affected == 0) {
                    logger.warning("UPDATE не обновил ни одной строки");
                    return false;
                }

            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Ошибка установки комментария к заявке", e);
                throw new RuntimeException("Ошибка установки комментария к заявке", e);
            }
            logger.info("Успешно установлен комментарий к заявке " + id);
            return true;
        }

        @Override
        public List<Announcement> getAllAnnouncementsOfUser(int userId) {
            List<Announcement> announcementsFound = new ArrayList<>();
            String query = "SELECT id, category, title, description, status, created_at, updated_at, comment, employee_id, user_id FROM announcements WHERE user_id = ?";

            try(Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, userId);

                try(ResultSet resultSet = stmt.executeQuery()) {
                    while(resultSet.next()) {
                        announcementsFound.add(mapRow(resultSet));
                    }
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Ошибка получения заявок пользователя " + userId, e);
                throw new RuntimeException("Ошибка получения заявок пользователя", e);
            }
            logger.info("Заявки пользователя " + userId + " успешно получены");
            return announcementsFound;
        }

        public boolean cancelAnnouncement(int id) {
            String query = "UPDATE announcements SET status = ? WHERE id = ?";

            try(Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, AnnouncementStatus.CANCELLED.name());
                stmt.setInt(2, id);

                int affected = stmt.executeUpdate();

                if (affected > 0) {
                    logger.info("заявка #" + id + " успешно отменена");
                    return true;
                }

            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Ошибка отмены заявки", e);
                throw new RuntimeException("Ошибка отмены заявки", e);
            }
            logger.warning("заявка #" + id + "не была отменена");
            return false;
        }

        public boolean deleteAnnouncement(int id) {
            String query = "DELETE FROM announcements WHERE id = ?";

            try(Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, id);

                int affected = stmt.executeUpdate();
                if (affected > 0) {
                    logger.info("заявка #" + id + " успешно удалена");
                    return true;
                }
            }catch(SQLException e) {
                logger.log(Level.SEVERE, "Ошибка удаления заявки", e);
                throw new RuntimeException("Ошибка удаления заявки", e);
            }
            logger.warning("заявка #" + id + " не была удалена");
            return false;
        }

    }
