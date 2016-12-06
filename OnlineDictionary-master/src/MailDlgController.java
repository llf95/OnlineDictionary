import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;
import java.util.Vector;


public class MailDlgController implements Initializable{
    private Client client;
    @FXML private ListView fxMails;
    @FXML private AnchorPane fxDlgPane;
    @FXML private Label fxInfo;

    public void setClient(Client c) { this.client = c; }

    public void showMails() throws Exception{
        RequestPackage reqPack = new RequestPackage();
        reqPack.setType("req:mails");
        reqPack.addRequest(client.currentuser);
        client.out.writeObject(reqPack);
        client.out.flush();
        ResponsePackage resPack = (ResponsePackage) client.in.readObject();
        if(!resPack.getType().equals("res:mails")) return;
        Vector<String> mails = resPack.getContent();
        ObservableList<String> list = FXCollections.observableArrayList();
        for(String s : mails) {
            list.add(s);
        }
        fxMails.setItems(list);
    }


    @FXML private void clickOnBack() throws Exception{
        client.switchToMainboard();
    }
    @FXML private void clickOnShow() throws Exception{
        String mail = (String) fxMails.getSelectionModel().getSelectedItem();
        if(mail == null || "".equals(mail)) return;
        String idstr = mail.split("\t")[0];
        idstr = idstr.substring(4);
        int id = Integer.parseInt(idstr);

        RequestPackage reqPack = new RequestPackage();
        reqPack.setType("req:show");
        reqPack.addRequest(id+"");
        client.out.writeObject(reqPack);
        client.out.flush();
        ResponsePackage resPack = (ResponsePackage) client.in.readObject();
        String image = resPack.getContent().elementAt(0);
        byte[] bytes = Base64.getDecoder().decode(image);
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
        WritableImage writableImage = SwingFXUtils.toFXImage(bufferedImage, null);
        client.switchToWordcard(writableImage);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
