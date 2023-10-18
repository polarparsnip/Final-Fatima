package hi.verkefni.vinnsla;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Leikur {
    private static final IntegerProperty counter = new SimpleIntegerProperty(0);

    public static IntegerProperty counterProperty() {
        return counter;
    }


    /**
     * Hækkar counter um 1
     */
    public void haekkaCounter() {
        counter.set(counter.getValue()+1);

    }
    /**
     * Endursetur counter aftur í 0 þegar leik er lokið.
     */
    public void leikLokid() {
        counter.set(0);
    }



}
