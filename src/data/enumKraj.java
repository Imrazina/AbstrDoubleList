
package data;

public enum enumKraj {
    PRAHA("Praha"),    
    JIHOCESKY("Jihocesky"),
    JIHOMORAVSKY("Jihomoravsky"),   
    KARLOVARSKY("Karlovarsky"),
    KRAVYSOCINA("Kravysocina"),  
    KRALOVEHRADECKY("Kralovehradecky"),
    LIBERECKY("Liberecky"),  
    MORAVSKOSLEZSKY("Moravskoslezsky"),
    OLOMOUCKY("Olomoucky"), 
    PARDUBICKY("Pardubicky"),
    PLZENSKY("Plzensky"),  
    STREDOCESKY("Stredocesky"),
    USTECKY("Ustecky"),  
    ZLINSKY("Zlinsky");
    
    private final String nazev;
    
    private enumKraj(String nazev) {
        this.nazev = nazev;
    }
   
    public String nazev(){
    return nazev;
    }
}
