Event Planner (Data Structures in Action)

This project is a console-based Java application that plans a small event, demonstrating practical use of the data structures we studied this semester. The program allows for venue selection, guest management, seating planning, and task management with undo functionality.

Features Implemented

Venue Selection: The program selects a venue that fits a given budget and guest count, choosing the lowest-cost option and breaking ties with the smallest capacity.

Guest Management: Users can add, remove, find, and list guests.

Seating Planner: Guests are grouped by groupTag (e.g., family, friends, neighbors) and seated at tables fairly, keeping groups together when possible.

Task Manager: Allows adding preparation tasks, executing them in order, and undoing the last executed task.

Data Structures Used

GuestListManager

Uses a LinkedList<Guest> for the master guest list, which is the source of truth.

Uses a HashMap<String, Guest> for fast lookup by name.

All guests are added via the addGuest method, ensuring the linked list owns the data.

VenueSelector

Uses a TreeSet or sorting with a comparator to select the best venue based on cost and capacity.

This ensures the selection is efficient and correct.

SeatingPlanner

Uses a Map<String, Queue<Guest>> to group guests by groupTag.

Uses a Queue<Guest> to seat guests fairly in each group.

Uses a TreeSet to order groups by remaining size so groups sit together when possible.

TaskManager

Uses a Queue<Task> to store upcoming tasks and execute them in FIFO order.

Uses a Stack<Task> to store completed tasks for undo functionality.

Algorithms and Big-O Complexity

Finding a guest: O(1) on average using the HashMap.

Selecting a venue: O(n log n) using TreeSet or sorting, where n is the number of venues.

Generating seating: O(g log g + m), where g is the number of groups and m is the number of guests.


Tests include
adding, removing, looking up guests, selecting venues, seating guests by group, and executing/undoing tasks.


Console Menu:
Load sample data
Add guest
Remove guest
Select venue
Generate seating chart
Add preparation task
Execute next task
Undo last task
Print event summary
