package javafxfrontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafxfrontend.apis.RetrofitClient;
import javafxfrontend.dto.UserRegisterRequestDto;
import javafxfrontend.dto.UserValExistanceDto;
import javafx.scene.control.Label;
import javafxfrontend.service.RegistrationService;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {
    // ловит поля введенные пользователем 1 и 2
    @FXML private StackPane rootPane;
    @FXML private TextField emailField;
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private TextField visiblePasswordField;

    @FXML private Button nextButton;

    @FXML private Label errorLabel;

    @FXML private TextField LastName;
    @FXML private TextField Name;
    @FXML private TextField Patronymic;

    static String savedLogin;
    static String savedEmail;
    static String savedpassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            if (rootPane != null) {
                rootPane.requestFocus();
            }
        });
        if (savedLogin != null) {
            loginField.setText(savedLogin);
            emailField.setText(savedEmail);
            passwordField.setText(savedpassword);
        }
    }

    private final RegistrationService registrationService = new RegistrationService();

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
    public void NextSTepRegistration(){
        savedLogin = loginField.getText().trim();
        savedEmail = emailField.getText().trim();
        savedpassword = passwordField.getText();



        if (savedLogin.isEmpty() || savedEmail.isEmpty() || savedpassword.isEmpty()){
            errorLabel.setText("Заполните все поля!");
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
            return;
        }
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        nextButton.setDisable(true);


        UserValExistanceDto request = new UserValExistanceDto(savedLogin, savedEmail);

        registrationService.checkExistance(request, new RegistrationService.CheckCallBack() {
            @Override
            public void OnSuccess() {
                System.out.println(">>> ВОШЛИ В ONSCCESS КОНТРОЛЛЕРА");
                Platform.runLater(() -> {
                    System.out.println(">>> ПОПЫТКА ЗАГРУЗИТЬ FXML");
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxfrontend/RegistrationGetFIO-view.fxml"));
                        Parent root = loader.load();
                        System.out.println(">>> FXML ЗАГРУЖЕН!");
                        Stage stage = (Stage) nextButton.getScene().getWindow();
                        stage.getScene().setRoot(root);
                        System.out.println(">>> ЭКРАН ЗАМЕНЕН!");
                    } catch (Exception e) {
                        System.out.println(">>> ОШИБКА ЗАГРУЗКИ: " + e.getMessage());
                        e.printStackTrace();
                        nextButton.setDisable(false);
                    }
                });
            }

            @Override
            public void OnError(String message) {
                System.out.println(">>> ВОШЛИ В ONERROR: " + message);
                Platform.runLater(() -> {
                    nextButton.setDisable(false);
                    errorLabel.setText(message);
                    errorLabel.setVisible(true);
                    errorLabel.setManaged(true);
                });
            }
        });
    }
    @FXML
    public void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxfrontend/Login-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) loginField.getScene().getWindow();
            stage.getScene().setRoot(root);

            savedLogin = null;
            savedEmail = null;
            savedpassword = null;

        } catch (Exception e) {
            System.out.println("Ошибка перехода на экран входа: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
