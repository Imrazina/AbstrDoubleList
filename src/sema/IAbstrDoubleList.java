
package sema;

import java.util.Iterator;

public interface IAbstrDoubleList<T> extends Iterable<T> {
    
    void zrus();
    
    boolean jePrazdny();
    
    void vlozPrvni(T data);
    
    void vlozPosledni(T data) throws KolekceException;
    
    void vlozNaslednika(T data) throws KolekceException;
    
    void vlozPredchudce(T data) throws KolekceException;
    
    T zpristupniAktualni() throws KolekceException;
    
    T zpristupniPrvni() throws KolekceException;
    
    T zpristupniPosledni() throws KolekceException;
    
    T zpristupniNaslednika() throws KolekceException;
    
    T zpristupniPredchudce() throws KolekceException;
    
    T odeberAktualni() throws KolekceException;
    
    T odeberPrvni() throws KolekceException;
    
    T odeberPosledni() throws KolekceException;
    
    T odeberNaslednika() throws KolekceException;
    
    T odeberPredchudce() throws KolekceException;
    
    int size();

    Iterator<T> iterator();
}
