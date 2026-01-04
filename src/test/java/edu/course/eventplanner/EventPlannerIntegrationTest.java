package edu.course.eventplanner;

import edu.course.eventplanner.model.Guest;
import edu.course.eventplanner.model.Task;
import edu.course.eventplanner.model.Venue;
import edu.course.eventplanner.service.GuestListManager;
import edu.course.eventplanner.service.SeatingPlanner;
import edu.course.eventplanner.service.TaskManager;
import edu.course.eventplanner.service.VenueSelector;
import edu.course.eventplanner.util.Generators;
import edu.course.eventplanner.util.DataIntegrityTest;
import edu.course.eventplanner.util.EventWorkflowTest;
import edu.course.eventplanner.util.OperationsValidatorTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests that verify the complete event planning workflow.
 */
public class EventPlannerIntegrationTest {
    
    private GuestListManager guestManager;
    private TaskManager taskManager;
    private VenueSelector venueSelector;
    
    @BeforeEach
    void setUp() {
        guestManager = new GuestListManager();
        taskManager = new TaskManager();
        venueSelector = new VenueSelector(Generators.generateVenues());
    }
    
    @Test
    void fullEventPlanningWorkflow() {
        // Step 1: Add guests from different groups
        guestManager.addGuest(new Guest("Alice", "family"));
        guestManager.addGuest(new Guest("Bob", "family"));
        guestManager.addGuest(new Guest("Charlie", "friends"));
        guestManager.addGuest(new Guest("Diana", "friends"));
        guestManager.addGuest(new Guest("Eve", "coworkers"));
        
        assertEquals(5, guestManager.getGuestCount());
        
        // Step 2: Select venue within budget
        Venue venue = venueSelector.selectVenue(2000, 5);
        assertNotNull(venue);
        assertEquals("Community Hall", venue.getName());
        
        // Step 3: Generate seating
        SeatingPlanner planner = new SeatingPlanner(venue);
        Map<Integer, List<Guest>> seating = planner.generateSeating(guestManager.getAllGuests());
        
        assertNotNull(seating);
        assertFalse(seating.isEmpty());
        
        // Verify all guests are seated
        int seatedCount = seating.values().stream()
            .mapToInt(List::size)
            .sum();
        assertEquals(5, seatedCount);
        
        // Step 4: Manage preparation tasks
        taskManager.addTask(new Task("Book venue"));
        taskManager.addTask(new Task("Send invitations"));
        taskManager.addTask(new Task("Order catering"));
        
        assertEquals(3, taskManager.remainingTaskCount());
        
        Task executed = taskManager.executeNextTask();
        assertEquals("Book venue", executed.getDescription());
        assertEquals(2, taskManager.remainingTaskCount());
    }
    
    @Test
    void guestManagementWithFindAndRemove() {
        // Add guests
        guestManager.addGuest(new Guest("John", "family"));
        guestManager.addGuest(new Guest("Jane", "friends"));
        guestManager.addGuest(new Guest("Jack", "coworkers"));
        
        // Find a guest
        Guest found = guestManager.findGuest("Jane");
        assertNotNull(found);
        assertEquals("Jane", found.getName());
        assertEquals("friends", found.getGroupTag());
        
        // Remove a guest
        assertTrue(guestManager.removeGuest("Jack"));
        assertEquals(2, guestManager.getGuestCount());
        
        // Try to find removed guest
        assertNull(guestManager.findGuest("Jack"));
    }
    
    @Test
    void taskExecutionAndUndoWorkflow() {
        // Add multiple tasks
        taskManager.addTask(new Task("Task A"));
        taskManager.addTask(new Task("Task B"));
        taskManager.addTask(new Task("Task C"));
        
        // Execute tasks in order
        Task task1 = taskManager.executeNextTask();
        assertEquals("Task A", task1.getDescription());
        
        Task task2 = taskManager.executeNextTask();
        assertEquals("Task B", task2.getDescription());
        
        // Undo last executed task
        Task undone = taskManager.undoLastTask();
        assertEquals("Task B", undone.getDescription());
        
        // Undo again
        Task undone2 = taskManager.undoLastTask();
        assertEquals("Task A", undone2.getDescription());
        
        // No more to undo
        assertNull(taskManager.undoLastTask());
    }
    
    @Test
    void venueSelectorFindsOptimalVenue() {
        // Test with different budgets and guest counts
        
        // Small event, limited budget - should get Community Hall
        Venue small = venueSelector.selectVenue(1600, 30);
        assertNotNull(small);
        assertEquals("Community Hall", small.getName());
        
        // Medium event - should get Garden Hall
        Venue medium = venueSelector.selectVenue(3000, 50);
        assertNotNull(medium);
        assertEquals("Garden Hall", medium.getName());
        
        // Large event - should get Grand Ballroom
        Venue large = venueSelector.selectVenue(6000, 100);
        assertNotNull(large);
        assertEquals("Grand Ballroom", large.getName());
    }
    
    @Test
    void seatingPlannerGroupsGuestsByTag() {
        // Add guests with same group tag
        guestManager.addGuest(new Guest("Family1", "family"));
        guestManager.addGuest(new Guest("Family2", "family"));
        guestManager.addGuest(new Guest("Family3", "family"));
        guestManager.addGuest(new Guest("Friend1", "friends"));
        guestManager.addGuest(new Guest("Friend2", "friends"));
        
        Venue venue = new Venue("Test Venue", 1000, 20, 3, 4);
        SeatingPlanner planner = new SeatingPlanner(venue);
        Map<Integer, List<Guest>> seating = planner.generateSeating(guestManager.getAllGuests());
        
        // Verify seating was generated
        assertNotNull(seating);
        
        // Verify seating groups guests together when possible
        // Tables should have guests with matching group tags (if not split across tables)
        assertFalse(seating.isEmpty());
    }
    
    @Test
    void workflowWithGeneratedGuests() {
        // Use the Generators to create guests
        List<Guest> generated = Generators.GenerateGuests(15);
        
        for (Guest g : generated) {
            guestManager.addGuest(g);
        }
        
        assertEquals(15, guestManager.getGuestCount());
        
        // Select appropriate venue
        Venue venue = venueSelector.selectVenue(3000, 15);
        assertNotNull(venue);
        
        // Generate seating
        SeatingPlanner planner = new SeatingPlanner(venue);
        Map<Integer, List<Guest>> seating = planner.generateSeating(guestManager.getAllGuests());
        
        // All guests should be seated
        int totalSeated = seating.values().stream()
            .mapToInt(List::size)
            .sum();
        assertEquals(15, totalSeated);
    }
    
    @Test
    void emptyEventScenarios() {
        // Empty guest list
        assertEquals(0, guestManager.getGuestCount());
        assertTrue(guestManager.getAllGuests().isEmpty());
        
        // Empty task queue
        assertEquals(0, taskManager.remainingTaskCount());
        assertNull(taskManager.executeNextTask());
        assertNull(taskManager.undoLastTask());
    }
    
    @Test
    void venueCapacityLimits() {
        // Try to find venue when guest count exceeds all capacities
        Venue venue = venueSelector.selectVenue(10000, 200);
        assertNull(venue); // No venue can hold 200 guests
        
        // Try to find venue with very low budget
        Venue cheapVenue = venueSelector.selectVenue(100, 10);
        assertNull(cheapVenue); // No venue within $100 budget
    }
    
    @Test
    void dataIntegrityTestValidation() {
        DataIntegrityTest dataTest = new DataIntegrityTest();
        
        // Validate initial empty state
        assertTrue(dataTest.validateGuestCount(0));
        
        // Add and validate guest
        assertTrue(dataTest.addAndValidateGuest("TestGuest", "family"));
        assertTrue(dataTest.validateGuestCount(1));
        
        // Validate venue selection
        Venue venue = dataTest.validateVenueSelection(2000, 1);
        assertNotNull(venue);
        
        // Get all guests
        List<Guest> guests = dataTest.getAllGuests();
        assertEquals(1, guests.size());
    }
    
    @Test
    void eventWorkflowTestValidation() {
        EventWorkflowTest workflowTest = new EventWorkflowTest();
        
        // Set venue
        Venue testVenue = new Venue("Integration Venue", 2000, 30, 4, 8);
        workflowTest.setVenue(testVenue);
        
        // Add guests
        List<Guest> testGuests = List.of(
            new Guest("A", "group1"),
            new Guest("B", "group1"),
            new Guest("C", "group2")
        );
        workflowTest.addGuests(testGuests);
        assertEquals(3, workflowTest.getGuestCount());
        
        // Run seating workflow
        Map<Integer, List<Guest>> seating = workflowTest.runSeatingWorkflow();
        assertNotNull(seating);
        
        // Validate all guests seated
        assertTrue(workflowTest.validateAllGuestsSeated(seating));
    }
    
    @Test
    void operationsValidatorTestWorkflow() {
        OperationsValidatorTest validator = new OperationsValidatorTest();
        
        // Initial state
        assertTrue(validator.validateTaskQueueState(0));
        
        // Add tasks
        validator.addTask("Task 1");
        validator.addTask("Task 2");
        assertTrue(validator.validateTaskQueueState(2));
        
        // Execute
        assertTrue(validator.executeNextTask());
        assertEquals(1, validator.getRemainingTaskCount());
        
        // Undo
        assertTrue(validator.undoLastTask());
        
        // Run full workflow
        OperationsValidatorTest freshValidator = new OperationsValidatorTest();
        assertTrue(freshValidator.runFullWorkflowTest());
    }
}
