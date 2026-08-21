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
                }
                try {
                    taskCount = handleCommand(command, tasks, taskCount);
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
     * @throws QuackersException if the command or one of its required fields is invalid
     */
    private static int handleCommand(String command, Task[] tasks, int taskCount) throws QuackersException {
        if (command.equals("list")) {
            System.out.println("     Here are the tasks in your list:");
            for (int i = 0; i < taskCount; i++) {
                System.out.println("     " + (i + 1) + "." + tasks[i]);
            }
            return taskCount;
        }
        if (command.startsWith("mark")) {
            return updateTaskStatus(command, "mark", tasks, taskCount, true);
        }
        if (command.startsWith("unmark")) {
            return updateTaskStatus(command, "unmark", tasks, taskCount, false);
        }
        if (command.equals("todo") || command.startsWith("todo ")) {
            String description = command.length() == 4 ? "" : command.substring(5).trim();
            requireText(description, "Quack? Give me a todo description!");
            return addTask(tasks, taskCount, new Todo(description));
        }
        if (command.equals("deadline") || command.startsWith("deadline ")) {
            int byMarker = command.indexOf(" /by ");
            if (byMarker < 0) {
                throw new QuackersException("Quack? Use /by to give the deadline.");
            }
            String description = command.substring(9, byMarker).trim();
            String by = command.substring(byMarker + 5).trim();
            requireText(description, "Quack? Give me a deadline description!");
            requireText(by, "Quack? Give me a deadline date!");
            return addTask(tasks, taskCount, new Deadline(description, by));
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
            return addTask(tasks, taskCount, new Event(description, from, to));
        }
        throw new QuackersException("Quack? I don't know what that means :-(");
    }

    /**
     * Marks a numbered task as done or not done.
     *
     * @param command the entered status command
     * @param keyword the command keyword, either {@code mark} or {@code unmark}
     * @param tasks the task storage
     * @param isDone whether the task should be marked done
     * @throws QuackersException if the task number is missing, invalid, or not in the list
     */
    private static int updateTaskStatus(String command, String keyword, Task[] tasks, int taskCount,
                                        boolean isDone) throws QuackersException {
        String numberText = command.substring(keyword.length()).trim();
        try {
            int taskIndex = Integer.parseInt(numberText) - 1;
            if (taskIndex < 0 || taskIndex >= taskCount) {
                throw new QuackersException("Quack? Please enter a task number from the list.");
            }
            if (isDone) {
                tasks[taskIndex].markAsDone();
                System.out.println("     Nice! I've marked this task as done:");
            } else {
                tasks[taskIndex].markAsUndone();
                System.out.println("     OK, I've marked this task as not done yet:");
            }
            System.out.println("       " + tasks[taskIndex]);
            return taskCount;
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
     * Adds a task and prints the standard confirmation message when capacity remains.
     *
     * @param tasks the task storage
     * @param taskCount the number of stored tasks before adding
     * @param task the task to add
     * @return the updated task count
     * @throws QuackersException if the list has reached its capacity
     */
    private static int addTask(Task[] tasks, int taskCount, Task task) throws QuackersException {
        if (taskCount == MAX_TASKS) {
            throw new QuackersException("Quack? Your task list is full.");
        }
        tasks[taskCount] = task;
        int updatedTaskCount = taskCount + 1;
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + updatedTaskCount + " tasks in the list.");
        return updatedTaskCount;
    }
}
