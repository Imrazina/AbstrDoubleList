
package generator;

import java.util.Random;
import data.Obec;
import data.Obyvatele;
import data.enumKraj;
import data.enumPozice;
import static data.enumPozice.POSLEDNI;
import sema.AbstrDoubleList;
import sema.KolekceException;
import spravce.SpravaObec;


public class Generator {
    private static Random random = new Random();
    static Obyvatele obyvatelstvo = new Obyvatele();
    private static final AbstrDoubleList<Obec> instance = SpravaObec.getInstance();
 
    private static String[] prefixes = {"Krav", "Od", "Fren", "Nov", "Vla", "Zla", "Mor", "Rad"};

    private static String[] suffixes = {"ava", "ov", "ek", "ice", "ky", "sko", "na", "ost"};

    private static String generateNazev() {
        String prefix = prefixes[random.nextInt(prefixes.length)];
        String suffix = suffixes[random.nextInt(suffixes.length)];
        return prefix + suffix;
    }
    
    public static void generujObce(int pocet, AbstrDoubleList<Obec> instance) throws KolekceException {

        for (int i = 0; i < pocet; i++) {
            String nazev = generateNazev();
            int psc = 1000 + random.nextInt(9000); 
            int pocetMuzu = random.nextInt(1000); 
            int pocetZen = random.nextInt(1000); 
            
            enumKraj kraj = enumKraj.values()[random.nextInt(enumKraj.values().length)];
            enumPozice pozice = (i == 0) ? enumPozice.PRVNI : enumPozice.POSLEDNI;
            
            Obec novyObec = new Obec(nazev, psc, pocetMuzu, pocetZen, kraj, pozice);
            obyvatelstvo.vlozObec(novyObec, pozice, kraj);      
        }
    }}
