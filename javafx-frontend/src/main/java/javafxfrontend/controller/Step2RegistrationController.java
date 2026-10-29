package javafxfrontend.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxfrontend.dto.UserRegisterRequestDto;
import javafxfrontend.service.RegistrationService;

public class Step2RegistrationController {

    @FXML private TextField LastName;
    @FXML private TextField Name;
    @FXML private TextField Patronymic;
    @FXML private Label errorLabel;

    private final RegistrationService registrationService = new RegistrationService();

    @FXML
    public void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxfrontend/Registration-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) LastName.getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (Exception e) {
            System.out.println("Ошибка возврата: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    public void handleRegistration() {

        String fio = LastName.getText().trim() + " " + Name.getText().trim() + " " + Patronymic.getText().trim();
        if (LastName.getText().trim().isEmpty() || Name.getText().trim().isEmpty()) {
            errorLabel.setText("Введите хотя бы Фамилию и Имя.");
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
            return;
        }

        UserRegisterRequestDto request = new UserRegisterRequestDto(
                RegistrationController.savedLogin,
                RegistrationController.savedpassword,
                fio,
                RegistrationController.savedEmail
        );

        registrationService.registerUser(request, new RegistrationService.CheckCallBack() {
            @Override
            public void OnSuccess() {
                Platform.runLater(() -> {
                    System.out.println("Регистрация успешна!");
                    try {
                        RegistrationController.savedLogin = null;
                        RegistrationController.savedEmail = null;
                        RegistrationController.savedpassword = null;
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxfrontend/Login-view.fxml"));
                        Parent root = loader.load();

                        Stage stage = (Stage) LastName.getScene().getWindow();
                        stage.getScene().setRoot(root);

                    } catch (Exception e) {
                        System.out.println("Ошибка перехода на экран входа: " + e.getMessage());
                        e.printStackTrace();
                    }
                    // Тут можно перекинуть на экран входа
                });
            }

            @Override
            public void OnError(String message) {
                Platform.runLater(() -> {
                    errorLabel.setText(message);
                    errorLabel.setVisible(true);
                    errorLabel.setManaged(true);
                });
            }
        });
    }
}