package edu.course.eventplanner;

import edu.course.eventplanner.model.*;
import edu.course.eventplanner.service.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    
    @ParameterizedTest(name = "Task: {0}")
    @ValueSource(strings = {"Decorate", "Send invitations"})
    void addTask_and_executeNextTask(String taskDescription) {
        TaskManager manager = new TaskManager();
        Task task = new Task(taskDescription);
        manager.addTask(task);
        
        assertEquals(1, manager.remainingTaskCount());
        Task executed = manager.executeNextTask();
        assertEquals(task, executed);
        assertEquals(0, manager.remainingTaskCount());
    }

    @Test
    void undoLastTask_returnsLastExecutedTask() {
        TaskManager manager = new TaskManager();
        Task task = new Task("Decorate");
        manager.addTask(task);
        manager.executeNextTask();
        
        Task undone = manager.undoLastTask();
        assertEquals(task, undone);
    }

    @ParameterizedTest(name = "Adding {0} tasks - FIFO order")
    @ValueSource(ints = {2, 5})
    void addMultipleTasks_executesInFifoOrder(int taskCount) {
        TaskManager manager = new TaskManager();
        IntStream.rangeClosed(1, taskCount)
            .forEach(i -> manager.addTask(new Task("Task " + i)));
        
        assertEquals(taskCount, manager.remainingTaskCount());
        for (int i = 1; i <= taskCount; i++) {
            assertEquals("Task " + i, manager.executeNextTask().getDescription());
        }
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
    
    @Test
    void undoMultipleTasks_returnsInLifoOrder() {
        TaskManager manager = new TaskManager();
        IntStream.rangeClosed(1, 3)
            .forEach(i -> manager.addTask(new Task("Task " + i)));
        
        for (int i = 0; i < 3; i++) {
            manager.executeNextTask();
        }
        
        // Undo in reverse order (LIFO)
        for (int i = 3; i >= 1; i--) {
            assertEquals("Task " + i, manager.undoLastTask().getDescription());
        }
        assertNull(manager.undoLastTask());
    }
}
