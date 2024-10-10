
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
    
    public Obec(String nazev, int psc, int pocetZen, int pocetMuzu) {
        this.nazev = nazev;
        this.psc = psc;
        this.pocetZen = pocetZen;
        this.pocetMuzu = pocetMuzu;
        this.celkem = pocetZen + pocetMuzu;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public void setPsc(int psc) {
        this.psc = psc;
    }

    public void setPocetZen(int pocetZen) {
        this.pocetZen = pocetZen;
    }

    public void setPocetMuzu(int pocetMuzu) {
        this.pocetMuzu = pocetMuzu;
    }

    public void setCelkem(int celkem) {
        this.celkem = celkem;
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
    
    @Override
    public String toString() {
        return "Obec{" + 
               "nazev='" + nazev + '\'' + 
               ", psc=" + psc + 
               ", pocetZen=" + pocetZen + 
               ", pocetMuzu=" + pocetMuzu + 
               ", celkem=" + celkem + 
               '}';
    }

    @Override
    public int compareTo(Obec o) {
         return nazev.compareTo(o.nazev);
    }
}
