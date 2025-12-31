package edu.course.eventplanner.service;

import edu.course.eventplanner.model.*;
import java.util.*;

public class SeatingPlanner {
    private final Venue venue;

    public SeatingPlanner(Venue venue) {
        this.venue = venue;
    }

    /**
     * Generates a seating arrangement for the given list of guests.
     * Tables are filled from table 1 to the last table.
     *
     * @param guests the list of guests to be seated
     * @return a map where the key is the table number and the value is the list of
     *         guests assigned to that table
     * @throws IllegalStateException if the venue configuration is invalid or
     *                               there is insufficient capacity
     */
    public Map<Integer, List<Guest>> generateSeating(List<Guest> guests) {
        Map<Integer, List<Guest>> seating = new LinkedHashMap<>();
        if (guests == null || guests.size() == 0) {
            return seating;
        }

        int tableCount = venue.getTables();
        int seatsPerTable = venue.getSeatsPerTable();

        if (tableCount <= 0 || seatsPerTable <= 0) {
            return seating;
        }
        // Calculate total capacity
        int totalCapacityInVenue = tableCount * seatsPerTable;

        if (guests.size() > totalCapacityInVenue) {
            throw new IllegalStateException(String.format("Not enough seats for all guests"));
        }

        int guestIndex = 0;

        for (int table = 1; table <= tableCount; table++) {
            List<Guest> tableList = new ArrayList<>(seatsPerTable);

            for (int seat = 0; seat < seatsPerTable && guestIndex < guests.size(); seat++) {
                tableList.add(guests.get(guestIndex));
                guestIndex++;
            }

            seating.put(table, tableList);
        }

        return seating;
    }
}
