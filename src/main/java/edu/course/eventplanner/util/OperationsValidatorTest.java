package edu.course.eventplanner.util;

import edu.course.eventplanner.model.Task;
import edu.course.eventplanner.service.TaskManager;

/**
 * Utility class for testing task management operations.
 * This test helper validates task execution and undo workflows.
 */
public class OperationsValidatorTest {
    
    private final TaskManager taskManager;
    
    public OperationsValidatorTest() {
        this.taskManager = new TaskManager();
    }
    
    /**
     * Adds a task for testing.
     */
    public void addTask(String description) {
        taskManager.addTask(new Task(description));
    }
    
    /**
     * Executes the next task and returns success status.
     */
    public boolean executeNextTask() {
        Task task = taskManager.executeNextTask();
        return task != null;
    }
    
    /**
     * Undoes the last task and returns success status.
     */
    public boolean undoLastTask() {
        Task task = taskManager.undoLastTask();
        return task != null;
    }
    
    /**
     * Gets the remaining task count.
     */
    public int getRemainingTaskCount() {
        return taskManager.remainingTaskCount();
    }
    
    /**
     * Validates the task queue state.
     */
    public boolean validateTaskQueueState(int expectedCount) {
        return taskManager.remainingTaskCount() == expectedCount;
    }
    
    /**
     * Runs a full task workflow test.
     */
    public boolean runFullWorkflowTest() {
        // Add tasks
        addTask("Task 1");
        addTask("Task 2");
        addTask("Task 3");
        
        // Execute one
        if (!executeNextTask()) return false;
        if (getRemainingTaskCount() != 2) return false;
        
        // Undo
        if (!undoLastTask()) return false;
        
        return true;
    }
}
