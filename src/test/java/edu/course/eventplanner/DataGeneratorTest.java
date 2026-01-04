package edu.course.eventplanner;

import edu.course.eventplanner.model.Guest;
import edu.course.eventplanner.model.Venue;
import edu.course.eventplanner.util.Generators;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Generators utility class.
 */
public class DataGeneratorTest {
    
    // Venue generation 
    static Stream<Arguments> venueData() {
        return Stream.of(
            Arguments.of(0, "Community Hall", 1500.0, 40, 5, 8),
            Arguments.of(1, "Garden Hall", 2500.0, 60, 8, 8),
            Arguments.of(2, "Grand Ballroom", 5000.0, 120, 15, 8)
        );
    }
    
    @ParameterizedTest(name = "Venue {0}: {1}")
    @MethodSource("venueData")
    void generateVenuesHasCorrectProperties(int index, String expectedName, 
            double expectedCost, int expectedCapacity, int expectedTables, int expectedSeatsPerTable) {
        List<Venue> venues = Generators.generateVenues();
        
        assertEquals(3, venues.size());
        Venue venue = venues.get(index);
        assertEquals(expectedName, venue.getName());
        assertEquals(expectedCost, venue.getCost());
        assertEquals(expectedCapacity, venue.getCapacity());
        assertEquals(expectedTables, venue.getTables());
        assertEquals(expectedSeatsPerTable, venue.getSeatsPerTable());
    }
    
    // Guest generation 
    @ParameterizedTest(name = "Generate {0} guests")
    @ValueSource(ints = {0, 1, 8, 100})
    void generateGuestsReturnsCorrectCount(int count) {
        List<Guest> guests = Generators.GenerateGuests(count);
        
        assertNotNull(guests);
        assertEquals(count, guests.size());
        
        if (count > 0) {
            // Verify naming pattern
            assertEquals("Guest1", guests.get(0).getName());
            assertEquals("Guest" + count, guests.get(count - 1).getName());
        }
    }
    
    // Group tag distribution 
    static Stream<Arguments> groupTagData() {
        return Stream.of(
            Arguments.of(0, "friends"),    // i=1, 1%4=1
            Arguments.of(1, "neighbors"),  // i=2, 2%4=2
            Arguments.of(2, "coworkers"),  // i=3, 3%4=3
            Arguments.of(3, "family")      // i=4, 4%4=0
        );
    }
    
    @ParameterizedTest(name = "Guest at index {0} has group tag {1}")
    @MethodSource("groupTagData")
    void generateGuestsDistributesGroupTags(int index, String expectedGroupTag) {
        List<Guest> guests = Generators.GenerateGuests(4);
        assertEquals(expectedGroupTag, guests.get(index).getGroupTag());
    }
    
    @Test
    void generateGuestsHasBalancedGroupDistribution() {
        List<Guest> guests = Generators.GenerateGuests(8);
        
        long familyCount = guests.stream().filter(g -> "family".equals(g.getGroupTag())).count();
        long friendsCount = guests.stream().filter(g -> "friends".equals(g.getGroupTag())).count();
        long neighborsCount = guests.stream().filter(g -> "neighbors".equals(g.getGroupTag())).count();
        long coworkersCount = guests.stream().filter(g -> "coworkers".equals(g.getGroupTag())).count();
        
        assertEquals(2, familyCount);
        assertEquals(2, friendsCount);
        assertEquals(2, neighborsCount);
        assertEquals(2, coworkersCount);
    }
}
