package edu.course.eventplanner;

import edu.course.eventplanner.model.*;
import edu.course.eventplanner.service.*;
import org.junit.jupiter.api.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Student tests for GuestListManager functionality.
 */
class ManagerGuestListTest {
    @Test
    void addGuest_increasesCountAndFindsGuest() {
        GuestListManager manager = new GuestListManager();
        Guest g = new Guest("Alice", "family");
        manager.addGuest(g);
        assertEquals(1, manager.getGuestCount());
        assertEquals(g, manager.findGuest("Alice"));
    }

    @Test
    void removeGuest_decreasesCountAndRemovesFromFind() {
        GuestListManager manager = new GuestListManager();
        Guest g = new Guest("Bob", "friends");
        manager.addGuest(g);
        assertTrue(manager.removeGuest("Bob"));
        assertNull(manager.findGuest("Bob"));
        assertEquals(0, manager.getGuestCount());
    }

    @Test
    void findGuest_returnsNullIfNotFound() {
        GuestListManager manager = new GuestListManager();
        assertNull(manager.findGuest("Nonexistent"));
    }

    @Test
    void getAllGuests_returnsAllAddedGuests() {
        GuestListManager manager = new GuestListManager();
        Guest g1 = new Guest("A", "family");
        Guest g2 = new Guest("B", "friends");
        manager.addGuest(g1);
        manager.addGuest(g2);
        List<Guest> all = manager.getAllGuests();
        assertTrue(all.contains(g1));
        assertTrue(all.contains(g2));
        assertEquals(2, all.size());
    }
}
