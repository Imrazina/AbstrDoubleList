package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
                enumPozice pozice = data.length >= 8 ? enumPozice.valueOf(data[7].toUpperCase()) : enumPozice.POSLEDNI;
                enumKraj kraj = enumKraj.values()[ciscoKraji - 1];
                Obec obec = new Obec(nazev, psc, pocetMuzu, pocetZen, kraj, pozice);
                System.out.println("Imported datas: " + obec);

                vlozObec(obec, enumPozice.POSLEDNI, kraj);
                count++;

            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Size of imported datas: " + count);
        return count;
    }

    @Override
    public void vlozObec(Obec obec, enumPozice pozice, enumKraj kraj) {
        try {
            switch (pozice) {
                case PRVNI:
                    instance.vlozPrvni(obec);
                    break;

                case POSLEDNI:
                    instance.vlozPosledni(obec);
                    break;

                case NASLEDNIK:
                    instance.vlozNaslednika(obec);
                    break;

                case PREDCHUDCE:
                    instance.vlozPredchudce(obec);
                    break;

                default:
                    throw new IllegalArgumentException("Unknown position: " + pozice);
            }
        } catch (KolekceException e) {
            System.err.println("Error adding community: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Add: " + obec + "in position: " + pozice + "for region: " + kraj);
    }

    @Override
    public Obec zpristupniObec(enumPozice pozice, enumKraj kraj) {

        if (instance.jePrazdny()) {
            System.err.println("The list is empty, there are no elements to access.");
            return null;
        }

        Obec obec = null;

        try {
            switch (pozice) {
                case PRVNI:
                    obec = instance.zpristupniPrvni();
                    break;
                case POSLEDNI:
                    obec = instance.zpristupniPosledni();
                    break;
                case NASLEDNIK:
                    obec = instance.zpristupniNaslednika();
                    break;
                case PREDCHUDCE:
                    obec = instance.zpristupniPredchudce();
                    break;
                case AKTUALNI:
                    obec = instance.zpristupniAktualni();
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

        System.out.println("Size: " + instance.size());
        if (instance.jePrazdny()) {
            System.err.println("The list is empty and the item cannot be deleted.");
            return null;
        }
        
            List<Obec> filteredObce = new ArrayList<>();
            for (Obec obec : instance) {
                if (obec.getKraj() == kraj) {
                    filteredObce.add(obec);
                }
            }

            if (filteredObce.isEmpty()) {
                System.err.println("No items found for the selected region.");
                return null;
            }
        
        Obec odebranaObec = null;

        try {
            switch (pozice) {
                case PRVNI:
                odebranaObec = filteredObce.get(0);
                instance.odeberPrvni(); 
                break;

            case POSLEDNI:
                odebranaObec = filteredObce.get(filteredObce.size() - 1);
                instance.odeberPosledni(); 
                break;

            case NASLEDNIK:
                if (filteredObce.size() > 1) {
                    odebranaObec = filteredObce.get(1); 
                    instance.odeberNaslednika(); 
                } else {
                    System.err.println("There is no next item to delete.");
                }
                break;

            case PREDCHUDCE:
                if (filteredObce.size() > 1) {
                    odebranaObec = filteredObce.get(filteredObce.size() - 2); 
                    instance.odeberPredchudce(); 
                } else {
                    System.err.println("There is no previous item to delete.");
                }
                break;

            case AKTUALNI:
                odebranaObec = filteredObce.get(0); 
                instance.odeberAktualni(); 
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
        System.out.println("Regions: " + kraj + ": " + instance);

        if (instance.jePrazdny()) {
            System.out.println("Regions: " + kraj + " empty");
            return 0;
        }

        int celkovyPocetObyvatel = 0;
        int pocetObci = 0;

        Iterator<Obec> iterator = instance.iterator();
        while (iterator.hasNext()) {
            Obec obec = iterator.next();

            if (kraj == null || obec.getKraj().equals(kraj)) {
                System.out.println("Processing the community: " + obec);
                celkovyPocetObyvatel += obec.getCelkem();
                pocetObci++;
            }
        }

        if (pocetObci == 0) {
            System.out.println("List is empty for this region");
            return 0;
        }

        System.out.println("Communitys: " + pocetObci + ", Total population: " + celkovyPocetObyvatel);
        return (float) celkovyPocetObyvatel / pocetObci;

    }

    @Override
    public Obec[] zobrazObce(enumKraj kraj) {

        if (instance.jePrazdny()) {
            System.out.println("List is empty for: " + kraj);
            return new Obec[0];
        }

        List<Obec> obeclist = new ArrayList<>();
        Iterator<Obec> iterator = instance.iterator();
        while (iterator.hasNext()) {
            Obec obec = iterator.next();

            if (obec.getKraj() == kraj) {
                System.out.println(obec);
                obeclist.add(obec);
            }
        }
        return obeclist.toArray(new Obec[0]);
    }

    @Override
    public Obec[] zobrazObceNadPrumer(enumKraj kraj) {
        if (instance.jePrazdny()) {
            System.out.println("List is empty for: " + kraj);
            return new Obec[0];
        }

        float prumer;
        if (kraj == null) {
            prumer = zjistiPrumerVsechnyKraje();
        } else {
            prumer = zjistiPrumer(kraj);
        }

        List<Obec> obceNadPrumer = new ArrayList<>();

        for (Obec obec : instance) {
            if ((kraj == null || obec.getKraj() == kraj) && obec.getCelkem() > prumer) {
                obceNadPrumer.add(obec);
                System.out.println(obec);
            }
        }

        return obceNadPrumer.toArray(new Obec[0]);
    }

    private float zjistiPrumerVsechnyKraje() {
        int celkemObce = 0;
        int pocetObci = 0;

        for (Obec obec : instance) {
            celkemObce += obec.getCelkem();
            pocetObci++;
        }

        if (pocetObci == 0) {
            return 0;
        }

        return (float) celkemObce / pocetObci;
    }

    @Override
    public void zrus(enumKraj kraj) {
        if (instance.jePrazdny()) {
            System.out.println("List is empty for: " + kraj);
        }
        
         try {
        List<Obec> newObceList = new ArrayList<>();
        int count = 0;
        
        for (Obec obec : instance) {
            if (kraj == null || obec.getKraj() == kraj) {
                count++;
            } else {
                newObceList.add(obec);  
            }
        }

        instance.zrus();  
        for (Obec obec : newObceList) {
    instance.vlozPosledni(obec);  
} 
        


        System.out.println("Deleted communitys for region: " + kraj + ": " + count);
        
    } catch (Exception e) {
        System.err.println("Error because of deleting: " + e.getMessage());
        e.printStackTrace();
    }}

}
