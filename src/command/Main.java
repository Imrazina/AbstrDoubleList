package command;

import data.Obec;
import data.Obyvatele;
import data.enumKraj;
import static data.enumKraj.JIHOMORAVSKY;
import data.enumPozice;
import generator.Generator;
import java.util.logging.Level;
import java.util.logging.Logger;
import sema.IAbstrDoubleList;
import sema.KolekceException;


public class Main {

    public static void main(String[] args) {
        Obyvatele obyvatelstvo = new Obyvatele();

      
        int importedCount = 0;
        try {
            importedCount = obyvatelstvo.importData("/Users/shabossova/NetBeansProjects/semA/src/command/kraje.csv");
        } catch (KolekceException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Количество импортированных записей: " + importedCount);

        Obec novaObec = new Obec("Nová Ves", 12345, 1000, 1200);
        obyvatelstvo.vlozObec(novaObec, enumPozice.PRVNI, enumKraj.JIHOMORAVSKY);
        System.out.println("Добавлена новая община 'Nová Ves' в Jihomoravský kraj.");


        Obec Prvaobec = obyvatelstvo.zpristupniObec(enumPozice.PRVNI, enumKraj.JIHOMORAVSKY);
        if (Prvaobec != null) {
            System.out.println("Первая община в Jihomoravský kraj: " + Prvaobec);
        }

        
        Obec odebranaObec = obyvatelstvo.odeberObec(enumPozice.POSLEDNI, enumKraj.JIHOMORAVSKY);
        if (odebranaObec != null) {
            System.out.println("Удалена последняя община: " + odebranaObec);
        }
 
        float prumer = obyvatelstvo.zjistiPrumer(enumKraj.JIHOMORAVSKY);
        System.out.println("Среднее количество жителей в Jihomoravský kraj: " + prumer);

  
        System.out.println("Вывод всех общин в Jihomoravský kraj:");
        obyvatelstvo.zobrazObce(enumKraj.JIHOMORAVSKY);

 
        System.out.println("Вывод общин с населением выше среднего в Jihomoravský kraj:");
        obyvatelstvo.zobrazObceNadPrumer(enumKraj.JIHOMORAVSKY);

       
        System.out.println("Удаление всех общин в Jihomoravský kraj.");
        obyvatelstvo.zrus(enumKraj.JIHOMORAVSKY);
    }
    }
    

