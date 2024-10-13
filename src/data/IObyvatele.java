
package data;

public interface IObyvatele {
    
    void vlozObec(Obec obec, enumPozice pozice, enumKraj kraj);
    Obec zpristupniObec(enumPozice pozice, enumKraj kraj);
    Obec odeberObec(enumPozice pozice, enumKraj kraj);
    float zjistiPrumer(enumKraj kraj);
    Obec[] zobrazObce(enumKraj kraj);
    Obec[] zobrazObceNadPrumer(enumKraj kraj);
    void zrus(enumKraj kraj);
}
