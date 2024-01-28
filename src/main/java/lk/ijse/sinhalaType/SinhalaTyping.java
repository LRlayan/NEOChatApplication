package lk.ijse.sinhalaType;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class SinhalaTyping extends VBox {

    private ListView<String> sinhalaWordsView;

    public SinhalaTyping(){
        List<String> sinhala = new ArrayList<>();

        String[] sinhalaCharacter = new String[]{String.valueOf('\u0D85'), String.valueOf('\u0D86'),
                    String.valueOf('\u0D87'), String.valueOf('\u0D88'), String.valueOf('\u0D89'),
                    String.valueOf('\u0D90'), String.valueOf('\u0D91'), String.valueOf('\u0D92'),
                    String.valueOf('\u0D94'), String.valueOf('\u0D95'), String.valueOf('\u0D96'),
                    String.valueOf('\u0D9C'), String.valueOf('\u0D9A'), String.valueOf('\u0DCF'),
                    String.valueOf('\u0DDC'), String.valueOf('\u0D83'), String.valueOf('\u0D82'),
                    String.valueOf('\u0D79'), String.valueOf('\u0D78'), String.valueOf('\u0D77'),
                    String.valueOf('\u0D76'), String.valueOf('\u0D75'), String.valueOf('\u0D74'),
                    String.valueOf('\u0D73'), String.valueOf('\u0D72'), String.valueOf('\u0D71'),
                    String.valueOf('\u0D70')
        };

        for (String sinhalaChar : sinhalaCharacter){
            sinhala.add(sinhalaChar);
        }

        sinhalaWordsView = new ListView<>();
        sinhalaWordsView.setItems(FXCollections.observableArrayList(sinhala));

        HBox hBox = new HBox(sinhalaWordsView);
        hBox.setPadding(new Insets(10));

        getChildren().add(hBox);
    }

    public ListView<String> getSinhalaWordsView(){
        return sinhalaWordsView;
    }
}
