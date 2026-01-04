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
        Venue venue = new Venue("Hall", 100, 8, 2, 4);
        SeatingPlanner planner = new SeatingPlanner(venue);

        List<Guest> guests = Arrays.asList(
            new Guest("A1", "GroupA"), new Guest("A2", "GroupA"), new Guest("A3", "GroupA"),
            new Guest("B1", "GroupB"), new Guest("B2", "GroupB")
        );

        Map<Integer, List<Guest>> seating = planner.generateSeating(guests);
        assertEquals(5, seating.values().stream().mapToInt(List::size).sum());

        // Verify GroupA members are at same table
        int tablesWithGroupA = 0;
        for (List<Guest> table : seating.values()) {
            if (table.stream().anyMatch(g -> "GroupA".equals(g.getGroupTag()))) tablesWithGroupA++;
        }
        assertEquals(1, tablesWithGroupA);
    }

    @Test
    void largeGroup_spansTables_firstTableFull() {
        Venue venue = new Venue("SmallHall", 200, 6, 2, 3);
        SeatingPlanner planner = new SeatingPlanner(venue);

        List<Guest> guests = new ArrayList<>();
        for (int i = 0; i < 5; i++) guests.add(new Guest("A" + i, "GroupA"));
        guests.add(new Guest("B1", "GroupB"));

        Map<Integer, List<Guest>> seating = planner.generateSeating(guests);

        List<Guest> table1 = seating.get(1);
        assertEquals(3, table1.size());
        assertTrue(table1.stream().allMatch(g -> "GroupA".equals(g.getGroupTag())));
        assertEquals(6, seating.values().stream().mapToInt(List::size).sum());
    }
    
    @Test
    void singleGuest_seatedAlone() {
        Venue venue = new Venue("SingleTable", 100, 10, 1, 10);
        SeatingPlanner planner = new SeatingPlanner(venue);
        
        Map<Integer, List<Guest>> seating = planner.generateSeating(
            Collections.singletonList(new Guest("Solo", "alone")));
        
        assertEquals(1, seating.values().stream().mapToInt(List::size).sum());
    }
}