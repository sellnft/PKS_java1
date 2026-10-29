package javafxfrontend.controller;

import javafx.stage.FileChooser;
import javafxfrontend.util.ExcelExporter;
import java.io.File;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafxfrontend.dto.AnnouncementCancelledStatusDto;
import javafxfrontend.dto.AnnouncementDoneStatusDto;
import javafxfrontend.dto.AnnouncementResponseDto;
import javafxfrontend.service.MainService;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private TableView<AnnouncementResponseDto> mainTable;
    @FXML private TableColumn<AnnouncementResponseDto, Integer> colId;
    @FXML private TableColumn<AnnouncementResponseDto, String> colTitle;
    @FXML private TableColumn<AnnouncementResponseDto, String> colCategory;
    @FXML private TableColumn<AnnouncementResponseDto, String> colStatus;
    @FXML private TableColumn<AnnouncementResponseDto, String> colDate;
    @FXML private TableColumn<AnnouncementResponseDto, String> colComment;
    @FXML private Label emptyLabel;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterCombo;

    private final MainService mainService = new MainService();
    private ObservableList<AnnouncementResponseDto> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Важно: используем cellData.getValue() для доступа к DTO
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().id()).asObject());
        colTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().title()));
        colCategory.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().category()));
        colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().status()));
        colDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().createdAt()));
        colComment.setCellValueFactory(cellData -> {
            String comment = cellData.getValue().comment();
            return new SimpleStringProperty(comment != null ? comment : "");
        });
        filterCombo.setItems(FXCollections.observableArrayList("Все", "PENDING", "IN_PROGRESS", "DONE", "CANCELLED"));
        loadData();
    }

    public void loadData() {
        mainService.loadAllAnnouncements(new MainService.DataCallback<List<AnnouncementResponseDto>>() {
            @Override
            public void onSuccess(List<AnnouncementResponseDto> result) {
                Platform.runLater(() -> {
                    data.setAll(result);
                    mainTable.setItems(data);
                    emptyLabel.setVisible(result.isEmpty());
                });
            }

            @Override
            public void onError(String message) {
                Platform.runLater(() -> showAlert("Ошибка", message));
            }
        });
    }

    @FXML
    public void handleDelete() {
        AnnouncementResponseDto selected = mainTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Внимание", "Выберите запись для отмены!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText("Отмена заявки");
        alert.setContentText("Вы уверены, что хотите отменить заявку: " + selected.title() + "?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                mainService.cancelAnnouncement(selected.id(), new MainService.DataCallback<AnnouncementCancelledStatusDto>() {
                    @Override
                    public void onSuccess(AnnouncementCancelledStatusDto result) {
                        Platform.runLater(() -> {
                            loadData(); // Обновляем таблицу
                            showInfo("Успех", "Заявка успешно отменена!");
                        });
                    }

                    @Override
                    public void onError(String message) {
                        Platform.runLater(() -> showAlert("Ошибка", message));
                    }
                });
            }
        });
    }

    // --- Заглушки для остальных кнопок ---
    @FXML
    public void handleAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxfrontend/AddAnnouncement-view.fxml"));
            Parent root = loader.load();

            // Получаем контроллер нового окна и передаем ему ссылку на этот (MainController)
            AddAnnouncementController addController = loader.getController();
            addController.setMainController(this);

            Stage stage = new Stage();
            stage.setTitle("Добавление заявки");
            stage.setScene(new Scene(root));
            stage.initModality(javafx.stage.Modality.WINDOW_MODAL); // Блокирует главное окно, пока не закроют это
            stage.initOwner(mainTable.getScene().getWindow());
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleEdit() {
        AnnouncementResponseDto selected = mainTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Внимание", "Выберите запись для редактирования!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxfrontend/EditAnnouncement-view.fxml"));
            Parent root = loader.load();

            EditAnnouncementController controller = loader.getController();
            controller.setAnnouncement(selected);
            controller.setMainController(this);

            Stage stage = new Stage();
            stage.setTitle("Редактирование заявки");
            stage.setScene(new Scene(root));
            stage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            stage.initOwner(mainTable.getScene().getWindow());
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось открыть форму: " + e.getMessage());
        }
    }

    private void askCommentAndFinish(AnnouncementResponseDto selected) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Завершение заявки");
        dialog.setHeaderText("Заявка: " + selected.title());
        dialog.setContentText("Введите комментарий (обязательно):");

        dialog.showAndWait().ifPresent(comment -> {
            if (comment.trim().isEmpty()) {
                showAlert("Ошибка", "Комментарий не может быть пустым!");
                return;
            }

            mainService.setDoneStatus(selected.id(), comment, new MainService.DataCallback<AnnouncementDoneStatusDto>() {
                @Override
                public void onSuccess(AnnouncementDoneStatusDto result) {
                    Platform.runLater(() -> {
                        loadData();
                        showInfo("Успех", "Заявка закрыта с комментарием!");
                    });
                }

                @Override
                public void onError(String message) {
                    Platform.runLater(() -> showAlert("Ошибка", message));
                }
            });
        });
    }

    private void cancelSelected(AnnouncementResponseDto selected) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Подтверждение");
        confirm.setHeaderText("Отмена заявки");
        confirm.setContentText("Вы уверены, что хотите отменить заявку: " + selected.title() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                mainService.cancelAnnouncement(selected.id(), new MainService.DataCallback<AnnouncementCancelledStatusDto>() {
                    @Override
                    public void onSuccess(AnnouncementCancelledStatusDto result) {
                        Platform.runLater(() -> {
                            loadData();
                            showInfo("Успех", "Заявка отменена!");
                        });
                    }

                    @Override
                    public void onError(String message) {
                        Platform.runLater(() -> showAlert("Ошибка", message));
                    }
                });
            }
        });
    }
    @FXML
    public void handleExport() {
        if (data.isEmpty()) {
            showAlert("Внимание", "Нет данных для экспорта!");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить в Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel файлы", "*.xlsx"));
        fileChooser.setInitialFileName("announcements.xlsx");
        File file = fileChooser.showSaveDialog(mainTable.getScene().getWindow());

        if (file != null) {
            try {
                ExcelExporter.exportToExcel(data, file);
                showInfo("Успех", "Данные экспортированы в " + file.getName());
            } catch (Exception e) {
                showAlert("Ошибка", "Не удалось сохранить: " + e.getMessage());
            }
        }

    }
    @FXML
    public void goToStats() {
        if (data.isEmpty()) {
            showAlert("Внимание", "Нет данных для статистики!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxfrontend/Stats-view.fxml"));
            Parent root = loader.load();

            StatsController controller = loader.getController();
            controller.setData(data);

            Stage stage = new Stage();
            stage.setTitle("Статистика заявок");
            stage.setScene(new Scene(root));
            stage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            stage.initOwner(mainTable.getScene().getWindow());
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось открыть статистику: " + e.getMessage());
        }
    }

    @FXML
    public void handleSearch() {
        String query = searchField.getText().toLowerCase();
        String filter = filterCombo.getValue();

        ObservableList<AnnouncementResponseDto> filtered = FXCollections.observableArrayList();
        for (AnnouncementResponseDto item : data) {
            boolean matchesQuery = item.title().toLowerCase().contains(query);
            boolean matchesFilter = (filter == null || filter.equals("Все") || item.status().equals(filter));

            if (matchesQuery && matchesFilter) {
                filtered.add(item);
            }
        }
        mainTable.setItems(filtered);
    }

    @FXML
    public void handleReset() {
        searchField.clear();
        filterCombo.getSelectionModel().clearSelection();
        mainTable.setItems(data);
    }

    @FXML
    public void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxfrontend/Login-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) mainTable.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}