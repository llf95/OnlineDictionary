import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Tony Jiang on 2016/11/14.
 */
public class LoginController {
    private Client client;

    @FXML private BorderPane fxLoginBoard;
    @FXML private TextField fxUsername;
    @FXML private PasswordField fxPassword;
    @FXML private ImageView fxRegister;
    @FXML private ImageView fxLogin;
    @FXML private Label fxLoginInfo;

    @FXML
    private void clickOnRegister() {
        fxLoginInfo.setText("Register");
    }

    @FXML
    private void clickOnLogin() throws Exception{
        String username = fxUsername.getText();
        String password = fxPassword.getText();
        if("user".equals(username) && "user".equals(password))
            client.replaceSceneContent("Mainboard.fxml");
        else {
            fxLoginInfo.setText("Wrong Username or Password");
        }
    }

    public void setClient(Client c) { this.client = c;}

}
