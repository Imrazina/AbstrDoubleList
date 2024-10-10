
package data;

public enum enumPozice {
    PRVNI("prvni"),
    POSLEDNI("posledni"),
    PREDCHUDCE("predchudce"),
    NASLEDNIK("naslednik"),
    AKTUALNI("aktyalni");
    
   private final String nazev;
    
     private enumPozice(String nazev) {
        this.nazev = nazev;
    }
   
    public String nazev(){
    return nazev;
    }
}
