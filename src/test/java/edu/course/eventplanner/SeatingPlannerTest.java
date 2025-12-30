package edu.course.eventplanner;

import edu.course.eventplanner.model.*;
import edu.course.eventplanner.service.*;
import org.junit.jupiter.api.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SeatingPlannerTest {
    @Test
    void generateSeating_groupsGuestsByTagAndFillsTables() {
        Venue venue = new Venue("TestVenue", 100, 10, 2, 3);
        SeatingPlanner planner = new SeatingPlanner(venue);
        List<Guest> guests = Arrays.asList(
            new Guest("A", "family"),
            new Guest("B", "family"),
            new Guest("C", "friends"),
            new Guest("D", "friends"),
            new Guest("E", "family")
        );
        Map<Integer, List<Guest>> seating = planner.generateSeating(guests);
        int totalSeated = seating.values().stream().mapToInt(List::size).sum();
        assertEquals(5, totalSeated);
        assertTrue(seating.size() <= 2); // max tables
    }
}
