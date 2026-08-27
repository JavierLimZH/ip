package quackers;

import java.util.List;
import java.util.Scanner;

/**
 * Handles all console input and output for Quackers.
 */
public class Ui implements AutoCloseable {
    private static final String SEPARATOR = "_".repeat(60);
    private static final String BANNER = "+--------------------+\n"
            + "|      QUACKERS      |\n"
            + "+--------------------+";

    private final Scanner scanner;

    /**
     * Creates a console UI that reads from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the application greeting.
     */
    public void showWelcome() {
        System.out.println(SEPARATOR);
        System.out.println(BANNER);
        System.out.println("Hello! I'm Quackers.");
        System.out.println("What can I do for you? Quack!");
        System.out.println(SEPARATOR);
    }

    /**
     * Returns whether another command is available.
     *
     * @return {@code true} when another line can be read
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command from standard input.
     *
     * @return the entered command
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the separator between command responses.
     */
    public void showLine() {
        System.out.println(SEPARATOR);
    }

    /**
     * Displays the farewell message.
     */
    public void showGoodbye() {
        System.out.println("     Bye. Hope to see you again soon!");
    }

    /**
     * Displays an error message.
     *
     * @param message the user-facing explanation
     */
    public void showError(String message) {
        System.out.println("     " + message);
    }

    /**
     * Displays all tasks with one-based numbering.
     *
     * @param tasks the tasks to display
     */
    public void showTasks(List<Task> tasks) {
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays confirmation that a task was added.
     *
     * @param task the added task
     * @param taskCount the resulting number of tasks
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays confirmation that a task was removed.
     *
     * @param task the removed task
     * @param taskCount the resulting number of tasks
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("     Noted. I've removed this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays confirmation that a task status changed.
     *
     * @param task the updated task
     * @param isDone whether the task is now complete
     */
    public void showTaskStatusChanged(Task task, boolean isDone) {
        if (isDone) {
            System.out.println("     Nice! I've marked this task as done:");
        } else {
            System.out.println("     OK, I've marked this task as not done yet:");
        }
        System.out.println("       " + task);
    }

    /**
     * Closes the console input scanner.
     */
    @Override
    public void close() {
        scanner.close();
    }
}
