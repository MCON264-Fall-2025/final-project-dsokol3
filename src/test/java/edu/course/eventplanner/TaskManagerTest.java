package edu.course.eventplanner;

import edu.course.eventplanner.model.*;
import edu.course.eventplanner.service.*;
import org.junit.jupiter.api.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    @Test
    void addTask_and_executeNextTask() {
        TaskManager manager = new TaskManager();
        Task t1 = new Task("Decorate");
        Task t2 = new Task("Cook");
        manager.addTask(t1);
        manager.addTask(t2);
        assertEquals(2, manager.remainingTaskCount());
        Task executed = manager.executeNextTask();
        assertEquals(t1, executed);
        assertEquals(1, manager.remainingTaskCount());
    }

    @Test
    void undoLastTask_returnsLastExecutedTask() {
        TaskManager manager = new TaskManager();
        Task t1 = new Task("Decorate");
        manager.addTask(t1);
        manager.executeNextTask();
        Task undone = manager.undoLastTask();
        assertEquals(t1, undone);
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
}
