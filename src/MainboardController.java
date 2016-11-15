import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Tony Jiang on 2016/11/14.
 */
public class MainboardController {
    private Client client;

    @FXML private GridPane fxMainboard;
    @FXML private TextField fxSearchBar;
    @FXML private ImageView fxSearchButton;
    @FXML private CheckBox fxCheck1;
    @FXML private CheckBox fxCheck2;
    @FXML private CheckBox fxCheck3;
    @FXML private TextArea fxResult1;
    @FXML private TextArea fxResult2;
    @FXML private TextArea fxResult3;
    @FXML private ImageView fxLike1;
    @FXML private ImageView fxLike2;
    @FXML private ImageView fxLike3;
    @FXML private Label fxWord;
    @FXML private Label fxPronBR;
    @FXML private Label fxPronUS;
    @FXML private ImageView fxIcon1;
    @FXML private ImageView fxIcon2;
    @FXML private ImageView fxIcon3;


    public void setClient(Client c) { this.client = c;}

    @FXML private void clickOnSearch() throws Exception{
        String word = fxSearchBar.getText();
        OnlineFetch exp = new OnlineFetch(word);
        String baidu = "";
        String bing = "";
        String youdao = "";
        for(String s : exp.getTranslationBaidu()){
            baidu = baidu + s + "\n";
        }
        for(String s :exp.getTranslationBing()) {
            bing += (s + "\n");
        }
        for(String s : exp.getTranslationYoudao()) {
            youdao += (s + "\n");
        }
        fxResult1.setText(baidu);
        fxResult2.setText(bing);
        fxResult3.setText(youdao);
        fxWord.setText(word);
        fxPronBR.setText("[Br] " + exp.getPronBR());
        fxPronUS.setText("[Us] " + exp.getPronUS());
    }

}
