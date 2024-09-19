import java.util.*;
import java.util.logging.*;

// Observer interface to notify about task conflicts or updates
interface TaskObserver {
    void notify(String message);
}

// Task class to represent each task
class Task {
    private String description;
    private String startTime;
    private String endTime;
    private String priority;
    private boolean completed;

    public Task(String description, String startTime, String endTime, String priority) {
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
        this.completed = false;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void editTask(String newDescription, String newStartTime, String newEndTime, String newPriority) {
        this.description = newDescription;
        this.startTime = newStartTime;
        this.endTime = newEndTime;
        this.priority = newPriority;
    }

    public void displayTask() {
        String status = completed ? " (Completed)" : "";
        System.out.println(startTime + " - " + endTime + ": " + description + " [" + priority + "]" + status);
    }
}

// Singleton Schedule Manager with observer pattern for task conflict notifications
class ScheduleManager implements TaskObserver {
    private static ScheduleManager instance;
    private List<Task> tasks;
    private static Logger logger = Logger.getLogger(ScheduleManager.class.getName());

    private ScheduleManager() {
        tasks = new ArrayList<>();
    }

    public static ScheduleManager getInstance() {
        if (instance == null) {
            instance = new ScheduleManager();
        }
        return instance;
    }

    // Add a new task
    public void addTask(Task task) {
        for (Task t : tasks) {
            if (task.getStartTime().equals(t.getStartTime()) || (task.getStartTime().compareTo(t.getEndTime()) < 0 && task.getEndTime().compareTo(t.getStartTime()) > 0)) {
                notify("Error: Task conflicts with an existing task \"" + t.getDescription() + "\".");
                logger.log(Level.WARNING, "Task conflict detected: " + task.getDescription());
                return;
            }
        }
        tasks.add(task);
        tasks.sort(Comparator.comparing(Task::getStartTime));
        System.out.println("Task added successfully. No conflicts.");
        logger.log(Level.INFO, "Task added: " + task.getDescription());
    }

    // Edit an existing task
    public void editTask(String description, String newDescription, String newStartTime, String newEndTime, String newPriority) {
        Task taskToEdit = findTaskByDescription(description);
        if (taskToEdit != null) {
            taskToEdit.editTask(newDescription, newStartTime, newEndTime, newPriority);
            tasks.sort(Comparator.comparing(Task::getStartTime));
            System.out.println("Task edited successfully.");
            logger.log(Level.INFO, "Task edited: " + newDescription);
        } else {
            System.out.println("Error: Task not found.");
            logger.log(Level.WARNING, "Edit failed: Task not found.");
        }
    }

    // Mark a task as completed
    public void completeTask(String description) {
        Task taskToComplete = findTaskByDescription(description);
        if (taskToComplete != null) {
            taskToComplete.setCompleted(true);
            System.out.println("Task marked as completed.");
            logger.log(Level.INFO, "Task completed: " + description);
        } else {
            System.out.println("Error: Task not found.");
            logger.log(Level.WARNING, "Completion failed: Task not found.");
        }
    }

    // Remove an existing task
    public void removeTask(String description) {
        Task taskToRemove = findTaskByDescription(description);
        if (taskToRemove != null) {
            tasks.remove(taskToRemove);
            System.out.println("Task removed successfully.");
            logger.log(Level.INFO, "Task removed: " + description);
        } else {
            System.out.println("Error: Task not found.");
            logger.log(Level.WARNING, "Removal failed: Task not found.");
        }
    }

    // View all tasks sorted by start time
    public void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks scheduled for the day.");
        } else {
            for (Task t : tasks) {
                t.displayTask();
            }
        }
    }

    // View tasks filtered by priority
    public void viewTasksByPriority(String priority) {
        boolean found = false;
        for (Task t : tasks) {
            if (t.getPriority().equalsIgnoreCase(priority)) {
                t.displayTask();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No tasks found with priority: " + priority);
        }
    }

    // Observer notification for task conflicts
    @Override
    public void notify(String message) {
        System.out.println(message);
    }

    // Find a task by description
    private Task findTaskByDescription(String description) {
        for (Task t : tasks) {
            if (t.getDescription().equals(description)) {
                return t;
            }
        }
        return null;
    }
}

// Factory to create Task objects
class TaskFactory {
    public static Task createTask(String description, String startTime, String endTime, String priority) {
        return new Task(description, startTime, endTime, priority);
    }
}

// Main application class
public class AstronautScheduleApp {
    public static void main(String[] args) {
        ScheduleManager scheduleManager = ScheduleManager.getInstance();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Add Task\n2. Edit Task\n3. Complete Task\n4. Remove Task\n5. View Tasks\n6. View Tasks by Priority\n7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter task description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter start time (HH:MM): ");
                    String startTime = scanner.nextLine();
                    System.out.print("Enter end time (HH:MM): ");
                    String endTime = scanner.nextLine();
                    System.out.print("Enter priority (High, Medium, Low): ");
                    String priority = scanner.nextLine();
                    Task task = TaskFactory.createTask(description, startTime, endTime, priority);
                    scheduleManager.addTask(task);
                    break;

                case 2:
                    System.out.print("Enter the description of the task to edit: ");
                    String oldDescription = scanner.nextLine();
                    System.out.print("Enter new task description: ");
                    String newDescription = scanner.nextLine();
                    System.out.print("Enter new start time (HH:MM): ");
                    String newStartTime = scanner.nextLine();
                    System.out.print("Enter new end time (HH:MM): ");
                    String newEndTime = scanner.nextLine();
                    System.out.print("Enter new priority (High, Medium, Low): ");
                    String newPriority = scanner.nextLine();
                    scheduleManager.editTask(oldDescription, newDescription, newStartTime, newEndTime, newPriority);
                    break;

                case 3:
                    System.out.print("Enter the description of the task to mark as completed: ");
                    String completeDescription = scanner.nextLine();
                    scheduleManager.completeTask(completeDescription);
                    break;

                case 4:
                    System.out.print("Enter the description of the task to remove: ");
                    String descToRemove = scanner.nextLine();
                    scheduleManager.removeTask(descToRemove);
                    break;

                case 5:
                    scheduleManager.viewTasks();
                    break;

                case 6:
                    System.out.print("Enter priority (High, Medium, Low): ");
                    String priorityLevel = scanner.nextLine();
                    scheduleManager.viewTasksByPriority(priorityLevel);
                    break;

                case 7:
                    System.out.println("Exiting application.");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
