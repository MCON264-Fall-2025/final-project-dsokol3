package edu.course.eventplanner;

import edu.course.eventplanner.model.*;
import edu.course.eventplanner.service.*;
import edu.course.eventplanner.util.Generators;
import java.util.*;

public class Main {
    private static GuestListManager guestListManager = new GuestListManager();
    private static VenueSelector venueSelector;
    private static Venue selectedVenue = null;
    private static TaskManager taskManager = new TaskManager();
    private static Scanner kb = new Scanner(System.in);

    public static void main(String[] args) {
        // use the shared scanner `kb`
        System.out.println("Welcome to the Event Planner Application");
        menu();
        int choice = readMenuChoice();
        while (choice != 0) {
            switch (choice) {
                case 1:
                    loadSampleData();
                    break;
                case 2:
                    addGuest();
                    break;
                case 3:
                    removeGuest();
                    break;
                case 4:
                    selectVenue();
                    break;
                case 5:
                    generateSeatingChart();
                    break;
                case 6:
                    addPreparationTask();
                    break;
                case 7:
                    executeNextTask();
                    break;
                case 8:
                    undoLastTask();
                    break;
                case 9:
                    printEventSummary();
                    break;
                default:
                    System.out.println("Error: Please try again.");
            }
            menu();
            choice = readMenuChoice();
        }
        
        System.out.println("Goodbye!");
        kb.close();
    }

    private static int readMenuChoice() {
        String line = kb.nextLine();
        if (line == null) return 0;
        line = line.trim();
        if (line.isEmpty()) {
            System.out.println("Invalid choice.");
            return -1;
        }
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice.");
            return -1;
        }
    }

    private static void menu() {
        System.out.println("Menu");
        System.out.println("1.Load data");
        System.out.println("2.Add guest");
        System.out.println("3.Remove guest");
        System.out.println("4.Select venue");
        System.out.println("5.Generate seating chart");
        System.out.println("6.Add task");
        System.out.println("7.Execute task");
        System.out.println("8.Undo task");
        System.out.println("9.Summary");
        System.out.println("0.Exit");
        System.out.println("Choice: ");
    }

    private static void loadSampleData() {
        System.out.print("Number of guests: ");
        try {
            int numGuests = Integer.parseInt(kb.nextLine().trim());
            if (numGuests <= 0) {
                System.out.println("Must be positive.");
                return;
            }

            for (Guest guest : Generators.GenerateGuests(numGuests)) {
                guestListManager.addGuest(guest);
            }

            venueSelector = new VenueSelector(Generators.generateVenues());
            System.out.println("Loaded " + numGuests + " guests and 3 venues.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
        }
    }

    private static void addGuest() {
        System.out.print("Name: ");
        String guestName = kb.nextLine().trim();
        System.out.print("Group: ");
        String groupTag = kb.nextLine().trim();
        
        if (guestName.isEmpty() || groupTag.isEmpty()) {
            System.out.println("Name and group required.");
            return;
        }

        guestListManager.addGuest(new Guest(guestName, groupTag));
        System.out.println("Added " + guestName);
    }

    private static void removeGuest() {
        System.out.print("Name to remove: ");
        String guestName = kb.nextLine().trim();
        System.out.println(guestListManager.removeGuest(guestName) ? "Removed" : "Not found");
    }

    private static void selectVenue() {
        if (venueSelector == null) {
            venueSelector = new VenueSelector(Generators.generateVenues());
        }
        
        int guestCount = guestListManager.getGuestCount();
        if (guestCount == 0) {
            System.out.println("Add guests first.");
            return;
        }

        System.out.print("Budget: $");
        try {
            double budget = Double.parseDouble(kb.nextLine().trim());
            selectedVenue = venueSelector.selectVenue(budget, guestCount);
            
            if (selectedVenue != null) {
                System.out.println("Selected: " + selectedVenue.getName() + 
                    " ($" + selectedVenue.getCost() + ", capacity " + selectedVenue.getCapacity() + ")");
            } else {
                System.out.println("No venue fits.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid budget.");
        }
    }

    private static void generateSeatingChart() {
        if (selectedVenue == null) {
            System.out.println("Select a venue first.");
            return;
        }
        
        List<Guest> guests = guestListManager.getAllGuests();
        if (guests.isEmpty()) {
            System.out.println("No guests to seat.");
            return;
        }
        
        try {
            Map<Integer, List<Guest>> seating = new SeatingPlanner(selectedVenue).generateSeating(guests);
            System.out.println("\nSeating for " + selectedVenue.getName() + ":");
            for (Map.Entry<Integer, List<Guest>> entry : seating.entrySet()) {
                System.out.print("Table " + entry.getKey() + ": ");
                List<String> seatedNames = new ArrayList<>();
                for (Guest guest : entry.getValue()) {
                    seatedNames.add(guest.getName() + "(" + guest.getGroupTag() + ")");
                }
                System.out.println(String.join(", ", seatedNames));
            }
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void addPreparationTask() {
        System.out.print("Task: ");
        String taskDescription = kb.nextLine().trim();
        if (taskDescription.isEmpty()) {
            System.out.println("Task cannot be empty.");
            return;
        }
        taskManager.addTask(new Task(taskDescription));
        System.out.println("Added. Remaining: " + taskManager.remainingTaskCount());
    }

    private static void executeNextTask() {
        Task task = taskManager.executeNextTask();
        if (task != null) {
            System.out.println("Executed: " + task.getDescription() + " (Remaining: " + taskManager.remainingTaskCount() + ")");
        } else {
            System.out.println("No tasks.");
        }
    }

    private static void undoLastTask() {
        Task task = taskManager.undoLastTask();
        if (task != null) {
            System.out.println("Undone: " + task.getDescription() + " (Remaining: " + taskManager.remainingTaskCount() + ")");
        } else {
            System.out.println("Nothing to undo.");
        }
    }

    private static void printEventSummary() {
        System.out.println("\n=== Event Summary ===");
        
        List<Guest> guests = guestListManager.getAllGuests();
        System.out.println("Guests: " + guestListManager.getGuestCount());
        if (!guests.isEmpty()) {
            Map<String, Integer> groups = new HashMap<>();
            for (Guest guest : guests) {
                groups.put(guest.getGroupTag(), groups.getOrDefault(guest.getGroupTag(), 0) + 1);
            }
            for (Map.Entry<String, Integer> entry : groups.entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }
        }
        
        System.out.println("Venue: " + (selectedVenue != null ? 
            selectedVenue.getName() + " ($" + selectedVenue.getCost() + ")" : "None"));
        
        System.out.println("Pending tasks: " + taskManager.remainingTaskCount());
    }
}
