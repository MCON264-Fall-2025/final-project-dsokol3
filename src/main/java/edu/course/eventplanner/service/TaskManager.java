package edu.course.eventplanner.service;

import edu.course.eventplanner.model.Task;
import java.util.*;

public class TaskManager {
    private final Queue<Task> upcoming = new LinkedList<>();
    private final Stack<Task> completed = new Stack<>();

    public void addTask(Task task) { /* TODO */
        upcoming.add(task);
    }

    public Task executeNextTask() {
        Task task = upcoming.poll();
        if (task != null) {
            completed.push(task);
        }
        return task;
    }

    public Task undoLastTask() {
        if (!completed.isEmpty()) {
            Task task = completed.pop();
            upcoming.add(task);
            return task;
        }
        return null;
    }

    public int remainingTaskCount() {
        return upcoming.size();
    }
}
