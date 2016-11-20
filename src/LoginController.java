import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;



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
    private void clickOnRegister() throws Exception{
        String username = fxUsername.getText();
        String password = fxPassword.getText();
        RequestPackage rp = new RequestPackage();
        rp.setType("req:register");
        rp.addRequest(username);
        rp.addRequest(password);

        Client.out.writeObject(rp);
        Client.out.flush();
        ResponsePackage resPack = (ResponsePackage) Client.in.readObject();
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
        Client.out.writeObject(reqPack);
        Client.out.flush();
        ResponsePackage resPack = (ResponsePackage) Client.in.readObject();
        if(resPack.getType().equals("res:login"))
            fxLoginInfo.setText(resPack.getContent().elementAt(0));
        if("Successfully login.".equals(resPack.getContent().elementAt(0))){
            client.replaceSceneContent("Mainboard.fxml");
        }
    }

    public void setClient(Client c) { this.client = c;}


}
