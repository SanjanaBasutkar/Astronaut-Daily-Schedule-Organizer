Astronaut Daily Schedule Organizer
This Java project is a Daily Schedule Organizer designed specifically for astronauts to manage their tasks efficiently. It offers features to add, edit, complete, and remove tasks while ensuring no schedule conflicts occur. The project follows key software design patterns, such as Singleton, Factory, and Observer, to ensure optimal performance and code maintainability.

Features
-Add Task: Schedule tasks with a description, start time, end time, and priority (High, Medium, Low).
-Edit Task: Modify existing tasks by updating the description, timing, or priority.
-Complete Task: Mark tasks as completed, indicating that they have been successfully accomplished.
-Remove Task: Delete tasks from the schedule when they are no longer needed.
-View Tasks: Display all scheduled tasks in a day, sorted by start time.
-View Tasks by Priority: Filter and view tasks based on their priority level.
-Conflict Detection: Automatically notifies users of any scheduling conflicts using the Observer design pattern.

Design Patterns
-Singleton: Used in the ScheduleManager class to ensure only one instance of the manager exists throughout the application.
-Observer: Implemented to notify users of task conflicts and updates.
-Factory: Simplifies the creation of task objects through the TaskFactory class.

Technologies Used
-Java
-Java util library for data structures and logging
-Command-line interface (CLI) for user interaction

How to Run
-Clone the repository.
-Compile and run the AstronautScheduleApp.java file.
-Use the command-line menu to interact with the task organizer.
