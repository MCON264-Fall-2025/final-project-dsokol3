package edu.course.eventplanner;

import edu.course.eventplanner.model.Guest;
import edu.course.eventplanner.model.Venue;
import edu.course.eventplanner.service.SeatingPlanner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SeatingPlannerGroupingTest {

    // Data provider for group fitting scenarios
    static Stream<Arguments> groupFitsScenarios() {
        return Stream.of(
            // tables, seatsPerTable, groupASize, groupBSize, groupATag, groupBTag
            Arguments.of(2, 4, 3, 2, "GroupA", "GroupB"),
            Arguments.of(3, 5, 4, 3, "Family", "Friends"),
            Arguments.of(2, 6, 5, 4, "Team1", "Team2"),
            Arguments.of(4, 3, 3, 2, "VIP", "Regular")
        );
    }

    @ParameterizedTest(name = "{4}({2}) and {5}({3}) at {0} tables x {1} seats")
    @MethodSource("groupFitsScenarios")
    void groupFitsAtOneTable_allMembersTogether(int tables, int seatsPerTable, 
            int groupASize, int groupBSize, String groupATag, String groupBTag) {
        Venue venue = new Venue("Hall", 100, tables * seatsPerTable, tables, seatsPerTable);
        SeatingPlanner planner = new SeatingPlanner(venue);

        List<Guest> guests = new ArrayList<>();
        for (int i = 1; i <= groupASize; i++) {
            guests.add(new Guest(groupATag + i, groupATag));
        }
        for (int i = 1; i <= groupBSize; i++) {
            guests.add(new Guest(groupBTag + i, groupBTag));
        }

        Map<Integer, List<Guest>> seating = planner.generateSeating(guests);
        int totalSeated = seating.values().stream().mapToInt(List::size).sum();
        assertEquals(groupASize + groupBSize, totalSeated);

        // Verify that all members of groupA are seated at the same table (if they fit)
        if (groupASize <= seatsPerTable) {
            int tablesWithGroupA = 0;
            for (List<Guest> table : seating.values()) {
                long count = table.stream().filter(g -> groupATag.equals(g.getGroupTag())).count();
                if (count > 0) tablesWithGroupA++;
            }
            assertEquals(1, tablesWithGroupA, 
                "All " + groupATag + " members should be at the same table when they fit");
        }
    }

    // Data provider for large group spanning scenarios
    static Stream<Arguments> largeGroupScenarios() {
        return Stream.of(
            // tables, seatsPerTable, largeGroupSize, smallGroupSize
            Arguments.of(2, 3, 5, 1),
            Arguments.of(3, 4, 10, 2),
            Arguments.of(2, 5, 8, 2)
        );
    }

    @ParameterizedTest(name = "Large group of {2} spans {0} tables (x {1} seats)")
    @MethodSource("largeGroupScenarios")
    void largeGroup_spansTables_firstTableFull(int tables, int seatsPerTable, 
            int largeGroupSize, int smallGroupSize) {
        Venue venue = new Venue("SmallHall", 200, tables * seatsPerTable, tables, seatsPerTable);
        SeatingPlanner planner = new SeatingPlanner(venue);

        List<Guest> guests = new ArrayList<>();
        for (int i = 0; i < largeGroupSize; i++) {
            guests.add(new Guest("A" + i, "GroupA"));
        }
        for (int i = 0; i < smallGroupSize; i++) {
            guests.add(new Guest("B" + i, "GroupB"));
        }

        Map<Integer, List<Guest>> seating = planner.generateSeating(guests);

        // Table 1 should be full and contain only GroupA members
        List<Guest> table1 = seating.get(1);
        assertEquals(seatsPerTable, table1.size());
        assertTrue(table1.stream().allMatch(g -> "GroupA".equals(g.getGroupTag())),
            "Table 1 should have only GroupA members and be full");

        // All guests seated
        int totalSeated = seating.values().stream().mapToInt(List::size).sum();
        assertEquals(largeGroupSize + smallGroupSize, totalSeated);
    }
    
    @Test
    void singleGuest_seatedAlone() {
        Venue venue = new Venue("SingleTable", 100, 10, 1, 10);
        SeatingPlanner planner = new SeatingPlanner(venue);
        
        List<Guest> guests = Collections.singletonList(new Guest("Solo", "alone"));
        Map<Integer, List<Guest>> seating = planner.generateSeating(guests);
        
        assertEquals(1, seating.values().stream().mapToInt(List::size).sum());
    }
}