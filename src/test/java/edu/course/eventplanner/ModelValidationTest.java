package edu.course.eventplanner;

import edu.course.eventplanner.model.Guest;
import edu.course.eventplanner.model.Task;
import edu.course.eventplanner.model.Venue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for model classes ensuring proper construction and behavior.
 */
public class ModelValidationTest {
    
    // Guest model tests - parameterized
    @ParameterizedTest(name = "Guest {0} in group {1}")
    @CsvSource({
        "Alice, family",
        "Bob, friends",
        "Charlie, coworkers",
        "Diana, neighbors",
        "John Doe, family"
    })
    void guestConstructorSetsNameAndGroupTag(String name, String groupTag) {
        Guest guest = new Guest(name, groupTag);
        
        assertEquals(name, guest.getName());
        assertEquals(groupTag, guest.getGroupTag());
    }
    
    // Data provider for venue configurations
    static Stream<Arguments> venueConfigurations() {
        return Stream.of(
            Arguments.of("Test Hall", 1500.0, 50, 6, 8),
            Arguments.of("Small Room", 500.0, 20, 4, 5),
            Arguments.of("Grand Hall", 10000.0, 500, 50, 10),
            Arguments.of("Conference Room", 800.0, 40, 5, 8),
            Arguments.of("Community Center", 0.0, 30, 4, 8)
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
    
    // Task model tests - parameterized
    @ParameterizedTest(name = "Task: {0}")
    @ValueSource(strings = {
        "Book the venue",
        "Send invitations",
        "Order catering",
        "Arrange decorations",
        "This is a very long task description that includes multiple steps"
    })
    void taskConstructorSetsDescription(String description) {
        Task task = new Task(description);
        assertEquals(description, task.getDescription());
    }
    
    // Edge cases - parameterized
    @ParameterizedTest(name = "Guest with group tag: [{0}]")
    @ValueSource(strings = {"", "group-a", "VIP", "special_chars_123"})
    void guestWithVariousGroupTags(String groupTag) {
        Guest guest = new Guest("Test Guest", groupTag);
        assertEquals(groupTag, guest.getGroupTag());
    }
    
    // Data provider for capacity matching tests
    static Stream<Arguments> capacityMatchingData() {
        return Stream.of(
            Arguments.of(5, 8, 40),   // 5 * 8 = 40
            Arguments.of(4, 5, 20),   // 4 * 5 = 20
            Arguments.of(10, 10, 100) // 10 * 10 = 100
        );
    }
    
    @ParameterizedTest(name = "{0} tables x {1} seats = {2} capacity")
    @MethodSource("capacityMatchingData")
    void venueCapacityMatchesTablesAndSeats(int tables, int seatsPerTable, int expectedCapacity) {
        Venue venue = new Venue("Test Venue", 1000, expectedCapacity, tables, seatsPerTable);
        assertEquals(tables * seatsPerTable, venue.getCapacity());
    }
    
    // Parameterized test for multiple guests in same group
    @ParameterizedTest(name = "{0} guests in group {1}")
    @CsvSource({
        "3, group-a",
        "5, family",
        "2, VIP"
    })
    void multipleGuestsSameGroup(int count, String groupTag) {
        Guest[] guests = new Guest[count];
        for (int i = 0; i < count; i++) {
            guests[i] = new Guest("Person " + (i + 1), groupTag);
        }
        
        for (int i = 1; i < count; i++) {
            assertEquals(guests[0].getGroupTag(), guests[i].getGroupTag());
        }
    }
    
    @Test
    void venueWithDecimalCost() {
        Venue venue = new Venue("Decimal Cost Venue", 1999.99, 30, 4, 8);
        assertEquals(1999.99, venue.getCost(), 0.01);
    }
}
