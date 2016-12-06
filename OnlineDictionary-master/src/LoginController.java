import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable{
    private Client client;

    @FXML private BorderPane fxLoginBoard;
    @FXML private TextField fxUsername;
    @FXML private PasswordField fxPassword;
    @FXML private ImageView fxRegister;
    @FXML private ImageView fxLogin;
    @FXML private Label fxLoginInfo;

    @FXML
    private void clickOnRegister() throws Exception{
        String username = fxUsername.getText();
        String password = fxPassword.getText();
        RequestPackage rp = new RequestPackage();
        rp.setType("req:register");
        rp.addRequest(username);
        rp.addRequest(password);

        client.out.writeObject(rp);
        client.out.flush();
        ResponsePackage resPack = (ResponsePackage) client.in.readObject();
        if(resPack.getType().equals("res:register"))
            fxLoginInfo.setText(resPack.getContent().elementAt(0));
    }

    @FXML
    private void clickOnLogin() throws Exception{
        String username = fxUsername.getText();
        String password = fxPassword.getText();
        RequestPackage reqPack = new RequestPackage();
        reqPack.setType("req:login");
        reqPack.addRequest(username);
        reqPack.addRequest(password);
        client.out.writeObject(reqPack);
        client.out.flush();
        ResponsePackage resPack = (ResponsePackage) client.in.readObject();
        if(resPack.getType().equals("res:login"))
            fxLoginInfo.setText(resPack.getContent().elementAt(0));
        if("Successfully login.".equals(resPack.getContent().elementAt(0))){
            client.currentuser = username;
            client.switchToMainboard();
        }
    }

    public void setClient(Client c) { this.client = c;}


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
}
