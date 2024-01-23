package lk.ijse.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientFormController implements Initializable {

    @FXML
    private Label lblUsername;
    public static String username;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vBox;
    @FXML
    private TextField txtMsg;
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    public void setUsername(){
        lblUsername.setText(username);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUsername();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("localhost" , 3009);
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    ServerFormController.receiveMessage(username + " joined...");

                    while (socket.isConnected()){
                        String receiveMsg = dataInputStream.readUTF();
                        receiveMessage(receiveMsg , ClientFormController.this.vBox);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        this.vBox.heightProperty().addListener(new ChangeListener<Number>() { //VBox Auto Scrolling
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                scrollPane.setVvalue((Double) newValue);
            }
        });
    }

    public static void receiveMessage(String receiveMessage , VBox vBox){
        String username = receiveMessage.split("-")[0];
        String clientMsg = receiveMessage.split("-")[1];

        HBox hBoxName = new HBox();
        hBoxName.setAlignment(Pos.CENTER_LEFT);

        Text name = new Text(username);
        TextFlow textFlowName = new TextFlow(name);
        hBoxName.getChildren().add(textFlowName);

        HBox hBoxMsg = new HBox();
        hBoxMsg.setAlignment(Pos.CENTER_LEFT);
        hBoxMsg.setPadding(new Insets(5,5,5,10));

        Text textMsg = new Text(clientMsg);
        textMsg.setFill(Color.color(0,0,0));
        TextFlow textFlow = new TextFlow(textMsg);
        textFlow.setStyle("-fx-background-color: #abb8c3; -fx-font-weight: bold; -fx-background-radius: 20");
        textFlow.setPadding(new Insets(5,10,5,10));
        hBoxMsg.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBoxName);
                vBox.getChildren().add(hBoxMsg);
            }
        });
    }

    @FXML
    void sendMsgOnAction(ActionEvent event) {
        sendMsg(txtMsg.getText());
    }

    public void sendMsg(String sendClientMsg){
        //String sendClientMsg = txtMsg.getText();
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 5, 0, 10));

            Text text = new Text(sendClientMsg);
            text.setStyle("-fx-font-size: 14");
            TextFlow textFlow = new TextFlow(text);

            textFlow.setStyle("-fx-background-color: #20c32a; -fx-font-weight: bold; -fx-color: white; -fx-background-radius: 20px");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(1, 1, 1));

            hBox.getChildren().add(textFlow);

            HBox hBoxName = new HBox();
            hBoxName.setAlignment(Pos.CENTER_RIGHT);
            hBoxName.setPadding(new Insets(5, 5, 0, 10));

            Text textName = new Text("me ");
            textName.setStyle("-fx-font-size: 12");

            TextFlow textFlowName = new TextFlow(textName);
            textName.setFill(Color.color(0, 0, 0));
            hBoxName.getChildren().add(textFlowName);

            vBox.getChildren().add(hBoxName);
            vBox.getChildren().add(hBox);

            try {
                dataOutputStream.writeUTF(username + "-" + sendClientMsg);
                dataOutputStream.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            txtMsg.clear();
    }
}
