package client;

import client.controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.io.InputStream;


public class Client extends Application {
    public static Socket server;
    public ObjectInputStream in;
    public ObjectOutputStream out;
    public String currentuser = "";

    private Stage stage = new Stage();
    private Scene scene;

    private void initClient() throws Exception {
        server = new Socket(InetAddress.getLocalHost(), 1637);
        out = new ObjectOutputStream(server.getOutputStream());
        in = new ObjectInputStream(server.getInputStream());

        FXMLLoader loader1 = new FXMLLoader();
        loader1.setLocation(this.getClass().getResource("fxml/Login.fxml"));
        loader1.load();
        LoginController loginController = ((LoginController)loader1.getController());
        loginController.setClient(this);

        FXMLLoader loader2 = new FXMLLoader();
        loader2.setLocation(this.getClass().getResource("fxml/Mainboard.fxml"));
        loader2.load();
        MainboardController mainboardController = ((MainboardController)loader2.getController());
        mainboardController.setClient(this);

        scene = new Scene(loader1.getRoot());
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        initClient();

        primaryStage.setScene(scene);
        primaryStage.setTitle("OnlineDictionary Beta v1.0");
        stage = primaryStage;
        stage.show();
    }

    public Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Client.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Client.class.getResource(fxml));
        Pane page;
        try {
            page = (Pane) loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable)loader.getController();
    }

    public void switchToLoginboard() throws Exception{
        LoginController login = (LoginController) replaceSceneContent("fxml/Login.fxml");
        login.setClient(this);
    }
    public void switchToMainboard() throws Exception {
        MainboardController mainboard = (MainboardController) replaceSceneContent("fxml/Mainboard.fxml");
        mainboard.setClient(this);
    }
    public void switchToOnlineUsers(WritableImage image) throws Exception{
        OnlineDlgController onlineUsers = (OnlineDlgController) replaceSceneContent("fxml/OnlineDlg.fxml");
        onlineUsers.setClient(this);
        onlineUsers.setImage(image);
        onlineUsers.showOnlineUsers();
    }
    public void switchToMail() throws Exception{
        MailDlgController mails = (MailDlgController) replaceSceneContent("fxml/MailDlg.fxml");
        mails.setClient(this);
        mails.showMails();
    }
    public void switchToWordcard(WritableImage image) throws Exception{
        WordcardDlgController wordcarddlg = (WordcardDlgController) replaceSceneContent("fxml/WordcardDlg.fxml");
        wordcarddlg.setClient(this);
        wordcarddlg.showWordcard(image);
    }



    public static void main(String[] args) throws Exception {
        Application.launch(args);
    }
}
