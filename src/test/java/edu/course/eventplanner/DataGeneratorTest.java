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
    
    @Test
    void generateVenuesReturnsThreeVenues() {
        List<Venue> venues = Generators.generateVenues();
        
        assertNotNull(venues);
        assertEquals(3, venues.size());
    }
    
    // Data provider for venue verification
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
        Venue venue = venues.get(index);
        
        assertEquals(expectedName, venue.getName());
        assertEquals(expectedCost, venue.getCost());
        assertEquals(expectedCapacity, venue.getCapacity());
        assertEquals(expectedTables, venue.getTables());
        assertEquals(expectedSeatsPerTable, venue.getSeatsPerTable());
    }
    
    // Parameterized test for various guest counts
    @ParameterizedTest(name = "Generate {0} guests")
    @ValueSource(ints = {1, 5, 10, 25, 50, 100})
    void generateGuestsReturnsCorrectCount(int count) {
        List<Guest> guests = Generators.GenerateGuests(count);
        
        assertNotNull(guests);
        assertEquals(count, guests.size());
    }
    
    // Data provider for guest naming verification
    static Stream<Arguments> guestNamingData() {
        return Stream.of(
            Arguments.of(5, 0, "Guest1"),
            Arguments.of(5, 2, "Guest3"),
            Arguments.of(5, 4, "Guest5"),
            Arguments.of(10, 9, "Guest10"),
            Arguments.of(100, 99, "Guest100")
        );
    }
    
    @ParameterizedTest(name = "Guest at index {1} should be {2}")
    @MethodSource("guestNamingData")
    void generateGuestsAssignsCorrectNames(int totalGuests, int index, String expectedName) {
        List<Guest> guests = Generators.GenerateGuests(totalGuests);
        assertEquals(expectedName, guests.get(index).getName());
    }
    
    // Data provider for group tag distribution - groups cycle: friends, neighbors, coworkers, family
    static Stream<Arguments> groupTagData() {
        return Stream.of(
            Arguments.of(0, "friends"),    // i=1, 1%4=1 -> friends
            Arguments.of(1, "neighbors"),  // i=2, 2%4=2 -> neighbors
            Arguments.of(2, "coworkers"),  // i=3, 3%4=3 -> coworkers
            Arguments.of(3, "family"),     // i=4, 4%4=0 -> family
            Arguments.of(4, "friends"),    // i=5, 5%4=1 -> friends
            Arguments.of(7, "family")      // i=8, 8%4=0 -> family
        );
    }
    
    @ParameterizedTest(name = "Guest at index {0} has group tag {1}")
    @MethodSource("groupTagData")
    void generateGuestsDistributesGroupTags(int index, String expectedGroupTag) {
        List<Guest> guests = Generators.GenerateGuests(8);
        assertEquals(expectedGroupTag, guests.get(index).getGroupTag());
    }
    
    @Test
    void generateGuestsWithZeroReturnsEmptyList() {
        List<Guest> guests = Generators.GenerateGuests(0);
        
        assertNotNull(guests);
        assertTrue(guests.isEmpty());
    }
    
    // Parameterized test to verify group distribution for larger sets
    @ParameterizedTest(name = "Generate {0} guests - verify group distribution")
    @ValueSource(ints = {4, 8, 12, 20})
    void generateGuestsHasBalancedGroupDistribution(int count) {
        List<Guest> guests = Generators.GenerateGuests(count);
        
        // Count guests per group
        long familyCount = guests.stream().filter(g -> "family".equals(g.getGroupTag())).count();
        long friendsCount = guests.stream().filter(g -> "friends".equals(g.getGroupTag())).count();
        long neighborsCount = guests.stream().filter(g -> "neighbors".equals(g.getGroupTag())).count();
        long coworkersCount = guests.stream().filter(g -> "coworkers".equals(g.getGroupTag())).count();
        
        // For counts divisible by 4, distribution should be equal
        if (count % 4 == 0) {
            int expectedPerGroup = count / 4;
            assertEquals(expectedPerGroup, familyCount);
            assertEquals(expectedPerGroup, friendsCount);
            assertEquals(expectedPerGroup, neighborsCount);
            assertEquals(expectedPerGroup, coworkersCount);
        }
        
        // All guests should be in one of the four groups
        assertEquals(count, familyCount + friendsCount + neighborsCount + coworkersCount);
    }
}
