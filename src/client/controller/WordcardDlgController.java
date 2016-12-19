package client.controller;

import client.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class WordcardDlgController implements Initializable{
    private Client client;
    @FXML private ImageView fxWordcard;
    @FXML private AnchorPane fxPane;
    @FXML private Button fxBack;

    public void setClient(Client c) { this.client = c; }

    public void showWordcard(WritableImage image){
        fxWordcard.setImage(image);
    }

    @FXML private void clickOnBack() throws Exception{
        client.switchToMail();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
