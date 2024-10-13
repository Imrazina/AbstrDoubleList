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
    
    private class Prvek<T> {
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
        size = 0;
        prvni = posledni = aktualni = null;
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
        Prvek newPrvek = new Prvek(data, prvni, posledni);
        if (jePrazdny()) {
            prvni = posledni = newPrvek;
            prvni.next = prvni.prev = newPrvek;
        } else {
            prvni.prev = newPrvek;
            posledni.next = newPrvek;
            prvni = newPrvek;
        }
        size++;
    }

    @Override
    public void vlozPosledni(T data) throws KolekceException {
        if (data == null) {
            throw new NullPointerException("Cannot insert null element");
        }
        if (jePrazdny()) {
            vlozPrvni(data);
        } else {
            Prvek newPrvek = new Prvek(data, prvni, posledni);
            posledni.next = newPrvek;
            prvni.prev = newPrvek;
            posledni = newPrvek;
            size++;
        }
    }

    @Override
    public void vlozNaslednika(T data) throws KolekceException {
        if (data == null) {
            throw new NullPointerException("Cannot insert null element");
        }
        if (jePrazdny()) {
            vlozPrvni(data);
        } else {
            Prvek newPrvek = new Prvek(data, aktualni.next, aktualni);
            aktualni.next.prev = newPrvek;
            aktualni.next = newPrvek;
            if (aktualni == posledni) {
                posledni = newPrvek;
            }
            size++;
        }
    }

    @Override
    public void vlozPredchudce(T data) throws KolekceException {
        if (data == null) {
            throw new NullPointerException("Cannot insert null element");
        }
        if (jePrazdny()) {
            vlozPrvni(data);
        } else {
            Prvek newPrvek = new Prvek(data, aktualni, aktualni.prev);
            aktualni.prev.next = newPrvek;
            aktualni.prev = newPrvek;
            if (aktualni == prvni) {
                prvni = newPrvek;
            }
            size++;
        }
    }

    @Override
    public T zpristupniAktualni() throws KolekceException {
        if (aktualni == null) {
            throw new KolekceException("No current elements to access");
        }
        return (T) aktualni.data;
    }

    @Override
    public T zpristupniPrvni() throws KolekceException {
        if (prvni == null) {
            throw new KolekceException("No elements to access");
        }
        aktualni = prvni;
        return (T) prvni.data;
    }

    @Override
    public T zpristupniPosledni() throws KolekceException {
        if (posledni == null) {
            throw new KolekceException("No elements to access");
        }
        aktualni = posledni;
        return (T) posledni.data;
    }

    @Override
    public T zpristupniNaslednika() throws KolekceException {
        if (aktualni == null || aktualni.next == null) {
            throw new KolekceException("No successors to access");
        }
        aktualni = aktualni.next;
        return (T) aktualni.data;
    }

    @Override
    public T zpristupniPredchudce() throws KolekceException {
        if (aktualni == null || aktualni.prev == null) {
            throw new KolekceException("No predecessors to access");
        }
        aktualni = aktualni.prev;
        return (T) aktualni.data;
    }

    @Override
    public T odeberAktualni() throws KolekceException {
        if (jePrazdny()) {
            throw new KolekceException("List is empty cannot remove the element");
        }
        if (aktualni == prvni) {
            return odeberPrvni();
        }
        if (aktualni == posledni) {
            return odeberPosledni();
        }

        T data = (T) aktualni.data;

        aktualni.prev.next = aktualni.next;
        aktualni.next.prev = aktualni.prev;

        aktualni = null;
        size--;

        return data;
    }

    @Override
    public T odeberPrvni() throws KolekceException {
        if (jePrazdny()) {
            throw new KolekceException("List is empty cannot remove the element");
        }

        T data = (T) prvni.data;
        if (prvni == posledni) { 
            zrus();
        } else {
            prvni = prvni.next;
            prvni.prev = posledni;
            posledni.next = prvni;
            size--;
        }

        return data;
    }

    @Override
    public T odeberPosledni() throws KolekceException {
        if (jePrazdny()) {
            throw new KolekceException("List is empty cannot remove the element");
        }

        T data = (T) posledni.data;
        if (prvni == posledni) {
            zrus();
        } else {
            posledni = posledni.prev;
            posledni.next = prvni;
            prvni.prev = posledni;
            size--;
        }

        return data;
    }

    @Override
    public T odeberNaslednika() throws KolekceException {
        if (jePrazdny()) {
            throw new KolekceException("List is empty cannot remove the element");
        }
        if (aktualni.next == prvni) {
            throw new KolekceException("No successors to remove");
        }
        if (aktualni.next == posledni) {
            return odeberPosledni();
        }

        T data = (T) aktualni.next.data;
        aktualni.next = aktualni.next.next;
        aktualni.next.prev = aktualni;
        size--;

        return data;
    }

    @Override
    public T odeberPredchudce() throws KolekceException {
        if (jePrazdny()) {
            throw new KolekceException("List is empty cannot remove the element");
        }
        if (aktualni.prev == posledni) {
            throw new KolekceException("No predecessors to remove");
        }
        if (aktualni.prev == prvni) {
            return odeberPrvni();
        }

        T data = (T) aktualni.prev.data;
        aktualni.prev = aktualni.prev.prev;
        aktualni.prev.next = aktualni;
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
                return aktualniIterator.next != prvni;
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
