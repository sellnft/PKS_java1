package javafxfrontend.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javafx.scene.control.PasswordField;

import javax.swing.*;

public class RegistrationController {
    // ловит поля введенные пользователем 1 и 2
    @FXML private TextField loginFiled;
    @FXML private PasswordField passwordField;

    @FXML private TextField FIOFileid;
    @FXML private TextField emailField;

    private String savedLogin;
    private String savedPassword;

    @FXML
    public void handleDataFields(){
        savedLogin = loginFiled.getText();
        savedPassword = passwordField.getText();
        if (userRepository.existsByLogin(login)) {
            throw UserAlreadyExistsException.byLogin();
        }
        if (userRepository.existsByEmail(email)) {
            throw UserAlreadyExistsException.byEmail(email);
        }

    }


}
