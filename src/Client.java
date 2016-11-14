
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class Client extends Application {
    public static Socket server;



    @Override
    public void start(Stage primaryStage) throws IOException{
        server = new Socket(InetAddress.getLocalHost(), 1637);
        BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        PrintWriter out = new PrintWriter(server.getOutputStream());



        Parent loginBoard = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Parent mainBoard = FXMLLoader.load(getClass().getResource("Mainboard.fxml"));
        Scene scene = new Scene(mainBoard);


        primaryStage.setTitle("OnlineDictionary Beta v0.1");
        primaryStage.setScene(scene);
        primaryStage.show();
    }






    public static void main(String[] args) throws Exception {
        Application.launch(args);
    }
}
