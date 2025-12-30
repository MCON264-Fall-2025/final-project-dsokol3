package edu.course.eventplanner.service;

import edu.course.eventplanner.model.*;
import java.util.*;

public class SeatingPlanner {
    private final Venue venue;

    public SeatingPlanner(Venue venue) {
        this.venue = venue;
    }

    public Map<Integer, List<Guest>> generateSeating(List<Guest> guests) {
        Map<Integer, List<Guest>> seating = new HashMap<>();
        int tableCount = venue.getTables();
        int seatsPerTable = venue.getSeatsPerTable();
        for (int i = 0; i < tableCount; i++) {
            seating.put(i + 1, new ArrayList<>());
        }
        int tableIndex = 0;
        for (Guest guest : guests) {
            seating.get((tableIndex % tableCount) + 1).add(guest);
            tableIndex++;
        }
        return seating;
    }
}
