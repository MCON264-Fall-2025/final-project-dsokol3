package edu.course.eventplanner;

import edu.course.eventplanner.model.Guest;
import edu.course.eventplanner.service.GuestListManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Instructor-provided tests for GuestListManager
 * Tests linked list usage, map consistency, add/remove/find operations
 */
public class GuestManagementTest {

    private GuestListManager manager;

    @BeforeEach
    public void setUp() {
        manager = new GuestListManager();
    }

    @Test
    public void testAddGuest() {
        Guest guest = new Guest("Alice Smith", "Family");
        manager.addGuest(guest);

        assertEquals(1, manager.getGuestCount(), "Guest count should be 1 after adding one guest");
        assertNotNull(manager.findGuest("Alice Smith"), "Should be able to find added guest");
    }

    @Test
    public void testAddMultipleGuests() {
        manager.addGuest(new Guest("Alice Smith", "Family"));
        manager.addGuest(new Guest("Bob Jones", "Friends"));
        manager.addGuest(new Guest("Carol White", "Colleagues"));

        assertEquals(3, manager.getGuestCount(), "Should have 3 guests");
        List<Guest> allGuests = manager.getAllGuests();
        assertEquals(3, allGuests.size(), "getAllGuests should return all 3 guests");
    }

    @Test
    public void testFindGuest() {
        Guest alice = new Guest("Alice Smith", "Family");
        Guest bob = new Guest("Bob Jones", "Friends");
        manager.addGuest(alice);
        manager.addGuest(bob);

        Guest found = manager.findGuest("Alice Smith");
        assertNotNull(found, "Should find Alice Smith");
        assertEquals("Alice Smith", found.getName());
        assertEquals("Family", found.getGroupTag());
    }

    @Test
    public void testFindNonexistentGuest() {
        manager.addGuest(new Guest("Alice Smith", "Family"));

        Guest notFound = manager.findGuest("John Doe");
        assertNull(notFound, "Should return null for nonexistent guest");
    }

    @Test
    public void testRemoveGuest() {
        manager.addGuest(new Guest("Alice Smith", "Family"));
        manager.addGuest(new Guest("Bob Jones", "Friends"));

        boolean removed = manager.removeGuest("Alice Smith");
        assertTrue(removed, "Should successfully remove Alice Smith");
        assertEquals(1, manager.getGuestCount(), "Guest count should be 1 after removal");
        assertNull(manager.findGuest("Alice Smith"), "Should not find removed guest");
        assertNotNull(manager.findGuest("Bob Jones"), "Should still find Bob Jones");
    }

    @Test
    public void testRemoveNonexistentGuest() {
        manager.addGuest(new Guest("Alice Smith", "Family"));

        boolean removed = manager.removeGuest("John Doe");
        assertFalse(removed, "Should return false when removing nonexistent guest");
        assertEquals(1, manager.getGuestCount(), "Guest count should remain unchanged");
    }

    @Test
    public void testMapConsistency() {
        // Test that both LinkedList and Map are kept in sync
        manager.addGuest(new Guest("Alice", "A"));
        manager.addGuest(new Guest("Bob", "B"));
        manager.addGuest(new Guest("Carol", "C"));

        manager.removeGuest("Bob");

        List<Guest> allGuests = manager.getAllGuests();
        assertEquals(2, allGuests.size(), "List should have 2 guests");
        assertNull(manager.findGuest("Bob"), "Map should not contain removed guest");

        // Verify the remaining guests are findable
        for (Guest g : allGuests) {
            assertNotNull(manager.findGuest(g.getName()),
                "All guests in list should be findable in map");
        }
    }

    @Test
    public void testEmptyGuestList() {
        assertEquals(0, manager.getGuestCount(), "New manager should have 0 guests");
        assertTrue(manager.getAllGuests().isEmpty(), "Guest list should be empty");
    }
}

