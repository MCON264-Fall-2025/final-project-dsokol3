package edu.course.eventplanner;

import edu.course.eventplanner.model.*;
import edu.course.eventplanner.service.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GuestListManagerTest {
    
    // Parameterized test: add guests with different names and group tags
    @ParameterizedTest(name = "Adding guest {0} in group {1}")
    @CsvSource({
        "Alice, family",
        "Bob, friends",
        "Charlie, coworkers",
        "Diana, neighbors"
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

    // Parameterized test: remove guests with different names
    @ParameterizedTest(name = "Removing guest {0} from group {1}")
    @CsvSource({
        "Alice, family",
        "Bob, friends",
        "Charlie, coworkers"
    })
    void removeGuest_decreasesCountAndRemovesFromFind(String name, String groupTag) {
        GuestListManager manager = new GuestListManager();
        Guest g = new Guest(name, groupTag);
        manager.addGuest(g);
        assertTrue(manager.removeGuest(name));
        assertNull(manager.findGuest(name));
        assertEquals(0, manager.getGuestCount());
    }

    // Parameterized test: find non-existent guests
    @ParameterizedTest(name = "Finding non-existent guest: {0}")
    @ValueSource(strings = {"Nonexistent", "Unknown", "Missing", ""})
    void findGuest_returnsNullIfNotFound(String name) {
        GuestListManager manager = new GuestListManager();
        assertNull(manager.findGuest(name));
    }

    // Parameterized test: add multiple guests and verify count
    @ParameterizedTest(name = "Adding {0} guests")
    @ValueSource(ints = {1, 2, 5, 10})
    void getAllGuests_returnsCorrectCount(int guestCount) {
        GuestListManager manager = new GuestListManager();
        String[] groupTags = {"family", "friends", "coworkers", "neighbors"};
        
        for (int i = 0; i < guestCount; i++) {
            manager.addGuest(new Guest("Guest" + i, groupTags[i % groupTags.length]));
        }
        
        List<Guest> all = manager.getAllGuests();
        assertEquals(guestCount, all.size());
        assertEquals(guestCount, manager.getGuestCount());
    }
}
