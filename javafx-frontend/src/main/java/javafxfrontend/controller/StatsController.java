package javafxfrontend.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafxfrontend.dto.AnnouncementResponseDto;

import java.util.List;

public class StatsController {

    @FXML private Label totalLabel;
    @FXML private Label pendingLabel;
    @FXML private Label inProgressLabel;
    @FXML private Label doneLabel;
    @FXML private Label cancelledLabel;

    public void setData(List<AnnouncementResponseDto> data) {
        long pending = data.stream().filter(a -> "PENDING".equals(a.status())).count();
        long inProgress = data.stream().filter(a -> "IN_PROGRESS".equals(a.status())).count();
        long done = data.stream().filter(a -> "DONE".equals(a.status())).count();
        long cancelled = data.stream().filter(a -> "CANCELLED".equals(a.status())).count();

        totalLabel.setText(String.valueOf(data.size()));
        pendingLabel.setText(String.valueOf(pending));
        inProgressLabel.setText(String.valueOf(inProgress));
        doneLabel.setText(String.valueOf(done));
        cancelledLabel.setText(String.valueOf(cancelled));
    }

    @FXML
    public void handleClose() {
        Stage stage = (Stage) totalLabel.getScene().getWindow();
        stage.close();
    }
}