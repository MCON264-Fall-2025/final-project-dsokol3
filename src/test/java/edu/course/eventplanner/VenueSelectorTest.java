package edu.course.eventplanner;

import edu.course.eventplanner.model.*;
import edu.course.eventplanner.service.*;
import org.junit.jupiter.api.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class VenueSelectorTest {
    @Test
    void selectVenue_returnsBestValidVenue() {
        List<Venue> venues = Arrays.asList(
            new Venue("A", 100, 10, 2, 5),
            new Venue("B", 80, 10, 2, 5),
            new Venue("C", 80, 12, 3, 4)
        );
        VenueSelector selector = new VenueSelector(venues);
        Venue result = selector.selectVenue(90, 10);
        assertNotNull(result);
        assertEquals("B", result.getName());
    }

    @Test
    void selectVenue_returnsNullIfNoVenueFits() {
        List<Venue> venues = Collections.singletonList(new Venue("A", 200, 5, 1, 5));
        VenueSelector selector = new VenueSelector(venues);
        assertNull(selector.selectVenue(100, 10));
    }
}
