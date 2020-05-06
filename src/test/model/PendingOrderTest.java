package model;

import exceptions.InvalidAmountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class PendingOrderTest {
    private PendingOrder p;

    @BeforeEach
    public void beforeEach() {
        try {
            p = new PendingOrder("Pending Order 1", "Test", 20, "Testing1234");
        } catch (InvalidAmountException e) {
            fail("Error shouldn't have been thrown");
        }
    }

    @Test
    public void testGetters() {
        assertEquals("Pending Order 1", p.getName());
        assertEquals("Test", p.getType());
        assertEquals(20, p.getQuantity());
        assertEquals("Testing1234", p.getBuyerID());
    }

    @Test
    public void errorThrown() {
        try {
            new PendingOrder("F1", "Fail", -100, "fail12345");
            fail();
        } catch (InvalidAmountException e) {
            //all good
        }
    }
}
