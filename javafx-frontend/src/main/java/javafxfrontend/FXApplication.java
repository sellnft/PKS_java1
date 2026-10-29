package javafxfrontend;

import com.dlsc.fxmlkit.fxml.FxmlKit;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafxfrontend.apis.SecureTokenStorage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class FXApplication extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        FxmlKit.setLogLevel(java.util.logging.Level.FINE);
        FxmlKit.enableDevelopmentMode();

        String token = SecureTokenStorage.getToken();
        FXMLLoader fxmlLoader;

        if (token != null && !token.isEmpty()) {
            fxmlLoader = new FXMLLoader(FXApplication.class.getResource("MainWindow-view.fxml"));
        } else {
            fxmlLoader = new FXMLLoader(FXApplication.class.getResource("Login-view.fxml"));
        }
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
        try {
            scene.getStylesheets().add(FXApplication.class.getResource("styles.css").toExternalForm());
        }catch (NullPointerException e){}

        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setTitle("Система обработотк заявок и тд");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(FXApplication.class, args);
    }
}


