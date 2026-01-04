package edu.course.eventplanner;

import edu.course.eventplanner.model.*;
import edu.course.eventplanner.service.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    
    // Parameterized test: add and execute tasks with various descriptions
    @ParameterizedTest(name = "Task: {0}")
    @ValueSource(strings = {"Decorate", "Cook", "Send invitations", "Book venue", "Order catering"})
    void addTask_and_executeNextTask(String taskDescription) {
        TaskManager manager = new TaskManager();
        Task task = new Task(taskDescription);
        manager.addTask(task);
        
        assertEquals(1, manager.remainingTaskCount());
        Task executed = manager.executeNextTask();
        assertEquals(task, executed);
        assertEquals(taskDescription, executed.getDescription());
        assertEquals(0, manager.remainingTaskCount());
    }

    // Parameterized test: undo with different task descriptions
    @ParameterizedTest(name = "Undo task: {0}")
    @ValueSource(strings = {"Decorate", "Setup tables", "Arrange flowers"})
    void undoLastTask_returnsLastExecutedTask(String taskDescription) {
        TaskManager manager = new TaskManager();
        Task task = new Task(taskDescription);
        manager.addTask(task);
        manager.executeNextTask();
        
        Task undone = manager.undoLastTask();
        assertEquals(task, undone);
        assertEquals(taskDescription, undone.getDescription());
    }

    // Parameterized test: add multiple tasks and verify queue order (FIFO)
    @ParameterizedTest(name = "Adding {0} tasks in queue order")
    @ValueSource(ints = {2, 3, 5, 10})
    void addMultipleTasks_executesInFifoOrder(int taskCount) {
        TaskManager manager = new TaskManager();
        
        // Add tasks numbered 1 to taskCount
        IntStream.rangeClosed(1, taskCount)
            .forEach(i -> manager.addTask(new Task("Task " + i)));
        
        assertEquals(taskCount, manager.remainingTaskCount());
        
        // Execute and verify FIFO order
        for (int i = 1; i <= taskCount; i++) {
            Task executed = manager.executeNextTask();
            assertEquals("Task " + i, executed.getDescription());
        }
        assertEquals(0, manager.remainingTaskCount());
    }

    @Test
    void executeNextTask_returnsNullIfNoTasks() {
        TaskManager manager = new TaskManager();
        assertNull(manager.executeNextTask());
    }

    @Test
    void undoLastTask_returnsNullIfNoCompletedTasks() {
        TaskManager manager = new TaskManager();
        assertNull(manager.undoLastTask());
    }
    
    // Parameterized test: undo multiple tasks in LIFO order
    @ParameterizedTest(name = "Execute {0} tasks then undo in reverse order")
    @ValueSource(ints = {2, 3, 4})
    void undoMultipleTasks_returnsInLifoOrder(int taskCount) {
        TaskManager manager = new TaskManager();
        
        // Add and execute tasks
        IntStream.rangeClosed(1, taskCount)
            .forEach(i -> manager.addTask(new Task("Task " + i)));
        
        for (int i = 0; i < taskCount; i++) {
            manager.executeNextTask();
        }
        
        // Undo in reverse order (LIFO)
        for (int i = taskCount; i >= 1; i--) {
            Task undone = manager.undoLastTask();
            assertNotNull(undone);
            assertEquals("Task " + i, undone.getDescription());
        }
        
        // No more to undo
        assertNull(manager.undoLastTask());
    }
}
