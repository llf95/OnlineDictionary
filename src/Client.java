
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class Client extends Application{
    private static Socket server;
    private BufferedReader in;
    private PrintWriter out;

    private double height = 450;
    private double width = 600;


    private void setLoginPane(Pane login){
        Label label_user = new Label("用户：");
        Label label_psw = new Label("密码：");
        TextField text_user = new TextField();
        PasswordField text_psw = new PasswordField();
        Button button_log = new Button("登录");
        Button button_reg = new Button("注册");
        Label label_info = new Label("");

        label_user.setLayoutX(190);
        label_user.setLayoutY(120);
        label_psw.setLayoutX(190);
        label_psw.setLayoutY(170);
        label_info.setLayoutX(260);
        label_info.setLayoutY(300);
        label_info.setFont(Font.font("Arial", 20));
        label_info.setTextFill(Color.RED);

        text_user.setLayoutX(240);
        text_user.setLayoutY(120);
        text_psw.setLayoutX(240);
        text_psw.setLayoutY(170);

        button_log.setLayoutX(230);
        button_log.setLayoutY(220);
        button_reg.setLayoutX(350);
        button_reg.setLayoutY(220);

        login.getChildren().addAll(label_user, label_psw, text_user, text_psw,
                button_log, button_reg, label_info);
    }

    private void setMainPane(Pane main){
        Rectangle rect = new Rectangle(0, 0, width, 80);
        rect.setFill(Color.GOLD);

        main.getChildren().addAll(rect);
    }

    @Override
    public void start(Stage primaryStage) throws IOException{
        server = new Socket(InetAddress.getLocalHost(), 1637);
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        out = new PrintWriter(server.getOutputStream());

        Pane loginpane = new Pane();
        Pane mainpane = new Pane();
        setLoginPane(loginpane);
        setMainPane(mainpane);

        Scene scene = new Scene(loginpane, width, height);

        TextField       loginpane_textfield_username    = ((TextField)loginpane.getChildren().get(2));
        PasswordField   loginpane_textfield_password    = ((PasswordField)loginpane.getChildren().get(3));
        Button          loginpane_button_login          = ((Button)loginpane.getChildren().get(4));
        Button          loginpane_button_register       = ((Button)loginpane.getChildren().get(5));
        Label           loginpane_label_info            = ((Label)loginpane.getChildren().get(6));

        loginpane_button_login.setOnAction(e -> {
            out.println("req:login");
            out.println(loginpane_textfield_username.getText());
            out.println(loginpane_textfield_password.getText());
            out.flush();
            try {
                String echo = in.readLine();
                if(echo.equals("echo:login")){
                    echo = in.readLine();
                    if(echo.equals("succeed")) {
                        scene.setRoot(mainpane);
                    }
                    else {
                        loginpane_label_info.setText("登录失败");
                    }
                }
            } catch (IOException ioe) {
            } finally {
            }
        });



        primaryStage.setTitle("OnlineDictionary Beta v0.1");
        primaryStage.setScene(scene);
        primaryStage.show();
    }






    public static void main(String[] args) throws Exception {
        Application.launch(args);
    }
}
