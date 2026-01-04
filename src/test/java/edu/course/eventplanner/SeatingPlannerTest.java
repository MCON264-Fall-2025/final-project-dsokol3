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
    
    static Stream<Arguments> seatingScenarios() {
        return Stream.of(
            Arguments.of(2, 3, 5, new String[]{"family", "family", "friends", "friends", "family"}),
            Arguments.of(3, 4, 8, new String[]{"group1", "group1", "group2", "group2", "group1", "group2", "group1", "group2"})
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
        
        // Verify no table exceeds capacity
        for (List<Guest> table : seating.values()) {
            assertTrue(table.size() <= seatsPerTable);
        }
    }
    
    @Test
    void generateSeating_emptyGuestList() {
        Venue venue = new Venue("TestVenue", 100, 10, 2, 5);
        SeatingPlanner planner = new SeatingPlanner(venue);
        
        Map<Integer, List<Guest>> seating = planner.generateSeating(Collections.emptyList());
        
        assertTrue(seating.isEmpty() || seating.values().stream().allMatch(List::isEmpty));
    }
}
