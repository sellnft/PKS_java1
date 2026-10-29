package javafxfrontend.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafxfrontend.dto.AnnouncementResponseDto;
import javafxfrontend.service.MainService;

import java.net.URL;
import java.util.ResourceBundle;

public class EditAnnouncementController implements Initializable {

    @FXML private Label titleLabel;
    @FXML private ComboBox<String> statusCombo;
    @FXML private TextArea commentField;
    @FXML private Label errorLabel;

    private final MainService mainService = new MainService();
    private AnnouncementResponseDto announcement;
    private MainController mainController;

    public void setAnnouncement(AnnouncementResponseDto a) {
        this.announcement = a;
        titleLabel.setText(a.title());
        statusCombo.setValue(a.status());
        commentField.setText(a.comment() != null ? a.comment() : "");
    }

    public void setMainController(MainController c) {
        this.mainController = c;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statusCombo.setItems(FXCollections.observableArrayList(
                "PENDING", "IN_PROGRESS", "DONE", "CANCELLED"
        ));
    }

    @FXML
    public void handleSave() {
        String newStatus = statusCombo.getValue();
        String comment = commentField.getText() != null ? commentField.getText().trim() : "";

        if (newStatus == null) {
            showError("Выберите статус!");
            return;
        }

        boolean requiresComment = "DONE".equals(newStatus) || "CANCELLED".equals(newStatus);
        if (requiresComment && comment.isEmpty()) {
            showError("Для статуса " + newStatus + " нужен комментарий!");
            return;
        }

        mainService.updateStatusAndComment(announcement.id(), newStatus, comment,
                new MainService.DataCallback<AnnouncementResponseDto>() {
                    @Override
                    public void onSuccess(AnnouncementResponseDto result) {
                        Platform.runLater(() -> {
                            Stage stage = (Stage) titleLabel.getScene().getWindow();
                            stage.close();
                            if (mainController != null) mainController.loadData();
                        });
                    }

                    @Override
                    public void onError(String message) {
                        Platform.runLater(() -> showError(message));
                    }
                });
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}