package javafxfrontend;

import com.dlsc.fxmlkit.fxml.FxmlKit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class FXApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FxmlKit.setLogLevel(java.util.logging.Level.FINE);
        FxmlKit.enableDevelopmentMode();

        FXMLLoader fxmlLoader = new FXMLLoader(FXApplication.class.getResource("Registration-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setTitle("Система обработотк заявок и тд");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(FXApplication.class, args);
    }
}


