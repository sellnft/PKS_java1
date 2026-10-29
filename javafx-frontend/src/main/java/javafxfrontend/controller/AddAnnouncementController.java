package javafxfrontend.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxfrontend.dto.AnnouncementAddedRequestDto;
import javafxfrontend.dto.AnnouncementResponseDto;
import javafxfrontend.service.MainService;

import java.net.URL;
import java.util.ResourceBundle;

// ДОБАВИЛИ implements Initializable
public class AddAnnouncementController implements Initializable {

    @FXML private TextField titleField;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private TextArea descField;

    private final MainService mainService = new MainService();
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoryCombo.getItems().addAll(
                "Заявка на разработку",
                "Заявка на обслуживание",
                "Консультация",
                "Ошибка / баг",
                "Доступ и права",
                "Настройка оборудования",
                "Установка ПО",
                "Восстановление данных",
                "Обучение пользователя",
                "Прочее"
        );
    }

    @FXML
    public void handleSave() {
        String title = titleField.getText().trim();
        String category = categoryCombo.getValue();
        String desc = descField.getText().trim();

        // ПРОВЕРКА: category может быть null, если ничего не выбрано
        if (title.isEmpty() || category == null || category.isEmpty()) {
            System.out.println("Заполните название и выберите категорию!");
            return;
        }

        AnnouncementAddedRequestDto request = new AnnouncementAddedRequestDto(category, title, desc);

        mainService.createAnnouncement(request, new MainService.DataCallback<AnnouncementResponseDto>() {
            @Override
            public void onSuccess(AnnouncementResponseDto result) {
                Platform.runLater(() -> {
                    Stage stage = (Stage) titleField.getScene().getWindow();
                    stage.close();
                    if (mainController != null) {
                        mainController.loadData();
                    }
                });
            }

            @Override
            public void onError(String message) {
                Platform.runLater(() -> System.err.println("ОШИБКА: " + message));
            }
        });
    }
}