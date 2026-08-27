import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Runs the Quackers chatbot and keeps the user's tasks in memory.
 */
public class Quackers {
    public static void main(String[] args) {
        String separator = "_".repeat(60);
        String banner = "+--------------------+\n"
                + "|      QUACKERS      |\n"
                + "+--------------------+";
        Storage storage = new Storage(Path.of("data", "quackers.txt"));
        List<Task> tasks;
        String loadingError = null;
        try {
            tasks = storage.load();
        } catch (QuackersException error) {
            tasks = new ArrayList<>();
            loadingError = error.getMessage();
        }

        System.out.println(separator);
        System.out.println(banner);
        System.out.println("Hello! I'm Quackers.");
        System.out.println("What can I do for you? Quack!");
        if (loadingError != null) {
            System.out.println("     " + loadingError);
        }
        System.out.println(separator);

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();

                System.out.println(separator);

                if (command.equals("bye")) {
                    System.out.println("     Bye. Hope to see you again soon!");
                    System.out.println(separator);
                    break;
                }
                try {
                    handleCommand(command, tasks, storage);
                } catch (QuackersException error) {
                    System.out.println("     " + error.getMessage());
                }

                System.out.println(separator);
            }
        }
    }

    /**
     * Interprets one non-exit command and updates the task list when appropriate.
     *
     * @param command the command entered by the user
     * @param tasks the task storage
     * @param storage the file used to persist task changes
     * @throws QuackersException if the command or one of its required fields is invalid
     */
    private static void handleCommand(String command, List<Task> tasks, Storage storage)
            throws QuackersException {
        if (command.equals("list")) {
            System.out.println("     Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println("     " + (i + 1) + "." + tasks.get(i));
            }
            return;
        }
        if (command.startsWith("mark")) {
            updateTaskStatus(command, "mark", tasks, storage, true);
            return;
        }
        if (command.startsWith("unmark")) {
            updateTaskStatus(command, "unmark", tasks, storage, false);
            return;
        }
        if (command.startsWith("delete")) {
            deleteTask(command, tasks, storage);
            return;
        }
        if (command.equals("todo") || command.startsWith("todo ")) {
            String description = command.length() == 4 ? "" : command.substring(5).trim();
            requireText(description, "Quack? Give me a todo description!");
            addTask(tasks, new Todo(description), storage);
            return;
        }
        if (command.equals("deadline") || command.startsWith("deadline ")) {
            int byMarker = command.indexOf(" /by ");
            if (byMarker < 0) {
                throw new QuackersException("Quack? Use /by to give the deadline.");
            }
            String description = command.substring(9, byMarker).trim();
            String byText = command.substring(byMarker + 5).trim();
            requireText(description, "Quack? Give me a deadline description!");
            requireText(byText, "Quack? Give me a deadline date!");
            LocalDate by = parseDeadlineDate(byText);
            addTask(tasks, new Deadline(description, by), storage);
            return;
        }
        if (command.equals("event") || command.startsWith("event ")) {
            int fromMarker = command.indexOf(" /from ");
            int toMarker = command.indexOf(" /to ");
            if (fromMarker < 0 || toMarker < 0 || toMarker < fromMarker) {
                throw new QuackersException("Quack? Use /from START /to END for an event.");
            }
            String description = command.substring(6, fromMarker).trim();
            String from = command.substring(fromMarker + 7, toMarker).trim();
            String to = command.substring(toMarker + 5).trim();
            requireText(description, "Quack? Give me an event description!");
            requireText(from, "Quack? Give me an event start time!");
            requireText(to, "Quack? Give me an event end time!");
            addTask(tasks, new Event(description, from, to), storage);
            return;
        }
        throw new QuackersException("Quack? I don't know what that means :-(");
    }

    /**
     * Marks a numbered task as done or not done.
     *
     * @param command the entered status command
     * @param keyword the command keyword, either {@code mark} or {@code unmark}
     * @param tasks the task storage
     * @param storage the file used to persist task changes
     * @param isDone whether the task should be marked done
     * @throws QuackersException if the task number is missing, invalid, or not in the list
     */
    private static void updateTaskStatus(String command, String keyword, List<Task> tasks,
                                         Storage storage, boolean isDone) throws QuackersException {
        String numberText = command.substring(keyword.length()).trim();
        try {
            int taskIndex = Integer.parseInt(numberText) - 1;
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                throw new QuackersException("Quack? Please enter a task number from the list.");
            }
            if (isDone) {
                tasks.get(taskIndex).markAsDone();
            } else {
                tasks.get(taskIndex).markAsUndone();
            }
            storage.save(tasks);
            if (isDone) {
                System.out.println("     Nice! I've marked this task as done:");
            } else {
                System.out.println("     OK, I've marked this task as not done yet:");
            }
            System.out.println("       " + tasks.get(taskIndex));
            return;
        } catch (NumberFormatException error) {
            throw new QuackersException("Quack? Please enter a valid task number.");
        }
    }

    /**
     * Removes a numbered task from the list.
     *
     * @param command the delete command entered by the user
     * @param tasks the task list
     * @param storage the file used to persist task changes
     * @throws QuackersException if the task number is missing, invalid, or not in the list
     */
    private static void deleteTask(String command, List<Task> tasks, Storage storage)
            throws QuackersException {
        String numberText = command.substring("delete".length()).trim();
        try {
            int taskIndex = Integer.parseInt(numberText) - 1;
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                throw new QuackersException("Quack? Please enter a task number from the list.");
            }
            Task removedTask = tasks.remove(taskIndex);
            storage.save(tasks);
            System.out.println("     Noted. I've removed this task:");
            System.out.println("       " + removedTask);
            System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
        } catch (NumberFormatException error) {
            throw new QuackersException("Quack? Please enter a valid task number.");
        }
    }

    /**
     * Checks that a required command field is not blank.
     *
     * @param text the field text to check
     * @param errorMessage the user-facing error message
     * @throws QuackersException if the field is blank
     */
    private static void requireText(String text, String errorMessage) throws QuackersException {
        if (text.isEmpty()) {
            throw new QuackersException(errorMessage);
        }
    }

    /**
     * Parses a deadline date written in the required ISO format.
     *
     * @param dateText the date supplied by the user
     * @return the parsed deadline date
     * @throws QuackersException if the date is invalid or uses another format
     */
    private static LocalDate parseDeadlineDate(String dateText) throws QuackersException {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException error) {
            throw new QuackersException("Quack? Use yyyy-MM-dd for the deadline date.");
        }
    }

    /**
     * Adds a task and prints the standard confirmation message when capacity remains.
     *
     * @param tasks the task list
     * @param task the task to add
     * @param storage the file used to persist task changes
     * @throws QuackersException if the changed task list cannot be saved
     */
    private static void addTask(List<Task> tasks, Task task, Storage storage)
            throws QuackersException {
        tasks.add(task);
        storage.save(tasks);
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
    }
}
