package spravce;

import data.Obec;
import data.Obyvatele;
import data.enumKraj;
import data.enumPozice;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


interface Ovladani extends Iterable<Obec> {
    void novy(String nazev, int psc, int pocetZen, int pocetMuzu, enumPozice pozice, enumKraj kraj);
    
    void nastavKomparator(Comparator<? super Obec> comparator);

    Obec odeber();

    void prvni();

    void posledeni();
    
    void predchozi();
    
    void naslednik();

    Obec dej();

    void generuj(int pocet);

    int dejPocet();

    void vypis(Consumer<Obec> writer);

    void nactiText(String nazev);
    void ulozText(String nazev);

    void zrus();
    
    void vyjmi();
    
    default Stream<Obec> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
