package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {

    @FXML
    private TextField txtUsername;

    @FXML
    private Label lblErrorMsg;


    @FXML
    void loginOnAction(ActionEvent event) throws IOException {
        if (!txtUsername.getText().isEmpty()){
            ClientFormController.username = txtUsername.getText();
            lblErrorMsg.setText("");

            Parent rootNode = FXMLLoader.load(getClass().getResource("/view/clientForm.fxml"));
            Scene scene = new Scene(rootNode);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.initModality(Modality.WINDOW_MODAL);

            ClientFormController controller = new ClientFormController();
            stage.show();

            txtUsername.clear();

        }else {
            lblErrorMsg.setText("Please enter your username!");
        }
    }
}
