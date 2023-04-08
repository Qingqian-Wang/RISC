import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalMapTest {
    @Test
    public void test_constructor() {
        Territory t1 = new Territory("PlaceA", 1);
        Territory t2= new Territory("PlaceB", 2);
        Territory t3 = new Territory("PlaceC", 3);
        Territory t4 = new Territory("PlaceD", 4);
        Territory t5 = new Territory("PlaceE", 5);
        ArrayList<Territory> map = new ArrayList<>();
        map.add(t1);
        map.add(t2);
        map.add(t3);
        map.add(t4);
        map.add(t5);
        GlobalMap gm = new GlobalMap(map);
        assertEquals(5, gm.getMapArrayList().size());
        for(int i = 1; i <= 5; i++){
            assertEquals(i,gm.getMapArrayList().get(i).getOwnID());
        }
        GlobalMap gm1 = new GlobalMap();
        assertEquals(0, gm1.getMapArrayList().size());
    }
}
