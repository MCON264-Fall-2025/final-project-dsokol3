package edu.course.eventplanner;

import edu.course.eventplanner.model.Guest;
import edu.course.eventplanner.model.Task;
import edu.course.eventplanner.model.Venue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for model classes ensuring proper construction and behavior.
 */
public class ModelValidationTest {
    
    // Guest model - covers name, groupTag, edge cases (spaces, empty tag)
    @ParameterizedTest(name = "Guest {0} in group [{1}]")
    @CsvSource({
        "Alice, family",
        "Bob, friends",
        "John Doe, coworkers",
        "Solo, ''"
    })
    void guestConstructorSetsNameAndGroupTag(String name, String groupTag) {
        Guest guest = new Guest(name, groupTag);
        assertEquals(name, guest.getName());
        assertEquals(groupTag, guest.getGroupTag());
    }
    
    // Venue model - covers all fields including edge cases (zero cost, decimal cost)
    static Stream<Arguments> venueConfigurations() {
        return Stream.of(
            Arguments.of("Test Hall", 1500.0, 50, 6, 8),
            Arguments.of("Community Center", 0.0, 30, 4, 8),
            Arguments.of("Decimal Venue", 1999.99, 40, 5, 8)
        );
    }
    
    @ParameterizedTest(name = "Venue {0}: ${1}, capacity {2}")
    @MethodSource("venueConfigurations")
    void venueConstructorSetsAllFields(String name, double cost, int capacity, int tables, int seatsPerTable) {
        Venue venue = new Venue(name, cost, capacity, tables, seatsPerTable);
        assertEquals(name, venue.getName());
        assertEquals(cost, venue.getCost(), 0.01);
        assertEquals(capacity, venue.getCapacity());
        assertEquals(tables, venue.getTables());
        assertEquals(seatsPerTable, venue.getSeatsPerTable());
    }
    
    // Task model - covers short and long descriptions
    @ParameterizedTest(name = "Task: {0}")
    @CsvSource({
        "Book venue",
        "This is a very long task description with multiple steps"
    })
    void taskConstructorSetsDescription(String description) {
        Task task = new Task(description);
        assertEquals(description, task.getDescription());
    }
    
    @Test
    void multipleGuestsCanShareGroupTag() {
        Guest g1 = new Guest("Person 1", "family");
        Guest g2 = new Guest("Person 2", "family");
        assertEquals(g1.getGroupTag(), g2.getGroupTag());
    }
}
