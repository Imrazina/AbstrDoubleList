
package data;

public interface IObyvatele {
    
//    int importData(String soubor, instance);
    void vlozObec(Obec obec, enumPozice pozice, enumKraj kraj);
    Obec zpristupniObec(enumPozice pozice, enumKraj kraj);
    Obec odeberObec(enumPozice pozice, enumKraj kraj);
    float zjistiPrumer(enumKraj kraj);
    void zobrazObce(enumKraj kraj);
    void zobrazObceNadPrumer(enumKraj kraj);
    void zrus(enumKraj kraj);
}
