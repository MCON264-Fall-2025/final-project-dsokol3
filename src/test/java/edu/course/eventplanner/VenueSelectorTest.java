package edu.course.eventplanner;

import edu.course.eventplanner.model.*;
import edu.course.eventplanner.service.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for VenueSelector.
 */
class VenueSelectorTest {
    // Helper to create a Venue with a given name, cost, and capacity
    private static Venue v(String name, double cost, int capacity) {
        switch (name) {
            case "A": return new Venue("A", cost, capacity, 2, 5);
            case "B": return new Venue("B", cost, capacity, 2, 5);
            case "C": return new Venue("C", cost, capacity, 3, 4);
            default: throw new IllegalArgumentException("Unknown venue name");
        }
    }

    // Test data provider for parameterized tests
    static List<Object[]> selectVenueData() {
        return Arrays.asList(new Object[][]{
            // Test: returns best valid venue
            {Arrays.asList(v("A", 100, 10), v("B", 80, 10), v("C", 80, 12)), 90, 10, "B"},
            // Test: excludes venues over budget or under capacity
            {Arrays.asList(v("A", 200, 10), v("B", 80, 8), v("C", 90, 10)), 100, 10, "C"},
            // Test: tiebreaks by smallest capacity
            {Arrays.asList(v("A", 80, 15), v("B", 80, 10), v("C", 100, 10)), 100, 10, "B"},
        });
    }

    // Parameterized test for selectVenue
    @ParameterizedTest
    @MethodSource("selectVenueData")
    void selectVenue_variousScenarios(List<Venue> venues, double budget, int minCapacity, String expectedName) {
        VenueSelector selector = new VenueSelector(venues);
        Venue result = selector.selectVenue(budget, minCapacity);
        assertNotNull(result, "Expected a venue to be selected");
        assertEquals(expectedName, result.getName(), "Selected venue name mismatch");
    }


    // returns null if no venue fits the requirements
    @Test
    void selectVenue_returnsNullIfNoVenueFits() {
        List<Venue> venues = Collections.singletonList(v("A", 200, 5));
        VenueSelector selector = new VenueSelector(venues);
        assertNull(selector.selectVenue(100, 10), "Expected null when no venue fits");
    }

}
