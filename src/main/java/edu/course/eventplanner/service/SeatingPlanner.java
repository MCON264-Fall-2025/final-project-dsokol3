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
        if (guests == null || guests.isEmpty()) {
            return seating;
        }

        int tableCount = venue.getTables();
        int seatsPerTable = venue.getSeatsPerTable();

        if (tableCount <= 0 || seatsPerTable <= 0) {
            return seating;
        }

        int totalCapacityInVenue = tableCount * seatsPerTable;
        if (guests.size() > totalCapacityInVenue) {
            throw new IllegalStateException("Not enough seats for all guests");
        }

        // Group guests by groupTag using a queue for fair seating
        Map<String, Queue<Guest>> groups = new HashMap<>();
        for (Guest g : guests) {
            groups.computeIfAbsent(g.getGroupTag(), k -> new LinkedList<>()).add(g);
        }

        // TreeSet ordering groups by remaining size (desc), then by tag for tie-breaker
        class GroupEntry {
            final String tag;
            int size;
            GroupEntry(String tag, int size) { this.tag = tag; this.size = size; }
        }

        Comparator<GroupEntry> cmp = Comparator
                .comparingInt((GroupEntry e) -> -e.size)
                .thenComparing(e -> e.tag);

        TreeSet<GroupEntry> orderedGroups = new TreeSet<>(cmp);
        for (Map.Entry<String, Queue<Guest>> e : groups.entrySet()) {
            orderedGroups.add(new GroupEntry(e.getKey(), e.getValue().size()));
        }

        // Fill tables one at a time
        for (int table = 1; table <= tableCount; table++) {
            List<Guest> tableList = new ArrayList<>(seatsPerTable);
            int seatsLeft = seatsPerTable;

            while (seatsLeft > 0 && !orderedGroups.isEmpty()) {
                GroupEntry ge = orderedGroups.pollFirst();
                Queue<Guest> q = groups.get(ge.tag);
                int seatCount = Math.min(seatsLeft, q.size());

                for (int i = 0; i < seatCount; i++) {
                    tableList.add(q.poll());
                }

                seatsLeft -= seatCount;

                if (!q.isEmpty()) {
                    // update remaining size and reinsert
                    orderedGroups.add(new GroupEntry(ge.tag, q.size()));
                }
            }

            seating.put(table, tableList);
        }

        return seating;
    }
}
