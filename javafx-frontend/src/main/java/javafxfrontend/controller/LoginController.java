package javafxfrontend.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafxfrontend.dto.UserLoginDto;
import javafxfrontend.service.LoginService;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private StackPane rootPane;
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private TextField visiblePasswordField;

    @FXML private Button loginButton;
    @FXML private Label errorLabel;

    private final LoginService loginService = new LoginService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            if (rootPane != null) {
                rootPane.requestFocus();
            }
        });
    }

    @FXML
    private void togglePassword(ActionEvent event) {
        Button btn = (Button) event.getSource();

        if (passwordField.isVisible()) {
            visiblePasswordField.setText(passwordField.getText());
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            visiblePasswordField.setVisible(true);
            visiblePasswordField.setManaged(true);
        } else {
            passwordField.setText(visiblePasswordField.getText());
            visiblePasswordField.setVisible(false);
            visiblePasswordField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
        }
    }

    @FXML
    public void handleLogin() {
        String login = loginField.getText().trim();
        String password = passwordField.isVisible() ? passwordField.getText() : visiblePasswordField.getText();

        if (rootPane != null) {
            rootPane.requestFocus();
        }

        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        if (login.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Заполните все поля.");
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
            return;
        }

        loginButton.setDisable(true);

        UserLoginDto request = new UserLoginDto(login, password);

        loginService.login(request, new LoginService.CheckCallBack() {
            @Override
            public void OnSuccess() {
                Platform.runLater(() -> {
                    try {
                        System.out.println(">>> УСПЕШНЫЙ ВХОД!");
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxfrontend/MainWindow-view.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) loginButton.getScene().getWindow();
                        stage.getScene().setRoot(root);
                    } catch (Exception e) {
                        e.printStackTrace();
                        loginButton.setDisable(false);
                    }
                });
            }

            @Override
            public void OnError(String message) {
                Platform.runLater(() -> {
                    loginButton.setDisable(false);
                    loginButton.setText("Войти");
                    errorLabel.setText(message);
                    errorLabel.setVisible(true);
                    errorLabel.setManaged(true);
                });
            }
        });
    }

    @FXML
    public void goToRegistration() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxfrontend/Registration-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            System.out.println("Ошибка перехода на регистрацию: " + e.getMessage());
            e.printStackTrace();
        }
    }
}