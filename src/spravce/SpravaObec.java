
package spravce;

import data.Obec;
import data.Obyvatele;
import data.enumKraj;
import data.enumPozice;
import generator.Generator;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import sema.AbstrDoubleList;
import sema.KolekceException;


public class SpravaObec implements Ovladani {
    
    private Obyvatele obyvatelstvo = new Obyvatele();
    
    private Comparator<? super Obec> comparator;
    
    private static final AbstrDoubleList<Obec> instance = new AbstrDoubleList<>();
    private static final Iterator<Obec> itr = instance.iterator();
    
    public static AbstrDoubleList<Obec> getInstance() {
        return instance;
    }

    @Override
    public void novy(String nazev, int psc, int pocetZen, int pocetMuzu, enumPozice pozice, enumKraj kraj) {
           Obec obec = new Obec(nazev, psc, pocetZen, pocetMuzu, kraj, pozice);  

    try {
        switch (pozice) {
            case PRVNI -> { 
                if (instance.jePrazdny()) {
                    instance.vlozPrvni(obec);
                } else {
                    instance.vlozPrvni(obec);
                }
            }
            case POSLEDNI -> { 
                if (instance.jePrazdny()) {
                    instance.vlozPrvni(obec);
                } else {
                    instance.zpristupniPosledni();
                    instance.vlozNaslednika(obec);
                }
            }
            case NASLEDNIK -> { 
                if (instance.jePrazdny()) {
                    instance.vlozPrvni(obec);
                } else {
                    instance.vlozNaslednika(obec);
                }
            }
            case PREDCHUDCE -> { 
                if (instance.jePrazdny()) {
                    instance.vlozPrvni(obec);
                } else {
                    instance.vlozPredchudce(obec);
                }
            }
        }
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    }

    @Override
    public void nastavKomparator(Comparator<? super Obec> comparator) {
        Objects.requireNonNull(comparator);
        this.comparator = comparator;
    }

    @Override
    public Obec odeber() {
        try {
           return instance.odeberAktualni();
        } catch (Exception e) {
            e = new OvladaniException("element is not set");
            return null;
        }
    }

    @Override
    public void prvni() {
        if (instance.jePrazdny()) {
        throw new IllegalStateException("List is empty, cannot access first element.");
    }
        try {
            instance.zpristupniPrvni();
        } catch (Exception e) {
            e = new OvladaniException("element is not set");
        }
    }

    @Override
    public void posledeni() {
        if (instance.jePrazdny()) {
        throw new IllegalStateException("List is empty, cannot access first element.");
    }
        try {
            instance.zpristupniPosledni();
        } catch (Exception e) {
            e = new OvladaniException("element is not set");
        }
    }

    @Override
    public void predchozi() {
        if (instance.jePrazdny()) {
        throw new IllegalStateException("List is empty, cannot access first element.");
    }
        try {
            instance.zpristupniPredchudce();
        } catch (Exception e) {
            e = new OvladaniException("element is not set");
        }
    }

    @Override
    public void naslednik() {
        if (instance.jePrazdny()) {
        throw new IllegalStateException("List is empty, cannot access first element.");
    }
        try {
            instance.zpristupniNaslednika();
        } catch (Exception e) {
            e = new OvladaniException("element is not set");
        }
    }
    
     public void vlozPolozku(Obec data) throws KolekceException {
        instance.vlozPosledni(data);
    }
    
    @Override
    public Obec dej() {
        try {
            return instance.zpristupniAktualni();
        } catch (Exception e) {
            e = new OvladaniException("element is not set");
            return null;
        }
    }

    @Override
    public void generuj(int pocet) {
        try {
            Generator.generujObce(pocet, instance);
            try {
                instance.zpristupniPosledni();
            } catch (Exception ex) {
                ex = new OvladaniException("element is not set");
            }
        } catch (Exception ex) {
            ex = new OvladaniException("element is not set");
        }
    }

    @Override
    public int dejPocet() {
        return instance.size();
    }

    @Override
    public void vypis(Consumer<Obec> writer) {
       int count = 0;
    for (Obec obec : instance) {
        count++;
        writer.accept(obec);
    }
      System.out.println("Size: " + count);
    }

    @Override
    public void nactiText(String nazev) {
        try {
            int importedCount = obyvatelstvo.importData(nazev);
            System.out.println("Size: " + importedCount);
        } catch (KolekceException ex) {
            Logger.getLogger(SpravaObec.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void ulozText(String nazev) {
          try {
            FileWriter writer = new FileWriter(nazev);
            while (itr.hasNext()) {
                writer.write(itr.next().toString() + "\n");
            }
            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    @Override
    public void zrus() {
        instance.zrus();
    }

    @Override
    public Iterator<Obec> iterator() {  
        return instance.iterator(); 
    }

    @Override
    public void vyjmi() {
        try {
            instance.odeberAktualni();
        } catch (Exception e) {
            throw new RuntimeException("Aktualni is not set");
        }
    }
    
}
