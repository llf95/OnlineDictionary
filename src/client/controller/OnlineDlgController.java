package client.controller;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import pack.RequestPackage;
import pack.ResponsePackage;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class OnlineDlgController implements Initializable{
    private Client client;
    private WritableImage image;

    @FXML private ListView fxOnlineUsers;
    @FXML private AnchorPane fxDlgPane;
    @FXML private Label fxInfo;

    public void setImage(WritableImage image) throws Exception{
        this.image = image;
    }

    public void showOnlineUsers() throws Exception{
        RequestPackage reqPack = new RequestPackage();
        reqPack.setType("req:online");
        client.out.writeObject(reqPack);
        client.out.flush();
        ResponsePackage resPack = (ResponsePackage) client.in.readObject();
        if(!"res:online".equals(resPack.getType())) return;
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll(resPack.getContent());
        fxOnlineUsers.setItems(list);

    }

    @FXML private void clickOnBack() throws Exception{
        client.switchToMainboard();
    }
    @FXML private void clickOnSend() throws Exception{
        String receiver = (String) fxOnlineUsers.getSelectionModel().getSelectedItem();
        String sender = client.currentuser;
        if(receiver == null || sender == null || "".equals(receiver) || "".equals(sender))
            return;
        if(sender.equals(receiver)){
            fxInfo.setText("Cannot send to yourself!");
            return;
        }
        RequestPackage reqPack = new RequestPackage();
        reqPack.setType("req:send");
        reqPack.addRequest(receiver);
        reqPack.addRequest(sender);
        reqPack.addRequest(serializeImage(this.image));
        client.out.writeObject(reqPack);
        client.out.flush();
        ResponsePackage resPack = (ResponsePackage) client.in.readObject();
        if(!"res:send".equals(resPack.getType())) return;
        fxInfo.setText("Successfully send.");
    }


    public void setClient(Client c) { this.client = c; }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    private String serializeImage(WritableImage image) throws Exception{
        ByteArrayOutputStream bstream = new ByteArrayOutputStream();
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", bstream);
        byte[] b = bstream.toByteArray();
        bstream.close();
        String str = Base64.getEncoder().encodeToString(b);
        return str;
    }
}
