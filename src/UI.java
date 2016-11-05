/**
 * Created by Tony Jiang on 2016/11/2.
 */


import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.*;

public class UI extends Application {
    private Dictionary _dictionary;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this._dictionary = new Dictionary("dictionary.txt");
        BorderPane pane = new BorderPane();

        pane.setTop(getTop());
        pane.setLeft(getLeft());
        pane.setRight(getRight());

        Button button = (Button)((HBox)pane.getChildren().get(0)).getChildren().get(2);
        TextField inputArea = (TextField)((HBox)pane.getChildren().get(0)).getChildren().get(1);
        TextArea outputArea = (TextArea)((VBox)pane.getChildren().get(1)).getChildren().get(1);
        Label recommend = (Label)((VBox)pane.getChildren().get(2)).getChildren().get(0);
        ListView<String> listView = (ListView<String>)((VBox)pane.getChildren().get(2)).getChildren().get(1);

        ArrayList<String> idlearrayList = new ArrayList<>();
        for(int i = 1; i <= _dictionary.size(); i++){
            String word = _dictionary.searchByNumber(i);
            idlearrayList.add(word);
        }
        ObservableList<String> idlelist = FXCollections.observableArrayList(idlearrayList);
        listView.setItems(idlelist);

        button.setOnAction(e->{
            listView.setItems(idlelist);
            listView.scrollTo("a");
            String word = inputArea.getText();
            if(null == word || word.equals("")){
                outputArea.clear();
                recommend.setText("Similar words: ");
                listView.setItems(idlelist);
                return;
            }
            String explanation = _dictionary.searchExplanation(word);
            String pron = _dictionary.searchPronouncement(word);
            if(null == explanation){
                ArrayList<String> sim = _dictionary.similarWord(word);
                outputArea.clear();
                outputArea.appendText("Not Found!");
                recommend.setText("Do you mean:");
                ObservableList<String> list = FXCollections.observableArrayList(sim);
                listView.setItems(list);
            }
            else {
                explanation = exHandle(explanation);
                String output = "> " + word + "\n\n音标：  \n\t" + pron + "\n释义： \n\t" + explanation;
                outputArea.clear();
                outputArea.appendText(output);
                recommend.setText("Similar words: ");
                listView.setItems(idlelist);
                word = word.toLowerCase();
                word = Dictionary.trim(word);
                listView.scrollTo(word);
            }
        });

        inputArea.setOnKeyReleased(e->{
            button.fire();
        });


        listView.setOnMouseClicked(e->{
            String now = listView.getSelectionModel().getSelectedItem();
            if(now == null) return;
            inputArea.clear();
            inputArea.setText(now);
            button.fire();
        });
        listView.setOnKeyReleased(e->{
            String now = listView.getSelectionModel().getSelectedItem();
            if(now == null) return;
            inputArea.clear();
            inputArea.setText(now);
            button.fire();
        });




        Scene scene = new Scene(pane, 600, 400);
        primaryStage.setTitle("My Dictionary");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox getTop() {
        HBox top = new HBox(40);
        top.setAlignment(Pos.CENTER);
        Label label = new Label("Word:");
        label.setTranslateX(30);
        TextField text = new TextField();
        Button button = new Button("Search");

        top.setPadding(new Insets(30, 30, 30, 30));
        top.setStyle("-fx-background-color: gold");
        top.getChildren().addAll(label, text, button);
        return top;
    }
    private VBox getLeft(){
        VBox left = new VBox(10);
        left.setPadding(new Insets(30, 30, 30, 40));
        Label label = new Label("Explanation: ");
        TextArea text = new TextArea();
        text.setEditable(false);
        text.setWrapText(true);
        text.setPrefSize(250, 200);
        left.getChildren().addAll(label, text);
        return left;
    }
    private VBox getRight(){
        VBox right = new VBox(10);
        right.setPadding(new Insets(30, 30, 30, 30));
        Label label = new Label("Similar words: \n");
        label.setTranslateX(-20);
        ListView<String> list = new ListView<>();
        list.setTranslateX(-20);

        list.setPrefSize(200, 200);
        right.getChildren().addAll(label, list);
        return right;
    }
    private String exHandle(String ex){
        for(int i = 0; i < ex.length(); i++){
            if(ex.charAt(i) >= 'a' && ex.charAt(i) <= 'z' && i != 0 && !(ex.charAt(i-1) >= 'a' && ex.charAt(i-1) <= 'z')){
                String temp1 = ex.substring(0, i);
                String temp2 = ex.substring(i);
                ex = temp1 + "\n\t" + temp2;
                i = i + 2;
            }
        }
        while(ex.charAt(0) == '\n' || ex.charAt(0) == ' ' || ex.charAt(0) == '\t'){
            ex = ex.substring(1);
        }
        return ex;
    }

    public static void main(String[] args){
        Application.launch(args);
    }
}
