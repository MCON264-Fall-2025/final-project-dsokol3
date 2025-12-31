package edu.course.eventplanner.service;

import edu.course.eventplanner.model.Venue;
import java.util.*;

/*
    1. Venue Selection (Budget-Aware)

    The program asks the user for:

        Event budget
        Number of guests

    A venue is valid if:

        Its cost is less than or equal to the budget
        Its capacity is greater than or equal to the number of guests

    From all valid venues, select the best venue:

        Lowest cost
        If tied, smallest capacity that still fits

    You must use a sorting algorithm or a Binary Search Tree to justify your choice.
*/
public class VenueSelector {
    private final List<Venue> venues;

    public VenueSelector(List<Venue> venues) {
        this.venues = venues;
    }

    public Venue selectVenue(double budget, int guestCount) {
        Comparator<Venue> venueComparator = Comparator.comparingDouble(Venue::getCost)
                .thenComparingInt(Venue::getCapacity);
        TreeSet<Venue> validVenues = new TreeSet<>(venueComparator);

        for (Venue venue : venues) {
            if (venue.getCost() <= budget && venue.getCapacity() >= guestCount) {
                validVenues.add(venue);
            }
        }
        if (validVenues.isEmpty()) {
            return null;
        }
        return validVenues.first();
    }
}
