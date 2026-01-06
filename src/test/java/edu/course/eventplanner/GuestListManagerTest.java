package edu.course.eventplanner;

import edu.course.eventplanner.model.*;
import edu.course.eventplanner.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class GuestListManagerTest {
    
    // Add and find guests
    @ParameterizedTest(name = "Adding guest {0} in group {1}")
    @CsvSource({
        "Alice, family",
        "John Doe, coworkers"
    })
    void addGuest_increasesCountAndFindsGuest(String name, String groupTag) {
        GuestListManager manager = new GuestListManager();
        Guest g = new Guest(name, groupTag);
        manager.addGuest(g);
        assertEquals(1, manager.getGuestCount());
        Guest found = manager.findGuest(name);
        assertNotNull(found);
        assertEquals(name, found.getName());
        assertEquals(groupTag, found.getGroupTag());
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

    @ParameterizedTest(name = "Adding {0} guests")
    @ValueSource(ints = {1, 5})
    void getAllGuests_returnsCorrectCount(int guestCount) {
        GuestListManager manager = new GuestListManager();
        for (int i = 0; i < guestCount; i++) {
            manager.addGuest(new Guest("Guest" + i, "family"));
        }
        assertEquals(guestCount, manager.getAllGuests().size());
        assertEquals(guestCount, manager.getGuestCount());
    }
}
