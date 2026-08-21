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
        String[] tasks = new String[MAX_TASKS];
        boolean[] isDone = new boolean[MAX_TASKS];
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
                        String statusIcon = isDone[i] ? "X" : " ";
                        System.out.println("     " + (i + 1) + ".[" + statusIcon + "] " + tasks[i]);
                    }
                } else if (command.matches("mark \\d+")) {
                    int taskIndex = Integer.parseInt(command.substring(5)) - 1;
                    if (taskIndex >= 0 && taskIndex < taskCount) {
                        isDone[taskIndex] = true;
                        System.out.println("     Nice! I've marked this task as done:");
                        System.out.println("       [X] " + tasks[taskIndex]);
                    } else {
                        System.out.println("     Please enter a task number from the list.");
                    }
                } else if (command.matches("unmark \\d+")) {
                    int taskIndex = Integer.parseInt(command.substring(7)) - 1;
                    if (taskIndex >= 0 && taskIndex < taskCount) {
                        isDone[taskIndex] = false;
                        System.out.println("     OK, I've marked this task as not done yet:");
                        System.out.println("       [ ] " + tasks[taskIndex]);
                    } else {
                        System.out.println("     Please enter a task number from the list.");
                    }
                } else if (taskCount < MAX_TASKS) {
                    tasks[taskCount] = command;
                    taskCount++;
                    System.out.println("     added: " + command);
                }

                System.out.println(separator);
            }
        }
    }
}
