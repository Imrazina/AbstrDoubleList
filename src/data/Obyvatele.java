package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sema.AbstrDoubleList;
import sema.IAbstrDoubleList;
import sema.KolekceException;
import spravce.SpravaObec;

public class Obyvatele implements IObyvatele {
    private static final AbstrDoubleList<Obec> instance = SpravaObec.getInstance();
   

    public IAbstrDoubleList<Obec>[] kraje;

    public Obyvatele() {
        kraje = new IAbstrDoubleList[enumKraj.values().length];
        for (int i = 0; i < kraje.length; i++) {
            kraje[i] = new AbstrDoubleList<>(); 
        }
    }
    
//    @Override
    public int importData(String soubor) throws KolekceException {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(soubor))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                int ciscoKraji = Integer.parseInt(data[0]);
                String krajNazev = data[1];
                int psc = Integer.parseInt(data[2]);
                String nazev = data[3];
                int pocetMuzu = Integer.parseInt(data[4]);
                int pocetZen = Integer.parseInt(data[5]);
                int celkem = Integer.parseInt(data[6]);

                enumKraj kraj = enumKraj.values()[ciscoKraji - 1];
                Obec obec = new Obec(nazev, psc, pocetMuzu, pocetZen);
                System.out.println("Импортирование общины: " + obec);

                vlozObec(obec, enumPozice.POSLEDNI, kraj);
                instance.vlozPosledni(obec);
                count++;
                
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number: " + e.getMessage());
            e.printStackTrace();
        } 
      System.out.println("Импортировано объектов: " + count);
    return count;
    }

    @Override
    public void vlozObec(Obec obec, enumPozice pozice, enumKraj kraj) {
        IAbstrDoubleList<Obec> seznamObci = kraje[kraj.ordinal()];

        try {
            switch (pozice) {
                case PRVNI:
                    seznamObci.vlozPrvni(obec);
                    break;

                case POSLEDNI:
                    seznamObci.vlozPosledni(obec);
                    break;

                case NASLEDNIK:
                    seznamObci.vlozNaslednika(obec);
                    break;

                case PREDCHUDCE:
                    seznamObci.vlozPredchudce(obec);
                    break;

                default:
                    throw new IllegalArgumentException("Unknown position: " + pozice);
            }
        } catch (KolekceException e) {
            System.err.println("Error adding community: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Добавление объекта: " + obec + " в позицию: " + pozice + " для края: " + kraj);
    }

    @Override
    public Obec zpristupniObec(enumPozice pozice, enumKraj kraj) {
        IAbstrDoubleList<Obec> seznamObci = kraje[kraj.ordinal()];
        
        if (seznamObci.jePrazdny()) {
        System.err.println("The list is empty, there are no elements to access.");
        return null;
    }

    Obec obec = null;


        try {
            switch (pozice) {
                case PRVNI:
                    obec = seznamObci.zpristupniPrvni();
                    break;
                case POSLEDNI:
                    obec = seznamObci.zpristupniPosledni();
                    break;
                case NASLEDNIK:
                    obec = seznamObci.zpristupniNaslednika();
                    break;
                case PREDCHUDCE:
                    obec = seznamObci.zpristupniPredchudce();
                    break;
                case AKTUALNI:
                    obec = seznamObci.zpristupniAktualni();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown position: " + pozice);
            }
        } catch (KolekceException e) {
            System.err.println("Error accessing community: " + e.getMessage());
            e.printStackTrace();
        }
        return obec;
    }

    @Override
    public Obec odeberObec(enumPozice pozice, enumKraj kraj) {
        
        System.out.println("Количество объектов в списке: " + instance.size());
        if (instance.jePrazdny()) {
        System.err.println("The list is empty and the item cannot be deleted.");
        return null;
    }
        Obec odebranaObec = null;

        try {
            switch (pozice) {
                case PRVNI:
                    odebranaObec = instance.odeberPrvni();
                    break;

                case POSLEDNI:
                    odebranaObec = instance.odeberPosledni();
                    break;

                case NASLEDNIK:
                    odebranaObec = instance.odeberNaslednika();
                    break;

                case PREDCHUDCE:
                    odebranaObec = instance.odeberPredchudce();
                    break;

                case AKTUALNI:
                    odebranaObec = instance.odeberAktualni();
                    break;

                default:
                    throw new IllegalArgumentException("Unknown position: " + pozice);
            }
        } catch (KolekceException e) {
            System.err.println("Error deleting community: " + e.getMessage());
            e.printStackTrace();
        }
        
     

        return odebranaObec;
    }

    @Override
    public float zjistiPrumer(enumKraj kraj) {


        System.out.println("Список общин для " + kraj + ": " + instance);

        if (instance.jePrazdny()) {
            System.out.println("Список общин для " + kraj + " пуст.");
            return 0;
        }

        int celkovyPocetObyvatel = 0;
        int pocetObci = 0;

        Iterator<Obec> iterator = instance.iterator();
        while (iterator.hasNext()) {
            Obec obec = iterator.next();
            System.out.println("Обрабатывается община: " + obec);  
            celkovyPocetObyvatel += obec.getCelkem();
            pocetObci++;
        }

        if (pocetObci == 0) {
            return 0;
        }
        System.out.println("Обработано общин: " + pocetObci + ", Общее население: " + celkovyPocetObyvatel);
        return (float) celkovyPocetObyvatel / pocetObci;

    }


      @Override
      public void zobrazObce(enumKraj kraj) {
 IAbstrDoubleList<Obec> seznamObci = kraje[kraj.ordinal()]; // Получаем список общин для выбранного края

    if (seznamObci.jePrazdny()) {
        System.out.println("Нет общин для края: " + kraj);
        return;
    }

    System.out.println("Общины для края: " + kraj);
    
    Iterator<Obec> iterator = seznamObci.iterator();
    while (iterator.hasNext()) {
        Obec obec = iterator.next();
        System.out.println(obec);  
    }
      }
    
     
    @Override
    public void zobrazObceNadPrumer(enumKraj kraj) {
         float prumer = zjistiPrumer(kraj);

    if (kraj == null) { 
        for (IAbstrDoubleList<Obec> seznam : kraje) {
            for (Obec obec : seznam) {
                if (obec.getCelkem() > prumer) {
                    System.out.println(obec);
                }
            }
        }
    } else { 
        IAbstrDoubleList<Obec> seznamObci = kraje[kraj.ordinal()];
        for (Obec obec : seznamObci) {
            if (obec.getCelkem() > prumer) {
                System.out.println(obec);
            }
        }
    }
    }

    @Override
    public void zrus(enumKraj kraj) {
        IAbstrDoubleList<Obec> seznamObci = kraje[kraj.ordinal()];

        try {
            seznamObci.zrus();
        } catch (Exception e) {
            System.err.println("Error clearing communities: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
