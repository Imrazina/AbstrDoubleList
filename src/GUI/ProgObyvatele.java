package GUI;

import data.Obec;
import data.Obyvatele;
import data.enumKraj;
import data.enumPozice;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
import javax.swing.JOptionPane;
import sema.AbstrDoubleList;
import sema.IAbstrDoubleList;
import sema.KolekceException;
import spravce.SpravaObec;

public class ProgObyvatele extends Application {

    private Obyvatele obyvatelstvo = new Obyvatele();

    private final BorderPane bp = new BorderPane();
    private ListView<Obec> ObecView;
    private ComboBox<enumKraj> cbFiltr;
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

        Scene scene = new Scene(bp, 830, 600);
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
        horizontalPanel.setPadding(new Insets(15, 115, 15, 15));

        Button generujBtn = new Button("Generuj");
        generujBtn.setOnAction(e -> {
            generuj();
        });

//        Button ulozBtn = new Button("Uloz");
//        ulozBtn.setOnAction(e -> {
//            spravce.ulozText("kraje.csv");
//        });
        Button nactiBtn = new Button("Nacti");
        nactiBtn.setOnAction(e -> {
            ObecView.getItems().clear();
            spravce.nactiText("/Users/shabossova/NetBeansProjects/semA/src/command/kraje.csv");
            System.out.println("Количество объектов после импорта: " + spravce.dejPocet());
            vypis();
        });

        Button novyBtn = new Button("Novy");
        novyBtn.setOnAction(e -> {
            DialogNovy dialog = DialogNovy.getDialogNovy();
            Obec obec = dialog.dejResult();
            if (obec != null) {
                try {
                    spravce.vlozPolozku(obec);
                } catch (KolekceException ex) {
                    error("ERROR");
                }
                ObecView.getItems().add(obec);
            }

        });

        Button zrusBtn = new Button("Zrus");
        zrusBtn.setOnAction(e -> {
            spravce.zrus();
            ObecView.getItems().clear();
        });

        Button[] buttonsHor = {generujBtn, nactiBtn, novyBtn, zrusBtn};

        for (Button button : buttonsHor) {
            button.setMaxSize(70, 30);
            button.setMinSize(70, 30);
        }

        horizontalPanel.getChildren().addAll(generujBtn, nactiBtn, novyBtn,
                zrusBtn);
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

        Button zobrazObceBtn = new Button("Zobraz");
        zobrazObceBtn.setOnAction(e -> {
            Dialog<enumKraj> dialog = new Dialog<>();
            dialog.setTitle("Выбор края");

            ComboBox<enumKraj> krajComboBox = new ComboBox<>();
            krajComboBox.getItems().addAll(enumKraj.values());

            VBox dialogVBox = new VBox();
            dialogVBox.setSpacing(10);
            dialogVBox.getChildren().addAll(new Label("Выберите край:"), krajComboBox);

            dialog.getDialogPane().setContent(dialogVBox);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    return krajComboBox.getValue();
                }
                return null;
            });

            dialog.showAndWait().ifPresent(selectedKraj -> {
                ObecView.getItems().clear();  // Очищаем список перед обновлением

               if (selectedKraj != null) {
    obyvatelstvo.zobrazObce(selectedKraj);

    // Очищаем ListView перед обновлением
    ObecView.getItems().clear();

    // Добавляем только те общины, которые относятся к выбранному краю
    AbstrDoubleList<Obec> instance = (AbstrDoubleList<Obec>) obyvatelstvo.getObceByKraj(selectedKraj);

    Iterator<Obec> iterator = instance.iterator();
    while (iterator.hasNext()) {
        Obec obec = iterator.next();
        ObecView.getItems().add(obec);  // Добавляем объект в ListView
    
    }

} else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Внимание");
                    alert.setHeaderText(null);
                    alert.setContentText("Край не был выбран!");
                    alert.showAndWait();
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
                            alert.setTitle("Результат удаления");
                            alert.setHeaderText(null);
                            alert.setContentText("Удалена община: " + odebranaObec);
                            alert.showAndWait();

                            ObecView.getItems().remove(odebranaObec);
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Результат удаления");
                            alert.setHeaderText(null);
                            alert.setContentText("Нет общин для удаления.");
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText(null);
                        alert.setContentText("Позиция или край не были выбраны!");
                        alert.showAndWait();
                    }
                }
                return null;
            });

            dialog.showAndWait();
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
                posledniBtn, vijmiBtn, zobrazBtn, zobrazObceBtn, zjistiPrumerBtn, odeberBtn, clearBtn};
            for (Button button : buttonsVert) {
                button.setMinSize(150, 30);
                button.setMaxSize(150, 30);
            }

            verticalPanel.getChildren().addAll(prochLbl, prvniBtn, nasledniBtn, predchoziBtn,
                    posledniBtn, prikazyLbl, vijmiBtn, zobrazBtn, zobrazObceBtn, zjistiPrumerBtn, odeberBtn, clearBtn);
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
        System.out.println("Количество объектов в списке: " + spravce.dejPocet());
        spravce.vypis(obec -> {
            ObecView.getItems().add(obec);
            System.out.println("Объект добавлен: " + obec);
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
