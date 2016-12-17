package client.controller;

import client.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pack.RequestPackage;
import pack.ResponsePackage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainboardController implements Initializable{
    private Client client;

    private String transBaidu = "";
    private String transBing = "";
    private String transYoudao = "";
    private String pron_BR = "";
    private String pron_US = "";

    @FXML private GridPane fxMainboard;
    @FXML private TextField fxSearchBar;
    @FXML private ImageView fxSearchButton;
    @FXML private CheckBox fxBaiduCheck;
    @FXML private CheckBox fxBingCheck;
    @FXML private CheckBox fxYoudaoCheck;
    @FXML private TextArea fxBaiduResult;
    @FXML private TextArea fxBingResult;
    @FXML private TextArea fxYoudaoResult;
    @FXML private ImageView fxBaiduLike;
    @FXML private ImageView fxBingLike;
    @FXML private ImageView fxYoudaoLike;
    @FXML private Label fxWord;
    @FXML private Label fxPronBR;
    @FXML private Label fxPronUS;
    @FXML private ImageView fxBaiduIcon;
    @FXML private ImageView fxBingIcon;
    @FXML private ImageView fxYoudaoIcon;
    @FXML private HBox fxBaiduBox;
    @FXML private HBox fxBingBox;
    @FXML private HBox fxYoudaoBox;
    @FXML private VBox fxResultArea;
    @FXML private Label fxBaiduLikeNum;
    @FXML private Label fxBingLikeNum;
    @FXML private Label fxYoudaoLikeNum;
    @FXML private ImageView fxLogout;
    @FXML private ImageView fxOnline;

    public void setClient(Client c) { this.client = c;}

    @FXML private void clickOnSearch() throws Exception{
        String word = fxSearchBar.getText();
        if(word == null || "".equals(word.trim())) {
            fxBaiduResult.setText("");
            fxBingResult.setText("");
            fxYoudaoResult.setText("");
            fxBaiduLikeNum.setText("0");
            fxBingLikeNum.setText("0");
            fxYoudaoLikeNum.setText("0");
            fxWord.setText("");
            fxPronBR.setText("");
            fxPronUS.setText("");
            return;
        }
        RequestPackage reqPack = new RequestPackage();
        reqPack.setType("req:search");
        reqPack.addRequest(word);
        client.out.writeObject(reqPack);
        client.out.flush();
        ResponsePackage resPack = (ResponsePackage) client.in.readObject();
        if(!"res:search".equals(resPack.getType())) return;
        this.transBaidu = resPack.getContent().elementAt(0);
        this.transBing = resPack.getContent().elementAt(1);
        this.transYoudao = resPack.getContent().elementAt(2);
        this.pron_BR = resPack.getContent().elementAt(3);
        this.pron_US = resPack.getContent().elementAt(4);

        if(fxBaiduCheck.isSelected()) {
            fxBaiduResult.setText(transBaidu);
            fxBaiduLikeNum.setText(resPack.getContent().elementAt(5));
        } else {
            fxBaiduResult.setText("");
            fxBaiduLikeNum.setText("0");
        }
        if(fxBingCheck.isSelected()){
            fxBingResult.setText(transBing);
            fxBingLikeNum.setText(resPack.getContent().elementAt(6));
        } else {
            fxBingResult.setText("");
            fxBingLikeNum.setText("0");
        }
        if(fxYoudaoCheck.isSelected()){
            fxYoudaoResult.setText(transYoudao);
            fxYoudaoLikeNum.setText(resPack.getContent().elementAt(7));
        } else {
            fxYoudaoResult.setText("");
            fxYoudaoLikeNum.setText("0");
        }
        fxWord.setText(word);
        fxPronBR.setText("[Br] " + pron_BR);
        fxPronUS.setText("[Us] " + pron_US);


        HBox box1 = (HBox) fxResultArea.getChildren().get(0);
        HBox box2 = (HBox) fxResultArea.getChildren().get(1);
        HBox box3 = (HBox) fxResultArea.getChildren().get(2);
        int num1 = Integer.parseInt(((Label)((VBox)box1.getChildren().get(3)).getChildren().get(1)).getText());
        int num2 = Integer.parseInt(((Label)((VBox)box2.getChildren().get(3)).getChildren().get(1)).getText());
        int num3 = Integer.parseInt(((Label)((VBox)box3.getChildren().get(3)).getChildren().get(1)).getText());
        if(!((CheckBox)box1.getChildren().get(0)).isSelected()) num1 = -1;
        if(!((CheckBox)box2.getChildren().get(0)).isSelected()) num2 = -1;
        if(!((CheckBox)box3.getChildren().get(0)).isSelected()) num3 = -1;

        fxResultArea.getChildren().clear();
        if(num1 >= num2 && num1 >= num3){
            fxResultArea.getChildren().add(box1);
            if(num2 >= num3){
                fxResultArea.getChildren().add(box2);
                fxResultArea.getChildren().add(box3);
            }
            else {
                fxResultArea.getChildren().add(box3);
                fxResultArea.getChildren().add(box2);
            }
        }
        else if(num2 >= num1 && num2 >= num3){
            fxResultArea.getChildren().add(box2);
            if(num1 >= num3) {
                fxResultArea.getChildren().add(box1);
                fxResultArea.getChildren().add(box3);
            }
            else {
                fxResultArea.getChildren().add(box3);
                fxResultArea.getChildren().add(box1);
            }
        }
        else {
            fxResultArea.getChildren().add(box3);
            if(num1 >= num2){
                fxResultArea.getChildren().add(box1);
                fxResultArea.getChildren().add(box2);
            }
            else {
                fxResultArea.getChildren().add(box2);
                fxResultArea.getChildren().add(box1);
            }
        }
    }

    @FXML private void clickOnLikeBaidu() throws Exception{
        if(fxWord.getText() == null || fxWord.getText() == "") return;
        if(!fxBaiduCheck.isSelected()) return;
        RequestPackage reqPack = new RequestPackage();
        reqPack.setType("req:like");
        reqPack.addRequest(fxWord.getText());
        reqPack.addRequest("baidu");
        client.out.writeObject(reqPack);
        client.out.flush();
        ResponsePackage resPack = (ResponsePackage) client.in.readObject();
        if(!"res:like".equals(resPack.getType())) {
            return;
        }
        if(resPack.getContent().size() > 0)
            fxBaiduLikeNum.setText(resPack.getContent().elementAt(0));
    }

    @FXML private void clickOnLikeBing() throws  Exception{
        if(fxWord.getText() == null || fxWord.getText() == "") return;
        if(!fxBingCheck.isSelected()) return;
        RequestPackage reqPack = new RequestPackage();
        reqPack.setType("req:like");
        reqPack.addRequest(fxWord.getText());
        reqPack.addRequest("bing");
        client.out.writeObject(reqPack);
        client.out.flush();
        ResponsePackage resPack = (ResponsePackage) client.in.readObject();
        if(!"res:like".equals(resPack.getType())) return;
        if(resPack.getContent().size() > 0)
            fxBingLikeNum.setText(resPack.getContent().elementAt(0));
    }
    @FXML private void clickOnLikeYoudao() throws Exception{
        if(fxWord.getText() == null || fxWord.getText() == "") return;
        if(!fxYoudaoCheck.isSelected()) return;
        RequestPackage reqPack = new RequestPackage();
        reqPack.setType("req:like");
        reqPack.addRequest(fxWord.getText());
        reqPack.addRequest("youdao");
        client.out.writeObject(reqPack);
        client.out.flush();
        ResponsePackage resPack = (ResponsePackage) client.in.readObject();
        if(!"res:like".equals(resPack.getType())) return;
        if(resPack.getContent().size() > 0)
            fxYoudaoLikeNum.setText(resPack.getContent().elementAt(0));
    }

    @FXML private void clickOnLogout() throws Exception{
        String username = client.currentuser;
        RequestPackage reqPack = new RequestPackage();
        reqPack.setType("req:logout");
        reqPack.addRequest(username);
        client.out.writeObject(reqPack);
        client.out.flush();
        ResponsePackage resPack = (ResponsePackage) client.in.readObject();
        if(!"res:logout".equals(resPack.getType())) return;
        client.currentuser = "";
        client.switchToLoginboard();
    }

    @FXML private void clickOnOnlineUsers() throws Exception{
        WritableImage snapshot =  fxMainboard.snapshot(null, null);
        client.switchToOnlineUsers(snapshot);
    }
    @FXML private void clickOnMails() throws Exception{
        client.switchToMail();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
