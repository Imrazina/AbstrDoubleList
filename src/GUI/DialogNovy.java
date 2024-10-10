
package GUI;

import data.Obec;
import data.enumKraj;
import data.enumPozice;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class DialogNovy {
    
    private Dialog<Obec> dialog;
    private GridPane gridPane;
    private ComboBox<String> cbPozice;
    private ComboBox<String> cbKraj;
    private TextField tfNazev = new TextField("Uvedte nazev");
    private TextField tfpocetMuzu = new TextField("0");
    private TextField tfpocetZen = new TextField("0");

    private Obec result;
    
    public DialogNovy() {
        dialog = new Dialog<>();
        dialog.setTitle("New movie");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        nastavCbPozice();
        nastavCbKraj();

        gridPane = new GridPane();
        gridPane.setMinHeight(300);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        cbPozice.getSelectionModel().clearSelection();
        ziskejObec();
        nastavGrid();
        
        dialog.getDialogPane().setContent(gridPane);
        dialog.showAndWait();
    }

    static DialogNovy getDialogNovy() {
        return new DialogNovy();
    }

    Obec dejResult() {
        return result;
    }

    private void nastavCbPozice() {
        cbPozice = new ComboBox<>();
        for (enumPozice value : enumPozice.values()) {
            cbPozice.getItems().add(value.nazev());
        }
        cbPozice.getSelectionModel().selectFirst();
    }

    private void nastavCbKraj() {
        cbKraj = new ComboBox<>();
        for (enumKraj value : enumKraj.values()) {
            cbKraj.getItems().add(value.nazev());
        }
        cbKraj.getSelectionModel().selectFirst();
    }

    private void nastavGrid() {
        gridPane.add(new Label("Pozice:"), 0, 0);
        gridPane.add(cbPozice, 1, 0);
        gridPane.add(new Label("Kraj: "), 0, 1);
        gridPane.add(tfpocetMuzu, 1, 1);
        gridPane.add(new Label("Nazev: "), 0, 2);
        gridPane.add(tfNazev, 1, 2);
        gridPane.add(new Label("Pocet Muzu: "), 0, 3);
        gridPane.add(tfpocetMuzu, 1, 3);
        gridPane.add(new Label("Pocet Zen: "), 0, 4);
        gridPane.add(tfpocetZen, 1, 4);
//        
//        cbPozice= new ComboBox();
//        for(enumPozice value: enumPozice.values()){
//            cbPozice.getItems().add(value.nazev());
//        } 
//        
//        cbKraj= new ComboBox();
//        for(enumKraj value: enumKraj.values()){
//            cbKraj.getItems().add(value.nazev());
//        }
//        
//        cbPozice.getSelectionModel().selectFirst();
//        cbKraj.getSelectionModel().selectFirst();
    }

    private void ziskejObec() {
    
    }
    
}
