
package sema;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class AbstrDoubleList<T> implements IAbstrDoubleList<T> {
    public int size;
    private Prvek prvni;
    private Prvek aktualni;
    private Prvek posledni;
    
     public AbstrDoubleList() {
        this.size = 0;
        this.prvni = null;
        this.aktualni = null;
        this.posledni = null;
    }
    
    private class Prvek<T>{
        public T data;
        public Prvek<T> next;
        public Prvek<T> prev;
        public Prvek(T data, Prvek<T> next, Prvek<T> prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }


    @Override
    public void zrus() {
      size =0;
      prvni=posledni=aktualni=null;
    }

    @Override
    public boolean jePrazdny() {
        return prvni == null || size == 0;
    }

    @Override
    public void vlozPrvni(T data) {
        if (data == null) {
        throw new NullPointerException("Cannot insert null element");
    }
    Prvek newPrvek = new Prvek(data, prvni, null);
    if (prvni != null) {
        prvni.prev = newPrvek;
    }
    prvni = newPrvek; 
    if (size == 0) {
        posledni = prvni;
    }
    size++;
    }

    @Override
    public void vlozPosledni(T data) throws KolekceException {
        if(data == null){
            throw new NullPointerException("Cannot insert null element");
        }
         if(jePrazdny()){
            vlozPrvni(data);         
        } else {
            Prvek newPrvek = new Prvek(data, null, posledni);
            posledni.next = newPrvek;
            posledni = newPrvek;
            size++;
         }
    }

    @Override
    public void vlozNaslednika(T data) throws KolekceException {
        if(data==null) {
            throw new NullPointerException("Cannot insert null element");
        }
        if(jePrazdny()){
            vlozPrvni(data);
        } else {
            Prvek newPrvek = new Prvek (data, aktualni.next, aktualni);
            if(aktualni.next!=null){
                aktualni.next.prev = newPrvek;
            }
            if(aktualni==posledni){
                posledni=newPrvek;
            }
            aktualni.next = newPrvek;
            size++;
        }
 
    }

    @Override
    public void vlozPredchudce(T data) throws KolekceException {
        if(data==null) {
            throw new NullPointerException("Cannot insert null element");
        }
        if(jePrazdny()){
            vlozPrvni(data);
        } else {
            Prvek newPrvek = new Prvek (data, aktualni, aktualni.prev);
            if(aktualni.prev!=null){
                aktualni.next.prev = newPrvek;
            } else{
                prvni = newPrvek;
            }
            aktualni.prev = newPrvek;
            size++;
        }
    }

    @Override
    public T zpristupniAktualni() throws KolekceException{
        if(aktualni == null){
            throw new KolekceException("No current elements to access");
        }
        return (T) aktualni.data;
    }

    @Override
    public T zpristupniPrvni() throws KolekceException{
        if(prvni == null) {
             throw new KolekceException("No current elements to access");
        }
        
        aktualni = prvni;
        return (T) prvni.data;
    }

    @Override
    public T zpristupniPosledni() throws KolekceException{
        if(posledni == null){
            throw new KolekceException("No current elements to access");
        }
        aktualni = posledni;
        return (T) posledni.data;
    }

    @Override
    public T zpristupniNaslednika() throws KolekceException{
        if(aktualni == null || aktualni.next == null){
            throw new KolekceException("No current elements to access");
        }
        aktualni = aktualni.next;
        return (T) aktualni.data;
    }

    @Override
    public T zpristupniPredchudce() throws KolekceException{
        if(aktualni == null || aktualni.prev == null){
            throw new KolekceException("No current elements to access");
        }
        aktualni = aktualni.prev;
        return (T) aktualni.data;
    }

    @Override
    public T odeberAktualni() throws KolekceException {
        if (jePrazdny()){
            throw new KolekceException("List is empty cannot remove the element");
        }
        if(aktualni == prvni){
            return odeberPrvni();
        }
        if(aktualni == posledni){
            return odeberPosledni();
        }
        
        T data = (T) aktualni.data;
        
        aktualni.next.prev = aktualni.next;
        if(aktualni.next !=null){
            aktualni.next.prev = aktualni.prev;
        }
        
        aktualni = null;
        
        size--;
        
        return data;
    }

    @Override
    public T odeberPrvni() throws KolekceException {
        if (jePrazdny()){
            throw new KolekceException("List is empty cannot remove the element");
        }
        
        T data = (T) prvni.data;
        
        prvni = prvni.next;
        
        if(prvni==null){
            posledni = null;
        } else {
        prvni.prev = null;
        }
        
        size--;
        
        return data;
    }

    @Override
    public T odeberPosledni() throws KolekceException {
        if (jePrazdny()){
            throw new KolekceException("List is empty cannot remove the element");
        }
        
        T data = (T) posledni.data;
        
        posledni = posledni.prev;
        
        if(posledni == null){
            prvni=null;
        } else {
            posledni.next = null;
        }
        
        size--;
        
        return data;
    }

    @Override
    public T odeberNaslednika() throws KolekceException {
        if (jePrazdny()){
            throw new KolekceException("List is empty cannot remove the element");
        }
         
         if(aktualni.next == null){
             throw new KolekceException("No successors to remove");
         }
        
        if(aktualni.next == posledni){
            return odeberPosledni();
        }
        
        T data = (T) aktualni.next.data;
        
        aktualni.next = aktualni.next.next;
        
        if(aktualni.next !=null){
            aktualni.next.prev = aktualni;
        }
        
        size--;
        
        return data;
    }

    @Override
    public T odeberPredchudce() throws KolekceException {
        if (jePrazdny()){
            throw new KolekceException("List is empty cannot remove the element");
        }
        
        if(aktualni.prev == null){
             throw new KolekceException("No predecessors to remove");
        }
        
        if(aktualni.prev == prvni){
            return odeberPrvni();
        }
        
        T data = (T) aktualni.prev.data;
        
        aktualni.prev = aktualni.prev.prev;
        
        if (aktualni.prev !=null){
            aktualni.prev.next = aktualni;
        }
        size--;
        return data;
    }
    
    @Override
     public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
        Prvek<T> aktualniIterator = null;

        @Override
        public boolean hasNext() {
            if (aktualniIterator == null) {
                return prvni != null;
            }
            return aktualniIterator.next != null;
        }

        @Override
        public T next() {
            if (hasNext()) {
                if (aktualniIterator == null) {
                    aktualniIterator = prvni;
                } else {
                    aktualniIterator = aktualniIterator.next;
                }
                return aktualniIterator.data;
            }
            throw new NoSuchElementException();
        }
    };
}
}
