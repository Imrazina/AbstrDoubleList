package GUI;

import data.Obec;
import data.Obyvatele;
import data.enumKraj;
import data.enumPozice;
import java.util.Arrays;
import java.util.Optional;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sema.KolekceException;
import spravce.SpravaObec;

public class ProgObyvatele extends Application {

    private Obyvatele obyvatelstvo = new Obyvatele();

    private final BorderPane bp = new BorderPane();
    private ListView<Obec> ObecView;
    private ComboBox<enumKraj> cbFiltr;
    private ComboBox<enumPozice> cbPozice;
    private VBox verticalPanel;
    private HBox horizontalPanel;
    private final SpravaObec spravce = new SpravaObec();

    static void main(String[] args) {
        Application.launch("");
    }

    @Override
    public void start(Stage stage) {
        setUpObecView();
        setUpRightPanel();
        setUpBottomPanel();
        closeApp(stage);

        Scene scene = new Scene(bp, 1200, 800);
        stage.setScene(scene);
        stage.show();
    }

    private void error(String msg) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("ERROR");
        error.setHeaderText(msg);
        error.showAndWait();
    }

    private void closeApp(Stage stage) {
        stage.setOnCloseRequest(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Close confirmation");
            alert.setHeaderText("Zavrit aplikace?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                stage.close();
            }
        });
    }

    private void setUpBottomPanel() {
        horizontalPanel = new HBox();
        horizontalPanel.setAlignment(Pos.CENTER);
        horizontalPanel.setSpacing(15);
        horizontalPanel.setMinWidth(115);
        horizontalPanel.setPadding(new Insets(15, 215, 15, 15));

        Button generujBtn = new Button("Generuj");
        generujBtn.setOnAction(e -> {
            generuj();
        });

        Button ulozBtn = new Button("Uloz");
        ulozBtn.setOnAction(e -> {
            spravce.ulozText("kraje.csv");
        });

        Button nactiBtn = new Button("Nacti");
        nactiBtn.setOnAction(e -> {
            ObecView.getItems().clear();
            spravce.nactiText("kraje.csv");
            System.out.println("Size after importing: " + spravce.dejPocet());
            vypis();
        });

        Button novyBtn = new Button("Novy");
        novyBtn.setOnAction(e -> {
            DialogNovy dialog = DialogNovy.getDialogNovy();
            Obec obec = dialog.dejResult();
            if (obec != null) {
                ObecView.getItems().add(obec);
                } else {
                    error("Obec je null");
            }
            

        });

        Button zrusBtn = new Button("Zrus");
        zrusBtn.setOnAction(e -> {
            spravce.zrus();
            ObecView.getItems().clear();
        });
        
        Button zrusKrajBtn = new Button("Zrus Podle kraju");
        zrusKrajBtn.setOnAction(e -> {
            Dialog<enumKraj> dialog = new Dialog<>();
            dialog.setTitle("Zrus podle kraju");

            ComboBox<enumKraj> krajComboBox = new ComboBox<>();
            krajComboBox.getItems().addAll(enumKraj.values());
            krajComboBox.setValue(enumKraj.PRAHA);

            VBox dialogVBox = new VBox();
            dialogVBox.setSpacing(10);
            dialogVBox.getChildren().add(krajComboBox);

            dialog.getDialogPane().setContent(dialogVBox);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    enumKraj selectedKraj = krajComboBox.getValue();
                    if (selectedKraj != null) {
                        obyvatelstvo.zrus(selectedKraj);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Vysledek: ");
                            alert.setHeaderText(null);
                            alert.setContentText("Schvaleno! byli odebrany obci ze kraju" + selectedKraj);
                            alert.showAndWait();
                        ObecView.getItems().removeIf(obec -> obec.getKraj().equals(selectedKraj));
                    } else {
                        error("Kraj nebyl vybran");
                    }
                }
                return null;
            });

            dialog.showAndWait();
        });

        Button[] buttonsHor = {generujBtn, nactiBtn,ulozBtn, novyBtn, zrusBtn, zrusKrajBtn};

        for (Button button : buttonsHor) {
            button.setMaxSize(120, 30);
            button.setMinSize(120, 30);
        }

        horizontalPanel.getChildren().addAll(generujBtn, nactiBtn,ulozBtn, novyBtn,
                zrusBtn, zrusKrajBtn);
        bp.setBottom(horizontalPanel);
    }

    private void setUpRightPanel() {
        verticalPanel = new VBox();
        verticalPanel.setAlignment(Pos.CENTER);
        verticalPanel.setSpacing(15);
        verticalPanel.setPadding(new Insets(15));

        Label prochLbl = new Label("Prochazeni");

        Button prvniBtn = new Button("Prvni");
        prvniBtn.setOnAction(e -> {
            kontrolaPrazdnoty();
            spravce.prvni();
            ObecView.getSelectionModel().select(spravce.dej());
        });

        Button nasledniBtn = new Button("Naslednik");
        nasledniBtn.setOnAction(e -> {
            kontrolaPrazdnoty();
            spravce.naslednik();
            ObecView.getSelectionModel().select(spravce.dej());
        });

        Button predchoziBtn = new Button("Predchozi");
        predchoziBtn.setOnAction(e -> {
            kontrolaPrazdnoty();
            spravce.predchozi();
            ObecView.getSelectionModel().select(spravce.dej());
        });

        Button posledniBtn = new Button("Posledni");
        posledniBtn.setOnAction(e -> {
            kontrolaPrazdnoty();
            spravce.posledeni();
            ObecView.getSelectionModel().select(spravce.dej());
        });

        Label prikazyLbl = new Label("Prikazy");

        Button zobrazBtn = new Button("Zobraz");
        zobrazBtn.setOnAction(e -> {
            kontrolaPrazdnoty();
            if (ObecView.getItems().isEmpty()) {
                vypis();
            }
        });

        Button zobrazObceBtn = new Button("Zobraz podle kraju");
        zobrazObceBtn.setOnAction(e -> {
            Dialog<enumKraj> dialog = new Dialog<>();
            dialog.setTitle("Vyberte kraj");

            ComboBox<enumKraj> cbFiltr = new ComboBox<>();
            cbFiltr.getItems().addAll(enumKraj.values());
            cbFiltr.setValue(enumKraj.PRAHA);

            VBox dialogVBox = new VBox();
            dialogVBox.setSpacing(10);
            dialogVBox.getChildren().addAll(new Label("Vyberte kraj:"), cbFiltr);

            dialog.getDialogPane().setContent(dialogVBox);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    return cbFiltr.getValue();
                }
                return null;
            });

            dialog.showAndWait().ifPresent(selectedKraj -> {
                ObecView.getItems().clear();
                if (selectedKraj != null) {
            Obec[] obceArray = obyvatelstvo.zobrazObce(selectedKraj); 
            
            for (Obec obec : obceArray) {
                ObecView.getItems().add(obec);  
            }
            
          } else {
                    error("ERROR");
                }
            });
        });

        Button zjistiPrumerBtn = new Button("Zjisti Prumer");
        zjistiPrumerBtn.setOnAction(e -> {
            Dialog<enumKraj> dialog = new Dialog<>();
            dialog.setTitle("Zjisti Prumer");

            ComboBox<enumKraj> krajComboBox = new ComboBox<>();
            krajComboBox.getItems().addAll(enumKraj.values());

            VBox dialogVBox = new VBox();
            dialogVBox.setSpacing(10);
            dialogVBox.getChildren().add(krajComboBox);

            dialog.getDialogPane().setContent(dialogVBox);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    enumKraj selectedKraj = krajComboBox.getValue();
                    if (selectedKraj != null) {
                        float prumer = 0;
                        prumer = obyvatelstvo.zjistiPrumer(selectedKraj);
                          System.out.println("Průměr pro kraj " + selectedKraj + ": " + prumer);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Vysledek: ");
                            alert.setHeaderText(null);
                            alert.setContentText("Prumer pro kraj: " + prumer);
                            alert.showAndWait();
                    } else {
                        System.out.println("Kraj nebyl vybrán!");
                    }
                }
                return null;
            });

            dialog.showAndWait();
        });

        Button odeberBtn = new Button("Odeber");
        odeberBtn.setOnAction(e -> {
            Dialog<enumPozice> dialog = new Dialog<>();
            dialog.setTitle("Odeber Obec");

            ComboBox<enumPozice> poziceComboBox = new ComboBox<>();
            poziceComboBox.getItems().addAll(enumPozice.values());

            ComboBox<enumKraj> krajComboBox = new ComboBox<>();
            krajComboBox.getItems().addAll(enumKraj.values());

            VBox dialogVBox = new VBox();
            dialogVBox.setSpacing(10);
            dialogVBox.getChildren().addAll(new Label("Vyberte pozici:"), poziceComboBox, new Label("Vyberte kraj:"), krajComboBox);

            dialog.getDialogPane().setContent(dialogVBox);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    enumPozice selectedPozice = poziceComboBox.getValue();
                    enumKraj selectedKraj = krajComboBox.getValue();
                    if (selectedPozice != null && selectedKraj != null) {
                        Obec odebranaObec = obyvatelstvo.odeberObec(selectedPozice, selectedKraj);
                        if (odebranaObec != null) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Vysledek: ");
                            alert.setHeaderText(null);
                            alert.setContentText("Obec odstranen: " + odebranaObec);
                            alert.showAndWait();

                            ObecView.getItems().remove(odebranaObec);
                        } else {
                            error("Nejsou žádné obcy ke smazání.");
                        }
                    } else {
                        error("Pozice nebo kraj nejsou vybrany");
                    }
                }
                return null;
            });

            dialog.showAndWait();
        });
        
        Button zpristupniObecBtn = new Button("Zpristupni Obec");
        zpristupniObecBtn.setOnAction(e -> {
            Dialog<enumPozice> dialog = new Dialog<>();
            dialog.setTitle("Zpristupni Obec");

            ComboBox<enumPozice> poziceComboBox = new ComboBox<>();
            poziceComboBox.getItems().addAll(enumPozice.values());

            ComboBox<enumKraj> krajComboBox = new ComboBox<>();
            krajComboBox.getItems().addAll(enumKraj.values());

            VBox dialogVBox = new VBox();
            dialogVBox.setSpacing(10);
            dialogVBox.getChildren().addAll(new Label("Vyberte pozici:"), poziceComboBox, new Label("Vyberte kraj:"), krajComboBox);

            dialog.getDialogPane().setContent(dialogVBox);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    enumPozice selectedPozice = poziceComboBox.getValue();
                    enumKraj selectedKraj = krajComboBox.getValue();
                    if (selectedPozice != null && selectedKraj != null) {
                        Obec zpristupniObec = obyvatelstvo.zpristupniObec(selectedPozice, selectedKraj);
                        if (zpristupniObec != null) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Vysledek: ");
                            alert.setHeaderText(null);
                            alert.setContentText("Zpristupni Obec: " + zpristupniObec);
                            alert.showAndWait();
                        } else {
                            error("Nejsou žádné obcy.");
                        }
                    } else {
                        error("Pozice nebo kraj nejsou vybrany");
                    }
                }
                return null;
            });

            dialog.showAndWait();
        });
        
        Button ZobrazObceNadPrumerBtn = new Button("Zobraz Obce Nad Prumer");
        ZobrazObceNadPrumerBtn.setOnAction(e -> {
            Dialog<enumKraj> dialog = new Dialog<>();
            dialog.setTitle("Vyberte kraj");

            ComboBox<enumKraj> cbFiltr = new ComboBox<>();
            cbFiltr.getItems().addAll(enumKraj.values());
            cbFiltr.setValue(enumKraj.PRAHA);

            VBox dialogVBox = new VBox();
            dialogVBox.setSpacing(10);
            dialogVBox.getChildren().addAll(new Label("Vyberte kraj:"), cbFiltr);

            dialog.getDialogPane().setContent(dialogVBox);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    return cbFiltr.getValue();
                }
                return null;
            });

            dialog.showAndWait().ifPresent(selectedKraj -> {
                ObecView.getItems().clear();
                if (selectedKraj != null) {
            Obec[] obceArray = obyvatelstvo.zobrazObceNadPrumer(selectedKraj); 
            
            for (Obec obec : obceArray) {
                ObecView.getItems().add(obec);  
            } } else {
                    error("ERROR");
                }
            });
        });

        Button clearBtn = new Button("Clear");
        clearBtn.setOnAction(e -> {
            ObecView.getItems().clear();
        });

        Button vijmiBtn = new Button("Vijmi");
        vijmiBtn.setOnAction(e -> {
            if (spravce.dej() != null) {
                spravce.vyjmi();
                ObecView.getItems().remove(ObecView.getSelectionModel().getSelectedItem());
            }
        });

        Button[] buttonsVert = {prvniBtn, nasledniBtn, predchoziBtn,
            posledniBtn, vijmiBtn, zobrazBtn, zobrazObceBtn, zjistiPrumerBtn, odeberBtn, zpristupniObecBtn, ZobrazObceNadPrumerBtn, clearBtn};
        for (Button button : buttonsVert) {
            button.setMinSize(200, 30);
            button.setMaxSize(200, 30);
        }

        verticalPanel.getChildren().addAll(prochLbl, prvniBtn, nasledniBtn, predchoziBtn,
                posledniBtn, prikazyLbl, vijmiBtn, zobrazBtn, zobrazObceBtn, zjistiPrumerBtn, odeberBtn,
                zpristupniObecBtn, ZobrazObceNadPrumerBtn, clearBtn);
        bp.setRight(verticalPanel);
    }

    private void setUpObecView() {
        ObecView = new ListView<>();
        bp.setCenter(ObecView);
        bp.setMaxSize(300, 300);
    }

    private void kontrolaPrazdnoty() {
        if (spravce.dejPocet() == 0) {
            error("List is empty");
        }
    }

    private void vypis() {
        System.out.println("Size: " + spravce.dejPocet());
        spravce.vypis(obec -> {
            ObecView.getItems().add(obec);
            System.out.println("Add element: " + obec);
        });
    }

    private void generuj() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Random");
        dialog.setContentText("Enter the number to generate");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            int pocet = Integer.parseInt(dialog.getEditor().getText());
            ObecView.getItems().clear();
            spravce.generuj(pocet);
            vypis();
        }

    }
}
