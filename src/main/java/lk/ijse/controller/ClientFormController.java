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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
        if (receiveMessage.matches(".*\\.(png|jpe?g|gif)$")){
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);

            Text textName = new Text(receiveMessage.split("-")[0]);
            TextFlow textFlow = new TextFlow(textName);
            hBox.getChildren().add(textFlow);

            Image image = new Image(receiveMessage.split("-")[1]);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(200);
            imageView.setFitWidth(200);

            HBox hBoxImage = new HBox();
            hBoxImage.setAlignment(Pos.CENTER_LEFT);
            hBoxImage.setPadding(new Insets(5,5,5,10));
            hBoxImage.getChildren().add(imageView);

            HBox hBoxTime = setTimeOnMsg("LEFT");

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vBox.getChildren().add(hBox);
                    vBox.getChildren().add(hBoxImage);
                    vBox.getChildren().add(hBoxTime);
                }
            });
        }else {

            String username = receiveMessage.split("-")[0];
            String clientMsg = receiveMessage.split("-")[1];

            HBox hBoxName = new HBox();
            hBoxName.setAlignment(Pos.CENTER_LEFT);
            hBoxName.setPadding(new Insets(5, 10, 0, 10));

            Text name = new Text(username);
            TextFlow textFlowName = new TextFlow(name);
            hBoxName.getChildren().add(textFlowName);

            HBox hBoxMsg = new HBox();
            hBoxMsg.setAlignment(Pos.CENTER_LEFT);
            hBoxMsg.setPadding(new Insets(5, 10, 0, 5));

            Text textMsg = new Text(clientMsg);
            textMsg.setFill(Color.color(0, 0, 0));
            TextFlow textFlow = new TextFlow(textMsg);
            textFlow.setStyle("-fx-background-color: #abb8c3; -fx-font-weight: bold; -fx-background-radius: 20");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            hBoxMsg.getChildren().add(textFlow);

            HBox hBoxTime = ClientFormController.setTimeOnMsg("LEFT");

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vBox.getChildren().add(hBoxName);
                    vBox.getChildren().add(hBoxMsg);
                    vBox.getChildren().add(hBoxTime);
                }
            });
        }
    }

    @FXML
    void sendMsgOnAction(ActionEvent event) {
        sendMsg(txtMsg.getText());
    }

    @FXML
    void sendImageOnAction(ActionEvent event) {
        FileDialog fileDialog = new FileDialog((Frame) null,"Select Image");
        fileDialog.setMode(FileDialog.LOAD);
        fileDialog.setVisible(true);
        String file = fileDialog.getDirectory() + fileDialog.getFile();
        fileDialog.dispose();
        sendImage(file);
    }

    public void sendImage(String sendImage){
        Image image = new Image(sendImage);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(5,5,5,10));
        hBox.getChildren().add(imageView);

        HBox hBoxTime = setTimeOnMsg("RIGHT");

        vBox.getChildren().add(hBox);
        vBox.getChildren().add(hBoxTime);
        try {
            dataOutputStream.writeUTF(username + "-" + sendImage);
            dataOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMsg(String sendClientMsg){
        //String sendClientMsg = txtMsg.getText();
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 5, 0, 10));

            Text text = new Text(sendClientMsg);
            text.setStyle("-fx-font-size: 14");
            TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle("-fx-background-color: #904aae; -fx-font-weight: bold; -fx-color: white; -fx-background-radius: 20px");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(1, 1, 1));

        hBox.getChildren().add(textFlow);

        HBox hBoxName = new HBox();
        hBoxName.setAlignment(Pos.CENTER_RIGHT);
        hBoxName.setPadding(new Insets(5, 5, 0, 5));

        HBox hBoxTime = setTimeOnMsg("RIGHT");

        vBox.getChildren().add(hBox);
        vBox.getChildren().add(hBoxTime);
        System.out.println(sendClientMsg.length());

            try {
                dataOutputStream.writeUTF(username + "-" + sendClientMsg);
                dataOutputStream.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            txtMsg.clear();
    }

    public static HBox setTimeOnMsg(String alignment){
        HBox hBox = new HBox();
        if (alignment == "RIGHT"){
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(3,12,5,10));
        }else {
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(3,10,5,12));
        }

        String setTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        Text time = new Text(setTime);
        time.setStyle("-fx-font-size: 8");

        hBox.getChildren().add(time);
        return hBox;
    }
}
