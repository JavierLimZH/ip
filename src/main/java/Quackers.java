import java.util.Scanner;

/**
 * Runs the Quackers chatbot and keeps the user's tasks in memory.
 */
public class Quackers {
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) {
        String separator = "_".repeat(60);
        String banner = "+--------------------+\n"
                + "|      QUACKERS      |\n"
                + "+--------------------+";
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        System.out.println(separator);
        System.out.println(banner);
        System.out.println("Hello! I'm Quackers.");
        System.out.println("What can I do for you? Quack!");
        System.out.println(separator);

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();

                System.out.println(separator);

                if (command.equals("bye")) {
                    System.out.println("     Bye. Hope to see you again soon!");
                    System.out.println(separator);
                    break;
                } else if (command.equals("list")) {
                    System.out.println("     Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println("     " + (i + 1) + "." + tasks[i]);
                    }
                } else if (command.matches("mark \\d+")) {
                    int taskIndex = Integer.parseInt(command.substring(5)) - 1;
                    if (taskIndex >= 0 && taskIndex < taskCount) {
                        tasks[taskIndex].markAsDone();
                        System.out.println("     Nice! I've marked this task as done:");
                        System.out.println("       " + tasks[taskIndex]);
                    } else {
                        System.out.println("     Please enter a task number from the list.");
                    }
                } else if (command.matches("unmark \\d+")) {
                    int taskIndex = Integer.parseInt(command.substring(7)) - 1;
                    if (taskIndex >= 0 && taskIndex < taskCount) {
                        tasks[taskIndex].markAsUndone();
                        System.out.println("     OK, I've marked this task as not done yet:");
                        System.out.println("       " + tasks[taskIndex]);
                    } else {
                        System.out.println("     Please enter a task number from the list.");
                    }
                } else if (command.startsWith("todo ")) {
                    taskCount = addTask(tasks, taskCount, new Todo(command.substring(5).trim()));
                } else if (command.startsWith("deadline ")) {
                    int byMarker = command.indexOf(" /by ");
                    if (byMarker < 0) {
                        System.out.println("     Use: deadline DESCRIPTION /by DATE");
                    } else {
                        String description = command.substring(9, byMarker).trim();
                        String by = command.substring(byMarker + 5).trim();
                        taskCount = addTask(tasks, taskCount, new Deadline(description, by));
                    }
                } else if (command.startsWith("event ")) {
                    int fromMarker = command.indexOf(" /from ");
                    int toMarker = command.indexOf(" /to ");
                    if (fromMarker < 0 || toMarker < 0 || toMarker < fromMarker) {
                        System.out.println("     Use: event DESCRIPTION /from START /to END");
                    } else {
                        String description = command.substring(6, fromMarker).trim();
                        String from = command.substring(fromMarker + 7, toMarker).trim();
                        String to = command.substring(toMarker + 5).trim();
                        taskCount = addTask(tasks, taskCount, new Event(description, from, to));
                    }
                } else {
                    System.out.println("     I don't understand that command.");
                }

                System.out.println(separator);
            }
        }
    }

    /**
     * Adds a task and prints the standard confirmation message when capacity remains.
     *
     * @param tasks the task storage
     * @param taskCount the number of stored tasks before adding
     * @param task the task to add
     * @return the updated task count
     */
    private static int addTask(Task[] tasks, int taskCount, Task task) {
        if (taskCount == MAX_TASKS) {
            System.out.println("     Your task list is full.");
            return taskCount;
        }

        tasks[taskCount] = task;
        int updatedTaskCount = taskCount + 1;
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + updatedTaskCount + " tasks in the list.");
        return updatedTaskCount;
    }
}
