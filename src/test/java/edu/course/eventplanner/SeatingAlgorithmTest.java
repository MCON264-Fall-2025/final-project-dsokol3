package edu.course.eventplanner;

import edu.course.eventplanner.model.Guest;
import edu.course.eventplanner.model.Venue;
import edu.course.eventplanner.service.SeatingPlanner;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Instructor-provided tests for SeatingPlanner
 * Tests grouping logic, queue usage, BST usage, and correctness
 */
public class SeatingAlgorithmTest {

    @Test
    public void testBasicSeatingAssignment() {
        Venue venue = new Venue("Test Hall", 1000, 100, 10, 10);
        SeatingPlanner planner = new SeatingPlanner(venue);

        List<Guest> guests = Arrays.asList(
            new Guest("Alice", "Family"),
            new Guest("Bob", "Family"),
            new Guest("Carol", "Friends"),
            new Guest("Dave", "Friends")
        );

        Map<Integer, List<Guest>> seating = planner.generateSeating(guests);
        assertNotNull(seating, "Seating arrangement should not be null");
        assertFalse(seating.isEmpty(), "Seating arrangement should not be empty");

        // Count total seated guests
        int totalSeated = seating.values().stream()
            .mapToInt(List::size)
            .sum();
        assertEquals(guests.size(), totalSeated, "All guests should be seated");
    }

    @Test
    public void testGroupsSeatedTogether() {
        Venue venue = new Venue("Test Hall", 1000, 100, 10, 10);
        SeatingPlanner planner = new SeatingPlanner(venue);

        List<Guest> guests = Arrays.asList(
            new Guest("Alice", "Family"),
            new Guest("Bob", "Family"),
            new Guest("Carol", "Family"),
            new Guest("Dave", "Friends"),
            new Guest("Eve", "Friends")
        );

        Map<Integer, List<Guest>> seating = planner.generateSeating(guests);

        // Check that guests with same group tag are preferably seated together
        for (List<Guest> table : seating.values()) {
            if (table.size() > 1) {
                // If there are multiple people at a table, check for group cohesion
                Set<String> groups = new HashSet<>();
                for (Guest g : table) {
                    groups.add(g.getGroupTag());
                }
                // Ideally, tables should have fewer distinct groups (better grouping)
            }
        }
    }

    @Test
    public void testNoOverfilledTables() {
        Venue venue = new Venue("Small Hall", 500, 50, 5, 10);
        SeatingPlanner planner = new SeatingPlanner(venue);

        List<Guest> guests = new ArrayList<>();
        for (int i = 0; i < 45; i++) {
            guests.add(new Guest("Guest" + i, "Group" + (i % 5)));
        }

        Map<Integer, List<Guest>> seating = planner.generateSeating(guests);

        for (Map.Entry<Integer, List<Guest>> entry : seating.entrySet()) {
            assertTrue(entry.getValue().size() <= venue.getSeatsPerTable(),
                "Table " + entry.getKey() + " should not exceed capacity");
        }
    }

    @Test
    public void testWithinTableLimit() {
        Venue venue = new Venue("Small Hall", 500, 50, 5, 10);
        SeatingPlanner planner = new SeatingPlanner(venue);

        List<Guest> guests = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            guests.add(new Guest("Guest" + i, "Group1"));
        }

        Map<Integer, List<Guest>> seating = planner.generateSeating(guests);

        assertTrue(seating.size() <= venue.getTables(),
            "Should not use more tables than available");
    }

    @Test
    public void testEmptyGuestList() {
        Venue venue = new Venue("Test Hall", 1000, 100, 10, 10);
        SeatingPlanner planner = new SeatingPlanner(venue);

        Map<Integer, List<Guest>> seating = planner.generateSeating(new ArrayList<>());
        assertNotNull(seating, "Should return a map even for empty guest list");
        assertTrue(seating.isEmpty() || seating.values().stream().allMatch(List::isEmpty),
            "Should have no seated guests");
    }

    @Test
    public void testSingleGuest() {
        Venue venue = new Venue("Test Hall", 1000, 100, 10, 10);
        SeatingPlanner planner = new SeatingPlanner(venue);

        List<Guest> guests = Arrays.asList(new Guest("Alice", "Solo"));
        Map<Integer, List<Guest>> seating = planner.generateSeating(guests);

        int totalSeated = seating.values().stream()
            .mapToInt(List::size)
            .sum();
        assertEquals(1, totalSeated, "Single guest should be seated");
    }

    @Test
    public void testMultipleGroupsDistribution() {
        Venue venue = new Venue("Medium Hall", 1500, 150, 15, 10);
        SeatingPlanner planner = new SeatingPlanner(venue);

        List<Guest> guests = Arrays.asList(
            new Guest("A1", "GroupA"),
            new Guest("A2", "GroupA"),
            new Guest("A3", "GroupA"),
            new Guest("B1", "GroupB"),
            new Guest("B2", "GroupB"),
            new Guest("C1", "GroupC"),
            new Guest("C2", "GroupC"),
            new Guest("C3", "GroupC"),
            new Guest("C4", "GroupC")
        );

        Map<Integer, List<Guest>> seating = planner.generateSeating(guests);

        // All guests should be seated
        int totalSeated = seating.values().stream()
            .mapToInt(List::size)
            .sum();
        assertEquals(9, totalSeated, "All 9 guests should be seated");

        // Verify no table is overfilled
        for (List<Guest> table : seating.values()) {
            assertTrue(table.size() <= 10, "No table should exceed 10 seats");
        }
    }
}

