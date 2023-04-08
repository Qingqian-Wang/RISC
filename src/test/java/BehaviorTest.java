import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BehaviorTest {

	@Test
	public void Test1()
	{
		Territory from = new Territory("duke", 1);
		Territory to = new Territory("dku", 2);
		Behavior temp1 = new Behavior(from, to, 30, 3, "attack");
		assertEquals(temp1.getOrigin(), from);
		assertEquals(temp1.getDestination(), to);
		assertEquals(temp1.getOwnID(), 3);
		assertEquals(temp1.getUnit(), 30);
		assertEquals(temp1.getType(), "attack");
	}

}