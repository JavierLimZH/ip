import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Interprets raw user commands and converts their arguments into task data.
 */
public class Parser {
    private Parser() {
    }

    /**
     * Determines the type of a command.
     *
     * @param command the complete command entered by the user
     * @return the matching command type
     * @throws QuackersException if the command keyword is unknown
     */
    public static CommandType parseCommandType(String command) throws QuackersException {
        if (command.equals("bye")) {
            return CommandType.BYE;
        }
        if (command.equals("list")) {
            return CommandType.LIST;
        }
        if (hasKeyword(command, "mark")) {
            return CommandType.MARK;
        }
        if (hasKeyword(command, "unmark")) {
            return CommandType.UNMARK;
        }
        if (hasKeyword(command, "delete")) {
            return CommandType.DELETE;
        }
        if (hasKeyword(command, "todo")) {
            return CommandType.TODO;
        }
        if (hasKeyword(command, "deadline")) {
            return CommandType.DEADLINE;
        }
        if (hasKeyword(command, "event")) {
            return CommandType.EVENT;
        }
        throw new QuackersException("Quack? I don't know what that means :-(");
    }

    /**
     * Extracts a zero-based task index from a numbered command.
     *
     * @param command the complete command
     * @param keyword the command keyword to remove
     * @return the zero-based task index
     * @throws QuackersException if the task number is missing or not an integer
     */
    public static int parseTaskIndex(String command, String keyword) throws QuackersException {
        String numberText = command.substring(keyword.length()).trim();
        try {
            return Integer.parseInt(numberText) - 1;
        } catch (NumberFormatException error) {
            throw new QuackersException("Quack? Please enter a valid task number.");
        }
    }

    /**
     * Creates a to-do from its command.
     *
     * @param command the complete to-do command
     * @return the parsed to-do task
     * @throws QuackersException if the description is missing
     */
    public static Todo parseTodo(String command) throws QuackersException {
        String description = command.length() == "todo".length()
                ? ""
                : command.substring("todo".length() + 1).trim();
        requireText(description, "Quack? Give me a todo description!");
        return new Todo(description);
    }

    /**
     * Creates a deadline from its command.
     *
     * @param command the complete deadline command
     * @return the parsed deadline task
     * @throws QuackersException if a field or date is invalid
     */
    public static Deadline parseDeadline(String command) throws QuackersException {
        int byMarker = command.indexOf(" /by ");
        if (byMarker < 0) {
            throw new QuackersException("Quack? Use /by to give the deadline.");
        }

        String description = command.substring("deadline".length(), byMarker).trim();
        String byText = command.substring(byMarker + " /by ".length()).trim();
        requireText(description, "Quack? Give me a deadline description!");
        requireText(byText, "Quack? Give me a deadline date!");
        return new Deadline(description, parseDeadlineDate(byText));
    }

    /**
     * Creates an event from its command.
     *
     * @param command the complete event command
     * @return the parsed event task
     * @throws QuackersException if a required field is missing
     */
    public static Event parseEvent(String command) throws QuackersException {
        int fromMarker = command.indexOf(" /from ");
        int toMarker = command.indexOf(" /to ");
        if (fromMarker < 0 || toMarker < 0 || toMarker < fromMarker) {
            throw new QuackersException("Quack? Use /from START /to END for an event.");
        }

        String description = command.substring("event".length(), fromMarker).trim();
        String from = command.substring(fromMarker + " /from ".length(), toMarker).trim();
        String to = command.substring(toMarker + " /to ".length()).trim();
        requireText(description, "Quack? Give me an event description!");
        requireText(from, "Quack? Give me an event start time!");
        requireText(to, "Quack? Give me an event end time!");
        return new Event(description, from, to);
    }

    private static boolean hasKeyword(String command, String keyword) {
        return command.equals(keyword) || command.startsWith(keyword + " ");
    }

    private static LocalDate parseDeadlineDate(String dateText) throws QuackersException {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException error) {
            throw new QuackersException("Quack? Use yyyy-MM-dd for the deadline date.");
        }
    }

    private static void requireText(String text, String errorMessage) throws QuackersException {
        if (text.isEmpty()) {
            throw new QuackersException(errorMessage);
        }
    }
}
