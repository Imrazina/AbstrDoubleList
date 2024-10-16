
package data;

import sema.AbstrDoubleList;
import spravce.SpravaObec;

public class Obec implements Comparable<Obec>{
    private static final AbstrDoubleList<Obec> instance = SpravaObec.getInstance();
    
    private String nazev;
    private int psc;
    private int pocetZen;
    private int pocetMuzu;
    private int celkem;
    private enumPozice pozice;

    private enumKraj kraj;
    
    public Obec(String nazev, int psc, int pocetZen, int pocetMuzu, enumKraj kraj, enumPozice pozice) {
        this.nazev = nazev;
        this.psc = psc;
        this.pocetZen = pocetZen;
        this.pocetMuzu = pocetMuzu;
        this.celkem = pocetZen + pocetMuzu;
        this.kraj = kraj;
        this.pozice = pozice;
    }

    public String getNazev() {
        return nazev;
    }

    public int getPsc() {
        return psc;
    }

    public int getPocetZen() {
        return pocetZen;
    }

    public int getPocetMuzu() {
        return pocetMuzu;
    }
    
    public int getCelkem(){
        return celkem;
    }
    
     public enumPozice getPozice() {
        return pozice;
    }

    public enumKraj getKraj() {
        return kraj;
    }
    
    @Override
    public String toString() {
        return
                (kraj.ordinal() + 1) + ";" + 
               kraj + ";" + psc + ";" + nazev + ";"
                + pocetMuzu + ";" + pocetZen + ";"
               + celkem;
    }

    @Override
    public int compareTo(Obec o) {
         return nazev.compareTo(o.nazev);
    }
}
