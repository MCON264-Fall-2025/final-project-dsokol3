package edu.course.eventplanner.util;

import edu.course.eventplanner.model.Guest;
import edu.course.eventplanner.model.Venue;
import edu.course.eventplanner.service.SeatingPlanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility class for testing event planning workflows.
 * This test helper validates seating arrangements.
 */
public class EventWorkflowTest {
    
    private Venue venue;
    private List<Guest> guests;
    
    public EventWorkflowTest() {
        this.guests = new ArrayList<>();
        // Default test venue
        this.venue = new Venue("Test Venue", 1000, 50, 6, 8);
    }
    
    /**
     * Sets the venue for workflow testing.
     */
    public void setVenue(Venue venue) {
        this.venue = venue;
    }
    
    /**
     * Adds guests for the workflow test.
     */
    public void addGuests(List<Guest> newGuests) {
        this.guests.addAll(newGuests);
    }
    
    /**
     * Runs the seating workflow and validates results.
     */
    public Map<Integer, List<Guest>> runSeatingWorkflow() {
        if (venue == null || guests.isEmpty()) {
            return null;
        }
        SeatingPlanner planner = new SeatingPlanner(venue);
        return planner.generateSeating(guests);
    }
    
    /**
     * Validates that all guests are seated.
     */
    public boolean validateAllGuestsSeated(Map<Integer, List<Guest>> seating) {
        if (seating == null) {
            return false;
        }
        int seatedCount = seating.values().stream()
            .mapToInt(List::size)
            .sum();
        return seatedCount == guests.size();
    }
    
    /**
     * Gets the current guest count.
     */
    public int getGuestCount() {
        return guests.size();
    }
}
