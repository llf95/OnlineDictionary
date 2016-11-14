import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.net.Socket;

/**
 * Created by Tony Jiang on 2016/11/14.
 */
public class LoginController {

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
    private void clickOnLogin(){
        fxLoginInfo.setText("Login");
    }

}
