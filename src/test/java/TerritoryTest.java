import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TerritoryTest {
    @Test
    public void test_constructor() {
        Territory t = new Territory("PlaceA", 1);
        assertEquals("PlaceA", t.getName());
        assertEquals(1, t.getOwnID());
        assertEquals(0, t.getUnit());
        assertEquals(0, t.getNeighbor().size());
    }

    @Test
    public void test_set_id() {
        Territory t = new Territory("PlaceA", 1);
        t.setOwnID(2);
        assertNotEquals(1, t.getOwnID());
        assertEquals(2, t.getOwnID());
    }

    @Test
    public void test_set_units() {
        Territory t = new Territory("PlaceA", 1);
        t.setUnit(10);
        assertNotEquals(0, t.getUnit());
        assertEquals(10, t.getUnit());
        t.setUnit(20);
        assertNotEquals(10, t.getUnit());
        assertEquals(20, t.getUnit());
    }

    @Test
    public void test_updateNeighbor_initializing() {
        Territory t1 = new Territory("PlaceA", 1);
        Territory t2 = new Territory("PlaceB", 2);
        Territory t3 = new Territory("PlaceC", 3);
        t1.updateNeighbor(t2);
        t1.updateNeighbor(t3);
        t2.updateNeighbor(t1);
        t3.updateNeighbor(t1);
        assertEquals(2, t1.getNeighbor().size());
        assertEquals(1, t1.getNeighbor().get(2).size());
        assertEquals("PlaceB", t1.getNeighbor().get(2).get(0));
        assertEquals(1, t1.getNeighbor().get(3).size());
        assertEquals("PlaceC", t1.getNeighbor().get(3).get(0));

        assertEquals(1, t2.getNeighbor().size());
        assertEquals(1, t2.getNeighbor().get(1).size());
        assertEquals("PlaceA", t2.getNeighbor().get(1).get(0));

        assertEquals(1, t3.getNeighbor().size());
        assertEquals(1, t3.getNeighbor().get(1).size());
        assertEquals("PlaceA", t3.getNeighbor().get(1).get(0));
    }

    @Test
    public void test_updateNeighbor_changeID() {
        Territory t1 = new Territory("PlaceA", 1);
        Territory t2 = new Territory("PlaceB", 2);
        Territory t3 = new Territory("PlaceC", 3);
        Territory t4 = new Territory("PlaceD", 4);
        Territory t5 = new Territory("PlaceE", 5);
        t1.updateNeighbor(t3);
        t1.updateNeighbor(t4);
        t2.updateNeighbor(t3);
        t2.updateNeighbor(t5);
        t3.updateNeighbor(t1);
        t3.updateNeighbor(t2);
        t3.updateNeighbor(t5);
        t4.updateNeighbor(t1);
        t5.updateNeighbor(t2);
        t5.updateNeighbor(t3);

        t3.setOwnID(1);
        t1.updateNeighbor(t3);
        t2.updateNeighbor(t3);
        t5.updateNeighbor(t3);

        assertEquals(0, t1.getNeighbor().get(3).size());
        assertEquals(1, t1.getNeighbor().get(1).size());
        assertEquals("PlaceC", t1.getNeighbor().get(1).get(0));

        assertEquals(0, t2.getNeighbor().get(3).size());
        assertEquals(1, t2.getNeighbor().get(1).size());
        assertEquals("PlaceC", t2.getNeighbor().get(1).get(0));

        assertEquals(0, t5.getNeighbor().get(3).size());
        assertEquals(1, t5.getNeighbor().get(1).size());
        assertEquals("PlaceC", t5.getNeighbor().get(1).get(0));

        t5.setOwnID(2);
        t2.updateNeighbor(t5);
        t3.updateNeighbor(t5);

        assertEquals(0, t2.getNeighbor().get(5).size());
        assertEquals(1, t2.getNeighbor().get(2).size());
        assertEquals("PlaceE", t2.getNeighbor().get(2).get(0));

        assertEquals(0, t3.getNeighbor().get(5).size());
        assertEquals(2, t3.getNeighbor().get(2).size());
        ArrayList<String> expect1 = new ArrayList<>();
        expect1.add("PlaceB");
        expect1.add("PlaceE");
        assertEquals(expect1.get(0), t3.getNeighbor().get(2).get(0));
        assertEquals(expect1.get(1), t3.getNeighbor().get(2).get(1));

        t1.setOwnID(4);
        t3.updateNeighbor(t1);
        t4.updateNeighbor(t1);

        assertEquals(0, t3.getNeighbor().get(1).size());
        assertEquals(1, t3.getNeighbor().get(4).size());
        assertEquals("PlaceA", t3.getNeighbor().get(4).get(0));

        assertEquals(0, t4.getNeighbor().get(1).size());
        assertEquals(1, t4.getNeighbor().get(4).size());
        assertEquals("PlaceA", t4.getNeighbor().get(4).get(0));
    }
}
