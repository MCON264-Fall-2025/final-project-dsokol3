package edu.course.eventplanner.service;

import edu.course.eventplanner.model.Task;
import java.util.*;

public class TaskManager {
    private final Queue<Task> upcoming = new LinkedList<>();
    private final Stack<Task> completed = new Stack<>();

    public void addTask(Task task) {
        upcoming.add(task);
    }

    /**
     * Execute the next task
     * @return the task that was executed, or null if there are no upcoming tasks
     */
    public Task executeNextTask() {
        Task task = upcoming.poll();
        if (task != null) {
            completed.push(task);
        }
        return task;
    }

    /***
     * Undo the last task that was executed
     * @return the task that was undone, or null if there are no completed tasks
     */
    public Task undoLastTask() {
        if (!completed.isEmpty()) {
            return completed.pop();
        }
        return null;
    }

    /**
     * Get the number of tasks that have not yet been executed
     * @return the number of remaining tasks
     */
    public int remainingTaskCount() {
        return upcoming.size();
    }
}
