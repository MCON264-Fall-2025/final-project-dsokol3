package edu.course.eventplanner;

import edu.course.eventplanner.model.*;
import edu.course.eventplanner.service.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SeatingPlannerTest {
    
    // Data provider for various venue and guest configurations
    static Stream<Arguments> seatingScenarios() {
        return Stream.of(
            // tables, seatsPerTable, guestCount, groupTags
            Arguments.of(2, 3, 5, new String[]{"family", "family", "friends", "friends", "family"}),
            Arguments.of(3, 4, 8, new String[]{"group1", "group1", "group2", "group2", "group1", "group2", "group1", "group2"}),
            Arguments.of(4, 5, 12, new String[]{"a", "a", "a", "b", "b", "b", "c", "c", "c", "d", "d", "d"}),
            Arguments.of(2, 4, 4, new String[]{"single", "single", "single", "single"})
        );
    }
    
    @ParameterizedTest(name = "{0} tables x {1} seats, {2} guests")
    @MethodSource("seatingScenarios")
    void generateSeating_seatsAllGuests(int tables, int seatsPerTable, int guestCount, String[] groupTags) {
        Venue venue = new Venue("TestVenue", 100, tables * seatsPerTable, tables, seatsPerTable);
        SeatingPlanner planner = new SeatingPlanner(venue);
        
        List<Guest> guests = new ArrayList<>();
        for (int i = 0; i < guestCount; i++) {
            guests.add(new Guest("Guest" + (i + 1), groupTags[i]));
        }
        
        Map<Integer, List<Guest>> seating = planner.generateSeating(guests);
        
        int totalSeated = seating.values().stream().mapToInt(List::size).sum();
        assertEquals(guestCount, totalSeated, "All guests should be seated");
        assertTrue(seating.size() <= tables, "Should not exceed max tables");
    }
    
    // Data provider for capacity limit tests
    static Stream<Arguments> capacityLimitScenarios() {
        return Stream.of(
            Arguments.of(2, 3, 6),   // Exactly fills 2 tables
            Arguments.of(1, 5, 5),   // Exactly fills 1 table
            Arguments.of(3, 2, 5)    // Partial fill of 3 tables
        );
    }
    
    @ParameterizedTest(name = "{0} tables x {1} seats with {2} guests")
    @MethodSource("capacityLimitScenarios")
    void generateSeating_respectsTableCapacity(int tables, int seatsPerTable, int guestCount) {
        Venue venue = new Venue("TestVenue", 100, tables * seatsPerTable, tables, seatsPerTable);
        SeatingPlanner planner = new SeatingPlanner(venue);
        
        List<Guest> guests = new ArrayList<>();
        for (int i = 0; i < guestCount; i++) {
            guests.add(new Guest("G" + i, "group" + (i % 2)));
        }
        
        Map<Integer, List<Guest>> seating = planner.generateSeating(guests);
        
        // Verify no table exceeds capacity
        for (List<Guest> table : seating.values()) {
            assertTrue(table.size() <= seatsPerTable, 
                "No table should exceed " + seatsPerTable + " seats");
        }
    }
    
    @Test
    void generateSeating_emptyGuestList() {
        Venue venue = new Venue("TestVenue", 100, 10, 2, 5);
        SeatingPlanner planner = new SeatingPlanner(venue);
        
        Map<Integer, List<Guest>> seating = planner.generateSeating(Collections.emptyList());
        
        assertTrue(seating.isEmpty() || seating.values().stream().allMatch(List::isEmpty),
            "Empty guest list should result in empty or all-empty seating");
    }
}
