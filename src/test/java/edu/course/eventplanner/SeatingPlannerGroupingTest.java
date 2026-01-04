package edu.course.eventplanner;

import edu.course.eventplanner.model.Guest;
import edu.course.eventplanner.model.Venue;
import edu.course.eventplanner.service.SeatingPlanner;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SeatingPlannerGroupingTest {

    @Test
    void groupFitsAtOneTable_allMembersTogether() {
        Venue venue = new Venue("Hall", 100, 20, 2, 4); // 2 tables, 4 seats each
        SeatingPlanner planner = new SeatingPlanner(venue);

        List<Guest> guests = Arrays.asList(
            new Guest("G1", "GroupA"),
            new Guest("G2", "GroupA"),
            new Guest("G3", "GroupA"),
            new Guest("G4", "GroupB"),
            new Guest("G5", "GroupB")
        );

        Map<Integer, List<Guest>> seating = planner.generateSeating(guests);
        int totalSeated = seating.values().stream().mapToInt(List::size).sum();
        assertEquals(5, totalSeated);

        // Verify that all GroupA members are seated at the same table
        int tablesWithGroupA = 0;
        for (List<Guest> table : seating.values()) {
            long count = table.stream().filter(g -> "GroupA".equals(g.getGroupTag())).count();
            if (count > 0) tablesWithGroupA++;
        }
        assertEquals(1, tablesWithGroupA, "All GroupA members should be at the same table when they fit");
    }

    @Test
    void largeGroup_spansTables_firstTableFull() {
        Venue venue = new Venue("SmallHall", 200, 50, 2, 3); // 2 tables, 3 seats each
        SeatingPlanner planner = new SeatingPlanner(venue);

        List<Guest> guests = new ArrayList<>();
        // GroupA: 5 members
        for (int i = 0; i < 5; i++) guests.add(new Guest("A" + i, "GroupA"));
        // GroupB: 1 member
        guests.add(new Guest("B1", "GroupB"));

        Map<Integer, List<Guest>> seating = planner.generateSeating(guests);

        // Table 1 should be full and contain only GroupA members
        List<Guest> table1 = seating.get(1);
        assertEquals(3, table1.size());
        assertTrue(table1.stream().allMatch(g -> "GroupA".equals(g.getGroupTag())),
            "Table 1 should have only GroupA members and be full");

        // All guests seated
        int totalSeated = seating.values().stream().mapToInt(List::size).sum();
        assertEquals(6, totalSeated);
    }
}