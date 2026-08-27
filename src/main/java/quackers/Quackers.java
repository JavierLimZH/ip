package quackers;

import java.nio.file.Path;

/**
 * Coordinates command parsing, task operations, storage, and user interaction.
 */
public class Quackers {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;
    private final String loadingError;

    /**
     * Creates Quackers using the specified file for persistent task data.
     *
     * @param filePath the task data file
     */
    public Quackers(Path filePath) {
        this.storage = new Storage(filePath);
        this.ui = new Ui();

        TaskList loadedTasks;
        String errorMessage = null;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (QuackersException error) {
            loadedTasks = new TaskList();
            errorMessage = error.getMessage();
        }
        this.tasks = loadedTasks;
        this.loadingError = errorMessage;
    }

    /**
     * Runs the command loop until the user exits or input ends.
     */
    public void run() {
        ui.showWelcome();
        if (loadingError != null) {
            ui.showError(loadingError);
        }

        try {
            while (ui.hasNextCommand()) {
                String command = ui.readCommand();
                ui.showLine();
                try {
                    if (executeCommand(command)) {
                        ui.showGoodbye();
                        ui.showLine();
                        break;
                    }
                } catch (QuackersException error) {
                    ui.showError(error.getMessage());
                }
                ui.showLine();
            }
        } finally {
            ui.close();
        }
    }

    /**
     * Starts Quackers with its default relative task-file path.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        new Quackers(Path.of("data", "quackers.txt")).run();
    }

    /**
     * Executes one command.
     *
     * @param command the complete command entered by the user
     * @return {@code true} when the command requests application exit
     * @throws QuackersException if the command is invalid or a save fails
     */
    private boolean executeCommand(String command) throws QuackersException {
        CommandType commandType = Parser.parseCommandType(command);
        switch (commandType) {
            case BYE:
                return true;
            case LIST:
                ui.showTasks(tasks.getTasks());
                break;
            case MARK:
                updateTaskStatus(command, "mark", true);
                break;
            case UNMARK:
                updateTaskStatus(command, "unmark", false);
                break;
            case DELETE:
                deleteTask(command);
                break;
            case TODO:
                addTask(Parser.parseTodo(command));
                break;
            case DEADLINE:
                addTask(Parser.parseDeadline(command));
                break;
            case EVENT:
                addTask(Parser.parseEvent(command));
                break;
            default:
                throw new QuackersException("Quack? I don't know what that means :-(");
        }
        return false;
    }

    private void updateTaskStatus(String command, String keyword, boolean isDone)
            throws QuackersException {
        int taskIndex = Parser.parseTaskIndex(command, keyword);
        Task task = tasks.updateStatus(taskIndex, isDone);
        storage.save(tasks.getTasks());
        ui.showTaskStatusChanged(task, isDone);
    }

    private void deleteTask(String command) throws QuackersException {
        int taskIndex = Parser.parseTaskIndex(command, "delete");
        Task removedTask = tasks.delete(taskIndex);
        storage.save(tasks.getTasks());
        ui.showTaskDeleted(removedTask, tasks.size());
    }

    private void addTask(Task task) throws QuackersException {
        tasks.add(task);
        storage.save(tasks.getTasks());
        ui.showTaskAdded(task, tasks.size());
    }
}
