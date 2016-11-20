import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Tony Jiang on 2016/11/14.
 */
public class MainboardController {
    private Client client;
    private String currentUsername = "";
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
    @FXML private ImageView fxCurrentUser;
    @FXML private Label fxCurrentUsername;
    @FXML private HBox fxBaiduBox;
    @FXML private HBox fxBingBox;
    @FXML private HBox fxYoudaoBox;
    @FXML private Label fxBaiduLikeNum;
    @FXML private Label fxBingLikeNum;
    @FXML private Label fxYoudaoLikeNum;

    public void setClient(Client c) { this.client = c;}

    @FXML private void clickOnSearch() throws Exception{
        String word = fxSearchBar.getText();
        RequestPackage reqPack = new RequestPackage();
        reqPack.setType("req:search");
        reqPack.addRequest(word);
        Client.out.writeObject(reqPack);
        Client.out.flush();
        ResponsePackage resPack = (ResponsePackage) Client.in.readObject();
        if(!"res:search".equals(resPack.getType())) return;
        this.transBaidu = resPack.getContent().elementAt(0);
        this.transBing = resPack.getContent().elementAt(1);
        this.transYoudao = resPack.getContent().elementAt(2);
        this.pron_BR = resPack.getContent().elementAt(3);
        this.pron_US = resPack.getContent().elementAt(4);

        if(fxBaiduCheck.isSelected()) fxBaiduResult.setText(transBaidu);
        if(fxBingCheck.isSelected()) fxBingResult.setText(transBing);
        if(fxYoudaoCheck.isSelected()) fxYoudaoResult.setText(transYoudao);
        fxWord.setText(word);
        fxPronBR.setText("[Br] " + pron_BR);
        fxPronUS.setText("[Us] " + pron_US);
    }

}
