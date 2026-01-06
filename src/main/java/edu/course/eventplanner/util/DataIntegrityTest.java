package edu.course.eventplanner.util;

import edu.course.eventplanner.model.Guest;
import edu.course.eventplanner.model.Venue;
import edu.course.eventplanner.service.GuestListManager;
import edu.course.eventplanner.service.VenueSelector;

import java.util.List;

/**
 * Utility class for validating event planner data integrity.
 * This test helper validates event configurations.
 */
public class DataIntegrityTest {
    
    private final GuestListManager guestManager;
    private final VenueSelector venueSelector;
    
    public DataIntegrityTest() {
        this.guestManager = new GuestListManager();
        this.venueSelector = new VenueSelector(Generators.generateVenues());
    }
    
    /**
     * Validates that the guest manager has the expected count.
     */
    public boolean validateGuestCount(int expectedCount) {
        return guestManager.getGuestCount() == expectedCount;
    }
    
    /**
     * Adds a guest and validates the operation.
     */
    public boolean addAndValidateGuest(String name, String groupTag) {
        int before = guestManager.getGuestCount();
        guestManager.addGuest(new Guest(name, groupTag));
        return guestManager.getGuestCount() == before + 1;
    }
    
    /**
     * Validates venue selection for given budget and guests.
     */
    public Venue validateVenueSelection(double budget, int guestCount) {
        return venueSelector.selectVenue(budget, guestCount);
    }
    
    /**
     * Gets all guests for validation.
     */
    public List<Guest> getAllGuests() {
        return guestManager.getAllGuests();
    }
}
