package edu.course.eventplanner;

import edu.course.eventplanner.model.Venue;
import edu.course.eventplanner.service.VenueSelector;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Instructor-provided tests for VenueSelector
 * Tests filtering, selection rules, and algorithmic approach
 */
public class VenueSelectionTest {

    @Test
    public void testSelectVenueWithinBudgetAndCapacity() {
        List<Venue> venues = Arrays.asList(
            new Venue("Small Hall", 500, 50, 5, 10),
            new Venue("Medium Center", 1000, 100, 10, 10),
            new Venue("Large Ballroom", 2000, 200, 20, 10)
        );
        VenueSelector selector = new VenueSelector(venues);

        Venue selected = selector.selectVenue(1200, 80);
        assertNotNull(selected, "Should select a venue within budget and capacity");
        assertTrue(selected.getCost() <= 1200, "Selected venue must be within budget");
        assertTrue(selected.getCapacity() >= 80, "Selected venue must have sufficient capacity");
    }

    @Test
    public void testSelectBestValueVenue() {
        List<Venue> venues = Arrays.asList(
            new Venue("Expensive Small", 1000, 60, 6, 10),
            new Venue("Affordable Medium", 800, 100, 10, 10),
            new Venue("Budget Large", 900, 150, 15, 10)
        );
        VenueSelector selector = new VenueSelector(venues);

        Venue selected = selector.selectVenue(1500, 70);
        assertNotNull(selected, "Should select best value venue");
        // Should prefer venues that meet requirements with best cost-to-capacity ratio
    }

    @Test
    public void testNoVenueAvailable() {
        List<Venue> venues = Arrays.asList(
            new Venue("Small Hall", 500, 50, 5, 10),
            new Venue("Medium Center", 1000, 100, 10, 10)
        );
        VenueSelector selector = new VenueSelector(venues);

        Venue selected = selector.selectVenue(400, 80);
        assertNull(selected, "Should return null when no venue meets requirements");
    }

    @Test
    public void testExactBudgetAndCapacityMatch() {
        List<Venue> venues = Arrays.asList(
            new Venue("Perfect Fit", 1000, 100, 10, 10),
            new Venue("Too Expensive", 1100, 100, 10, 10)
        );
        VenueSelector selector = new VenueSelector(venues);

        Venue selected = selector.selectVenue(1000, 100);
        assertNotNull(selected, "Should select venue with exact match");
        assertEquals("Perfect Fit", selected.getName());
    }

    @Test
    public void testEmptyVenueList() {
        VenueSelector selector = new VenueSelector(Arrays.asList());

        Venue selected = selector.selectVenue(1000, 50);
        assertNull(selected, "Should return null for empty venue list");
    }
}

